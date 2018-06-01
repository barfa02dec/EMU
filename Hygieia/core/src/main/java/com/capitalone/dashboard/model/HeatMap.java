package com.capitalone.dashboard.model;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Representation of Heatmap
 * @author unKnown
 *
 */

@Document(collection = "heatmap")
@CompoundIndex(def = "{'projectId':1, 'submissionDate':1}", name = "index_heatmap_projectId_submissionDate", unique=true)
public class HeatMap extends BaseModel { // implements Comparable<HeatMap>  {
	
	private String projectId;
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private String submissionDate;
	
	private ProjectHeatmapData projectHeatmapData;

	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public ProjectHeatmapData getProjectHeatmapData() {
		return projectHeatmapData;
	}
	public void setProjectHeatmapData(ProjectHeatmapData projectHeatmapData) {
		this.projectHeatmapData = projectHeatmapData;
	}

	public String  getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(String  submissionDate) {
		this.submissionDate = submissionDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((submissionDate == null) ? 0 : submissionDate.hashCode());
		return result;
	}

    public Date getFormattedSubmissionDate() {
    	Date date1 = null;
    	try{
    		date1 = new SimpleDateFormat("MMyyyy").parse(getSubmissionDate());
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
        return date1;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeatMap other = (HeatMap) obj;
		if (submissionDate == null) {
			if (other.submissionDate != null)
				return false;
		} else if (!submissionDate.equals(other.submissionDate))
			return false;
		return true;
	}
	

	/*@Override
	public int compareTo(HeatMap arg0) {
		//sort by descending order
		if((arg0.submissionDate).compareTo(this.submissionDate) > 0){
			return 1;
		}else if ((arg0.submissionDate).compareTo(this.submissionDate) == 0){
			return 0;
		}
		return -1; 
		
      if (arg0.getFormattedSubmissionDate() == null || this.getFormattedSubmissionDate() == null)
        return 0;
      return arg0.getFormattedSubmissionDate().compareTo(arg0.getFormattedSubmissionDate());
	}*/
}
