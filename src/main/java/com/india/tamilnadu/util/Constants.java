package com.india.tamilnadu.util;

public class Constants {

	public static final String DB_GET_PRODUCTS_DETAIL = "select product.productid, product.productCode , product.name , product.quantity , product.price,  supplier.supplierID , supplier.name , supplier.phone from "
														 + "products product, suppliers supplier where supplier.supplierID = product.supplierID";
	public static final String DB_ADD_PRODUCTS = "insert into products(productCode, name, quantity, price, supplierID) values (?, ?, ?, ?, ?)";
	public static final String DB_GET_SUPPLIER_DETAIL = "select supplierID, name, phone from suppliers";

	/*public static final String DB_ADD_TALLY_SUMMARY = "insert into tally_summary(report_id, report_name, report_key, report_value1, report_value2, created_date, check_flag) values (?, ?, ?, ?, ?, ?, ?)";
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
	*/
	
	//TRIAL_BALANCE
	public static final String DB_ADD_TALLY_SUMMARY = "insert into TRIAL_BALANCE(report_id, report_key, report_value1, report_value2, created_date, check_flag, company_id) values (?, ?, ?, ?, ?, ?, ?)";
	public static final String DB_GET_TALLY_SUMMARY_REPORT_ID_NEXTVAL = "select max(report_id) report_id from TRIAL_BALANCE"; //delete this later as logic is changed to delete all records before insert
	//public static final String DB_GET_TALLY_SUMMARY = "select tally_summary_id, report_id, report_key, report_value1, report_value2, DATE_FORMAT(created_date,'%d-%m-%Y') created_date, check_flag from TRIAL_BALANCE where company_id = ? and check_flag = false and report_id in (select max(report_id) from TRIAL_BALANCE)";
	public static final String DB_GET_TALLY_SUMMARY = "select tally_summary_id, report_id, report_key, report_value1, report_value2, DATE_FORMAT(created_date,'%d-%m-%Y') created_date, check_flag from TRIAL_BALANCE where company_id = ? and check_flag = false";
	public static final String DB_UPDATE_TALLY_SUMMARY = "update TRIAL_BALANCE set check_flag = true where tally_summary_id = ? and report_id = ? and company_id = ?";
	public static final String DB_DELETE_TALLY_SUMMARY= "delete from TRIAL_BALANCE where company_id = ?";
	
	//DAYBOOK_MASTER - DELETE (new approach, check later)
	public static final String DB_DELETE_DAYBOOK_MASTER = "delete from DAYBOOK_MASTER where VOUCHER_KEY = ?";
	public static final String DB_DELETE_DAYBOOK_LEDGER = "delete from DAYBOOK_INVENTORY where id > 0 and VOUCHER_KEY = ?";
	public static final String DB_DELETE_DAYBOOK_INVENTORY = "delete from DAYBOOK_LEDGER where id > 0 and VOUCHER_KEY = ?"; 
	
