package com.capitalone.dashboard.request;

import com.capitalone.dashboard.model.CollectorItem;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class CollectorItemRequest {
    @NotNull
    private ObjectId collectorId;

    private String description;
    private Map<String,Object> options = new HashMap<>();
    private String project;

    public ObjectId getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(ObjectId collectorId) {
        this.collectorId = collectorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
    
    public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public CollectorItem toCollectorItem() {
        CollectorItem item = new CollectorItem();
        item.setCollectorId(collectorId);
        item.setProject(project);
        item.setDescription(description);
        item.setEnabled(true);
        item.getOptions().putAll(options);
        return item;
    }
}
