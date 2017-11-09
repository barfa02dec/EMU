package com.capitalone.dashboard.request;

public class SprintMetrcisRequest {
	
	private Long sprintId;
	private String sprintName;
	private String desc;
	private int committedStoriesCount;
	private int committedStoryPoints;
	private int completedStoriesCount;
	private int completedStoryPoints;
	private int storiesAdded;
	private int storypointsAdded;
	private int storiesRemoed;
	private int storypointsRemoed;
	private boolean released;
	private int efforts;
	
	private int criticalDefectsFound;
    private int mediumDefectsFound;
    private int lowDefectsFound;
    private int highDefectsFound;
    
    private int criticalDefectsClosed;
    private int mediumDefectsClosed;
    private int lowDefectsClosed;
    private int highDefectsClosed;
    
    private int criticalDefectsUnresolved;
    private int mediumDefectsUnresolved;
    private int lowDefectsUnresolved;
    private int highDefectsUnresolved;
    private String endDate;
    private String startDate;
    
	private String projectId;
	private String projectName;
	
		
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getCommittedStoriesCount() {
		return committedStoriesCount;
	}
	public void setCommittedStoriesCount(int committedStoriesCount) {
		this.committedStoriesCount = committedStoriesCount;
	}
	public int getCommittedStoryPoints() {
		return committedStoryPoints;
	}
	public void setCommittedStoryPoints(int committedStoryPoints) {
		this.committedStoryPoints = committedStoryPoints;
	}
	public int getCompletedStoriesCount() {
		return completedStoriesCount;
	}
	public void setCompletedStoriesCount(int completedStoriesCount) {
		this.completedStoriesCount = completedStoriesCount;
	}
	public int getCompletedStoryPoints() {
		return completedStoryPoints;
	}
	public void setCompletedStoryPoints(int completedStoryPoints) {
		this.completedStoryPoints = completedStoryPoints;
	}
	public int getStoriesAdded() {
		return storiesAdded;
	}
	public void setStoriesAdded(int storiesAdded) {
		this.storiesAdded = storiesAdded;
	}
	public int getStorypointsAdded() {
		return storypointsAdded;
	}
	public void setStorypointsAdded(int storypointsAdded) {
		this.storypointsAdded = storypointsAdded;
	}
	public int getStoriesRemoed() {
		return storiesRemoed;
	}
	public void setStoriesRemoed(int storiesRemoed) {
		this.storiesRemoed = storiesRemoed;
	}
	public int getStorypointsRemoed() {
		return storypointsRemoed;
	}
	public void setStorypointsRemoed(int storypointsRemoed) {
		this.storypointsRemoed = storypointsRemoed;
	}
	public boolean isReleased() {
		return released;
	}
	public void setReleased(boolean released) {
		this.released = released;
	}
	public int getEfforts() {
		return efforts;
	}
	public void setEfforts(int efforts) {
		this.efforts = efforts;
	}
	public int getCriticalDefectsFound() {
		return criticalDefectsFound;
	}
	public void setCriticalDefectsFound(int criticalDefectsFound) {
		this.criticalDefectsFound = criticalDefectsFound;
	}
	public int getMediumDefectsFound() {
		return mediumDefectsFound;
	}
	public void setMediumDefectsFound(int mediumDefectsFound) {
		this.mediumDefectsFound = mediumDefectsFound;
	}
	public int getLowDefectsFound() {
		return lowDefectsFound;
	}
	public void setLowDefectsFound(int lowDefectsFound) {
		this.lowDefectsFound = lowDefectsFound;
	}
	public int getHighDefectsFound() {
		return highDefectsFound;
	}
	public void setHighDefectsFound(int highDefectsFound) {
		this.highDefectsFound = highDefectsFound;
	}
	public int getCriticalDefectsClosed() {
		return criticalDefectsClosed;
	}
	public void setCriticalDefectsClosed(int criticalDefectsClosed) {
		this.criticalDefectsClosed = criticalDefectsClosed;
	}
	public int getMediumDefectsClosed() {
		return mediumDefectsClosed;
	}
	public void setMediumDefectsClosed(int mediumDefectsClosed) {
		this.mediumDefectsClosed = mediumDefectsClosed;
	}
	public int getLowDefectsClosed() {
		return lowDefectsClosed;
	}
	public void setLowDefectsClosed(int lowDefectsClosed) {
		this.lowDefectsClosed = lowDefectsClosed;
	}
	public int getHighDefectsClosed() {
		return highDefectsClosed;
	}
	public void setHighDefectsClosed(int highDefectsClosed) {
		this.highDefectsClosed = highDefectsClosed;
	}
	public int getCriticalDefectsUnresolved() {
		return criticalDefectsUnresolved;
	}
	public void setCriticalDefectsUnresolved(int criticalDefectsUnresolved) {
		this.criticalDefectsUnresolved = criticalDefectsUnresolved;
	}
	public int getMediumDefectsUnresolved() {
		return mediumDefectsUnresolved;
	}
	public void setMediumDefectsUnresolved(int mediumDefectsUnresolved) {
		this.mediumDefectsUnresolved = mediumDefectsUnresolved;
	}
	public int getLowDefectsUnresolved() {
		return lowDefectsUnresolved;
	}
	public void setLowDefectsUnresolved(int lowDefectsUnresolved) {
		this.lowDefectsUnresolved = lowDefectsUnresolved;
	}
	public int getHighDefectsUnresolved() {
		return highDefectsUnresolved;
	}
	public void setHighDefectsUnresolved(int highDefectsUnresolved) {
		this.highDefectsUnresolved = highDefectsUnresolved;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
    
	
	

}
