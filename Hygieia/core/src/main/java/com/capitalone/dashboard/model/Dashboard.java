package com.capitalone.dashboard.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A collection of widgets, collectors and application components that represent a software
 * project under development and/or in production use.
 *
 */
@Document(collection="dashboards")
public class Dashboard extends BaseModel {
    private String template;

    //NOTE Mongodb treats strings as different if they have different case
    @Indexed(unique=true)
    private String title;

    private List<Widget> widgets = new ArrayList<>();
    private String owner;
    private Set<String> usersList;
    private DashboardType type;
    private String createdOn;
    private String updatedOn;
    private Application application;
    
    private ObjectId projectId;

    public ObjectId getProjectId() {
		return projectId;
	}

	public void setProjectId(ObjectId projectId) {
		this.projectId = projectId;
	}

	Dashboard() {
    }

    public Dashboard(String template, String title, Application application,String owner, DashboardType type) {
        this.template = template;
        this.title = title;
        this.application = application;
        this.owner = owner;
        this.type = type;
    }
    public Dashboard(String template, String title, Application application,String owner, DashboardType type, ObjectId projectId) {
        this.template = template;
        this.title = title;
        this.application = application;
        this.owner = owner;
        this.type = type;
        this.projectId=projectId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

    public DashboardType getType(){ return this.type; }

    public void setType(DashboardType type) { this.type = type; }

	public Set<String> getUsersList() {
		return usersList;
	}

	public void setUsersList(Set<String> usersList) {
		this.usersList = usersList;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public void setWidgets(List<Widget> widgets) {
		this.widgets = widgets;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
    
	@Override
	public boolean equals(Object o) {
		Dashboard that = (Dashboard) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.getId().toString(), that.getId().toString()).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.getId()).toHashCode();
	}
    

}
