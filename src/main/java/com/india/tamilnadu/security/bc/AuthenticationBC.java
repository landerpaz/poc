package com.india.tamilnadu.security.bc;

import java.util.List;

import com.india.tamilnadu.dao.AuthenticationDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.security.util.JWTHelper;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Role;
import com.india.tamilnadu.vo.User;

public class AuthenticationBC {
	
	public User authenticate(String userName, byte[] pwd, String trackingId, String userAgent) throws Exception {
		
		User user = new User();
		
		//authenticate user against Hardcoded logic
		//user = authenticateCredentials(login);
		
		//authenticate user against DB logic
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		user = authenticationDAO.authenticate(userName, pwd);
		
		//if auth success, get jwt and store them in concurrent mapping
		//key - jwt  | value - auth object(created time , exp time)
		if(user.isAuthenticate()) {
			user.setToken(JWTHelper.getJWT(user, userAgent));
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
	
	public List<User> getUsers(String companyId, String trackingId) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		return authenticationDAO.getUsers(companyId);
	}

	public List<Role> getRoles(String trackingId) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		return authenticationDAO.getRoles();
	}
	
	public void updateUser(String trackingId, User user) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		authenticationDAO.updateUsers(user);
	}
	
	public void addUser(String trackingId, LoginUser loginUser) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		authenticationDAO.addUser(loginUser);
	}
}
