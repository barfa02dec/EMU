package com.capitalone.dashboard.service;

import java.nio.charset.StandardCharsets;

import com.capitalone.dashboard.model.Authentication;
import com.capitalone.dashboard.repository.AuthenticationRepository;
import com.capitalone.dashboard.request.AuthenticationResponse;
import com.google.common.hash.Hashing;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationRepository authenticationRepository;

	@Autowired
	public AuthenticationServiceImpl(
			AuthenticationRepository authenticationRepository) {
		this.authenticationRepository = authenticationRepository;
	}

	@Override
	public Iterable<Authentication> all() {

		return authenticationRepository.findAll();
	}

	@Override
	public Authentication get(ObjectId id) {

		Authentication authentication = authenticationRepository.findOne(id);
		return authentication;
	}

	@Override
	public String create(String username, String password) {
		Authentication authentication = new Authentication(username, password);
		try {
			authenticationRepository.save(authentication);
			return "User is created";
		} catch (DuplicateKeyException e) {
			return "User already exists";
		}

	}

	@Override
	public String update(String username, String password) {
		Authentication authentication = authenticationRepository.findByUsername(username);
		if (null != authentication) {
			authentication.setPassword(password);
			authenticationRepository.save(authentication);
			return "User is updated";
		} else {
			return "User Does not Exist";
		}

	}

	@Override
	public void delete(ObjectId id) {
		Authentication authentication = authenticationRepository.findOne(id);
		if (authentication != null) {
			authenticationRepository.delete(authentication);
		}
	}

	@Override
	public void delete(String username) {
		Authentication authentication = authenticationRepository
				.findByUsername(username);
		if (authentication != null) {
			authenticationRepository.delete(authentication);
		}
	}

	@Override
	public AuthenticationResponse authenticate(String username, String password) {
		boolean flag = false;
		Authentication authentication = authenticationRepository.findByUsername(username);
		AuthenticationResponse authResponse= new AuthenticationResponse(false,false);

		if (authentication != null && authentication.checkPassword(password)) {
			flag = true;
			authResponse.setAuthenticated(flag);
			authResponse.setSysAdmin(authentication.isSysAdmin());
			return authResponse;
		}
		return authResponse;

	}

	@Override
	public Authentication getAuthUser(String username) {

		return authenticationRepository.findByUsername(username);
	}

	@Override
	public String changePassword(String username, String password, String newPassword) {
		Authentication authentication = authenticationRepository.findByUsername(username);
		if (null != authentication) {
			if(!hash(newPassword).equals(authentication.getPassword())){
				authentication.setPassword(newPassword);
			}else {
				return "Same Password, Please insert new Password.";
			}
			authenticationRepository.save(authentication);
			return "Password is updated";
		} else {
			return "User Does not Exist";
		}
	}

	static String hash(String password) {
		if (!password.startsWith("sha512:")) {
			return "sha512:" + Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
		}
		return password;
	}
}
