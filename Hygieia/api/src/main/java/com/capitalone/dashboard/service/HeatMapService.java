package com.capitalone.dashboard.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.request.HeatMapRequest;

@Service
public interface HeatMapService {

	public List<HeatMap> getHeatmaps(String projectId);

	public HeatMap createProjectHeatmap(HeatMapRequest heatMapRequest);
		
	public HeatMap updateProjectHeatmap(ObjectId objectId, HeatMapRequest heatMapRequest) ;

	public void deletePrjectHeatMap(String heatmapId);
}
