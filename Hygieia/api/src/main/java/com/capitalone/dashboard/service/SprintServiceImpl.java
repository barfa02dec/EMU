package com.capitalone.dashboard.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.Burndown;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.DefectCount;
import com.capitalone.dashboard.model.NameValuePair;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.Sprint;
import com.capitalone.dashboard.model.SprintData;
import com.capitalone.dashboard.repository.CollectorRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.repository.SprintRepository;
import com.capitalone.dashboard.request.SprintMetrcisRequest;
@Service
public class SprintServiceImpl implements SprintService {

	private final SprintRepository repository;
	private final ScopeRepository  scopeRepository;
	private final CollectorRepository collectorRepository;
	
	@Autowired
	public SprintServiceImpl(SprintRepository repository, ScopeRepository  scopeRepository,CollectorRepository collectorRepository) {
		this.repository = repository;
		this.scopeRepository = scopeRepository;
		this.collectorRepository = collectorRepository;
		
	}

	@Override
	public List<Sprint> getSprints(String projectId, String projectName) {
		return (List<Sprint>) repository.findByProjectId(projectId,projectName);
	}

	@Override
	public Sprint getSprintDetails(Long sprintId,String projectId) {
		return repository.findBySprintId(sprintId,projectId);
	}

	@Override
	public Sprint create(SprintMetrcisRequest re) {
		createScope(re);
		return repository.save(mapSprintRequestToSprintModel(re));
	}
	
	private void createScope(SprintMetrcisRequest rq) {
		Scope scope = scopeRepository.getScopeByIdAndProjectName(
				rq.getProjectId(), rq.getProjectName());

		if (scope == null) {
			scope = new Scope();
			scope.setCollectorId(getJiraCollectorId());

			// ID;
			scope.setpId(rq.getProjectId());
			// project ID

			scope.setProjectId(rq.getProjectId());
			scope.setName(rq.getProjectName());
			scope.setIsDeleted("False");
			scope.setAssetState("Active");
			scope.setToShowInEMUDashboard(Boolean.TRUE);
			scope.setProjectPath(rq.getProjectName());

			scopeRepository.save(scope);
		}
	}
	
