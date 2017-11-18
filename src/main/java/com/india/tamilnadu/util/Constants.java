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
	
	public static final String DB_GET_DAYBOOK_MASTER = "select VOUCHER_KEY, VCH_TYPE, VOUCHER_ACTION, VOUCHER_DATE, VOUCHER_TYPE_NAME, VOUCHER_NUMBER, PARTY_LEDGER_NAME, EFFECTIVE_DATE, PERSISTED_VIEW, ALTER_ID, MASTER_ID, LEDGER_NAME, FLAG, CREATED_DATE, MODIFIED_DATE from DAYBOOK_MASTER where flag = false order by VOUCHER_DATE desc limit 50";
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
	
	public static final String DB_GET_STOCK_MASTER = "SELECT VOUCHER_TYPE, VOUCHER_ACTION, DATE_ALT, DATE_ENT, VOUCHER_TYPE_NAME, VOUCHER_NUMBER, VOUCHER_KEY, EFFECTIVE_DATE,PERSISTED_VIEW, ALTER_ID, MASTER_ID, OPR_DATE, REEL_WEIGHT, START_TIME, REWIND_START, REWIND_END, OPERATED_BY, FOREMAN1, FOREMAN2 FROM stock_master";
	public static final String DB_GET_STOCK_DETAIL = "SELECT STOCK_DETAILS_ID, STOCK_ITEM_NAME, RATE, AMOUNT, BILLED_QTY, ACTUAL_QTY, STATUS, VOUCHER_KEY FROM stock_details";
	public static final String DB_GET_STOCK_ITEM_DETAIL = "SELECT STOCK_ITEM_DETAILS_ID, GSM_TGT, GSM_ACT, BF_TGT, BF_ACT, SIZE_ACT, REEL_LEN, JOINTS, REEL_DIA, MOIST, SIZE_TGT1, SIZE_ACT1, LENGTH1, TEMP, UNITS, VOUCHER_KEY, STOCK_DETAILS_ID FROM stock_item_details";
	
	public static final String DB_GET_STOCKS = "SELECT SM.VOUCHER_NUMBER, SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SD.RATE, SD.AMOUNT, SD.BILLED_QTY, SID.STOCK_ITEM_DETAILS_ID, SID.GSM_TGT, SID.GSM_ACT, SID.BF_TGT, SID.BF_ACT, SID.SIZE_ACT, SID.REEL_LEN, SID.JOINTS, SID.REEL_DIA, SID.MOIST, SID.SIZE_TGT1, SID.SIZE_ACT1, SID.LENGTH1, SID.TEMP, SID.UNITS, SID.VOUCHER_KEY FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID";
	
	public static final String DB_GET_STOCK_GSM_DETAIL = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.GSM_TGT, SID.GSM_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID";
	public static final String DB_GET_STOCK_BF_DETAIL = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BF_TGT, SID.BF_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID";
	
	public static final String DB_GET_STOCK_GSM_LAST_7_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.GSM_TGT, SID.GSM_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 7 DAY)";
	public static final String DB_GET_STOCK_GSM_LAST_30_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.GSM_TGT, SID.GSM_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 30 DAY)";
	
	public static final String DB_GET_STOCK_BF_LAST_7_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BF_TGT, SID.BF_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 7 DAY)";
	public static final String DB_GET_STOCK_BF_LAST_30_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BF_TGT, SID.BF_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 30 DAY)";
	
	public static final String DB_GET_PRODUCTION_YEAR = "select sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key";
	public static final String DB_GET_PRODUCTION_QUARTER = "select QUARTER(now()) qy, sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key group by qy"; 
	//public static final String DB_GET_PRODUCTION_MONTH = "select  sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key group by dayofmonth(now())";
	public static final String DB_GET_PRODUCTION_MONTH = "select  YEAR(NOW()) as year1, MONTH(NOW()) as month1,  sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key and YEAR(sm.effective_date)=YEAR(NOW()) AND MONTH(sm.effective_date)=MONTH(NOW()) group by YEAR(NOW()),MONTH(NOW())";
	public static final String DB_GET_PRODUCTION_WEEK = "select  week(sm.effective_date) week_num, sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key group by week(sm.effective_date) order by week_num desc";
	
	public static final String DB_GET_PRODUCTION_DASHBOARD_CHART = "select sm.effective_date ed, sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key group by ed"; 
	
	public static final String VOUCHER_TYPE = "VOUCHER_TYPE";
	public static final String VOUCHER_ACTION = "VOUCHER_ACTION";
	public static final String DATE_ALT = "DATE_ALT";
	public static final String DATE_ENT = "DATE_ENT";
	public static final String VOUCHER_TYPE_NAME = "VOUCHER_TYPE_NAME";
	public static final String VOUCHER_NUMBER = "VOUCHER_NUMBER";
	public static final String VOUCHER_KEY = "VOUCHER_KEY";
	public static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
	public static final String PERSISTED_VIEW = "PERSISTED_VIEW";
	public static final String ALTER_ID = "ALTER_ID";
	public static final String MASTER_ID = "MASTER_ID";
	public static final String OPR_DATE = "OPR_DATE";
	public static final String REEL_WEIGHT = "REEL_WEIGHT";
	public static final String START_TIME = "START_TIME";
	public static final String REWIND_START = "REWIND_START";
	public static final String REWIND_END = "REWIND_END";
	public static final String OPERATED_BY = "OPERATED_BY";
	public static final String FOREMAN1 = "FOREMAN1";
	public static final String FOREMAN2 = "FOREMAN2";
	public static final String STOCK_DETAILS_ID = "STOCK_DETAILS_ID";
	public static final String STOCK_ITEM_NAME = "STOCK_ITEM_NAME";
	//public static final String RATE = "RATE";
	//public static final String AMOUNT = "AMOUNT";
	public static final String BILLED_QTY = "BILLED_QTY";
	public static final String ACTUAL_QTY = "ACTUAL_QTY";
	public static final String STATUS = "STATUS";
	public static final String STOCK_ITEM_DETAILS_ID = "STOCK_ITEM_DETAILS_ID";
	public static final String GSM_TGT = "GSM_TGT";
	public static final String GSM_ACT = "GSM_ACT";
	public static final String BF_TGT = "BF_TGT";
	public static final String BF_ACT = "BF_ACT";
	public static final String SIZE_ACT = "SIZE_ACT";
	public static final String REEL_LEN = "REEL_LEN";
	public static final String JOINTS = "JOINTS";
	public static final String REEL_DIA = "REEL_DIA";
	public static final String MOIST = "MOIST";
	public static final String SIZE_TGT1 = "SIZE_TGT1";
	public static final String SIZE_ACT1= "SIZE_ACT1";
	public static final String LENGTH1 = "LENGTH1";
	public static final String TEMP = "TEMP";
	public static final String UNITS = "UNITS";
	public static final String DB_RATE = "RATE";
	public static final String DB_AMOUNT = "AMOUNT";
}
