package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.request.ReleaseMetricsRequest;

public interface ReleaseService {

	Iterable<Release> getReleases(String projectId);

	Release getReleaseDetails(String projectId, Long releaseId);

	Release create(ReleaseMetricsRequest re);
	
	Release update(ReleaseMetricsRequest re);
}
