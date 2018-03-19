package com.capitalone.dashboard.util;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.capitalone.dashboard.model.DefectCount;
import com.capitalone.dashboard.model.JiraIssue;
import com.capitalone.dashboard.model.NameValuePair;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class DefectUtil {
	
	public static List<JiraIssue> parseDefectsJson(String json , NewFeatureSettings featureSettings){
		JsonArray jsonArr = new GsonBuilder().create().fromJson(json, JsonObject.class).getAsJsonArray("issues");
		
		if(json != null && new GsonBuilder().create().fromJson(json, JsonObject.class).has("issues")){
			jsonArr = new GsonBuilder().create().fromJson(json, JsonObject.class).getAsJsonArray("issues");
		}else{ 
			return null;
		}
			
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		for(JsonElement element : jsonArr){
			JiraIssue issue = new JiraIssue();
			parseJson(issue, element.toString(), featureSettings);
			issues.add(issue);
		}		
		return issues;
	}
	
	public static List<JiraIssue> parseDefectsEnvironmentJson(String json, NewFeatureSettings featureSettings){
		JsonArray jsonArr = new GsonBuilder().create().fromJson(json, JsonObject.class).getAsJsonArray("issues");
		
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		for(JsonElement element : jsonArr){
			JiraIssue issue = new JiraIssue();
			parseJsonForEnvironment(issue, element.toString(), featureSettings);
			if(null!=issue.getEnvironment()){
				issue.getEnvironment().replaceAll(".", "-");
			}
			issues.add(issue);
		}		
		return issues;
	}
	
	public static List<NameValuePair> defectCountBySeverity(List<JiraIssue> issues){
		
		LinkedHashMap<String, NameValuePair> issuecountmap = new LinkedHashMap<String, NameValuePair> ();	
		if(!CollectionUtils.isEmpty(issues)){
			for(JiraIssue issue: issues){
				if(issuecountmap.containsKey(issue.getSeverity())){
					NameValuePair issuecategory = issuecountmap.get(issue.getSeverity());
					issuecategory.setValue(issuecategory.getValue() + 1);
					
					issuecountmap.put(issue.getSeverity(), issuecategory);
				}else if(!StringUtils.isEmpty(issue.getSeverity())){
					issuecountmap.put(issue.getSeverity(), new NameValuePair(issue.getSeverity(), 1));
				}
			}
		}
		
		return CollectionUtils.isEmpty(issuecountmap) ? null : new ArrayList<NameValuePair>(issuecountmap.values());
	}
	
	public static DefectCount defectCount(List<NameValuePair> defects){    	
		DefectCount defectcount = new DefectCount();
		
		if(!CollectionUtils.isEmpty(defects)){
			defectcount.setSeverity(defects);
			for(NameValuePair defect : defects){
				defectcount.setTotal(defectcount.getTotal() + defect.getValue());			
			}
		}
		return defectcount;
	}
	
	private static void parseJson(JiraIssue issue, String json, NewFeatureSettings featureSettings){
		JsonObject jsonObject = new GsonBuilder().create().fromJson(json, JsonObject.class);
		
		issue.setId(jsonObject.get("id").getAsString());
		issue.setKey(jsonObject.get("key").getAsString());
		
		if(!jsonObject.getAsJsonObject("fields").get("priority").isJsonNull()  
				&& jsonObject.getAsJsonObject("fields").getAsJsonObject("priority").isJsonObject()){
			issue.setSeverity(jsonObject.getAsJsonObject("fields").getAsJsonObject("priority").get("name").getAsString());
		}
		
		if(featureSettings.getEnvironmentFoundInFieldName() != null){		
			try{
				if(!jsonObject.getAsJsonObject("fields").get(featureSettings.getEnvironmentFoundInFieldName()).isJsonNull()){
					if(jsonObject.getAsJsonObject("fields").get(featureSettings.getEnvironmentFoundInFieldName()).isJsonObject())
						issue.setEnvironment(jsonObject.getAsJsonObject("fields").getAsJsonObject(featureSettings.getEnvironmentFoundInFieldName()).get("value").getAsString());
					else
						issue.setEnvironment(jsonObject.getAsJsonObject("fields").get(featureSettings.getEnvironmentFoundInFieldName()).getAsString());
				}
			}catch(Exception ex){
				ex.getMessage();
			}
		} else if(!jsonObject.getAsJsonObject("fields").get("environment").isJsonNull() &&
				jsonObject.getAsJsonObject("fields").get("environment").isJsonObject()){
			issue.setEnvironment(jsonObject.getAsJsonObject("fields").getAsJsonObject("environment").get("name").getAsString());
		}

		if(!jsonObject.getAsJsonObject("fields").get("resolutiondate").isJsonNull()){
			issue.setResolutionDate(jsonObject.getAsJsonObject("fields").get("resolutiondate").getAsString());
		}
		
		if(!jsonObject.getAsJsonObject("fields").get("created").isJsonNull()){
			issue.setCreateDate(jsonObject.getAsJsonObject("fields").get("created").getAsString());
		}
	}
	
	public static void parseJsonForEnvironment(JiraIssue issue, String json, NewFeatureSettings featureSettings){
		JsonObject jsonObject = new GsonBuilder().create().fromJson(json, JsonObject.class);
		
		if(!jsonObject.getAsJsonObject("fields").get("environment").isJsonNull()){
			issue.setEnvironment(jsonObject.getAsJsonObject("fields").get("environment").getAsString());
		}		
	}

}
