package com.capitalone.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.request.HeatMapRequest;

@Service
public interface HeatMapService {

	public List<HeatMap> getHeatmaps(String projectId);

	public HeatMap createHeatmap(HeatMapRequest heatMapRequest);
		
	public HeatMap updateHeatmap(HeatMapRequest heatMapRequest) ;

	public void deleteHeatmap(String heatmapId);
}
