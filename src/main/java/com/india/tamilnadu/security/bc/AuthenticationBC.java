package com.india.tamilnadu.security.bc;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dao.AuthenticationDAO;
import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.jaxrs.TallyServiceImpl;
import com.india.tamilnadu.security.util.JWTHelper;
import com.india.tamilnadu.tally.bc.CustomerBC;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Role;
import com.india.tamilnadu.vo.User;

public class AuthenticationBC {
	
	private final Logger LOG = LoggerFactory.getLogger(AuthenticationBC.class);
	
	public User authenticate(String userName, byte[] pwd, String trackingId, String userAgent, String userType) throws Exception {
		
		User user = new User();
		
		//authenticate user against Hardcoded logic
		//user = authenticateCredentials(login);
		
		//authenticate user against DB logic
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		user = authenticationDAO.authenticate(userName, pwd, userType);
		
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
	
	public List<User> getUsers(String companyId, String trackingId, String userType) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		return authenticationDAO.getUsers(companyId, userType);
	}

	public List<User> getAllUsers(String trackingId) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		return authenticationDAO.getAllUsers();
	}

	public List<Role> getRoles(String trackingId) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		return authenticationDAO.getRoles();
	}
	
	public void updateUser(String trackingId, User user) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		authenticationDAO.updateUsers(user);
	}
	
	public void updateExternalUser(String trackingId, User user) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		authenticationDAO.updateExternalUsers(user);
	}

	public void addUser(String trackingId, LoginUser loginUser, int role) throws Exception {
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		authenticationDAO.addUser(loginUser, role);
	}
	
	public Response customerRegistration(String trackingId, LoginUser loginUser) throws Exception {
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration In");
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, Validating company and customer information ....");
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, User type : " + loginUser.getUserType());
		
		//check the company id is valid, if not return error
		//check the customer name or gst number is there in the customer table , if not there send error message.
		TallyDAO tallyDAO = new TallyDAO();
		Response response = tallyDAO.validateCustomerEligibility(trackingId, loginUser);
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, Vaidation status : " + response.getStatus());
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, Validation status message : " + response.getStatusMessage());
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, Customer ID : " + response.getCustomerId());
		
		if(response.getStatus().equalsIgnoreCase("Success")) {
			
			LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, adding user in user table ....");
			
			//insert data in user table as inactive
			AuthenticationDAO authenticationDAO = new AuthenticationDAO();
			if(loginUser.getUserType().equalsIgnoreCase("supplier")) {
				loginUser.setUserType("EXTERNAL");
				authenticationDAO.addUser(loginUser, 6);
			} else if(loginUser.getUserType().equalsIgnoreCase("customer")) {
				loginUser.setUserType("EXTERNAL");
				authenticationDAO.addUser(loginUser, 5);
			}
			
			LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration, Adding user name and customer id in external table ....");
			
			//insert external user table
			authenticationDAO.addExternalUser(trackingId, loginUser.getEmail(), loginUser.getCompanyId(), response.getCustomerId());
		}
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration Out");
		
		return response;
	}
}
