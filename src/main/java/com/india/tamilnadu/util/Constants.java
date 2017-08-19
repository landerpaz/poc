package com.india.tamilnadu.util;

public class Constants {

	public static final String DB_GET_PRODUCTS_DETAIL = "select product.productid, product.productCode , product.name , product.quantity , product.price,  supplier.supplierID , supplier.name , supplier.phone from "
														 + "products product, suppliers supplier where supplier.supplierID = product.supplierID";
	public static final String DB_ADD_PRODUCTS = "insert into products(productCode, name, quantity, price, supplierID) values (?, ?, ?, ?, ?)";
	public static final String DB_GET_SUPPLIER_DETAIL = "select supplierID, name, phone from suppliers";

	
	public static final String RESPONSE_STATUS_SUCCESS = "Success";
	public static final String RESPONSE_STATUS_FAILED = "Fauliure";
	public static final String RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS = "Products added successfully";
	public static final String RESPONSE_MESSAGE_PRODUCT_ADD_FAILED = "Products are not added";
		
}
