package com.capitalone.dashboard.collector;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.CommitType;
import com.capitalone.dashboard.model.GitHubRepo;
import com.capitalone.dashboard.model.GitProjectSettings;
import com.capitalone.dashboard.util.Encryption;
import com.capitalone.dashboard.util.EncryptionException;
import com.capitalone.dashboard.util.Supplier;


/**
 * GitHubClient implementation that uses SVNKit to fetch information about
 * Subversion repositories.
 */

@Component
public class DefaultGitHubClient implements GitHubClient {
	private static final Log LOG = LogFactory.getLog(DefaultGitHubClient.class);

	private final GitHubSettings settings;
	private final Map<String, GitProjectSettings> gitSettingsMap = new HashMap<String, GitProjectSettings>();

	private final RestOperations restOperations;
	private static final String SEGMENT_API = "/api/v3/repos/";
	private static final String PUBLIC_GITHUB_REPO_HOST = "api.github.com/repos/";
	private static final String PUBLIC_GITHUB_HOST_NAME = "github.com";
	private static final int FIRST_RUN_HISTORY_DEFAULT = 14;

	@Autowired
	public DefaultGitHubClient(GitHubSettings settings,
			Supplier<RestOperations> restOperationsSupplier) {
		this.settings = settings;
		this.restOperations = restOperationsSupplier.get();
	}

