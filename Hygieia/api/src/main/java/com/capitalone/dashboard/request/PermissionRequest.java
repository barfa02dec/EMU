package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PermissionRequest {
	@NotNull
    @Size(min=3,max=20, message="Please enter permission name in 3-20 characters.")
	private String name;
	private String description;
	private String createdOn;
	private String lastUpdatedOn;
	private String createdBy;
	private String updatedBy;
	private String id;
	@NotNull
	private boolean status;
	private boolean editorEnabled;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isEditorEnabled() {
		return editorEnabled;
	}
	public void setEditorEnabled(boolean editorEnabled) {
		this.editorEnabled = editorEnabled;
	}
	
	

}
