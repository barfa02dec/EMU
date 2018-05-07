package com.capitalone.dashboard.model;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Representation of Heatmap
 * @author unKnown
 *
 */

@Document(collection = "heatmap")
public class HeatMap extends BaseModel implements Comparable<HeatMap>  {
	
	@Indexed(name="index_heatmap_projectId", unique=true)
	private String projectId;

	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	@Indexed(name="index_heatmap_submissionDate", unique=true)
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

	@Override
	public int compareTo(HeatMap arg0) {
		//sort by descending order
		if((arg0.submissionDate).compareTo(this.submissionDate) > 0){
			return 1;
		}else if ((arg0.submissionDate).compareTo(this.submissionDate) == 0){
			return 0;
		}
		return -1;
	}
}
