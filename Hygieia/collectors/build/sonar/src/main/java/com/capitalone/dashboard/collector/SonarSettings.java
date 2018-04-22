package com.capitalone.dashboard.collector;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bean to hold settings specific to the Sonar collector.
 */
@Component
@ConfigurationProperties(prefix = "sonar")
public class SonarSettings {
    private String cron;
    private List<String> servers;
    private List<String> projects;
    private List<String> usernames;
    private List<String> passwords;
    private List<String> componentUrls;
    private List<String> componentDetailUrls;
    private String metrics;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<String> passwords) {
        this.passwords = passwords;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public List<String> getComponentUrls() {
		return componentUrls;
	}

	public void setComponentUrls(List<String> componentUrls) {
		this.componentUrls = componentUrls;
	}

	public List<String> getComponentDetailUrls() {
		return componentDetailUrls;
	}

	public void setComponentDetailUrls(List<String> componentDetailUrls) {
		this.componentDetailUrls = componentDetailUrls;
	}

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }
}