	//DAYBOOK_MASTER - ADD
	public static final String DB_ADD_DAYBOOK_MASTER = "insert into DAYBOOK_MASTER(VOUCHER_KEY, VCH_TYPE, VOUCHER_DATE, VOUCHER_NUMBER, PARTY_LEDGER_NAME, EFFECTIVE_DATE, MASTER_ID, FLAG, CREATED_DATE, MODIFIED_DATE, COMPANY_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String DB_ADD_DAYBOOK_LEDGER = "insert into DAYBOOK_LEDGER(LEDGER_NAME, AMOUNT, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE, COMPANY_ID) values (?, ?, ?, ?, ?, ?)";
	public static final String DB_ADD_DAYBOOK_INVENTORY = "insert into DAYBOOK_INVENTORY(STOCK_ITEM_NAME, AMOUNT, RATE, BILLED_QTY, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE, COMPANY_ID) values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	//DAYBOOK_MASTER - GET
	//public static final String DB_GET_DAYBOOK_MASTER = "select VOUCHER_KEY, VCH_TYPE, DATE_FORMAT(VOUCHER_DATE,'%d-%m-%Y') VOUCHER_DATE_FORMATTED, VOUCHER_NUMBER, PARTY_LEDGER_NAME, DATE_FORMAT(EFFECTIVE_DATE,'%d-%m-%Y') EFFECTIVE_DATE, MASTER_ID, FLAG, CREATED_DATE, MODIFIED_DATE, VOUCHER_DATE from DAYBOOK_MASTER where company_id= ? and flag = false order by VOUCHER_DATE, VOUCHER_KEY desc limit 50";
	public static final String DB_GET_DAYBOOK_MASTER = "select VOUCHER_KEY, VCH_TYPE, DATE_FORMAT(VOUCHER_DATE,'%d-%m-%Y') VOUCHER_DATE_FORMATTED, VOUCHER_NUMBER, PARTY_LEDGER_NAME, DATE_FORMAT(EFFECTIVE_DATE,'%d-%m-%Y') EFFECTIVE_DATE, MASTER_ID, FLAG, CREATED_DATE, MODIFIED_DATE, VOUCHER_DATE from DAYBOOK_MASTER where company_id= ? and flag = false and VOUCHER_DATE >= ( CURDATE() - INTERVAL 5 DAY ) order by VOUCHER_DATE, VOUCHER_KEY desc";
	public static final String DB_GET_DAYBOOK_LEDGER = "select ID, LEDGER_NAME, AMOUNT, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE from DAYBOOK_LEDGER WHERE COMPANY_ID = ?";
	public static final String DB_GET_DAYBOOK_INVENTORY = "select ID, STOCK_ITEM_NAME, AMOUNT, RATE, BILLED_QTY, VOUCHER_KEY, CREATED_DATE, MODIFIED_DATE from DAYBOOK_INVENTORY WHERE COMPANY_ID = ?";
	
	//DAYBOOK_MASTER - UPDATE
	public static final String DB_UPDATE_DAYBOOK_INVENTORY_FLAG = "update DAYBOOK_MASTER set FLAG = 1 where VOUCHER_KEY = ? and COMPANY_ID = ?";
	
	
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
	
	//Stock details - not using, check it
	public static final String DB_GET_STOCK_MASTER = "SELECT VOUCHER_TYPE, DATE_ENT, VOUCHER_NUMBER, VOUCHER_KEY, EFFECTIVE_DATE, MASTER_ID, OPR_DATE, REEL_WEIGHT, START_TIME, REWIND_START, REWIND_END, OPERATED_BY, FOREMAN1, FOREMAN2 FROM stock_master where COMPANY_ID = ?";
	public static final String DB_GET_STOCK_DETAIL = "SELECT STOCK_DETAILS_ID, STOCK_ITEM_NAME, RATE, AMOUNT, BILLED_QTY, STATUS, VOUCHER_KEY FROM stock_details where COMPANY_ID = ?";
	public static final String DB_GET_STOCK_ITEM_DETAIL = "SELECT STOCK_ITEM_DETAILS_ID, GSM_TGT, GSM_ACT, BF_TGT, BF_ACT, COBB, REEL_LEN, JOINTS, REEL_DIA, MOIST, SIZE_ACT, SIZE_TGT, UNITS, VOUCHER_KEY, STOCK_DETAILS_ID,BATCH_NAME, RATE, AMOUNT, BILLED_QTY FROM stock_item_details where COMPANY_ID = ?";
	
	//Stock details - in use
	public static final String DB_GET_STOCKS = "SELECT SM.VOUCHER_NUMBER, DATE_FORMAT(SM.EFFECTIVE_DATE,'%d-%m-%Y') EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, "
			+ "SD.RATE SD_RATE, SD.AMOUNT SD_AMOUNT, SD.BILLED_QTY SD_BILLED_QTY, "
			+ "SID.STOCK_ITEM_DETAILS_ID, SID.GSM_TGT, SID.GSM_ACT, SID.BF_TGT, SID.BF_ACT, SID.COBB, SID.REEL_LEN, "
			+ "SID.JOINTS, SID.REEL_DIA, SID.MOIST, SID.SIZE_ACT, SID.SIZE_TGT, SID.UNITS, SID.VOUCHER_KEY, "
			+ "SID.BATCH_NAME, SID.RATE, SID.AMOUNT, SID.BILLED_QTY "
			+ "FROM STOCK_MASTER SM, STOCK_DETAILS SD, STOCK_ITEM_DETAILS SID WHERE SM.COMPANY_ID = ? AND SM.COMPANY_ID = SD.COMPANY_ID AND SD.COMPANY_ID = SID.COMPANY_ID AND SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID";
	
