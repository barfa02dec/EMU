package com.capitalone.dashboard.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class JiraVersion implements Comparable<JiraVersion> {

    private Long id;
    private String description;
    private String name;
    private Boolean archived;
    private Boolean released;
    private String releaseDate;
    private String startDate;
    private String overdue;
    private VersionData versionData;
    private String originalreleaseData;

    
	/**
     * No args constructor for use in serialization
     * 
     */
    public JiraVersion() {
    }

    public JiraVersion(Long id, String description, String name, Boolean released, String releaseDate, String startDate) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.released = released;
        this.releaseDate = releaseDate;
        this.startDate = startDate;
    }

    public String getOriginalreleaseData() {
		return originalreleaseData;
	}

	public void setOriginalreleaseData(String originalreleaseData) {
		this.originalreleaseData = originalreleaseData;
	}

	public VersionData getVersionData() {
		return versionData;
	}

	public void setVersionData(VersionData versionData) {
		this.versionData = versionData;
	}


    /**
     * 
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The archived
     */
    public Boolean getArchived() {
        return archived;
    }

    /**
     * 
     * @param archived
     *     The archived
     */
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    /**
     * 
     * @return
     *     The released
     */
    public Boolean getReleased() {
        return released;
    }

    /**
     * 
     * @param released
     *     The released
     */
    public void setReleased(Boolean released) {
        this.released = released;
    }

    /**
     * 
     * @return
     *     The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * 
     * @param releaseDate
     *     The releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

	public String getOverdue() {
		return overdue;
	}

	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}
	
	@Override
	public int compareTo(JiraVersion js) {
		
		if(this.id==js.id){
			return 0;
		}else if (this.id<js.id){
			return 1;
		}else{
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		JiraVersion that = (JiraVersion) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(id, that.id).build();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).toHashCode();
	}
}
