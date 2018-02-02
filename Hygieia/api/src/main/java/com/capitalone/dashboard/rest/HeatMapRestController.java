package com.capitalone.dashboard.rest;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.HeatMap;
import com.capitalone.dashboard.request.HeatMapRequest;
import com.capitalone.dashboard.service.HeatMapService;

/**
 * controller for heat map 
 * @author unknown
 *
 */
@RestController
public class HeatMapRestController {

	private final HeatMapService heatMapService;

	private static final Logger LOGGER = LoggerFactory.getLogger(HeatMapRestController.class);

	/**
	 * 
	 * @param heatMapService
	 */
	@Autowired
	public HeatMapRestController(HeatMapService heatMapService) {
		this.heatMapService = heatMapService;
	}
	
	/**
	 * EMU PROJECT HEATMAP FETCH USING REQUEST PARAMETERS
	 * 
	 * @param projectId
	 * @param heatmapId
	 * @return
	 */
	@RequestMapping(value = "/getheatmaps", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<HeatMap> getHeatmaps(
			@RequestParam(value = "projectId", required = true)  String projectId,
			@RequestParam(value = "noOfHeatMapToShow", required = true) int noOfHeatMapToShow) {
		
		LOGGER.debug("List all the project");
		
		List<HeatMap> heatMapList = new ArrayList<HeatMap>();
		List<HeatMap> heatMapListinDB = heatMapService.getHeatmaps(projectId);
		Collections.sort(heatMapListinDB);
		heatMapListinDB.stream().limit(noOfHeatMapToShow).forEach(sprint -> heatMapList.add(sprint));
		return heatMapList;
	}

	/**
	 * EMU PROJECTHEATMAP CREATE USING POST AND REQUEST BODY
	 * 
	 * @param heatMapRequest
	 * @param response
	 */
	@RequestMapping(value = "/projectheatmaps/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createHeatmap(@RequestBody HeatMapRequest heatMapRequest, HttpServletResponse response) {
		
		LOGGER.debug("Creating Heat map");
		heatMapService.createProjectHeatmap(heatMapRequest);
	}

	/**
	 * EMU PROJECTHEATMAPS UPDATE USING PUT
	 * 
	 * @param heatMapRequest
	 * @param response
	 */
	@RequestMapping(value = "/projectheatmaps/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void updateProjectHeatMap(@RequestBody HeatMapRequest heatMapRequest, HttpServletResponse response) {
		
		LOGGER.debug("Updating Heat map");
		heatMapService.updateProjectHeatmap(heatMapRequest);
	} 
	
	/**
	 * EMU PROJECTHEATMAPS DELETE USING DELETE
	 * 
	 * @param projectId
	 */
	@RequestMapping(value = "/projectheatmaps/delete", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteHeatMap(@RequestParam(value = "heatmapId", required = true) Long heatmapId) {
		LOGGER.debug("Deleting Heat map");
		heatMapService.deletePrjectHeatMap(heatmapId);
	}
}