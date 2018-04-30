package com.capitalone.dashboard.request;

public class ReleaseMetricsRequest {
	
	private String objectId;
	private Long releaseId;
    private String description;
    private String name;
    private boolean released;
    private String releaseDate;
    private String startDate;
    private String projectId;
    private String projectName;
    
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
    
    private int noofStoryCompleted;
    private int noofStoryCommitted;
    private int noOfStories;
    
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
    
	public Long getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isReleased() {
		return released;
	}
	public void setReleased(boolean released) {
		this.released = released;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
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
	
	public int getNoofStoryCompleted() {
		return noofStoryCompleted;
	}
	public void setNoofStoryCompleted(int noofStoryCompleted) {
		this.noofStoryCompleted = noofStoryCompleted;
	}
	
	public int getNoofStoryCommitted() {
		return noofStoryCommitted;
	}
	public void setNoofStoryCommitted(int noofStoryCommitted) {
		this.noofStoryCommitted = noofStoryCommitted;
	}
	
	public int getNoOfStories() {
		return noOfStories;
	}
	public void setNoOfStories(int noOfStories) {
		this.noOfStories = noOfStories;
	}
}
