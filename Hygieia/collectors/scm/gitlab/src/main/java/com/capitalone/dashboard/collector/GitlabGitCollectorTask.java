package com.capitalone.dashboard.collector;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.capitalone.dashboard.gitlab.DefaultGitlabGitClient;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitlabGitRepo;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.CommitRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.GitlabGitCollectorRepository;

/**
 * Created by benathmane on 23/06/16.
 */

/**
 * CollectorTask that fetches Commit information from Gitlab
 */
@Component
public class GitlabGitCollectorTask  extends CollectorTask<Collector> {
    private static final Log LOG = LogFactory.getLog(GitlabGitCollectorTask.class);

    private final BaseCollectorRepository<Collector> collectorRepository;
    private final GitlabGitCollectorRepository gitlabGitCollectorRepository;
    private final GitlabSettings gitlabSettings;
    private final DefaultGitlabGitClient defaultGitlabGitClient;
    private final ComponentRepository dbComponentRepository;
    private final CommitRepository commitRepository;
	private final Map<String, GitlabProjectSettings> gitSettingsMap = new HashMap<>();


    @Autowired
    public GitlabGitCollectorTask(TaskScheduler taskScheduler,
                                  BaseCollectorRepository<Collector> collectorRepository,
                                  GitlabSettings gitlabSettings,
                                  CommitRepository commitRepository,
                                  GitlabGitCollectorRepository gitlabGitCollectorRepository,
                                  DefaultGitlabGitClient defaultGitlabGitClient,
                                  ComponentRepository dbComponentRepository
    ) {
        super(taskScheduler, "Gitlab");
        this.collectorRepository = collectorRepository;
        this.gitlabSettings = gitlabSettings;
        this.commitRepository = commitRepository;
        this.gitlabGitCollectorRepository = gitlabGitCollectorRepository;
        this.defaultGitlabGitClient = defaultGitlabGitClient;
        this.dbComponentRepository = dbComponentRepository;
    }

	@Override
	public Collector getCollector() {
		Collector protoType = new Collector();
		protoType.setName("Gitlab");
		protoType.setCollectorType(CollectorType.SCM);
		protoType.setOnline(true);
		protoType.setEnabled(true);
		return protoType;
	}

    @Override
    public BaseCollectorRepository<Collector> getCollectorRepository() {
        return collectorRepository;
    }

    @Override
    public String getCron() {
        return gitlabSettings.getCron();
    }

    @Override
    public void collect(Collector collector) {
        logBanner("Starting...");
        long start = System.currentTimeMillis();
        
        initializeGitSettings();
        
        int repoCount = 0;
        int commitCount = 0;

        clean(collector);
        for (GitlabGitRepo repo : enabledRepos(collector)) {
        	
        	String projectId = repo.getProject();
			/*String hostUrl = repo.getRepoUrl();
			String hostName = "";

			try {
				hostName = getDomainName(hostUrl);
			} catch (URISyntaxException urx) {
				LOG.debug("Syntax Error", urx);
			}*/

        	if (gitSettingsMap.containsKey(projectId)){
				boolean firstRun = false;
				if (repo.getLastUpdated() == 0)
					firstRun = true;
				//repo.setLastUpdated(System.currentTimeMillis());
				//repo.removeLastUpdateDate();
				repo.setLastUpdated(System.currentTimeMillis()-TimeUnit.DAYS.toMillis(700));
				
				try {
					List<Commit> commits = defaultGitlabGitClient.getCommits(repo, firstRun, gitSettingsMap.get(projectId).getHost(), gitSettingsMap.get(projectId).getApiToken());
					commitCount = saveNewCommits(commitCount, repo, commits);
					gitlabGitCollectorRepository.save(repo);
				} catch (HttpClientErrorException | ResourceAccessException e) {
					LOG.info("Failed to retrieve data, the repo or collector is most likey misconfigured: " + repo.getRepoUrl() + ", " + e.getMessage());
				}
				repoCount++;
        	}
        }
        log("Repo Count", start, repoCount);
        log("New Commits", start, commitCount);
        log("Finished", start);
    }

	private int saveNewCommits(int commitCount, GitlabGitRepo repo, List<Commit> commits) {
		int totalCommitCount = commitCount;
		for (Commit commit : commits) {
			LOG.debug(commit.getTimestamp() + ":::" + commit.getScmCommitLog());
			if (isNewCommit(repo, commit)) {
				commit.setCollectorItemId(repo.getId());
				commitRepository.save(commit);
				totalCommitCount++;
			}
		}
		return totalCommitCount;
	}

	@SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts") // agreed, fixme
	private void clean(Collector collector) {
		Set<ObjectId> uniqueIDs = new HashSet<ObjectId>();
		/**
		 * Logic: For each component, retrieve the collector item list of the
		 * type SCM. Store their IDs in a unique set ONLY if their collector IDs
		 * match with GitHub collectors ID.
		 */
		for (com.capitalone.dashboard.model.Component comp : dbComponentRepository.findAll()) {
			if (comp.getCollectorItems() != null && !comp.getCollectorItems().isEmpty()) {
				List<CollectorItem> itemList = comp.getCollectorItems().get(CollectorType.SCM);
				if (itemList != null) {
					for (CollectorItem ci : itemList) {
						if (ci != null && ci.getCollectorId().equals(collector.getId())) {
							uniqueIDs.add(ci.getId());
						}
					}
				}
			}
		}

		/**
		 * Logic: Get all the collector items from the collector_item collection
		 * for this collector. If their id is in the unique set (above), keep
		 * them enabled; else, disable them.
		 */
		List<GitlabGitRepo> repoList = new ArrayList<>();
		Set<ObjectId> gitID = new HashSet<>();
		gitID.add(collector.getId());
		for (GitlabGitRepo repo : gitlabGitCollectorRepository.findByCollectorIdIn(gitID)) {
			if (repo != null) {
				repo.setEnabled(uniqueIDs.contains(repo.getId()));
				repoList.add(repo);
			}
		}
		gitlabGitCollectorRepository.save(repoList);
	}

    private List<GitlabGitRepo> enabledRepos(Collector collector) {
        return gitlabGitCollectorRepository.findEnabledGitlabRepos(collector.getId());
    }

	private boolean isNewCommit(GitlabGitRepo repo, Commit commit) {
		return commitRepository.findByCollectorItemIdAndScmRevisionNumber(repo.getId(),
				commit.getScmRevisionNumber()) == null;
	}
	
	private void initializeGitSettings() {
		for (int i = 0; i < gitlabSettings.getHost().size(); i++) {
			String projectId = gitlabSettings.getProjectId().get(i);
			
			GitlabProjectSettings gitProjectSettings = new GitlabProjectSettings();
			gitProjectSettings.setHost(gitlabSettings.getHost().get(i));
			gitProjectSettings.setProjectId(gitlabSettings.getProjectId().get(i));
			gitProjectSettings.setApiToken(gitlabSettings.getApiToken().get(i));
			
			gitSettingsMap.put(projectId, gitProjectSettings);
		}
	}

	private String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