	//Graph screens
	//public static final String DB_GET_STOCK_GSM_DETAIL = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BATCH_NAME, SID.GSM_TGT, SID.GSM_ACT FROM STOCK_MASTER SM, STOCK_DETAILS SD, STOCK_ITEM_DETAILS SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.COMPANY_ID = ?";
	//public static final String DB_GET_STOCK_BF_DETAIL = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BATCH_NAME, SID.BF_TGT, SID.BF_ACT FROM STOCK_MASTER SM, STOCK_DETAILS SD, STOCK_ITEM_DETAILS SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.COMPANY_ID = ?";
	
	public static final String DB_GET_STOCK_GSM_DETAIL = "SELECT DATE_FORMAT(SM.EFFECTIVE_DATE,'%d-%m-%Y') EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BATCH_NAME, SID.GSM_TGT, SID.GSM_ACT FROM STOCK_MASTER SM, STOCK_DETAILS SD, STOCK_ITEM_DETAILS SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.COMPANY_ID = ? AND SM.EFFECTIVE_DATE >= ? AND SM.EFFECTIVE_DATE <= ?";
	public static final String DB_GET_STOCK_BF_DETAIL = "SELECT DATE_FORMAT(SM.EFFECTIVE_DATE,'%d-%m-%Y') EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BATCH_NAME, SID.BF_TGT, SID.BF_ACT FROM STOCK_MASTER SM, STOCK_DETAILS SD, STOCK_ITEM_DETAILS SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.COMPANY_ID = ? AND SM.EFFECTIVE_DATE >= ? AND SM.EFFECTIVE_DATE <= ?";
	
	
	public static final String DB_GET_STOCK_GSM_LAST_7_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.GSM_TGT, SID.GSM_ACT FROM STOCK_MASTER SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 7 DAY)";
	public static final String DB_GET_STOCK_GSM_LAST_30_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.GSM_TGT, SID.GSM_ACT FROM STOCK_MASTER SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 30 DAY)";
	
	public static final String DB_GET_STOCK_BF_LAST_7_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BF_TGT, SID.BF_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 7 DAY)";
	public static final String DB_GET_STOCK_BF_LAST_30_DAYS = "SELECT SM.EFFECTIVE_DATE, SD.STOCK_ITEM_NAME, SID.BF_TGT, SID.BF_ACT FROM inventory.stock_master SM, inventory.stock_details SD, inventory.stock_item_details SID WHERE SD.STATUS = 'IN' AND SM.VOUCHER_KEY = SD.VOUCHER_KEY AND SD.STOCK_DETAILS_ID = SID.STOCK_DETAILS_ID AND SM.EFFECTIVE_DATE >= (NOW() - INTERVAL 30 DAY)";
	
	//Dashboard Stat
	public static final String DB_GET_PRODUCTION_YEAR = "select sum(sid.billed_qty) from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid "
			+ "where sm.voucher_key = sd.voucher_key and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID AND sm.COMPANY_ID = ?";
	
	public static final String DB_GET_PRODUCTION_QUARTER = "select QUARTER(now()) qy, sum(sid.billed_qty) from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid "
			+ "where sm.voucher_key = sd.voucher_key and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID  AND sm.COMPANY_ID = ? group by qy"; 
	
	//public static final String DB_GET_PRODUCTION_MONTH = "select  sum(sd.billed_qty) from stock_master sm, stock_details sd where sm.voucher_key = sd.voucher_key group by dayofmonth(now())";
	
	public static final String DB_GET_PRODUCTION_MONTH = "select  YEAR(NOW()) as year1, MONTH(NOW()) as month1,  sum(sid.billed_qty) "
			+ "from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid where sm.voucher_key = sd.voucher_key and YEAR(sm.effective_date)=YEAR(NOW()) "
			+ "AND MONTH(sm.effective_date)=MONTH(NOW()) and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID  AND sm.COMPANY_ID = ? group by YEAR(NOW()),MONTH(NOW())";
	
	public static final String DB_GET_PRODUCTION_WEEK = "select  week(sm.effective_date) week_num, sum(sid.billed_qty) "
			+ "from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid where sm.voucher_key = sd.voucher_key "
			+ "and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID  AND sm.COMPANY_ID = ? group by week(sm.effective_date) order by week_num desc";
	
	//Dashboard graph
	public static final String DB_GET_PRODUCTION_DASHBOARD_CHART = "select DATE_FORMAT(sm.effective_date,'%d-%m-%Y') ed, sum(sid.billed_qty) from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid "
			+ "where sm.voucher_key = sd.voucher_key and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID  AND sm.COMPANY_ID = ? group by ed"; 
	
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
	public static final String BATCH_NAME = "BATCH_NAME";
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
	public static final String COBB = "COBB";
	public static final String REEL_LEN = "REEL_LEN";
	public static final String JOINTS = "JOINTS";
	public static final String REEL_DIA = "REEL_DIA";
	public static final String MOIST = "MOIST";
	public static final String SIZE_ACT = "SIZE_ACT";
	public static final String SIZE_TGT= "SIZE_TGT";
	public static final String UNITS = "UNITS";
	public static final String DB_RATE = "RATE";
	public static final String DB_AMOUNT = "AMOUNT";
	
	//sales and production summary
	public static final String DB_GET_SALES_PRODUCTION_SUMMARY = "select MONTH, AMOUNT from HISTORY_DATA where COMPANY_ID = ? and GROUP_NAME = ? AND STATUS = ? AND YEAR = ?";
	public static final String DB_GET_SALES_PRODUCTION_SUMMARY_BY_YEAR = "select YEAR, sum(AMOUNT) from HISTORY_DATA where GROUP_NAME = ? AND STATUS= ? AND COMPANY_ID = ? group by YEAR";

	public static final String DB_GET_CURRENT_YEAR_SUMMARY = "select sum(AMOUNT) from HISTORY_DATA where GROUP_NAME = ? AND STATUS= ? AND COMPANY_ID = ? AND YEAR = ?";
	public static final String DB_GET_CURRENT_MONTH_SUMMARY = "select sum(AMOUNT) from HISTORY_DATA where GROUP_NAME = ? AND STATUS= ? AND COMPANY_ID = ? AND YEAR = ? AND MONTH = ?";
	
	public static final String FIRST_QUARTER = "('January', 'Febrarury', 'March')";
	public static final String SECOND_QUARTER = "('April', 'May', 'June')";
	public static final String THIRD_QUARTER = "('July', 'August', 'September')";
	public static final String FOURTH_QUARTER = "('October', 'November', 'December')";
	
	public static final String DB_GET_CURRENT_QUARTER_SUMMARY = "select sum(AMOUNT) from HISTORY_DATA where GROUP_NAME = ? AND STATUS= ? AND COMPANY_ID = ? AND YEAR = ? AND MONTH IN ";
	
	public static final String DB_AUTHENTICATION = "SELECT COMPANY_ID, ROLE_ID FROM USERS WHERE USER_NAME = ? AND PWD = ? AND USER_STATUS =? ";
	public static final String DB_GET_USERS = "SELECT USERS.USER_NAME, USERS.ROLE_ID, USERS.USER_STATUS, USERS.CREATED_DATE, ROLE.ROLE_NAME, USERS.FIRST_NAME, USERS.LAST_NAME FROM USERS, ROLE WHERE USERS.ROLE_ID = ROLE.ROLE_ID AND COMPANY_ID = ? ORDER BY USERS.USER_NAME";
	public static final String DB_GET_ROLES = "SELECT ROLE_ID, ROLE_NAME FROM ROLE";
	public static final String DB_UPDATE_USERS = "UPDATE USERS SET ROLE_ID = ?, USER_STATUS = ?, MODIFIED_DATE = ? WHERE USER_NAME = ? AND COMPANY_ID = ?";
	public static final String DB_ADD_USERS = "INSERT INTO USERS(USER_NAME, PWD,  COMPANY_ID, ROLE_ID, USER_STATUS, CREATED_DATE, MODIFIED_DATE, FIRST_NAME, LAST_NAME) VALUES (?,?,?,?,?,?,?,?,?)";
	public static final String DB_ADD_MESSAGE = "INSERT INTO MESSAGE (MESSAGE, URL, ROLE_ID, STATUS, CREATED_DATE, COMPANY_ID, TITLE) VALUES (?,?,?,?,?,?,?)";
	public static final String DB_GET_MESSAGE = "SELECT MESSAGE, URL, CREATED_DATE, ROLE_ID, TITLE FROM MESSAGE WHERE STATUS = 'active' AND COMPANY_ID = ? ORDER BY MESSAGE_ID DESC";
		
	//public static final String DB_GET_SALES_ORDER = "SELECT ID, VOUCHER_KEY, COMPANY, ORDER_DATE, ITEM, SIZE, QTY, ORDER_STATUS, COMPANY_ID FROM SALES_ORDER WHERE ORDER_STATUS = 0 AND COMPANY_ID = ? ORDER BY VOUCHER_KEY";
	public static final String DB_GET_SALES_ORDER = "SELECT ID, ORDER_NO, VOUCHER_KEY, COMPANY, ORDER_DATE, BF, GSM, SIZE, WEIGHT, REEL, ORDER_STATUS, ALTERED, COMPANY_ID FROM TALLYDB.SALES_ORDERS WHERE ORDER_STATUS = ? AND COMPANY_ID = ? ORDER BY ORDER_NO";
	
	public static final String DB_GET_SALES_ORDER_BY_ITEM = "SELECT ITEM, SUM(QTY) FROM SALES_ORDER WHERE ORDER_STATUS = 0 AND COMPANY_ID = ? GROUP BY ITEM";
	public static final String DB_GET_SALES_ORDER_BY_ITEM_SIZE = "SELECT ITEM, SIZE, SUM(QTY) FROM SALES_ORDER WHERE ORDER_STATUS = 0 AND COMPANY_ID = ? GROUP BY ITEM, SIZE";
	
	public static final String DB_GET_SALES_ORDER_BY_BF = "SELECT BF, SUM(WEIGHT) FROM SALES_ORDERS WHERE ORDER_STATUS = 0 AND COMPANY_ID = ? GROUP BY BF";
	public static final String DB_GET_SALES_ORDER_BY_BF_GSM = "SELECT BF, GSM, SUM(WEIGHT) FROM SALES_ORDERS WHERE ORDER_STATUS = 0 AND COMPANY_ID = ? GROUP BY BF, GSM";
	public static final String DB_GET_SALES_ORDER_BY_BF_GSM_SIZE = "SELECT BF, GSM, SIZE, SUM(WEIGHT) FROM SALES_ORDERS WHERE ORDER_STATUS = 0 AND COMPANY_ID = ? GROUP BY BF, GSM, SIZE";
	
	public static final String DB_UPDATE_SALES_ORDER = "update SALES_ORDERS set ORDER_STATUS = ? where ID = ? and COMPANY_ID = ?";
	public static final String DB_UPDATE_COMPLETED_SALES_ORDERS = "update SALES_ORDERS set ORDER_STATUS = 1, MODIFIED_DATE = sysdate() where ID = ? and COMPANY_ID = ?";
	public static final String DB_UPDATE_MODIFIED_SALES_ORDERS = "update SALES_ORDERS set WEIGHT = ?, REEL=?, ALTERED = '1', MODIFIED_DATE = sysdate() where ID = ? and COMPANY_ID = ?";
	
	public static final String DB_UPDATE_COMPLETED_SALES_ORDERS_RESTORED = "update SALES_ORDERS set ORDER_STATUS = 0, MODIFIED_DATE = sysdate() where ID = ? and COMPANY_ID = ?";
	public static final String DB_UPDATE_MODIFIED_SALES_ORDERS_RESTORED = "update SALES_ORDERS set WEIGHT = WEIGHT + ?, ALTERED = '0', MODIFIED_DATE = sysdate() where ID = ? and COMPANY_ID = ?";
	
	
	
	public static final String DB_ADD_SALES_ORDERS_PLANNED = "insert into SALES_ORDERS_PLANNED(ID, BATCH_NO, VOUCHER_KEY, ORDER_DATE, COMPANY, SIZE, WEIGHT, ORDER_STATUS, ALTERED, CREATED_DATE, MODIFIED_DATE, COMPANY_ID, ORDER_NO, BF, GSM, REEL, REEL_IN_STOCK) values (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
	//public static final String DB_GET_SALES_ORDERS_PLANNED = "SELECT ID, BATCH_NO, VOUCHER_KEY, ORDER_DATE, COMPANY, SIZE, WEIGHT, ORDER_STATUS, ALTERED, DATE_FORMAT(CREATED_DATE, '%Y-%m-%d'), MODIFIED_DATE, COMPANY_ID, ORDER_NO, BF, GSM, REEL, REEL_IN_STOCK FROM SALES_ORDERS_PLANNED WHERE COMPANY_ID = ? ORDER BY BATCH_NO DESC";
	public static final String DB_GET_SALES_ORDERS_PLANNED = "SELECT SALES_ORDERS_PLANNED_ID, ID, BATCH_NO, VOUCHER_KEY, ORDER_DATE, COMPANY, SIZE, WEIGHT, ORDER_STATUS, ALTERED, DATE_FORMAT(CREATED_DATE, '%Y-%m-%d'), MODIFIED_DATE, COMPANY_ID, ORDER_NO, BF, GSM, REEL, REEL_IN_STOCK FROM SALES_ORDERS_PLANNED WHERE COMPANY_ID = ? ORDER BY BATCH_NO DESC";
	
	//public static final String DB_GET_SALES_ORDERS_DISPATCH = "SELECT COMPANY, ORDER_NO, BF, GSM,  SIZE,  SUM(WEIGHT) FROM TALLYDB.SALES_ORDERS_PLANNED WHERE COMPANY_ID = ? AND BATCH_NO = ? GROUP BY COMPANY, ORDER_NO, BF, GSM,  SIZE";
	public static final String DB_GET_SALES_ORDERS_DISPATCH = "SELECT COMPANY, ORDER_NO, BF, GSM,  SIZE,  WEIGHT, REEL, REEL_IN_STOCK FROM TALLYDB.SALES_ORDERS_PLANNED WHERE COMPANY_ID = ? AND BATCH_NO = ? ORDER BY COMPANY, ORDER_NO";
	
	public static final String DB_ADD_SALES_ORDERS_PLANNED_JSON = "insert into SALES_ORDERS_PLANNED_JSON(BATCH_NO, JSON_TYPE, JSON_VALUE, CREATED_DATE, COMPANY_ID) values (?, ?, ?, CURRENT_TIMESTAMP, ?)";

	public static final String DB_GET_SALES_ORDER_PLANNED_BY_BF = "SELECT BF, SUM(WEIGHT) FROM SALES_ORDERS_PLANNED WHERE BATCH_NO = ? AND COMPANY_ID = ? GROUP BY BF";
	public static final String DB_GET_SALES_ORDER_PLANNED_BY_BF_GSM = "SELECT BF, GSM, SUM(WEIGHT) FROM SALES_ORDERS_PLANNED WHERE BATCH_NO = ? AND COMPANY_ID = ? GROUP BY BF, GSM";
	public static final String DB_GET_SALES_ORDER_PLANNED_BY_BF_GSM_SIZE = "SELECT BF, GSM, SIZE, SUM(WEIGHT), SUM(REEL), SUM(REEL_IN_STOCK) FROM SALES_ORDERS_PLANNED WHERE BATCH_NO = ? AND COMPANY_ID = ? GROUP BY BF, GSM, SIZE";
	
	public static final String DB_UPDATE_SALES_ORDERS_PLANNED_REEL = "UPDATE SALES_ORDERS_PLANNED SET REEL = ? WHERE COMPANY_ID = ? AND SALES_ORDERS_PLANNED_ID = ?";
	
	public static final String DB_DELETE_SALES_ORDER_PLANNED = "DELETE FROM SALES_ORDERS_PLANNED where SALES_ORDERS_PLANNED_ID = ? and COMPANY_ID = ?";
	
