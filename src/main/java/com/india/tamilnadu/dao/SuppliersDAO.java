package com.india.tamilnadu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.jaxrs.Product;
import com.india.tamilnadu.jaxrs.Supplier;
import com.india.tamilnadu.util.Constants;

public class SuppliersDAO implements BaseDAO {
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public List<Supplier> getSuppliers() {
		
		Supplier supplier = null;
		List<Supplier> suppliers =  new ArrayList<Supplier>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SUPPLIER_DETAIL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				supplier = new Supplier();
				supplier.setSupplierID(resultSet.getString("supplierid"));
				supplier.setName(resultSet.getString("name"));
				supplier.setPhone(resultSet.getString("phone"));
				
				suppliers.add(supplier);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting suppliers from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return suppliers;
	}
	
	/*public Response addProducts(List<Product> products) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_ADD_PRODUCTS);
			
			int parameterIndex = 1;
			for(Product product : products) {
				preparedStatement.setString(parameterIndex++, product.getProductCode());
				preparedStatement.setString(parameterIndex++, product.getProductName());
				preparedStatement.setString(parameterIndex++, product.getProductQuantity());
				preparedStatement.setString(parameterIndex++, product.getProductPrice());
				preparedStatement.setString(parameterIndex++, product.getSupplierId());
				
				preparedStatement.executeUpdate();
				parameterIndex = 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in adding products in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			closeResources();
		}
		
		return response;
	}*/
		
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
