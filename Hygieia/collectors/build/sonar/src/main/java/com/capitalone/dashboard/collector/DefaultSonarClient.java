package com.capitalone.dashboard.collector;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.model.CodeQuality;
import com.capitalone.dashboard.model.CodeQualityMetric;
import com.capitalone.dashboard.model.CodeQualityMetricStatus;
import com.capitalone.dashboard.model.CodeQualityType;
import com.capitalone.dashboard.model.SonarProject;
import com.capitalone.dashboard.util.SonarDashboardUrl;
import com.capitalone.dashboard.util.Supplier;

@Component
public class DefaultSonarClient implements SonarClient {
    private static final Log LOG = LogFactory.getLog(DefaultSonarClient.class);

    //private static final String URL_RESOURCES = "/api/resources?format=json";
    //private static final String URL_RESOURCE_DETAILS = "/api/resources?format=json&resource=%s&metrics=%s&includealerts=true";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KEY = "metric";
    private static final String VERSION = "version";
    private static final String MSR = "measures";
    private static final String ALERT = "alert";
    private static final String ALERT_TEXT = "alert_text";
    private static final String VALUE = "value";
    private static final String FORMATTED_VALUE = "frmt_val";
    private static final String STATUS_WARN = "WARN";
    private static final String STATUS_ALERT = "ALERT";
    private static final String DATE = "date";
    private static final String DATA="value";
    private static final String ERROR="ERROR";
    private static final String FAILED="FAILED";
    private static final String QUALITY_GATE_LEVEL="level";
    private static final String QUALITY_GATE_DETAILS="quality_gate_details";
    private static final String SECURITY_GATE_DETAILS="security_rating";
    private static final String RELIABILITY_GATE_DETAILS="reliability_rating";
    private static final String MAINTAINABILITY_GATE_DETAILS="sqale_rating";
    //security_rating , reliability_rating, sqale_rating(maintainability) , 
    private final RestOperations rest;
    private final SonarSettings sonarSettings;

    @Autowired
    public DefaultSonarClient(Supplier<RestOperations> restOperationsSupplier, SonarSettings settings) {
        this.rest = restOperationsSupplier.get();
        this.sonarSettings = settings;
    }

    @Override
    public List<SonarProject> getProjects(int index) {
        List<SonarProject> projects = new ArrayList<>();
        String url = sonarSettings.getServers().get(index) + sonarSettings.getComponentUrls().get(index);

        try {

            for (Object obj : parseAsArray(url, index)) {
                JSONObject prjData = (JSONObject) obj;

                SonarProject project = new SonarProject();
                project.setInstanceUrl(sonarSettings.getServers().get(index));
                project.setProjectId(str(prjData, ID));
                project.setProjectName(str(prjData, NAME));
                
                project.setProject(sonarSettings.getProjects().get(index));
                
                projects.add(project);
            }

        } catch (ParseException e) {
            LOG.error("Could not parse response from: " + url, e);
        } catch (RestClientException rce) {
            LOG.error(rce);
        }

        return projects;
    }