//	
//	public static final String DB_GET_PRODUCTION_QUARTER = "select QUARTER(now()) qy, sum(sid.billed_qty) from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid "
//			+ "where sm.voucher_key = sd.voucher_key and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID  AND sm.COMPANY_ID = ? group by qy"; 
//	
//	public static final String DB_GET_PRODUCTION_MONTH = "select  YEAR(NOW()) as year1, MONTH(NOW()) as month1,  sum(sid.billed_qty) "
//			+ "from STOCK_MASTER sm, STOCK_DETAILS sd, STOCK_ITEM_DETAILS sid where sm.voucher_key = sd.voucher_key and YEAR(sm.effective_date)=YEAR(NOW()) "
//			+ "AND MONTH(sm.effective_date)=MONTH(NOW()) and sd.STOCK_DETAILS_ID = sid.STOCK_DETAILS_ID  AND sm.COMPANY_ID = ? group by YEAR(NOW()),MONTH(NOW())";
	
	public static final String DB_GET_SALES_DETAILS = "SELECT SALES_ID, CUST_ID, VOUCHER_NUMBER, PARTY_LEDGER_NAME, SALE_DATE, EFFECTIVE_DATE, VCH_TYPE, VOUCHER_KEY, LEDGER_NAME, AMOUNT, CREATED_DATE, MODIFIED_DATE, COMPANY_ID FROM SALES_DETAILS WHERE CUST_ID = ? AND COMPANY_ID = ?";
	public static final String DB_GET_SALES_DETAILS_ALL = "SELECT SALES_ID, CUST_ID, VOUCHER_NUMBER, PARTY_LEDGER_NAME, SALE_DATE, EFFECTIVE_DATE, VCH_TYPE, VOUCHER_KEY, LEDGER_NAME, AMOUNT, CREATED_DATE, MODIFIED_DATE, COMPANY_ID FROM SALES_DETAILS WHERE COMPANY_ID = ?";
	
	public static final String DB_GET_RECEIPT_DETAILS = "SELECT RECEIPT_ID, CUST_ID, VOUCHER_NUMBER, PARTY_LEDGER_NAME, RECEIPT_DATE, EFFECTIVE_DATE, VCH_TYPE, VOUCHER_KEY, LEDGER_NAME, AMOUNT, CREATED_DATE, MODIFIED_DATE, COMPANY_ID FROM RECEIPT_DETAILS WHERE CUST_ID = ? AND COMPANY_ID = ?";
	public static final String DB_GET_RECEIPT_DETAILS_ALL = "SELECT RECEIPT_ID, CUST_ID, VOUCHER_NUMBER, PARTY_LEDGER_NAME, RECEIPT_DATE, EFFECTIVE_DATE, VCH_TYPE, VOUCHER_KEY, LEDGER_NAME, AMOUNT, CREATED_DATE, MODIFIED_DATE, COMPANY_ID FROM RECEIPT_DETAILS WHERE COMPANY_ID = ?";
	
	public static final String DB_GET_CUSTOMERS_ALL = "SELECT CUSTOMER_ID, GST_NO, NAME, CUSTOMER_TYPE, CUSTOMER_GROUP, COMPANY_ID, CREATED_DATE FROM CUSTOMERS WHERE COMPANY_ID = ?";
}
