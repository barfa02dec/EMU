package com.capitalone.dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.DefectCount;
import com.capitalone.dashboard.model.NameValuePair;
import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.VersionData;
import com.capitalone.dashboard.repository.CollectorRepository;
import com.capitalone.dashboard.repository.ReleaseRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.request.ReleaseMetricsRequest;
@Service
public class ReleaseServiceImpl implements ReleaseService {
	
	private final ReleaseRepository releaseRepository;
	private final ScopeRepository scopeRepository;
	
	private final CollectorRepository collectorRepository;
	@Autowired
	public ReleaseServiceImpl(ReleaseRepository releaseRepository, ScopeRepository scopeRepository, CollectorRepository collectorRepository) {
		this.scopeRepository = scopeRepository;
		this.releaseRepository = releaseRepository;
		this.collectorRepository = collectorRepository;
	}

	@Override
	public Iterable<Release> getReleases(String projectId) {
		return releaseRepository.findByProjectId(projectId);
	}

	@Override
	public Release getReleaseDetails(String projectId, Long releaseId) {
		return releaseRepository.findByReleaseId(projectId, releaseId);
	}

	@Override
	public Release create(ReleaseMetricsRequest releasereq) {
		createScope(releasereq);
		//Release existingrelease = releaseRepository.findByReleaseId(releasereq.getProjectId(), releasereq.getReleaseId());
		return releaseRepository.save(convertReleaseRequestToReleaseModel(null, releasereq));
	}

	@Override
	public Release update(ReleaseMetricsRequest releasereq) {
		Release existingsprint = releaseRepository.findOne(new ObjectId(releasereq.getObjectId()));
		return releaseRepository.save(convertReleaseRequestToReleaseModel(existingsprint, releasereq));
	}

	private void createScope(ReleaseMetricsRequest rq) {
		List<Scope> scopes = scopeRepository.getScopeById(rq.getProjectId());

		if (CollectionUtils.isEmpty(scopes)) {
			Scope scope = new Scope();
			scope.setCollectorId(getJiraCollectorId());

			scope.setpId(rq.getProjectId());
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
		return collector.getId();
	}
	
	private Release convertReleaseRequestToReleaseModel(Release release, ReleaseMetricsRequest releasereq) {
		
		if(null == release){
			release= new Release();
			release.setReleaseId(releasereq.getReleaseId());
			release.setCreatedOn(new Date());
			release.setCreatedBy(releasereq.getUser());
		}

		release.setProjectId(releasereq.getProjectId());
		release.setProjectName(releasereq.getProjectName());
		release.setName(releasereq.getName());
		release.setDescription(releasereq.getDescription());
		release.setReleased(releasereq.isReleased());
		release.setAutomated(0);
		release.setReleaseDate(StringUtils.isEmpty(releasereq.getReleaseDate()) ? null : new Date(Long.parseLong(releasereq.getReleaseDate())));
		release.setStartDate(StringUtils.isEmpty(releasereq.getStartDate()) ? null : new Date(Long.parseLong(releasereq.getStartDate())));
		release.setUpdatedOn(new Date());
		release.setUpdatedBy(releasereq.getUser());
		
		VersionData data=new VersionData();		
		data.setReleaseName(releasereq.getName());
		data.setReleased(releasereq.isReleased());
		data.setNoofStoryPoints(releasereq.getNoofStoryCommitted());
		data.setNoofStoryCompleted(releasereq.getNoofStoryCompleted());
		
		data.setReleaseDate(StringUtils.isEmpty(releasereq.getReleaseDate()) ? null : new Date(Long.parseLong(releasereq.getReleaseDate())));
		data.setStartDate(StringUtils.isEmpty(releasereq.getStartDate()) ? null : new Date(Long.parseLong(releasereq.getStartDate())));
		
		//defects found
		DefectCount defectsFound= new DefectCount();
		List<NameValuePair> list1= new ArrayList<NameValuePair>();
		NameValuePair np1= new NameValuePair();
		np1.setName("High");
		np1.setValue(releasereq.getHighDefectsFound());
		
		NameValuePair np2= new NameValuePair();
		np2.setName("Low");
		np2.setValue(releasereq.getLowDefectsFound());
		
		NameValuePair np3= new NameValuePair();
		np3.setName("Medium");
		np3.setValue(releasereq.getMediumDefectsFound());
		
		NameValuePair np4= new NameValuePair();
		np4.setName("Critical");
		np4.setValue(releasereq.getCriticalDefectsFound());
		
		list1.add(np1);
		list1.add(np2);
		list1.add(np3);
		list1.add(np4);

		Long total1 =  0L;
		total1=(long) (releasereq.getHighDefectsFound()+releasereq.getLowDefectsFound()+releasereq.getMediumDefectsFound()+releasereq.getCriticalDefectsFound());
		defectsFound.setTotal(total1);
		defectsFound.setSeverity(list1);
		data.setDefectsFound(defectsFound);
		//defects resolved
		DefectCount defectsResolved= new DefectCount();
		List<NameValuePair> list2= new ArrayList<NameValuePair>();
		NameValuePair dnp1= new NameValuePair();
		dnp1.setName("High");
		dnp1.setValue(releasereq.getHighDefectsClosed());
		
		NameValuePair dnp2= new NameValuePair();
		dnp2.setName("Low");
		dnp2.setValue(releasereq.getLowDefectsClosed());
		
		NameValuePair dnp3= new NameValuePair();
		dnp3.setName("Medium");
		dnp3.setValue(releasereq.getMediumDefectsClosed());
		
		NameValuePair dnp4= new NameValuePair();
		dnp4.setName("Critical");
		dnp4.setValue(releasereq.getCriticalDefectsClosed());
		
		list2.add(dnp1);
		list2.add(dnp2);
		list2.add(dnp3);
		list2.add(dnp4);
		
		Long total2 =  0L;
		total2=(long) (releasereq.getHighDefectsClosed()+releasereq.getLowDefectsClosed()+releasereq.getMediumDefectsClosed()+releasereq.getCriticalDefectsClosed());
		
		defectsResolved.setTotal(total2);
		defectsResolved.setSeverity(list2);
		
		data.setDefectsResolved(defectsResolved);
		//defects unresolved
		DefectCount defectsUnResolved= new DefectCount();
		List<NameValuePair> list3= new ArrayList<NameValuePair>();
		NameValuePair dnpp1= new NameValuePair();
		dnpp1.setName("High");
		dnpp1.setValue(releasereq.getHighDefectsUnresolved());
		
		NameValuePair dnpp2= new NameValuePair();
		dnpp2.setName("Low");
		dnpp2.setValue(releasereq.getLowDefectsUnresolved());
		
		NameValuePair dnpp3= new NameValuePair();
		dnpp3.setName("Medium");
		dnpp3.setValue(releasereq.getMediumDefectsUnresolved());
		
		NameValuePair dnpp4= new NameValuePair();
		dnpp4.setName("Critical");
		dnpp4.setValue(releasereq.getCriticalDefectsUnresolved());
		
		list3.add(dnpp1);
		list3.add(dnpp2);
		list3.add(dnpp3);
		list3.add(dnpp4);
		
		Long total3 =  0L;
		total3=(long) (releasereq.getHighDefectsUnresolved()+releasereq.getLowDefectsUnresolved()+releasereq.getMediumDefectsUnresolved()+releasereq.getCriticalDefectsUnresolved());
		
		defectsUnResolved.setTotal(total3);
		defectsUnResolved.setSeverity(list3);
		
		data.setDefectsUnresolved(defectsUnResolved);
		release.setVersionData(data);
		
		return release;
	}
}
