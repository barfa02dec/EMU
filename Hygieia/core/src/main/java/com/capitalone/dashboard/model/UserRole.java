package com.capitalone.dashboard.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_roles")
public class UserRole extends BaseModel{

	@Indexed(unique=true, name="index_UserRole_roleKey")
	private String roleKey;
	private String description;
	private String createdOn;
	private String lastUpdatedOn;
	private String createdBy;
	private String updatedBy;
	private boolean enabled;
	private boolean exposetoApi;
	private Map<String , Boolean> permissions= new HashMap<String , Boolean>();
	
	
	public boolean isExposetoApi() {
		return exposetoApi;
	}
	public void setExposetoApi(boolean exposetoApi) {
		this.exposetoApi = exposetoApi;
	}
	public Map<String, Boolean> getPermissions() {
		return permissions;
	}
	public void setPermissions(Map<String, Boolean> permissions) {
		this.permissions = permissions;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
		
	
}
