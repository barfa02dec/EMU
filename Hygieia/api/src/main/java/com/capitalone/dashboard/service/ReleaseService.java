package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Release;

public interface ReleaseService {

	Iterable<Release> getAllReleases(String projectId, String projectName);

	Release getDetailedReleaseDetails(Long releaseId,String projectId);

}
