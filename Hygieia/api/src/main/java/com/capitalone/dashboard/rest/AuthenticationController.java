package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.Authentication;
import com.capitalone.dashboard.request.AuthenticationRequest;
import com.capitalone.dashboard.request.AuthenticationResponse;
import com.capitalone.dashboard.service.AuthenticationService;


@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @RequestMapping(value = "/authenticateUser", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {
        // TODO: should return proper HTTP codes for invalid creds
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.authenticate(request.getUsername(), request.getPassword()));
    }

    @RequestMapping(value = "/registerUser", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthenticationRequest request) {
        // TODO: should return proper HTTP codes for existing users
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.create(request.getUsername(), request.getPassword()));
    }

    @RequestMapping(value = "/updateUser", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@Valid @RequestBody AuthenticationRequest request) {
        // TODO: should return proper HTTP codes for not found users
        // TODO: should validate revalidate current password before allowing changes?
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.update(request.getUsername(), request.getPassword()));
    }
    
    @RequestMapping(value = "/changePassword", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@Valid @RequestBody AuthenticationRequest request) {
        // TODO: should return proper HTTP codes for not found users
        // TODO: should validate revalidate current password before allowing changes?
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.changePassword(request.getUsername(), 
        		request.getPassword(), request.getNewPassword()));
    }
    
	@RequestMapping(value = "/getApplicationUsers", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<String> getAllUsers(){
    	List<String> appUsers= new ArrayList<String>();
    	for(Authentication auth:authenticationService.all()){
    		appUsers.add(auth.getUsername());
    	}
    	
    	return appUsers;
    }
	
	 /**
     *  Create User from CSV File
     *  
     *  put the file on below location:
     *  your-dir\intedd01\Hygieia\api
     *  
     * @param request
     * @return
     */
    @RequestMapping(value = "/createUser", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser() {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.createFromCSV());
    }
}
