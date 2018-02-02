package com.capitalone.dashboard.model;



import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JiraIssue {
	
	private String key;
	private String id;
	private String createDate;
	private String resolutionDate;
	private String severity;
	private String environment;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getResolutionDate() {
		return resolutionDate;
	}
	public void setResolutionDate(String resolutionDate) {
		this.resolutionDate = resolutionDate;
	}
	
	public void parseJson(String json){
		JsonObject jsonObject = new GsonBuilder().create().fromJson(json, JsonObject.class);
		
		id = jsonObject.get("id").getAsString();
		key = jsonObject.get("key").getAsString();
		
		if(!jsonObject.getAsJsonObject("fields").get("priority").isJsonNull()  
				&& jsonObject.getAsJsonObject("fields").getAsJsonObject("priority").isJsonObject()){
			severity = jsonObject.getAsJsonObject("fields").getAsJsonObject("priority").get("name").getAsString();
		}
		
		if(!jsonObject.getAsJsonObject("fields").get("environment").isJsonNull() &&
				jsonObject.getAsJsonObject("fields").get("environment").isJsonObject()){
				environment = jsonObject.getAsJsonObject("fields").getAsJsonObject("environment").get("name").getAsString();
		}
		
		if(!jsonObject.getAsJsonObject("fields").get("resolutiondate").isJsonNull()){
			resolutionDate = jsonObject.getAsJsonObject("fields").get("resolutiondate").getAsString();
		}
		
		if(!jsonObject.getAsJsonObject("fields").get("created").isJsonNull()){
			createDate = jsonObject.getAsJsonObject("fields").get("created").getAsString();
		}
	}
	
	public void parseJsonForEnvironment(String json){
		JsonObject jsonObject = new GsonBuilder().create().fromJson(json, JsonObject.class);
		
		if(!jsonObject.getAsJsonObject("fields").get("environment").isJsonNull()){
				environment = jsonObject.getAsJsonObject("fields").get("environment").getAsString();
		}		
		
	}
}
