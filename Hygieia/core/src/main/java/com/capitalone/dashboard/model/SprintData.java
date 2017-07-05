package com.capitalone.dashboard.model;



import java.util.Date;

public class SprintData {

    private Long sprintId;
    private String sprintName;
    private Date completeDate;
    private Date endDate;
    private Date startDate;
    private String state;
    private Integer daysRemaining;
    
    private DefectCount defectsFound;
    private DefectCount defectsResolved;
    private DefectCount defectsUnresolved;
    
    private Integer completedIssueCount;
    private Integer committedIssueCount;

    private float committedStoryPoints;
    private float completedStoryPoints;
    
    private Integer effortInPD;
    
    private Burndown burndown;
    
    public Long getSprintId() {
		return sprintId;
	}

	public void setSprintId(Long sprintId) {
		this.sprintId = sprintId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(Integer daysRemaining) {
		this.daysRemaining = daysRemaining;
	}

	public Integer getCompletedIssueCount() {
		return completedIssueCount;
	}

	public void setCompletedIssueCount(Integer completedIssueCount) {
		this.completedIssueCount = completedIssueCount;
	}

	public Integer getCommittedIssueCount() {
		return committedIssueCount;
	}

	public void setCommittedIssueCount(Integer committedIssueCount) {
		this.committedIssueCount = committedIssueCount;
	}

	public float getCompletedStoryPoints() {
		return completedStoryPoints;
	}

	public void setCompletedStoryPoints(float completedStoryPoints) {
		this.completedStoryPoints = completedStoryPoints;
	}

	public float getCommittedStoryPoints() {
		return committedStoryPoints;
	}

	public void setCommittedStoryPoints(float committedStoryPoints) {
		this.committedStoryPoints = committedStoryPoints;
	}

	public Burndown getBurndown() {
		return burndown;
	}

	public void setBurndown(Burndown burndown) {
		this.burndown = burndown;
	}

	public Integer getEffortInPD() {
		return effortInPD;
	}

	public void setEffortInPD(Integer effortInPD) {
		this.effortInPD = effortInPD;
	}

	public DefectCount getDefectsFound() {
		return defectsFound;
	}

	public void setDefectsFound(DefectCount defectsFound) {
		this.defectsFound = defectsFound;
	}

	public DefectCount getDefectsResolved() {
		return defectsResolved;
	}

	public void setDefectsResolved(DefectCount defectsResolved) {
		this.defectsResolved = defectsResolved;
	}

	public DefectCount getDefectsUnresolved() {
		return defectsUnresolved;
	}

	public void setDefectsUnresolved(DefectCount defectsUnresolved) {
		this.defectsUnresolved = defectsUnresolved;
	}

}
