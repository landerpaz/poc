package com.india.tamilnadu.dao;

import java.sql.Connection;

public class Sample {

	public static void main(String[] args) {
		try {
			//Connection connection = DatabaseManager.getInstance().getConnection();
			//System.out.println(connection);
			
			ProductsDAO productsDAO = new ProductsDAO();
			productsDAO.getProducts();
			
			
		}	catch (Exception e) {
			e.printStackTrace();
		}
	}
}