	@Override
	public List<Commit> getCommits(GitHubRepo repo, boolean firstRun) {

		initializeGitSettings();
		
		List<Commit> commits = new ArrayList<>();

		// format URL
		String repoUrl = (String) repo.getOptions().get("url");
		if (repoUrl.endsWith(".git")) {
			repoUrl = repoUrl.substring(0, repoUrl.lastIndexOf(".git"));
		}
		
		URL url;
		String hostName = "";
		String protocol = "";
		try {
			url = new URL(repoUrl);
			hostName = url.getHost();
			protocol = url.getProtocol();
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
		}
		
		String hostUrl = protocol + "://" + hostName + "/";
		String repoName = repoUrl.substring(hostUrl.length(), repoUrl.length());
		String apiUrl;
		if (hostName.startsWith(PUBLIC_GITHUB_HOST_NAME)) {
			apiUrl = protocol + "://" + PUBLIC_GITHUB_REPO_HOST + repoName;
		} else {
			apiUrl = protocol + "://" + hostName + SEGMENT_API + repoName;
			LOG.debug("API URL IS:"+apiUrl);
		}
		
		Date dt;
		if (firstRun) {
			int firstRunDaysHistory = settings.getFirstRunHistoryDays();
			if (firstRunDaysHistory > 0) {
				dt = getDate(new Date(), -firstRunDaysHistory, 0);
			} else {
				dt = getDate(new Date(), -FIRST_RUN_HISTORY_DEFAULT, 0);
			}
		} else {
			dt = getDate(new Date(repo.getLastUpdated()), 0, -10);
		}
		
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = calendar.getTimeZone();
		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(dt);
		String thisMoment = String.format("%tFT%<tRZ", cal);

		String queryUrl = apiUrl.concat("/commits?sha=" + repo.getBranch()
				+ "&since=" + thisMoment + "&access_token=" + gitSettingsMap.get(repo.getProject()).getApiToken());
		
		if(!StringUtils.isEmpty(repo.getPath()))
			queryUrl = queryUrl.concat("&path=" + repo.getPath());
		
		//String queryUrl = apiUrl.concat("/commits?access_token="+key);
		
		/*
		 * Calendar cal = Calendar.getInstance(); cal.setTime(dateInstance);
		 * cal.add(Calendar.DATE, -30); Date dateBefore30Days = cal.getTime();
		 */

		// decrypt password
		String decryptedPassword = "";
		if (repo.getPassword() != null && !repo.getPassword().isEmpty()) {
			try {
				decryptedPassword = Encryption.decryptString(
						repo.getPassword(), gitSettingsMap.get(repo.getProject()).getApiToken());
			} catch (EncryptionException e) {
				LOG.error(e.getMessage());
			}
		}
		
		boolean lastPage = false;
		int pageNumber = 1;
		String queryUrlPage = queryUrl;
		while (!lastPage) {
			try {
				ResponseEntity<String> response = makeRestCall(queryUrlPage, repo.getUserId(), decryptedPassword);
				JSONArray jsonArray = paresAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					String sha = str(jsonObject, "sha");
					JSONObject commitObject = (JSONObject) jsonObject.get("commit");
					JSONObject authorObject = (JSONObject) commitObject.get("author");
					String message = str(commitObject, "message");
					String author = str(authorObject, "name");
					long timestamp = new DateTime(str(authorObject, "date"))
							.getMillis();
                    JSONArray parents = (JSONArray) jsonObject.get("parents");
					List<String> parentShas = new ArrayList<>();
					if (parents != null) {
						for (Object parentObj : parents) {
							parentShas.add(str((JSONObject)parentObj, "sha"));
						}
					}
                    
					Commit commit = new Commit();
					commit.setTimestamp(System.currentTimeMillis());
					commit.setScmUrl(repo.getRepoUrl());
                    commit.setScmBranch(repo.getBranch());
					commit.setScmRevisionNumber(sha);
					commit.setScmParentRevisionNumbers(parentShas);
					commit.setScmAuthor(author);
					commit.setScmCommitLog(message);
					commit.setScmCommitTimestamp(timestamp);
					commit.setNumberOfChanges(1);
                    commit.setType(getCommitType(CollectionUtils.size(parents), message));
					commits.add(commit);
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				} else {
					lastPage = isThisLastPage(response);
					pageNumber++;
					queryUrlPage = queryUrl + "&page=" + pageNumber;
				}

			} catch (RestClientException re) {
				LOG.error(re.getMessage() + ":" + queryUrl);
				lastPage = true;

			}
		}
		return commits;
	}

	private CommitType getCommitType (int parentSize, String commitMessage ) {
	    if (parentSize > 1) return CommitType.Merge;
        if (settings.getNotBuiltCommits() == null) return CommitType.New;
        for (String s : settings.getNotBuiltCommits()) {
            if (commitMessage.contains(s)) {
                return CommitType.NotBuilt;
            }
        }
        return CommitType.New;
    }

	private Date getDate(Date dateInstance, int offsetDays, int offsetMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateInstance);
		cal.add(Calendar.DATE, offsetDays);
		cal.add(Calendar.MINUTE, offsetMinutes);
		return cal.getTime();
	}

	private boolean isThisLastPage(ResponseEntity<String> response) {
		HttpHeaders header = response.getHeaders();
		List<String> link = header.get("Link");
		if (link == null || link.isEmpty()) {
			return true;
		} else {
			for (String l : link) {
				if (l.contains("rel=\"next\"")) {
					return false;
				}

			}
		}
		return true;
	}

	private ResponseEntity<String> makeRestCall(String url, String userId,
			String password) {
		// Basic Auth only.
		if (!"".equals(userId) && !"".equals(password)) {
			return restOperations.exchange(url, HttpMethod.GET,
					new HttpEntity<>(createHeaders(userId, password)),
					String.class);

		} else {
			return restOperations.exchange(url, HttpMethod.GET, null,
					String.class);
		}
	}

	private HttpHeaders createHeaders(final String userId, final String password) {
		String auth = userId + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = "Basic " + new String(encodedAuth);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		return headers;
	}

	private JSONArray paresAsArray(ResponseEntity<String> response) {
		try {
			return (JSONArray) new JSONParser().parse(response.getBody());
		} catch (ParseException pe) {
			LOG.error(pe.getMessage());
		}
		return new JSONArray();
	}

	private String str(JSONObject json, String key) {
		Object value = json.get(key);
		return value == null ? null : value.toString();
	}
	
	private void initializeGitSettings() {
		for (int i = 0; i < settings.getProjectId().size(); i++) {
			String projectId = settings.getProjectId().get(i);
			
			GitProjectSettings gitProjectSettings = new GitProjectSettings();
			gitProjectSettings.setHost(settings.getHost().get(i));
			gitProjectSettings.setProjectId(settings.getProjectId().get(i));
			gitProjectSettings.setApiToken(settings.getKey().get(i));
			
			gitSettingsMap.put(projectId, gitProjectSettings);
		}
	}

}
