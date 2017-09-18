package com.india.tamilnadu.util;

public class Constants {

	public static final String DB_GET_PRODUCTS_DETAIL = "select product.productid, product.productCode , product.name , product.quantity , product.price,  supplier.supplierID , supplier.name , supplier.phone from "
														 + "products product, suppliers supplier where supplier.supplierID = product.supplierID";
	public static final String DB_ADD_PRODUCTS = "insert into products(productCode, name, quantity, price, supplierID) values (?, ?, ?, ?, ?)";
	public static final String DB_GET_SUPPLIER_DETAIL = "select supplierID, name, phone from suppliers";

	public static final String DB_ADD_TALLY_SUMMARY = "insert into tally_summary(report_id, report_name, report_key, report_value1, report_value2, created_date, check_flag) values (?, ?, ?, ?, ?, ?, ?)";
	public static final String DB_GET_TALLY_SUMMARY_REPORT_ID_NEXTVAL = "select max(report_id) report_id from tally_summary";
	public static final String DB_GET_TALLY_SUMMARY = "select tally_summary_id, report_id, report_name, report_key, report_value1, report_value2, created_date, check_flag from tally_summary where report_id in (select max(report_id) from tally_summary)";
	public static final String DB_UPDATE_TALLY_SUMMARY = "update tally_summary set check_flag = true where tally_summary_id = ? and report_id = ?";
	
	
	public static final String RESPONSE_STATUS_SUCCESS = "Success";
	public static final String RESPONSE_STATUS_FAILED = "Fauliure";
	public static final String RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS = "Products added successfully";
	public static final String RESPONSE_MESSAGE_PRODUCT_ADD_FAILED = "Products are not added";
	
	public static final String REPORT_ID = "report_id";
	
	
}
