package com.capitalone.dashboard.collector;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by benathmane on 23/06/16.
 */

/**
 * Bean to hold settings specific to the Gitlab collector.
 */

@Component
@ConfigurationProperties(prefix = "gitlab")
public class GitlabSettings {
    private String cron;
    private String protocol;
    private String port;
	private int firstRunHistoryDays;
	private boolean selfSignedCertificate;

    private List<String> host;
    private List<String> apiToken;
    private List<String> projectId;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public List<String> getApiToken() {
        return apiToken;
    }

    public void setApiToken(List<String> apiToken) {
        this.apiToken = apiToken;
    }

	public List<String> getHost() {
		return host;
	}

	public void setHost(List<String> host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getFirstRunHistoryDays() {
		return firstRunHistoryDays;
	}

	public void setFirstRunHistoryDays(int firstRunHistoryDays) {
		this.firstRunHistoryDays = firstRunHistoryDays;
	}

	public boolean isSelfSignedCertificate() {
		return selfSignedCertificate;
	}
	
	public void setSelfSignedCertificate(boolean selfSigned) {
		this.selfSignedCertificate = selfSigned;
	}

	public List<String> getProjectId() {
		return projectId;
	}

	public void setProjectId(List<String> projectId) {
		this.projectId = projectId;
	}
}
