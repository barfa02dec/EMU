package com.capitalone.dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.DefectAggregation;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.repository.CollectorRepository;
import com.capitalone.dashboard.repository.DefectAggregationRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.request.DefectSummaryRequest;
@Service
public class DefectSummaryServiceImpl implements DefectSummaryService {
	
	private final DefectAggregationRepository aggregationRepository;
	private final ScopeRepository  scopeRepository;
	private final CollectorRepository collectorRepository;

	
	@Autowired
	public DefectSummaryServiceImpl(DefectAggregationRepository aggregationRepository,ScopeRepository  scopeRepository,CollectorRepository collectorRepository) {
		this.collectorRepository = collectorRepository;
		this.scopeRepository = scopeRepository;
		this.aggregationRepository = aggregationRepository;
	}

	@Override
	public Iterable<DefectAggregation> getAllDefectDetails() {
			return aggregationRepository.findAll();
	}

	@Override
	public DefectAggregation findByProjectId(String id, String projectName) {
		return aggregationRepository.findByProjectIdAndName(id,projectName);
	}

	@Override
	public List<DefectAggregation> findByMetricsProjectId(String metricsProjectId) {
		return aggregationRepository.findByMetricsProjectId(metricsProjectId);
	}

	@Override
	public DefectAggregation create(DefectSummaryRequest request) {
		createScope(request);
		DefectAggregation dag = convertDefectSummaryReqToModel(request, null);
		return aggregationRepository.save(dag);
	}

	@Override
	public DefectAggregation update(DefectSummaryRequest request) {
		DefectAggregation existingRecord = aggregationRepository.findOne(new ObjectId(request.getObjectId()));
		DefectAggregation dag = convertDefectSummaryReqToModel(request, existingRecord);
		return aggregationRepository.save(dag);
	}

	private void createScope(DefectSummaryRequest rq) {
		List<Scope> scopes = scopeRepository.getScopeById(rq.getProjectId());

		if (CollectionUtils.isEmpty(scopes)) {
			Scope scope = new Scope();
			scope.setCollectorId(getJiraCollectorId());

			scope.setpId(rq.getProjectId());
			scope.setProjectId(rq.getProjectId());
			scope.setName(rq.getProjectName());
			scope.setIsDeleted("False");
			scope.setAssetState("Active");
			scope.setProjectPath(rq.getProjectName());
			scope.setToShowInEMUDashboard(Boolean.TRUE);
			
			scopeRepository.save(scope);
		}
	}
	
	private ObjectId getJiraCollectorId(){
		Collector collector = collectorRepository.findByName("Jira");
		return collector.getId();
	}
	
