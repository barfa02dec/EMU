package com.capitalone.dashboard.rest;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Defect;
import com.capitalone.dashboard.service.DefectService;

/**
 * 
 * @author Rukshanabegum.J
 *
 */

@RestController
public class DefectController {
	
	private final DefectService defectService;

	@Autowired
	public DefectController(DefectService defectService) {
		this.defectService = defectService;
	}
	
	@RequestMapping(value = "/defects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Defect> defects(@RequestParam(value = "collectorId", required = true) String cId,
			@RequestParam(value = "defectId", required = true) String defectId) {
		ObjectId collectorId = new ObjectId(cId);
		return this.defectService.getDefects(collectorId, defectId);
	}
	
	@RequestMapping(value = "/defects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Defect> getAllDefects() {
		List<Defect> defects = defectService.getAllDefects();
		return defects;
	}
	
	@RequestMapping(value = "/defects/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Defect> defectStatus(@RequestParam(value = "defectStatus", required = true) String defectStatus) {
		List<Defect> defects = defectService.getDefectByStatus(defectStatus);
		return defects;
	}
	
	@RequestMapping(value = "/defects/severity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Defect> defectSeverity(@RequestParam(value = "defectSeverity", required = true) String defectSeverity) {
		List<Defect> defects = defectService.defectBySeverity(defectSeverity);
		return defects;
	}
	
	@RequestMapping(value = "/defects/age", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Defect> defectAge(@RequestParam(value = "defectAge", required = true) Integer defectAge) {
		List<Defect> defects = defectService.defectByAge(defectAge);
		return defects;
	}
}
