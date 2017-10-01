package com.india.tamilnadu.util;

public class Constants {

	public static final String DB_GET_PRODUCTS_DETAIL = "select product.productid, product.productCode , product.name , product.quantity , product.price,  supplier.supplierID , supplier.name , supplier.phone from "
														 + "products product, suppliers supplier where supplier.supplierID = product.supplierID";
	public static final String DB_ADD_PRODUCTS = "insert into products(productCode, name, quantity, price, supplierID) values (?, ?, ?, ?, ?)";
	public static final String DB_GET_SUPPLIER_DETAIL = "select supplierID, name, phone from suppliers";

	public static final String DB_ADD_TALLY_SUMMARY = "insert into tally_summary(report_id, report_name, report_key, report_value1, report_value2, created_date, check_flag) values (?, ?, ?, ?, ?, ?, ?)";
	public static final String DB_GET_TALLY_SUMMARY_REPORT_ID_NEXTVAL = "select max(report_id) report_id from tally_summary";
	public static final String DB_GET_TALLY_SUMMARY = "select tally_summary_id, report_id, report_name, report_key, report_value1, report_value2, created_date, check_flag from tally_summary where check_flag = false and report_id in (select max(report_id) from tally_summary)";
	public static final String DB_UPDATE_TALLY_SUMMARY = "update tally_summary set check_flag = true where tally_summary_id = ? and report_id = ?";
	
	public static final String DB_DELETE_DAYBOOK_MASTER = "delete from inventory.DAYBOOK_MASTER where VOUCHER_KEY = ?";
	public static final String DB_DELETE_DAYBOOK_LEDGER = "delete from inventory.DAYBOOK_INVENTORY where id > 0 and VOUCHER_KEY = ?";
	public static final String DB_DELETE_DAYBOOK_INVENTORY = "delete from inventory.DAYBOOK_LEDGER where id > 0 and VOUCHER_KEY = ?"; 
	
	public static final String DB_ADD_DAYBOOK_MASTER = "insert into DAYBOOK_MASTER(VOUCHER_KEY, VCH_TYPE, VOUCHER_ACTION, VOUCHER_DATE, VOUCHER_TYPE_NAME, VOUCHER_NUMBER, PARTY_LEDGER_NAME, EFFECTIVE_DATE, PERSISTED_VIEW, ALTER_ID, MASTER_ID, LEDGER_NAME, FLAG, CREATED_DATE, MODIFIED_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String DB_ADD_DAYBOOK_LEDGER = "insert into DAYBOOK_LEDGER(LEDGER_NAME, AMOUNT, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE) values (?, ?, ?, ?, ?)";
	public static final String DB_ADD_DAYBOOK_INVENTORY = "insert into DAYBOOK_INVENTORY(STOCK_ITEM_NAME, AMOUNT, RATE, BILLED_QTY, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE) values (?, ?, ?, ?, ?, ?, ?)";
	
	public static final String DB_GET_DAYBOOK_MASTER = "select VOUCHER_KEY, VCH_TYPE, VOUCHER_ACTION, VOUCHER_DATE, VOUCHER_TYPE_NAME, VOUCHER_NUMBER, PARTY_LEDGER_NAME, EFFECTIVE_DATE, PERSISTED_VIEW, ALTER_ID, MASTER_ID, LEDGER_NAME, FLAG, CREATED_DATE, MODIFIED_DATE from DAYBOOK_MASTER where flag = false";
	public static final String DB_GET_DAYBOOK_LEDGER = "select ID, LEDGER_NAME, AMOUNT, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE from DAYBOOK_LEDGER";
	public static final String DB_GET_DAYBOOK_INVENTORY = "select ID, STOCK_ITEM_NAME, AMOUNT, RATE, BILLED_QTY, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE from DAYBOOK_INVENTORY";
	public static final String DB_UPDATE_DAYBOOK_INVENTORY_FLAG = "update DAYBOOK_MASTER set FLAG = 1 where VOUCHER_KEY = ?";
			
	public static final String RESPONSE_STATUS_SUCCESS = "Success";
	public static final String RESPONSE_STATUS_FAILED = "Fauliure";
	public static final String RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS = "Products added successfully";
	public static final String RESPONSE_MESSAGE_PRODUCT_ADD_FAILED = "Products are not added";
	
