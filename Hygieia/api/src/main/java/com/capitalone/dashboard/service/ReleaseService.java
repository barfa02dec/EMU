package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.request.ReleaseMetricsRequest;

public interface ReleaseService {

	Iterable<Release> getAllReleases(String projectId, String projectName);

	Release getDetailedReleaseDetails(Long releaseId,String projectId);

	Release create(ReleaseMetricsRequest re);
}
