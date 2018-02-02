package com.capitalone.dashboard.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Representation of Heatmap
 * @author unKnown
 *
 */

@Document(collection = "heatmap")
public class HeatMap extends BaseModel implements java.lang.Comparable<HeatMap> {

	@Indexed(unique = true, name="index_HeatMap_heatmapId")
	private Long heatmapId; 

	private String projectId;

	private ProjectHeatmapData projectHeatmapData;

	private Date submissionDate;

	/**
	 * @return the heatmapId
	 */
	public Long getHeatmapId() {
		return heatmapId;
	}

	/**
	 * @param heatmapId the heatmapId to set
	 */
	public void setHeatmapId(Long heatmapId) {
		this.heatmapId = heatmapId;
	}

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
	public Date getSubmissionDate() {
		return submissionDate;
	}

	/**
	 * @param submissionDate the submissionDate to set
	 */
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	/* 
	 * hashcode for heatmap
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((heatmapId == null) ? 0 : heatmapId.hashCode());
		return result;
	}

	/* 
	 * equals for heatmap
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
		if (heatmapId == null) {
			if (other.heatmapId != null)
				return false;
		} else if (!heatmapId.equals(other.heatmapId))
			return false;
		return true;
	}

	@Override
	public int compareTo(HeatMap arg0) {
		//sort by descending order
		if(arg0.heatmapId<this.heatmapId){
			return 1;
		}else if (arg0.heatmapId==this.heatmapId){
			return 0;
		}
		return -1;
	}
}

