package com.india.tamilnadu.dao;

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
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.Role;
import com.india.tamilnadu.vo.User;

public class AuthenticationDAO {

	private final Logger LOG = LoggerFactory.getLogger(AuthenticationDAO.class);
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public User authenticate(String userName, byte[] pwd) throws Exception {
		
		User user = null;
		
		try {
			
			System.out.println("Hashed pwd : " + new String(pwd));
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_AUTHENTICATION);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, new String(pwd));
			preparedStatement.setString(3, "active");
			resultSet = preparedStatement.executeQuery();
		
			
			if(resultSet.next()) {
				
				user = new User();
				user.setCompanyId(resultSet.getString(1));
				user.setRole(resultSet.getString(2));
				user.setAuthenticate(true);
				
			} else {
				throw new AuthenticationException();
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
	
	public List<User> getUsers(String companyId) throws Exception {
		
		User user = null;
		List<User> users = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_USERS);
			preparedStatement.setString(1, companyId);
			resultSet = preparedStatement.executeQuery();
		
			
			while(resultSet.next()) {
				
				user = new User();
				user.setUserName(resultSet.getString(1));
				user.setRole(resultSet.getString(2));
				user.setUserStatus(resultSet.getString(3));
				user.setCreatedDate(resultSet.getString(4));
				user.setRoleName(resultSet.getString(5));
				
				users.add(user);
				
			} 
			
		} catch (AuthenticationException e) {
			// TODO: handle exception
			System.out.println("Authentication failed...");
			throw new AuthenticationException();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting users from DB...");
			e.printStackTrace();
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
			
		} catch (AuthenticationException e) {
			// TODO: handle exception
			System.out.println("Authentication failed...");
			throw new AuthenticationException();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting roles from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return roles;
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