	private DefectAggregation convertDefectSummaryReqToModel(DefectSummaryRequest re, DefectAggregation da){
		
		if(null == da){
			da= new DefectAggregation();
			da.setProjectId(re.getProjectId());
			da.setMetricsProjectId(re.getMetricsProjectId());
			da.setProjectName(re.getProjectName());
			da.setCreatedOn(new Date());
			da.setCreatedBy(re.getUser());
			da.setAutomated(false);
		}
		
		da.setUpdatedOn(new Date());
		da.setUpdatedBy(re.getUser());
		
		//defects by priority
		Map<String,Integer> defectsByProirity= new LinkedHashMap<String,Integer>();
		defectsByProirity.put("Low", re.getLowPriorityDefectsCount());
		defectsByProirity.put("Medium", re.getMediumPriorityDefectsCount());
		defectsByProirity.put("High", re.getHighPriorityDefectsCount());
		defectsByProirity.put("Critical", re.getCriticalPriorityDefectsCount());
		da.setDefectsByProirity(defectsByProirity);

		//defects by environment 
		Map<String,Integer> defectsByEnvironment=new LinkedHashMap<String,Integer>();
		defectsByEnvironment.put("UAT", re.getUatDefects());
		defectsByEnvironment.put("QA", re.getQaDefects());
		defectsByEnvironment.put("PROD", re.getProdDefects());
		da.setDefectsByEnvironment(defectsByEnvironment);
		
		//open defects
		Map<String, List<Map<String,String>>> openDefectsByAge = new LinkedHashMap<String, List<Map<String,String>>>();
		
		List<Map<String,String>> l1=new ArrayList<Map<String,String>>();
		
		Map<String,String> m1= new LinkedHashMap<String,String>();
		m1.put("Low", ""+re.getOpenDefectsWithLowPriorityAndAgeLessThanOrEQ15Days());
		m1.put("Medium", ""+re.getOpenDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days());
		m1.put("High", ""+re.getOpenDefectsWithHighPriorityAndAgeLessThanOrEQ15Days());
		m1.put("Critical", ""+re.getOpenDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days());
		m1.put("Defect Age Strategy", "days <15");
		l1.add(m1);
		
		List<Map<String,String>> l2=new ArrayList<Map<String,String>>();
		
		Map<String,String> m2= new LinkedHashMap<String,String>();
		m2.put("Low", ""+re.getOpenDefectsWithLowPriorityAndAgeBetween15To30Days());
		m2.put("Medium", ""+re.getOpenDefectsWithMediumPriorityAndAgeBetween15To30Days());
		m2.put("High", ""+re.getOpenDefectsWithHighPriorityAndAgeBetween15To30Days());
		m2.put("Critical", ""+re.getOpenDefectsWithCriticalPriorityAndAgeBetween15To30Days());
		m2.put("Defect Age Strategy", "15< days <30");
		l2.add(m2);
		
		List<Map<String,String>> l3=new ArrayList<Map<String,String>>();
		
		Map<String,String> m3= new LinkedHashMap<String,String>();
		m3.put("Low", ""+re.getOpenDefectsWithLowPriorityAndAgeBetween30To60Days());
		m3.put("Medium", ""+re.getOpenDefectsWithMediumPriorityAndAgeBetween30To60Days());
		m3.put("High", ""+re.getOpenDefectsWithHighPriorityAndAgeBetween30To60Days());
		m3.put("Critical", ""+re.getOpenDefectsWithCriticalPriorityAndAgeBetween30To60Days());
		m3.put("Defect Age Strategy", "30< days <60");
		l3.add(m3);
		
		List<Map<String,String>> l4=new ArrayList<Map<String,String>>();
		Map<String,String> m4= new LinkedHashMap<String,String>();
		m4.put("Low", ""+re.getOpenDefectsWithLowPriorityAndAgeBetween60To90Days());
		m4.put("Medium", ""+re.getOpenDefectsWithMediumPriorityAndAgeBetween60To90Days());
		m4.put("High", ""+re.getOpenDefectsWithHighPriorityAndAgeBetween60To90Days());
		m4.put("Critical", ""+re.getOpenDefectsWithCriticalPriorityAndAgeBetween60To90Days());
		m4.put("Defect Age Strategy", "60< days <90");
		l4.add(m4);
		
		List<Map<String,String>> l5=new ArrayList<Map<String,String>>();
		Map<String,String> m5= new LinkedHashMap<String,String>();
		m5.put("Low", ""+re.getOpenDefectsWithLowPriorityAndAgeGreaterThan90());
		m5.put("Medium", ""+re.getOpenDefectsWithMediumPriorityAndAgeGreaterThan90());
		m5.put("High", ""+re.getOpenDefectsWithHighPriorityAndAgeGreaterThan90());
		m5.put("Critical", ""+re.getOpenDefectsWithCriticalPriorityAndAgeGreaterThan90());
		m5.put("Defect Age Strategy", "days >90");
		l5.add(m5);
		
		openDefectsByAge.put("Range1", l1);
		openDefectsByAge.put("Range2", l2);
		openDefectsByAge.put("Range3", l3);
		openDefectsByAge.put("Range4", l4);
		openDefectsByAge.put("Range5", l5);
		da.setOpenDefectsByAge(openDefectsByAge);
		
		//fixed defects by resolution
		
		Map<String, List<Map<String,String>>> fixeddefectsByResolutions= new LinkedHashMap<String, List<Map<String,String>>>();
		
		List<Map<String,String>> rl1=new ArrayList<Map<String,String>>();
		
		Map<String,String> rm1= new LinkedHashMap<String,String>();
		rm1.put("Low", ""+re.getFixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days());
		rm1.put("Medium", ""+re.getFixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days());
		rm1.put("High", ""+re.getFixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days());
		rm1.put("Critical", ""+re.getFixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days());
		rm1.put("Defect Resolution Strategy", "days <15");
		rl1.add(rm1);
		
		List<Map<String,String>> rl2=new ArrayList<Map<String,String>>();
		
		Map<String,String> rm2= new LinkedHashMap<String,String>();
		rm2.put("Low", ""+re.getFixedDefectsWithLowPriorityAndResolutionBetween15To30Days());
		rm2.put("Medium", ""+re.getFixedDefectsWithMediumPriorityAndResolutionBetween15To30Days());
		rm2.put("High", ""+re.getFixedDefectsWithHighPriorityAndResolutionBetween15To30Days());
		rm2.put("Critical", ""+re.getFixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days());
		rm2.put("Defect Resolution Strategy", "15< days <30");
		rl2.add(rm2);
		
		List<Map<String,String>> rl3=new ArrayList<Map<String,String>>();
		
		Map<String,String> rm3= new LinkedHashMap<String,String>();
		rm3.put("Low", ""+re.getFixedDefectsWithLowPriorityAndResolutionBetween30To60Days());
		rm3.put("Medium", ""+re.getFixedDefectsWithMediumPriorityAndResolutionBetween30To60Days());
		rm3.put("High", ""+re.getFixedDefectsWithHighPriorityAndResolutionBetween30To60Days());
		rm3.put("Critical", ""+re.getFixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days());
		rm3.put("Defect Resolution Strategy", "30< days <60");
		rl3.add(rm3);
		
		List<Map<String,String>> rl4=new ArrayList<Map<String,String>>();
		Map<String,String> rm4= new LinkedHashMap<String,String>();
		rm4.put("Low", ""+re.getFixedDefectsWithLowPriorityAndResolutionBetween60To90Days());
		rm4.put("Medium", ""+re.getFixedDefectsWithMediumPriorityAndResolutionBetween60To90Days());
		rm4.put("High", ""+re.getFixedDefectsWithHighPriorityAndResolutionBetween60To90Days());
		rm4.put("Critical", ""+re.getFixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days());
		rm4.put("Defect Resolution Strategy", "60< days <90");
		rl4.add(rm4);
		
		List<Map<String,String>> rl5=new ArrayList<Map<String,String>>();
		Map<String,String> rm5= new LinkedHashMap<String,String>();
		rm5.put("Low", ""+re.getFixedDefectsWithLowPriorityAndResolutionGreaterThan90());
		rm5.put("Medium", ""+re.getFixedDefectsWithMediumPriorityAndResolutionGreaterThan90());
		rm5.put("High", ""+re.getFixedDefectsWithHighPriorityAndResolutionGreaterThan90());
		rm5.put("Critical", ""+re.getFixedDefectsWithCriticalPriorityAndResolutionGreaterThan90());
		rm5.put("Defect Resolution Strategy", "days >90");
		rl5.add(rm5);
		
		fixeddefectsByResolutions.put("Range1", rl1);
		fixeddefectsByResolutions.put("Range2", rl2);
		fixeddefectsByResolutions.put("Range3", rl3);
		fixeddefectsByResolutions.put("Range4", rl4);
		fixeddefectsByResolutions.put("Range5", rl5);
		
		da.setFixeddefectsByResolutions(fixeddefectsByResolutions);
		
		return da;
	}
}
