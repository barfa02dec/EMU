package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.request.HeatMapRequest;

public interface HeatMapService {

	public List<HeatMap> getHeatmaps(String projectId);

	public HeatMap createProjectHeatmap(HeatMapRequest heatMapRequest);
		
	public HeatMap updateProjectHeatmap(HeatMapRequest heatMapRequest) ;

	public void deletePrjectHeatMap(Long heatmapId);
}
