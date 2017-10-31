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
	
	public static List<JiraIssue> parseDefectsJson(String json){
		JsonArray jsonArr = new GsonBuilder().create().fromJson(json, JsonObject.class).getAsJsonArray("issues");
		
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		for(JsonElement element : jsonArr){
		
			JiraIssue issue = new JiraIssue();
			issue.parseJson(element.toString());
			issues.add(issue);
		}		
		return issues;
	}
	
	public static List<JiraIssue> parseDefectsEnvironmentJson(String json){
		JsonArray jsonArr = new GsonBuilder().create().fromJson(json, JsonObject.class).getAsJsonArray("issues");
		
		List<JiraIssue> issues = new ArrayList<JiraIssue>();
		for(JsonElement element : jsonArr){
		
			JiraIssue issue = new JiraIssue();
			issue.parseJsonForEnvironment(element.toString());
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

}