	public static final String REPORT_ID = "report_id";
	
	
	//tally day book master data
	public static final String TALLYMESSAGE_COUNT_EXP = "count(//ENVELOPE/BODY/DATA/TALLYMESSAGE/VOUCHER)";
	public static final String TALLYMESSAGE_COUNT_EXP_TINY = "count(//TALLYMESSAGE/VOUCHER)";
	
	public static final String ENVELOPE_BODY_DATA_TALLYMESSAGE = "//ENVELOPE/BODY/DATA/TALLYMESSAGE[";
	public static final String ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY = "//TALLYMESSAGE[";
	
	public static final String VCHTYPE = "]/VOUCHER//@VCHTYPE";
	public static final String ACTION = "]/VOUCHER//@ACTION";
	public static final String DATE = "]/VOUCHER//DATE";
	public static final String VOUCHERTYPENAME = "]/VOUCHER//VOUCHERTYPENAME";
	public static final String VOUCHERNUMBER = "]/VOUCHER//VOUCHERNUMBER";
	public static final String PARTYLEDGERNAME = "]/VOUCHER//PARTYLEDGERNAME";
	public static final String VOUCHERKEY = "]/VOUCHER//VOUCHERKEY";
	public static final String EFFECTIVEDATE = "]/VOUCHER//EFFECTIVEDATE";
	public static final String PERSISTEDVIEW = "]/VOUCHER//PERSISTEDVIEW";
	public static final String ALTERID = "]/VOUCHER//ALTERID";
	public static final String MASTERID = "]/VOUCHER//MASTERID";
	
	//tally day book all ledgerlist data
	public static final String VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1 = "count(//ENVELOPE/BODY/DATA/TALLYMESSAGE[";
	public static final String VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1_TINY = "count(//TALLYMESSAGE[";
	public static final String VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_2 = "]/VOUCHER//ALLLEDGERENTRIES.LIST)";
	public static final String VOUCHER_ALLLEDGERENTRIES_LIST = "]/VOUCHER//ALLLEDGERENTRIES.LIST[";
	public static final String LEDGERNAME = "]/LEDGERNAME";
	public static final String AMOUNT = "]/AMOUNT";
	public static final String ISPARTYLEDGER = "]/ISPARTYLEDGER";
	
	//tally day book ledgerlist data
	public static final String VOUCHER_LEDGERENTRIES_LIST_COUNT_1 = "count(//ENVELOPE/BODY/DATA/TALLYMESSAGE[";
	public static final String VOUCHER_LEDGERENTRIES_LIST_COUNT_1_TINY = "count(//TALLYMESSAGE[";
	public static final String VOUCHER_LEDGERENTRIES_LIST_COUNT_2 = "]/VOUCHER//LEDGERENTRIES.LIST)";
	public static final String VOUCHER_LEDGERENTRIES_LIST = "]/VOUCHER//LEDGERENTRIES.LIST[";
	
	//tally day book inventorylist data
	public static final String VOUCHER_INVENTORYENTRIES_LIST_COUNT_1 = "count(//ENVELOPE/BODY/DATA/TALLYMESSAGE[";
	public static final String VOUCHER_INVENTORYENTRIES_LIST_COUNT_1_TINY = "count(//TALLYMESSAGE[";
	public static final String VOUCHER_INVENTORYENTRIES_LIST_COUNT_2 = "]/VOUCHER//ALLINVENTORYENTRIES.LIST)";
	public static final String VOUCHER_INVENTORYENTRIES_LIST = "]/VOUCHER//ALLINVENTORYENTRIES.LIST[";
	public static final String RATE = "]/RATE";
	public static final String BILLEDQTY = "]/BILLEDQTY";
	public static final String STOCKITEMNAME = "]/STOCKITEMNAME";
	
	//tally day book inventory in list data
	public static final String VOUCHER_INVENTORYENTRIES_IN_LIST_COUNT_2 = "]/VOUCHER//INVENTORYENTRIESIN.LIST)";
	public static final String VOUCHER_INVENTORYENTRIES_IN_LIST = "]/VOUCHER//INVENTORYENTRIESIN.LIST[";
	
	public static final String LOG_BASE_FORMAT = String.format("%s={}, %s={}", "trackingid", "message");
	public static final String LOG_DATA_FORMAT = String.format("%s={}, %s={}, %s={}", "trackingid", "message", "data");
}
