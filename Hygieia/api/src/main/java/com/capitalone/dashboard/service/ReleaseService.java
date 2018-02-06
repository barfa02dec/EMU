package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.request.ReleaseMetricsRequest;

public interface ReleaseService {

	Iterable<Release> getReleases(String projectId, String projectName);

	Release getReleaseDetails(Long releaseId,String projectId);

	Release create(ReleaseMetricsRequest re);
}
