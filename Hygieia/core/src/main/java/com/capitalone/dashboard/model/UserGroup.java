package com.capitalone.dashboard.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserGroup {
	private String user;
	private Set<ProjectRoles> userRoles= new HashSet<ProjectRoles>();
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Set<ProjectRoles> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Set<ProjectRoles> userRoles) {
		this.userRoles = userRoles;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserGroup that = (UserGroup) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(user, that.user).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(user).toHashCode();
	}
	public UserGroup(String user) {
		super();
		this.user = user;
	}
	
	
}
