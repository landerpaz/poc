package com.india.tamilnadu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.jaxrs.Product;
import com.india.tamilnadu.jaxrs.Tally;
import com.india.tamilnadu.util.Constants;
import com.india.tamilnadu.util.TallyBean;
import com.india.tamilnadu.util.TallyRequestContext;

public class TallyDAO implements BaseDAO {
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public List<Tally> getTallySummary() {
		
		Tally tally = null;
		List<Tally> tallySummaryList =  new ArrayList<Tally>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_TALLY_SUMMARY);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				tally = new Tally();
				tally.setTallySummaryIid(resultSet.getString("tally_summary_id"));
				tally.setReportId(resultSet.getString("report_id"));
				tally.setReportName(resultSet.getString("report_name"));
				tally.setReportKey(resultSet.getString("report_key"));
				tally.setReportValue1(resultSet.getString("report_value1"));
				tally.setReportValue2(resultSet.getString("report_value2"));
				tally.setCreatedTime(resultSet.getString("created_date"));
				tally.setCheckFlag(resultSet.getString("check_flag"));
				
				tallySummaryList.add(tally);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return tallySummaryList;
	}
	
	public int getNextValueForReportId() {
		
		int nextVal = 0;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_TALLY_SUMMARY_REPORT_ID_NEXTVAL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				nextVal = resultSet.getInt(Constants.REPORT_ID);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting products from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return nextVal + 1;
	}
	
	public Response addTallySummary(TallyRequestContext context) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			int batchSize = 100;
			int count = 0;
			java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
			
			connection = DatabaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Constants.DB_ADD_TALLY_SUMMARY);
			
			int parameterIndex = 1;
			for(int index=0; index<context.getKeys().size(); index++) {
				preparedStatement.setInt(parameterIndex++, context.getReportId());
				preparedStatement.setString(parameterIndex++, context.getReportName());
				preparedStatement.setString(parameterIndex++, context.getKeys().get(index));
				preparedStatement.setString(parameterIndex++, context.getValues1().get(index));
				preparedStatement.setString(parameterIndex++, context.getValues2().get(index));
				preparedStatement.setDate(parameterIndex++, date);
				preparedStatement.setBoolean(parameterIndex++, context.isCheckFlag());
				preparedStatement.addBatch();
				
				parameterIndex = 1;
				count++;
				
				if(count >= batchSize) {
					preparedStatement.executeBatch();
					connection.commit();
					count = 0;
				}
				
			}
			
			if(count > 0) {
				preparedStatement.executeBatch();
				connection.commit();
			}
			
		} catch (Exception e) {
			
			try {
				if(null != connection) {
					connection.rollback();
				}
			} catch (SQLException sqlException) {
				// TODO: handle exception
				System.out.println("Error in connection rollback...");
				sqlException.printStackTrace();
			}
			
			// TODO: handle exception
			System.out.println("Error in adding products in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			closeResources();
		}
		
		return response;
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
