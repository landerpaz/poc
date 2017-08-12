package com.india.tamilnadu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.india.tamilnadu.jaxrs.Product;
import com.india.tamilnadu.util.Constants;

public class ProductsDAO implements BaseDAO {
	
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
