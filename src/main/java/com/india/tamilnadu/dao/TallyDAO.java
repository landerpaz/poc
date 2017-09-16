package com.india.tamilnadu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.jaxrs.Product;
import com.india.tamilnadu.util.Constants;
import com.india.tamilnadu.util.TallyRequestContext;

public class TallyDAO implements BaseDAO {
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public List<Product> getProducts() {
		
		Product product = null;
		List<Product> products =  new ArrayList<Product>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTS_DETAIL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				product = new Product();
				product.setProductId(resultSet.getString("product.productid"));
				product.setProductCode(resultSet.getString("product.productCode"));
				product.setProductName(resultSet.getString("product.name"));
				product.setProductQuantity(resultSet.getString("product.quantity"));
				product.setProductPrice(resultSet.getString("product.price"));
				product.setSupplierId(resultSet.getString("supplier.supplierID"));
				product.setSupplierName(resultSet.getString("supplier.name"));
				product.setSupplierPhone(resultSet.getString("supplier.phone"));
				
				products.add(product);
				
				System.out.println(resultSet.getString("product.productid"));
				System.out.println(resultSet.getString("product.productCode"));
				System.out.println(resultSet.getString("product.name"));
				System.out.println(resultSet.getString("product.quantity"));
				System.out.println(resultSet.getString("product.price"));
				System.out.println(resultSet.getString("supplier.supplierID"));
				System.out.println(resultSet.getString("supplier.name"));
				System.out.println(resultSet.getString("supplier.phone"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting products from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return products;
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
				preparedStatement.setString(parameterIndex++, context.getReportName());
				preparedStatement.setString(parameterIndex++, context.getKeys().get(index));
				preparedStatement.setString(parameterIndex++, context.getValues1().get(index));
				preparedStatement.setString(parameterIndex++, context.getValues2().get(index));
				preparedStatement.setDate(parameterIndex++, date);
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
