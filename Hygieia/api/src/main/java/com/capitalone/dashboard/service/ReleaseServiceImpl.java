package com.capitalone.dashboard.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.model.DefectCount;
import com.capitalone.dashboard.model.NameValuePair;
import com.capitalone.dashboard.model.Release;
import com.capitalone.dashboard.model.VersionData;
import com.capitalone.dashboard.repository.ReleaseRepository;
import com.capitalone.dashboard.request.ReleaseMetricsRequest;
@Service
public class ReleaseServiceImpl implements ReleaseService {
	
	private final ReleaseRepository releaseRepository;
		
	@Autowired
	public ReleaseServiceImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	@Override
	public Iterable<Release> getAllReleases(String projectId, String projectName) {
		
		return releaseRepository.findByProjectId(projectId,projectName);
	}

	@Override
	public Release getDetailedReleaseDetails(Long releaseId,String projectId) {
		return releaseRepository.findByReleaseId(releaseId,projectId);
	}

	@Override
	public Release create(ReleaseMetricsRequest re) {
		// TODO Auto-generated method stub
		try {
			return releaseRepository.save(mapReleaseRequestToReleaseModel(re));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private Release mapReleaseRequestToReleaseModel(ReleaseMetricsRequest re) throws ParseException{
		Release release=releaseRepository.findByReleaseId(re.getReleaseId(),re.getProjectId());
		if(null==release){
			release= new Release();
		}
		release.setReleased(re.isReleased());
		release.setReleaseId(re.getReleaseId());
		release.setProjectId(re.getProjectId());
		release.setProjectName(re.getProjectName());
		release.setName(re.getName());
		release.setDescription(re.getDescription());
		release.setReleaseDate(re.getReleaseDate());
		release.setStartDate(re.getStartDate());
		VersionData data=new VersionData();
		
		data.setNoofStoryPoints(re.getNoofStoryCommitted());
		data.setNoofStoryCompleted(re.getNoofStoryCompleted());
		data.setReleased(re.isReleased());
		data.setReleaseId(re.getReleaseId());
		data.setReleaseName(re.getName());
		
		data.setReleaseDate(new SimpleDateFormat("dd-MM-yyyy").parse(re.getReleaseDate()));
		data.setStartDate(new SimpleDateFormat("dd-MM-yyyy").parse(re.getStartDate()));
		
		//defects found
		DefectCount defectsFound= new DefectCount();
		List<NameValuePair> list1= new ArrayList<NameValuePair>();
		NameValuePair np1= new NameValuePair();
		np1.setName("High");
		np1.setValue(re.getHighDefectsFound());
		
		NameValuePair np2= new NameValuePair();
		np2.setName("Low");
		np2.setValue(re.getLowDefectsFound());
		
		NameValuePair np3= new NameValuePair();
		np3.setName("Medium");
		np3.setValue(re.getMediumDefectsFound());
		
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
		data.setDefectsFound(defectsFound);
		//defects resolved
		DefectCount defectsResolved= new DefectCount();
		List<NameValuePair> list2= new ArrayList<NameValuePair>();
		NameValuePair dnp1= new NameValuePair();
		dnp1.setName("High");
		dnp1.setValue(re.getHighDefectsClosed());
		
		NameValuePair dnp2= new NameValuePair();
		dnp2.setName("Low");
		dnp2.setValue(re.getLowDefectsClosed());
		
		NameValuePair dnp3= new NameValuePair();
		dnp3.setName("Medium");
		dnp3.setValue(re.getMediumDefectsClosed());
		
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
		
		data.setDefectsResolved(defectsResolved);
		//defects unresolved
		DefectCount defectsUnResolved= new DefectCount();
		List<NameValuePair> list3= new ArrayList<NameValuePair>();
		NameValuePair dnpp1= new NameValuePair();
		dnpp1.setName("High");
		dnpp1.setValue(re.getHighDefectsUnresolved());
		
		NameValuePair dnpp2= new NameValuePair();
		dnpp2.setName("Low");
		dnpp2.setValue(re.getLowDefectsUnresolved());
		
		NameValuePair dnpp3= new NameValuePair();
		dnpp3.setName("Medium");
		dnpp3.setValue(re.getMediumDefectsUnresolved());
		
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
		
		data.setDefectsUnresolved(defectsUnResolved);
		
		release.setVersionData(data);
		
		return release;
		
	}

}
