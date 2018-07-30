package com.india.tamilnadu.dao;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.jaxrs.Tally;
import com.india.tamilnadu.tally.bc.TallyDayBookBC;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.util.Constants;
import com.india.tamilnadu.util.Utility;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Role;
import com.india.tamilnadu.vo.User;

public class AuthenticationDAO {

	private final Logger LOG = LoggerFactory.getLogger(AuthenticationDAO.class);
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public User authenticate(String userName, byte[] pwd, String userType) throws Exception {
		
		User user = new User();
		
		try {
			
			System.out.println("Hashed pwd : " + new String(pwd));
			System.out.println("userType : " + userType);
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_AUTHENTICATION);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, new String(pwd));
			preparedStatement.setString(3, "active");
			preparedStatement.setString(4, userType);
			resultSet = preparedStatement.executeQuery();
		
			System.out.println("resultset : " + resultSet);
			
			if(resultSet.next()) {
				
				System.out.println("resultset has value");
				
				//user = new User();
				user.setCompanyId(resultSet.getString(1));
				user.setRole(resultSet.getString(2));
				user.setAuthenticate(true);
				
			} else {
				throw new AuthenticationException();
			}
			
			if(null != userType && userType.equalsIgnoreCase("EXTERNAL")) {
				
				System.out.println("get customer detail");
				
				//get customer id from external user table and customer name from customer table 
				preparedStatement = connection.prepareStatement(Constants.DB_GET_CUSTOMER_DETAIL);
				preparedStatement.setString(1, userName);
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					
					System.out.println("get customer resultset has value");
					System.out.println("cust id : " + resultSet.getString(1));
					System.out.println("cust name : " + resultSet.getString(2));
					
					//user = new User();
					user.setCustomerId(resultSet.getString(1));
					user.setCustomerName(resultSet.getString(2));
					
				} else {
					throw new AuthenticationException();
				}
			}
			
		} catch (AuthenticationException e) {
			// TODO: handle exception
			System.out.println("Authentication failed...");
			throw new AuthenticationException();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in Authentication...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return user;
	}
	
	public User insertPwd(String userName, String pwd) throws Exception {
		
		User user = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("INSERT INTO USERS(USER_NAME, PWD, ROLE_ID, USER_STATUS, COMPANY_ID) VALUES (?, ?, 3, 'active', 'SPAK')");
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, new String(pwd));
			preparedStatement.executeUpdate();
			
		} catch (AuthenticationException e) {
			// TODO: handle exception
			System.out.println("Authentication failed...");
			throw new AuthenticationException();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return user;
	}
	
	public User updatePwd(String userName, String pwd) throws Exception {
		
		User user = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("UPDATE USERS SET PWD = ? WHERE USER_NAME = ?");
			preparedStatement.setString(1, new String(pwd));
			preparedStatement.setString(2, userName);
			preparedStatement.executeUpdate();
			
		} catch (AuthenticationException e) {
			// TODO: handle exception
			System.out.println("Authentication failed...");
			throw new AuthenticationException();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return user;
	}
	
	public List<User> getUsers(String companyId, String userType) throws Exception {
		
		User user = null;
		List<User> users = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_USERS);
			preparedStatement.setString(1, companyId);
			preparedStatement.setString(2, userType); //INTERNAL / EXTERNAL
			resultSet = preparedStatement.executeQuery();
		
			
			while(resultSet.next()) {
				
				user = new User();
				user.setUserName(resultSet.getString(1));
				user.setRole(resultSet.getString(2));
				user.setUserStatus(resultSet.getString(3));
				user.setCreatedDate(resultSet.getString(4));
				user.setRoleName(resultSet.getString(5));
				user.setFirstName(resultSet.getString(6));
				user.setLastName(resultSet.getString(7));
				
				users.add(user);
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting users from DB...");
			e.printStackTrace();
			throw new Exception("Server error");
		} finally {
			closeResources();
		}
		
		return users;
	}
	
	public List<User> getAllUsers() throws Exception {
		
		User user = null;
		List<User> users = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_ALL_USERS);
			resultSet = preparedStatement.executeQuery();
		
			
			while(resultSet.next()) {
				
				user = new User();
				user.setUserName(resultSet.getString(1));
				user.setRole(resultSet.getString(2));
				user.setUserStatus(resultSet.getString(3));
				user.setCreatedDate(resultSet.getString(4));
				user.setRoleName(resultSet.getString(5));
				user.setFirstName(resultSet.getString(6));
				user.setLastName(resultSet.getString(7));
				user.setCompanyId(resultSet.getString(8));
				
				users.add(user);
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting users from DB...");
			e.printStackTrace();
			throw new Exception("Server error");
		} finally {
			closeResources();
		}
		
		return users;
	}

	public List<Role> getRoles() throws Exception {
		
		Role role = null;
		List<Role> roles = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_ROLES);
			resultSet = preparedStatement.executeQuery();
		
			
			while(resultSet.next()) {
				
				role = new Role();
				role.setRoleId(resultSet.getString(1));
				role.setRoleName(resultSet.getString(2));
				
				roles.add(role);
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting roles from DB...");
			e.printStackTrace();
			throw new Exception("Server error");
		} finally {
			closeResources();
		}
		
		return roles;
	}
	
	public void updateUsers(User user) throws Exception {
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_USERS);
			preparedStatement.setString(1, user.getRole());
			preparedStatement.setString(2, user.getUserStatus());
			preparedStatement.setDate(3, Utility.getCurrentdate());
			preparedStatement.setString(4, user.getUserName());
			preparedStatement.setString(5, user.getCompanyId());
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in updating users in DB...");
			e.printStackTrace();
			throw new Exception("Server error");
		} finally {
			closeResources();
		}
	}

	public void updateExternalUsers(User user) throws Exception {
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_EXTERNAL_USERS);
			preparedStatement.setString(1, user.getUserStatus());
			preparedStatement.setDate(2, Utility.getCurrentdate());
			preparedStatement.setString(3, user.getUserName());
			preparedStatement.setString(4, user.getCompanyId());
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in updating external users in DB...");
			e.printStackTrace();
			throw new Exception("Server error");
		} finally {
			closeResources();
		}
	}
	
	public void addUser(LoginUser loginUser, int role) throws Exception {
		
		try {
			
			LOG.info(LOG_BASE_FORMAT, null, "addUser, In");
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_ADD_USERS);
			preparedStatement.setString(1, loginUser.getEmail());
			preparedStatement.setString(2, loginUser.getPassword());
			preparedStatement.setString(3, loginUser.getCompanyId());
			//preparedStatement.setInt(4, 2); //role
			preparedStatement.setInt(4, role); //role
			preparedStatement.setString(5, "inactive"); //status
			preparedStatement.setDate(6, Utility.getCurrentdate());
			preparedStatement.setDate(7, Utility.getCurrentdate());
			preparedStatement.setString(8, loginUser.getFirstName());
			preparedStatement.setString(9, loginUser.getLastName());
			preparedStatement.setString(10, loginUser.getUserType());
			
			int result = preparedStatement.executeUpdate();
			
			LOG.info(LOG_BASE_FORMAT, null, "addUser, insert status : " + result);
			LOG.info(LOG_BASE_FORMAT, null, "addUser, Out");
			
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(LOG_BASE_FORMAT, null, "addUser, Exception : " + e.getMessage());
			System.out.println("Error in adding users in DB...");
			e.printStackTrace();
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				LOG.warn(LOG_BASE_FORMAT, "User is already available");
				throw new Exception(e.getMessage());
				
			} else {
				throw new RuntimeException(e);
			}
			
		} finally {
			closeResources();
		}
	}
	
	public void addExternalUser(String trackingId, String userName, String copanyId, String customerId) throws Exception {
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_ADD_EXTERNAL_USERS);
			preparedStatement.setString(1, customerId);
			preparedStatement.setString(2, userName);
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in adding external users in DB...");
			e.printStackTrace();
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				LOG.warn(LOG_BASE_FORMAT, "User is already available");
				throw new Exception(e.getMessage());
				
			} else {
				throw new RuntimeException(e);
			}
			
		} finally {
			closeResources();
		}
	}

	private void closeResources() {
		
		try {
			if(null != preparedStatement) {
				preparedStatement.close();
			}
			
			if(null != resultSet) {
				resultSet.close();
			}
			
			if(null != connection) {
				connection.close();
			}
		} catch (SQLException sqlException) {
			// TODO: handle exception
			System.out.println("Error in closing DB resources...");
			sqlException.printStackTrace();
		}
	}
}
