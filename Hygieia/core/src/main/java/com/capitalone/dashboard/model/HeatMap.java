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

	@Indexed(name="index_heatmap_projectId")
	private String projectId;

	private ProjectHeatmapData projectHeatmapData;

	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private String submissionDate;

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the projectHeatmapData
	 */
	public ProjectHeatmapData getProjectHeatmapData() {
		return projectHeatmapData;
	}

	/**
	 * @param projectHeatmapData the projectHeatmapData to set
	 */
	public void setProjectHeatmapData(ProjectHeatmapData projectHeatmapData) {
		this.projectHeatmapData = projectHeatmapData;
	}

	/**
	 * @return the submissionDate
	 */
	public String  getSubmissionDate() {
		return submissionDate;
	}

	/**
	 * @param submissionDate the submissionDate to set
	 */
	public void setSubmissionDate(String  submissionDate) {
		this.submissionDate = submissionDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
