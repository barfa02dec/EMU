package com.capitalone.dashboard.request;

import javax.validation.constraints.NotNull;

import com.capitalone.dashboard.model.Authentication;

public class AuthenticationRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private boolean isSysAdmin=false;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isSysAdmin() {
		return isSysAdmin;
	}

	public void setSysAdmin(boolean isSysAdmin) {
		this.isSysAdmin = isSysAdmin;
	}

	public Authentication toAuthentication() {
        return new Authentication(username, password,isSysAdmin);
    }

    public Authentication copyTo(Authentication authentication) {
        Authentication updated = toAuthentication();
        updated.setId(authentication.getId());
        return updated;
    }

}
