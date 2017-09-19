package com.capitalone.dashboard.model;

import java.nio.charset.StandardCharsets;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.hash.Hashing;

/**
 * This class serves as the model for storing credential used for login & Signup.
 */


@Document(collection = "authentication")
public class Authentication extends BaseModel {
    static final String HASH_PREFIX = "sha512:";

    @Indexed(unique = true)
    private String username;

    private String password;
    
    private boolean isSysAdmin;


    public Authentication(String username, String password) {
        this.username = username;
        this.password = hash(password);
    }
    
    
    public Authentication() {
		super();
	}



	public Authentication(String username, String password, boolean isSysAdmin) {
		this.username = username;
		this.password = password;
		this.isSysAdmin = isSysAdmin;
	}



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
        this.password = hash(password);
    }
    
    public boolean isSysAdmin() {
		return isSysAdmin;
	}

	public void setSysAdmin(boolean isSysAdmin) {
		this.isSysAdmin = isSysAdmin;
	}

	static String hash(String password) {
        if (!password.startsWith(HASH_PREFIX)) {
            return HASH_PREFIX + Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
        }
        return password;
    }

    public boolean isHashed() {
        return password.startsWith(HASH_PREFIX);
    }

    public boolean checkPassword(String password) {
        return hash(this.password).equals(hash(password));
    }

    @Override
    public String toString() {
        return "Authentication [username=" + username + ", password=" + password + "]";
    }
}