	private ObjectId getJiraCollectorId(){
		Collector collector = collectorRepository.findByName("Jira");
		/*
		 * Fix for the scenario where jira collector is not required for
		 * dashboard setup, but user will provide jira metrcis through forms. If
		 * user create jira metrics through forms, then this API will create a
		 * collector of type jira iff doesn't exists in DB
		 */
		if (null == collector) {
			Collector jiraCollector = new Collector();
			jiraCollector.setName("Jira");
			jiraCollector.setEnabled(true);
			jiraCollector.setCollectorType(CollectorType.AgileTool);
			jiraCollector.setOnline(true);
			jiraCollector.setLastExecuted(System.currentTimeMillis());
			collectorRepository.save(jiraCollector);
			collector = collectorRepository.findByName("Jira");
		}
		return collector.getId();
	}
	private Sprint mapSprintRequestToSprintModel(SprintMetrcisRequest re){
		Sprint s= repository.findBySprintId(re.getSprintId(),re.getProjectId());
		if(null==s){
			s= new Sprint();
			s.setSid(re.getSprintId());
			s.setName(re.getSprintName());
			s.setProjectId(re.getProjectId());
			s.setProjectName(re.getProjectName());
			s.setStart(re.getStartDate());
			s.setEnd(re.getEndDate());
		}
		
		s.setClosed(re.isReleased());
		
		SprintData sd= new SprintData();
		sd.setSprintName(re.getSprintName());
		sd.setSprintId(re.getSprintId());
		sd.setCommittedStoryPoints(re.getCommittedStoryPoints());
		sd.setCompletedStoryPoints(re.getCompletedStoryPoints());
		//sd.setEndDate(re.getEndDate());
		//sd.setStartDate(re.getStartDate());
		
		try {
			sd.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(re.getEndDate()));
			sd.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(re.getStartDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//defects found
		DefectCount defectsFound= new DefectCount();
		List<NameValuePair> list1= new ArrayList<NameValuePair>();
		NameValuePair np1= new NameValuePair();
		np1.setName("Low");
		np1.setValue(re.getLowDefectsFound());
		
		NameValuePair np2= new NameValuePair();
		np2.setName("Medium");
		np2.setValue(re.getMediumDefectsFound()); 
		
		NameValuePair np3= new NameValuePair();
		np3.setName("High");
		np3.setValue(re.getHighDefectsFound());
		
		NameValuePair np4= new NameValuePair();
		np4.setName("Critical");
		np4.setValue(re.getCriticalDefectsFound());
		
		list1.add(np1);
		list1.add(np2);
		list1.add(np3);
		list1.add(np4);

		Long total1 =  0L;
		total1=(long) (re.getHighDefectsFound()+re.getLowDefectsFound()+re.getMediumDefectsFound()+re.getCriticalDefectsFound());
		defectsFound.setTotal(total1);
		defectsFound.setSeverity(list1);
		
		sd.setDefectsFound(defectsFound);

		//defects resolved
		DefectCount defectsResolved= new DefectCount();
		List<NameValuePair> list2= new ArrayList<NameValuePair>();
		
		NameValuePair dnp1= new NameValuePair();
		dnp1.setName("Low");
		dnp1.setValue(re.getLowDefectsClosed());
		
		NameValuePair dnp2= new NameValuePair();
		dnp2.setName("Medium");
		dnp2.setValue(re.getMediumDefectsClosed());
		
		NameValuePair dnp3= new NameValuePair();
		dnp3.setName("High");
		dnp3.setValue(re.getHighDefectsClosed());
		
		NameValuePair dnp4= new NameValuePair();
		dnp4.setName("Critical");
		dnp4.setValue(re.getCriticalDefectsClosed());
		
		list2.add(dnp1);
		list2.add(dnp2);
		list2.add(dnp3);
		list2.add(dnp4);
		
		Long total2 =  0L;
		total2=(long) (re.getHighDefectsClosed()+re.getLowDefectsClosed()+re.getMediumDefectsClosed()+re.getCriticalDefectsClosed());
		
		defectsResolved.setTotal(total2);
		defectsResolved.setSeverity(list2);
		
		sd.setDefectsResolved(defectsResolved);
		
		//defects unresolved
		DefectCount defectsUnResolved= new DefectCount();
		List<NameValuePair> list3= new ArrayList<NameValuePair>();
		
		NameValuePair dnpp1= new NameValuePair();
		dnpp1.setName("Low");
		dnpp1.setValue(re.getLowDefectsUnresolved());
		
		NameValuePair dnpp2= new NameValuePair();
		dnpp2.setName("Medium");
		dnpp2.setValue(re.getMediumDefectsUnresolved());
		
		NameValuePair dnpp3= new NameValuePair();
		dnpp3.setName("High");
		dnpp3.setValue(re.getHighDefectsUnresolved());
		
		NameValuePair dnpp4= new NameValuePair();
		dnpp4.setName("Critical");
		dnpp4.setValue(re.getCriticalDefectsUnresolved());
		
		list3.add(dnpp1);
		list3.add(dnpp2);
		list3.add(dnpp3);
		list3.add(dnpp4);
		
		Long total3 =  0L;
		total3=(long) (re.getHighDefectsUnresolved()+re.getLowDefectsUnresolved()+re.getMediumDefectsUnresolved()+re.getCriticalDefectsUnresolved());
		
		defectsUnResolved.setTotal(total3);
		defectsUnResolved.setSeverity(list3);
		
		sd.setDefectsUnresolved(defectsUnResolved);
		
		//burndown
		
		Burndown burndown = new Burndown();
		
		Burndown.IssueCount issuecount = burndown.new IssueCount();
		
		issuecount.setCount(re.getStoriesAdded());
		issuecount.setStoryPoints(0.0d);
		burndown.setIssuesAdded(issuecount);
		
		issuecount = burndown.new IssueCount();
		issuecount.setCount(re.getStoriesRemoed());
		issuecount.setStoryPoints(0.0d);
		burndown.setIssuesRemoved(issuecount);
		// this field is not populated from UI, hence setting zero for future implementation
		//int incompletedissuesount=0;
		
		//sd.setCommittedIssueCount(re.getCompletedIssueCount() + re.getStoriesRemoed() + incompletedissuesount - re.getStoriesAdded());
		//sd.setCommittedIssueCount(re.getCommittedStoriesCount() + re.getStoriesRemoed() + incompletedissuesount - re.getStoriesAdded());
		
		sd.setCompletedIssueCount(re.getCompletedIssueCount());
		sd.setCommittedIssueCount(re.getCommittedIssueCount());
		issuecount = burndown.new IssueCount();
		issuecount.setCount(sd.getCommittedIssueCount());	
		issuecount.setStoryPoints(0.0d);
		burndown.setInitialIssueCount(issuecount);

		sd.setBurndown(burndown);
		
		s.setSprintData(sd);

		return s;
	}
	
}
