package com.capitalone.dashboard.request;
/*
 * This will capture the auth response.
 */

public class AuthenticationResponse {
	
	private boolean isAuthenticated;
	private boolean isSysAdmin;
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
	public boolean isSysAdmin() {
		return isSysAdmin;
	}
	public void setSysAdmin(boolean isSysAdmin) {
		this.isSysAdmin = isSysAdmin;
	}
	
	
	
	

}
