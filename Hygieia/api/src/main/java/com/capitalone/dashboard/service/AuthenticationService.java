package com.capitalone.dashboard.service;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Authentication;
import com.capitalone.dashboard.request.AuthenticationResponse;

public interface AuthenticationService {
	
	  /**
     * Fetches all registered users, sorted.
     *
     * @return all users
     */
    Iterable<Authentication> all();


    /**
     * Fetches an AuthenticationObject.
     *
     * @param id authentication unique identifier
     * @return Authentication instance
     */
    Authentication get(ObjectId id);

    /**
     * Creates a new Users and saves it to the store.
     *
     * @param username new Authentication to createCollectorItem
     * @return newly created Authentication object
     */
    String create(String username, String password);

    /**
     * Updates an existing quthentication instance.
     *
     * @param username Authentication to update
     * @return updated Authentication instance
     */
    String update(String username, String password);

    /**
     * Deletes an existing Authentication instance.
     *
     * @param id unique identifier of authentication to delete
     */
    void delete(ObjectId id);
    
    /**
     * Deletes an existing authentication instance
     */
	void delete(String username);
	
	/*
	 * get the auth with user name
	 */
	Authentication getAuthUser(String username);
	
	/**
	 * 
	 * @param username
     * @param password
	 * @return
	 */
	AuthenticationResponse authenticate(String username, String password);
	
	/**
	 * change password 
	 * @param username
	 * @param password
	 * @return
	 */
	String changePassword(String username, String password, String newPassword);
	
	/**
     * Creates a new Users from CSV and saves it to the store.
     *
     * @param username new Authentication to createCollectorItem
     * @return newly created Authentication object
     */
    String createFromCSV(String username, String password);
}
