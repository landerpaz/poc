package com.india.tamilnadu.jaxrs;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;
import static com.india.tamilnadu.util.Constants.LOG_DATA_FORMAT;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.security.bc.AuthenticationBC;
import com.india.tamilnadu.util.Utility;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.User;

public class CustomerServiceImpl implements CustomerService {

	private final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Override
	public javax.ws.rs.core.Response customerRegistration(LoginUser loginUser) {
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "customerRegistration In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			//String scope = JWTHelper.validateJWT(token);
			
			AuthenticationBC authenticationBC = new AuthenticationBC();
			//loginUser.setCompanyId(companyId);
			loginUser.setPassword(new String(Utility.hashPassword(loginUser.getPassword().toCharArray(), loginUser.getEmail().getBytes(), 2, 256)));
			//loginUser.setUserType("EXTERNAL");
			authenticationBC.customerRegistration(trackingId, loginUser);
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "customerRegistration : addUser Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "customerRegistration, exception : ", e.getMessage());
			e.printStackTrace();
			
			/*if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}*/
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				response.setStatus("Duplicate");
				response.setStatusMessage("Duplicate");
				return javax.ws.rs.core.Response.ok(response).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}

	public Response userLogin(String userAgent, Login login) {
		
		String trackingID = Utility.getRandomNumber();
		
		LOG.info(LOG_BASE_FORMAT, trackingID, "userLogin In");
		LOG.info(LOG_BASE_FORMAT, trackingID, "userAgent : " + userAgent);
		
		Response response = new Response();
		response.setStatus("200");
		response.setStatusMessage("AUTH_FAILED");
		
		try {
			
			if(null == login || null == login.getEmail() || null == login.getPassword()) {
				return response;
			}
			
			//authenticate user
			AuthenticationBC authenticationBC = new AuthenticationBC();
			User user = authenticationBC.authenticate(login.getEmail(), 
					Utility.hashPassword(login.getPassword().toCharArray(), login.getEmail().getBytes(), 2, 256), trackingID, userAgent, "EXTERNAL");
			if(user.isAuthenticate()) {
				response.setStatusMessage("AUTH_SUCCESS");
				response.setRole(user.getRole());
				response.setToken(user.getToken());
				response.setFirstName(user.getFirstName());
				response.setLastName(user.getLastName());
				response.setCompanyId(user.getCompanyId());
				response.setCompanyName(user.getCompanyName());
				response.setCustomerId(user.getCustomerId());
				response.setCustomerName(user.getCustomerName());
			}
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "userLogin Out");
		
		} catch (AuthenticationException e) {
			LOG.error(LOG_DATA_FORMAT, trackingID, "exception captured in authenticate", e.getMessage());
			
			response.setStatus("401");
			response.setStatusMessage("AUTH ERROR");
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingID, "exception captured in authenticate", e.getMessage());
			e.printStackTrace();
			
			response.setStatus("500");
			response.setStatusMessage("SYSTEM ERROR");
		}
		
		return response;
	}
}
