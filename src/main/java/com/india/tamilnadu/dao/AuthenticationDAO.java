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
import com.india.tamilnadu.vo.User;

public class AuthenticationDAO {

	private final Logger LOG = LoggerFactory.getLogger(AuthenticationDAO.class);
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public User authenticate(Login login) throws Exception {
		
		User user = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_AUTHENTICATION);
			preparedStatement.setString(1, login.getEmail());
			preparedStatement.setString(2, login.getPassword());
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
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return user;
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
