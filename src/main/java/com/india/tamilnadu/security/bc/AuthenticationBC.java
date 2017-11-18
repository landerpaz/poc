package com.india.tamilnadu.security.bc;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.security.util.JWTHelper;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.User;

public class AuthenticationBC {
	
	public User authenticate(Login login, String trackingId) throws Exception {
		
		User user = new User();
		
		//authenticate user against DB/LDAP/Hardcoded logic
		user = authenticateCredentials(login);
		
		//if auth success, get jwt and store them in concurrent mapping
		//key - jwt  | value - auth object(created time , exp time)
		if(user.isAuthenticate()) {
			user.setToken(JWTHelper.getJWT(user));
		}
		
		return user;
	}
	
	public User authenticateCredentials(Login login) throws Exception {
		
		User user = new User();
			
		if(login.getEmail().equals("kms") && login.getPassword().equals("Spak#007")) {
			user.setAuthenticate(true);
			user.setRole("associate");
			user.setCompanyId("1");
			user.setCompanyName("Spak");
		} else if(login.getEmail().equals("admin") && login.getPassword().equals("Spak#007")) {
			user.setAuthenticate(true);
			user.setRole("admin");
			user.setCompanyId("1");
			user.setCompanyName("Spak");
		} else if(login.getEmail().equals("kms1") && login.getPassword().equals("Spak#007")) {
			user.setAuthenticate(true);
			user.setRole("associate");
			user.setCompanyId("2");
			user.setCompanyName("Google");
		} else if(login.getEmail().equals("admin1") && login.getPassword().equals("Spak#007")) {
			user.setAuthenticate(true);
			user.setRole("admin");
			user.setCompanyId("2");
			user.setCompanyName("Google");
		}
		
		return user;
	}

}
