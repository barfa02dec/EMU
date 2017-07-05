package com.capitalone.dashboard.model;



import java.util.Date;

public class VersionData {

	private Long releaseId;
	private String releaseName;
	private Boolean released;
	private String description;
	private Date startDate;
	private Date releaseDate;

	private DefectCount defectsFound;
	private DefectCount defectsResolved;
    private DefectCount defectsUnresolved;
    
	private Integer noofStoryCompleted;
	private float noofStoryPoints;
	
	private Integer effortInPD;
	
	public Long getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public Boolean getReleased() {
		return released;
	}
	public void setReleased(Boolean released) {
		this.released = released;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public DefectCount getDefectsResolved() {
		return defectsResolved;
	}
	public void setDefectsResolved(DefectCount defectsResolved) {
		this.defectsResolved = defectsResolved;
	}

	public DefectCount getDefectsFound() {
		return defectsFound;
	}
	public void setDefectsFound(DefectCount defectsFound) {
		this.defectsFound = defectsFound;
	}

	public DefectCount getDefectsUnresolved() {
		return defectsUnresolved;
	}
	public void setDefectsUnresolved(DefectCount defectsUnresolved) {
		this.defectsUnresolved = defectsUnresolved;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Integer getNoofStoryCompleted() {
		return noofStoryCompleted;
	}
	public void setNoofStoryCompleted(Integer noofStoryCompleted) {
		this.noofStoryCompleted = noofStoryCompleted;
	}
	public float getNoofStoryPoints() {
		return noofStoryPoints;
	}
	public void setNoofStoryPoints(float noofStoryPoints) {
		this.noofStoryPoints = noofStoryPoints;
	}
	public Integer getEffortInPD() {
		return effortInPD;
	}
	public void setEffortInPD(Integer effortInPD) {
		this.effortInPD = effortInPD;
	}
}
