package com.capitalone.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.repository.ReleaseRepository;
@Service
public class ReleaseServiceImpl implements ReleaseService {
	
	private final ReleaseRepository releaseRepository;
		
	@Autowired
	public ReleaseServiceImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	@Override
	public Iterable<Release> getAllReleases(String projectId) {
		
		return releaseRepository.findByProjectId(projectId);
	}

	@Override
	public Release getDetailedReleaseDetails(Long releaseId) {
		return releaseRepository.findByReleaseId(releaseId);
	}

}
