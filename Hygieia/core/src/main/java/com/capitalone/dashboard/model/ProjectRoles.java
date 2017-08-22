package com.capitalone.dashboard.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ProjectRoles {

	private String role;
	private Set<String> permissions=new HashSet<String>();
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Set<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProjectRoles that = (ProjectRoles) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(role, that.role).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(role).toHashCode();
	}
	
	
}
