package com.capitalone.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Base class for all Mongo model classes that has an id property.
 */
public class BaseModel {
    @Id
    private ObjectId id;
	private String createdOn;
	private String updatedOn;
	private String createdBy;
	private String updatedBy;

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

    /*
     * Note:
     * 
     * Having hashcode + equals is more complicated than simply comparing ObjectIds since
     * it does not provide a way to properly compare models that have not been saved yet.
     */
}
