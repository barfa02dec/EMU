package com.capitalone.dashboard.collector;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bean to hold settings specific to the UDeploy collector.
 */
@Component
@ConfigurationProperties(prefix = "github")
public class GitHubSettings {
    private String cron;
    private int firstRunHistoryDays;
    private String[] notBuiltCommits;

    private List<String> host;
    private List<String> key;
    private List<String> projectId;

	public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getFirstRunHistoryDays() {
		return firstRunHistoryDays;
	}

	public void setFirstRunHistoryDays(int firstRunHistoryDays) {
		this.firstRunHistoryDays = firstRunHistoryDays;
	}

    public String[] getNotBuiltCommits() {
        return notBuiltCommits;
    }

    public void setNotBuiltCommits(String[] notBuiltCommits) {
        this.notBuiltCommits = notBuiltCommits;
    }
    
	public List<String> getHost() {
		return host;
	}

	public void setHost(List<String> host) {
		this.host = host;
	}

	public List<String> getKey() {
		return key;
	}
	
	public void setKey(List<String> key) {
		this.key = key;
	}
	
	public List<String> getProjectId() {
		return projectId;
	}

	public void setProjectId(List<String> projectId) {
		this.projectId = projectId;
	}
}