    @Override
    public CodeQuality currentCodeQuality(SonarProject project, int index) {
        String url = String.format(
                project.getInstanceUrl() + sonarSettings.getComponentDetailUrls().get(index), project.getProjectId(), sonarSettings.getMetrics());

        try {
        	JSONObject prjData = parseMeasuresJson(url,index);

            if (prjData != null) {
            	CodeQuality codeQuality = new CodeQuality();
                codeQuality.setName(str(prjData, NAME));
                codeQuality.setUrl(new SonarDashboardUrl(project.getInstanceUrl(), project.getProjectId()).toString());
                codeQuality.setType(CodeQualityType.StaticAnalysis);
                codeQuality.setTimestamp(timestamp(prjData, DATE));
                codeQuality.setVersion(str(prjData, VERSION));

                for (Object metricObj : (JSONArray) prjData.get(MSR)) {
                    JSONObject metricJson = (JSONObject) metricObj;

                    CodeQualityMetric metric = new CodeQualityMetric(str(metricJson, KEY));
                    metric.setValue(metricJson.get(VALUE));
                    metric.setFormattedValue(str(metricJson, FORMATTED_VALUE));
                    metric.setStatus(metricStatus(str(metricJson, ALERT)));
                    metric.setStatusMessage(str(metricJson, ALERT_TEXT));
                    if(metric.getName().equals(QUALITY_GATE_DETAILS)){
                         JSONObject qualityGate= (JSONObject) new JSONParser().parse(str(metricJson, DATA).replaceAll("'\'",""));
                         metric.setStatus(metricStatus(STATUS_ALERT));
                         metric.setFormattedValue(qualityGate.get(QUALITY_GATE_LEVEL).toString());
                         //ERROR is setting as FAILED to show the Quality gate status as FAILED in UI, instead of error.
                         if(metric.getFormattedValue().equals(ERROR)){
                        	 metric.setFormattedValue(FAILED);
                         }
                    }
                    if(metric.getName().equals(SECURITY_GATE_DETAILS) && metric.getValue() != null){
                    	switch(new Double(metric.getValue().toString()).intValue()){
                    	case 1 : metric.setStatusMessage("0 vulnerability");
                    	case 2: metric.setStatusMessage("At least 1 minor vulnerability");
                    	case 3: metric.setStatusMessage("At least 1 major vulnerability");
                    	case 4: metric.setStatusMessage("At least 1 critical vulnerability");
                    	case 5: metric.setStatusMessage("At least 1 blocker vulnerability");
                    	}
                    }
                    if(metric.getName().equals(RELIABILITY_GATE_DETAILS) && metric.getValue() != null){
                    	switch(new Double(metric.getValue().toString()).intValue()){
                    	case 1 : metric.setStatusMessage("0 bug");
                    	case 2: metric.setStatusMessage("At least 1 minor bug");
                    	case 3: metric.setStatusMessage("At least 1 major bug");
                    	case 4: metric.setStatusMessage("At least 1 critical bug");
                    	case 5: metric.setStatusMessage("At least 1 blocker bug");
                    	}
                    }
                    
                    if(metric.getName().equals(MAINTAINABILITY_GATE_DETAILS) && metric.getValue() != null ){
                    	switch(new Double(metric.getValue().toString()).intValue()){
                    	case 1 : metric.setStatusMessage("<=5%");
                    	case 2: metric.setStatusMessage("between 6 to 10% ");
                    	case 3: metric.setStatusMessage("between 11 to 20% ");
                    	case 4: metric.setStatusMessage("between 21 to 50% ");
                    	case 5: metric.setStatusMessage(">= 50% ");
                    	}
                    }
                    codeQuality.getMetrics().add(metric);
                }
                return codeQuality;
            }

        } catch (ParseException e) {
            LOG.error("Could not parse response from: " + url, e);
        } catch (RestClientException rce) {
            LOG.error(rce);
        }

        return null;
    }

    private JSONArray parseAsArray(String url, int index) throws ParseException {
        HttpEntity<String> httpHeaders = new HttpEntity<String>(
                createHeaders(sonarSettings.getUsernames().get(index), sonarSettings.getPasswords().get(index)));

        ResponseEntity<String> response = rest.exchange(url, HttpMethod.GET, httpHeaders, String.class);
        return (JSONArray) new JSONParser().parse(str((JSONObject)new JSONParser().parse(response.getBody()), "components"));
    }

    @SuppressWarnings("unused")
	private JSONObject parseMeasuresJson(String url, int index) throws ParseException {
        HttpEntity<String> httpHeaders = new HttpEntity<String>(
                createHeaders(sonarSettings.getUsernames().get(index), sonarSettings.getPasswords().get(index)));
        ResponseEntity<String> response = rest.exchange(url, HttpMethod.GET, httpHeaders, String.class);
        return (JSONObject)((JSONObject)new JSONParser().parse(response.getBody())).get("component");
    }


    private long timestamp(JSONObject json, String key) {
        Object obj = json.get(key);
        if (obj != null) {
            try {
                return new SimpleDateFormat(DATE_FORMAT).parse(obj.toString()).getTime();
            } catch (java.text.ParseException e) {
                LOG.error(obj + " is not in expected format " + DATE_FORMAT, e);
            }
        }
        return 0;
    }

    private String str(JSONObject json, String key) {
        Object obj = json.get(key);
        return obj == null ? null : obj.toString();
    }
    @SuppressWarnings("unused")
    private Integer integer(JSONObject json, String key) {
        Object obj = json.get(key);
        return obj == null ? null : (Integer) obj;
    }

    @SuppressWarnings("unused")
    private BigDecimal decimal(JSONObject json, String key) {
        Object obj = json.get(key);
        return obj == null ? null : new BigDecimal(obj.toString());
    }

    @SuppressWarnings("unused")
    private Boolean bool(JSONObject json, String key) {
        Object obj = json.get(key);
        return obj == null ? null : Boolean.valueOf(obj.toString());
    }

    private CodeQualityMetricStatus metricStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return CodeQualityMetricStatus.Ok;
        }

        switch(status) {
            case STATUS_WARN:  return CodeQualityMetricStatus.Warning;
            case STATUS_ALERT: return CodeQualityMetricStatus.Alert;
            default:           return CodeQualityMetricStatus.Ok;
        }
    }

    private HttpHeaders createHeaders(String username, String password){
        HttpHeaders headers = new HttpHeaders();
        if (username != null && !username.isEmpty() &&
            password != null && !password.isEmpty()) {
          String auth = username + ":" + password;
          byte[] encodedAuth = Base64.encodeBase64(
              auth.getBytes(Charset.forName("US-ASCII"))
          );
          String authHeader = "Basic " + new String(encodedAuth);
          headers.set("Authorization", authHeader);
        }
        return headers;
    }
}
