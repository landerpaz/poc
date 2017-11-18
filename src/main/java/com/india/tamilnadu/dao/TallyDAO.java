package com.india.tamilnadu.dao;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.jaxrs.Product;
import com.india.tamilnadu.jaxrs.Tally;
import com.india.tamilnadu.tally.bc.TallyDayBookBC;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.DayBookMasterVO;
import com.india.tamilnadu.tally.vo.InventoryEntryVO;
import com.india.tamilnadu.tally.vo.LedgerEntryVO;
import com.india.tamilnadu.tally.vo.ProductionDashboardChart;
import com.india.tamilnadu.tally.vo.StockBFDetail;
import com.india.tamilnadu.tally.vo.StockItemDetail;
import com.india.tamilnadu.tally.vo.StockDetail;
import com.india.tamilnadu.tally.vo.StockGSMDetail;
import com.india.tamilnadu.tally.vo.StockMaster;
import com.india.tamilnadu.tally.vo.StockStatistics;
import com.india.tamilnadu.tally.vo.Stocks;
import com.india.tamilnadu.util.Constants;
import com.india.tamilnadu.util.TallyBean;
import com.india.tamilnadu.util.TallyRequestContext;
import com.india.tamilnadu.util.Utility;

public class TallyDAO implements BaseDAO {
	
	private final Logger LOG = LoggerFactory.getLogger(TallyDayBookBC.class);
	
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
				preparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
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
		
	public Response updateTallySummary(Tally tally) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_TALLY_SUMMARY);
			
			
			System.out.println("Update query : " + Constants.DB_UPDATE_TALLY_SUMMARY);
			System.out.println("tally summary id : " + tally.getTallySummaryIid());
			System.out.println("tally report id : " + tally.getReportId());
			
			int parameterIndex = 1;
			preparedStatement.setInt(parameterIndex++, Integer.parseInt(tally.getTallySummaryIid()));
			preparedStatement.setInt(parameterIndex++, Integer.parseInt(tally.getReportId()));
			
			preparedStatement.executeUpdate();
			
			
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
	}

	public Response updateDayBookMasterFlag(TallyInputDTO tallyInputDTO) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_DAYBOOK_INVENTORY_FLAG);
			
			System.out.println("Update query : " + Constants.DB_UPDATE_DAYBOOK_INVENTORY_FLAG);
			
			int parameterIndex = 1;
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getVoucherKey());
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			// TODO: handle exception
			System.out.println("Error in updating day book master flag field in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			closeResources();
		}
		
		return response;
	}

	public void addTallyDayBook_bk(TallyInputDTO tallyInputDTO) throws Exception {
		
		/*Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);*/
		
		PreparedStatement ledgerPreparedStatement = null;
		PreparedStatement inventoryPreparedStatement = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			
			
			
			
			for(DayBookMasterVO dayBookMasterVO : tallyInputDTO.getDayBookMasterVOs()) {
			
				int parameterIndex = 1;
				
				//delete data from table
				preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_LEDGER);
				preparedStatement.setString(parameterIndex, dayBookMasterVO.getVoucherKey());
				preparedStatement.execute();
				preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_INVENTORY);
				preparedStatement.setString(parameterIndex, dayBookMasterVO.getVoucherKey());
				preparedStatement.execute();
				preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_MASTER);
				preparedStatement.setString(parameterIndex, dayBookMasterVO.getVoucherKey());
				preparedStatement.execute();
				
				//insert data into table
				preparedStatement = connection.prepareStatement(Constants.DB_ADD_DAYBOOK_MASTER);
				ledgerPreparedStatement = connection.prepareStatement(Constants.DB_ADD_DAYBOOK_LEDGER);
				inventoryPreparedStatement = connection.prepareStatement(Constants.DB_ADD_DAYBOOK_INVENTORY);
				
				parameterIndex = 1;
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherKey());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherType());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherAction());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherTypeName());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherNumber());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getPartyLedgerName());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getEffectiveDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getPersistedView());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getAlterId());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getMasterId());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getLedgerName());
				preparedStatement.setBoolean(parameterIndex++, false);
				preparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
				preparedStatement.setString(parameterIndex++, null);
				
				preparedStatement.executeUpdate();
				
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBook, data inserted in DB DAYBOOK_MASTER for Party : " + dayBookMasterVO.getPartyLedgerName() + " , Ledger type : " + dayBookMasterVO.getVoucherType());
				
				//insert data in DAYBOOK_LEDGER
				double amount = 0.0;
				for(LedgerEntryVO ledgerEntryVO : dayBookMasterVO.getLedgerEntryVOs()) {
					parameterIndex = 1;
					
					/*if(dayBookMasterVO.getVoucherType().equalsIgnoreCase("Delivery Note GST")) {
						System.out.println("Ledger Name : " + ledgerEntryVO.getLedgerName());
					}*/
					
					ledgerPreparedStatement.setString(parameterIndex++, ledgerEntryVO.getLedgerName());
					if(null != ledgerEntryVO.getAmount() && ledgerEntryVO.getAmount().trim().length() > 0) {
						amount = Double.parseDouble(ledgerEntryVO.getAmount());
					} 
					ledgerPreparedStatement.setDouble(parameterIndex++, amount);
					ledgerPreparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherKey());
					ledgerPreparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
					ledgerPreparedStatement.setString(parameterIndex++, null);
					ledgerPreparedStatement.executeUpdate();
				}
				
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBook, data inserted in DB DAYBOOK_LEDGER for Party : " + dayBookMasterVO.getPartyLedgerName() + " , Ledger type : " + dayBookMasterVO.getVoucherType());
						
				//insert data in DAYBOOK_INVENTORY
				for(InventoryEntryVO inventoryEntryVO : dayBookMasterVO.getInventoryEntryVOs()) {
					parameterIndex = 1;
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getStockItemName());
					if(null == inventoryEntryVO.getAmount() || inventoryEntryVO.getAmount().trim().length() < 1) {
						inventoryPreparedStatement.setString(parameterIndex++, "0");
					} else {
						inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getAmount());
					}
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getRate());
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getBilledQuantity());
					inventoryPreparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherKey());
					inventoryPreparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
					inventoryPreparedStatement.setString(parameterIndex++, null);
					inventoryPreparedStatement.executeUpdate();
				}
			
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBook, data inserted in DB DAYBOOK_INVENTORY for Party : " + dayBookMasterVO.getPartyLedgerName() + " , Ledger type : " + dayBookMasterVO.getVoucherType());
			}
			
			connection.commit();
			
		} catch (Exception e) {
			
			if(null != connection) connection.rollback();
			e.printStackTrace();
			throw new RuntimeException(e);
			
			//response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			//response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			
			try {
			if(null != ledgerPreparedStatement) { ledgerPreparedStatement.close(); }
			if(null != inventoryPreparedStatement) { ledgerPreparedStatement.close(); }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			closeResources();
		}
		
		//return response;
	}

	public void addTallyDayBook(TallyInputDTO tallyInputDTO) throws Exception {
		
		/*Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);*/
		
		PreparedStatement ledgerPreparedStatement = null;
		PreparedStatement inventoryPreparedStatement = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			
			for(DayBookMasterVO dayBookMasterVO : tallyInputDTO.getDayBookMasterVOs()) {
			
				/*	int parameterIndex = 1;
				
				//delete data from table
				preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_LEDGER);
				preparedStatement.setString(parameterIndex, dayBookMasterVO.getVoucherKey());
				preparedStatement.execute();
				preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_INVENTORY);
				preparedStatement.setString(parameterIndex, dayBookMasterVO.getVoucherKey());
				preparedStatement.execute();
				preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_MASTER);
				preparedStatement.setString(parameterIndex, dayBookMasterVO.getVoucherKey());
				preparedStatement.execute();
	*/			
				//insert data into table
				preparedStatement = connection.prepareStatement(Constants.DB_ADD_DAYBOOK_MASTER);
				ledgerPreparedStatement = connection.prepareStatement(Constants.DB_ADD_DAYBOOK_LEDGER);
				inventoryPreparedStatement = connection.prepareStatement(Constants.DB_ADD_DAYBOOK_INVENTORY);
				
				int parameterIndex = 1;
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherKey());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherType());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherAction());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherTypeName());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherNumber());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getPartyLedgerName());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getEffectiveDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getPersistedView());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getAlterId());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getMasterId());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getLedgerName());
				preparedStatement.setBoolean(parameterIndex++, false);
				preparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
				preparedStatement.setString(parameterIndex++, null);
				
				preparedStatement.executeUpdate();
				
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBook, data inserted in DB DAYBOOK_MASTER for Party : " + dayBookMasterVO.getPartyLedgerName() + " , Ledger type : " + dayBookMasterVO.getVoucherType());
				
				//insert data in DAYBOOK_LEDGER
				double amount = 0.0;
				for(LedgerEntryVO ledgerEntryVO : dayBookMasterVO.getLedgerEntryVOs()) {
					parameterIndex = 1;
					
					/*if(dayBookMasterVO.getVoucherType().equalsIgnoreCase("Delivery Note GST")) {
						System.out.println("Ledger Name : " + ledgerEntryVO.getLedgerName());
					}*/
					
					ledgerPreparedStatement.setString(parameterIndex++, ledgerEntryVO.getLedgerName());
					if(null != ledgerEntryVO.getAmount() && ledgerEntryVO.getAmount().trim().length() > 0) {
						amount = Math.abs(Double.parseDouble(ledgerEntryVO.getAmount()));
					} 
					ledgerPreparedStatement.setDouble(parameterIndex++, amount);
					ledgerPreparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherKey());
					ledgerPreparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
					ledgerPreparedStatement.setString(parameterIndex++, null);
					ledgerPreparedStatement.executeUpdate();
				}
				
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBook, data inserted in DB DAYBOOK_LEDGER for Party : " + dayBookMasterVO.getPartyLedgerName() + " , Ledger type : " + dayBookMasterVO.getVoucherType());
						
				//insert data in DAYBOOK_INVENTORY
				for(InventoryEntryVO inventoryEntryVO : dayBookMasterVO.getInventoryEntryVOs()) {
					parameterIndex = 1;
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getStockItemName());
					if(null == inventoryEntryVO.getAmount() || inventoryEntryVO.getAmount().trim().length() < 1) {
						inventoryPreparedStatement.setString(parameterIndex++, "0");
					} else {
						inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getAmount());
					}
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getRate());
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getBilledQuantity());
					inventoryPreparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherKey());
					inventoryPreparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
					inventoryPreparedStatement.setString(parameterIndex++, null);
					inventoryPreparedStatement.executeUpdate();
				}
			
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBook, data inserted in DB DAYBOOK_INVENTORY for Party : " + dayBookMasterVO.getPartyLedgerName() + " , Ledger type : " + dayBookMasterVO.getVoucherType());
			}
			
			connection.commit();
			
		} catch (Exception e) {
			
			if(null != connection) connection.rollback();
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				LOG.warn(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Record is already available");
			} else {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			//response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			//response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			
			try {
			if(null != ledgerPreparedStatement) { ledgerPreparedStatement.close(); }
			if(null != inventoryPreparedStatement) { ledgerPreparedStatement.close(); }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			closeResources();
		}
		
		//return response;
	}
	
	public void test() throws Exception {
		
		/*Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);*/
		
		PreparedStatement ledgerPreparedStatement = null;
		PreparedStatement inventoryPreparedStatement = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			
			
			
			
			int parameterIndex = 1;
			
				
			//insert data into table
			preparedStatement = connection.prepareStatement("insert into daybook_master(voucher_key) values ('184705068564484')");
			
			try {
			
				boolean temp = preparedStatement.execute();
			
				System.out.println(temp);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
				
			System.out.println("outside");
			
			connection.commit();
			
		} catch (Exception e) {
			
			if(null != connection) connection.rollback();
			e.printStackTrace();
			throw new RuntimeException(e);
			
			//response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			//response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			
			try {
			if(null != ledgerPreparedStatement) { ledgerPreparedStatement.close(); }
			if(null != inventoryPreparedStatement) { ledgerPreparedStatement.close(); }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			closeResources();
		}
		
		//return response;
	}
	
	public static void main(String a[]) {
		try {
			
			TallyDAO tallyDAO = new TallyDAO();
			tallyDAO.test();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves day book data from DB
	 * 
	 * 
	 * */
	public List<DayBookMasterVO> getTallyDayBookMaster(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyDayBookMaster In");
		
		List<DayBookMasterVO> dayBookMasterVOs = new ArrayList<>();
		DayBookMasterVO dayBookMasterVO = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_DAYBOOK_MASTER);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				dayBookMasterVO = new DayBookMasterVO();
				dayBookMasterVO.setVoucherKey(resultSet.getString("VOUCHER_KEY"));
				dayBookMasterVO.setVoucherType(resultSet.getString("VCH_TYPE"));
				dayBookMasterVO.setVoucherAction(resultSet.getString("VOUCHER_ACTION"));
				dayBookMasterVO.setVoucherDate(resultSet.getString("VOUCHER_DATE"));
				dayBookMasterVO.setVoucherType(resultSet.getString("VOUCHER_TYPE_NAME"));
				dayBookMasterVO.setVoucherNumber(resultSet.getString("VOUCHER_NUMBER"));
				dayBookMasterVO.setPartyLedgerName(resultSet.getString("PARTY_LEDGER_NAME"));
				dayBookMasterVO.setEffectiveDate(resultSet.getString("EFFECTIVE_DATE"));
				dayBookMasterVO.setPersistedView(resultSet.getString("PERSISTED_VIEW"));
				dayBookMasterVO.setAlterId(resultSet.getString("ALTER_ID"));
				dayBookMasterVO.setMasterId(resultSet.getString("MASTER_ID"));
				dayBookMasterVO.setLedgerName(resultSet.getString("LEDGER_NAME"));
				dayBookMasterVO.setCheckFlag(resultSet.getString("FLAG"));
				
				dayBookMasterVOs.add(dayBookMasterVO);
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyDayBookMaster Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return dayBookMasterVOs;
	}
	
	public List<LedgerEntryVO> getTallyDayBookLedgerEntries() {
		
		List<LedgerEntryVO> ledgerEntryVOs = new ArrayList<>();
		LedgerEntryVO ledgerEntryVO = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_DAYBOOK_LEDGER);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				ledgerEntryVO = new LedgerEntryVO();
				ledgerEntryVO.setId(resultSet.getString("ID"));
				ledgerEntryVO.setLedgerName(resultSet.getString("LEDGER_NAME"));
				ledgerEntryVO.setAmount(resultSet.getString("AMOUNT"));
				ledgerEntryVO.setVoucherKey(resultSet.getString("VOUCHER_KEY"));
				
				ledgerEntryVOs.add(ledgerEntryVO);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return ledgerEntryVOs;
	}

	public List<InventoryEntryVO> getTallyDayBookInventoryEntries() {
		
		List<InventoryEntryVO> inventoryEntryVOs = new ArrayList<>();
		InventoryEntryVO inventoryEntryVO = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_DAYBOOK_INVENTORY);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				inventoryEntryVO = new InventoryEntryVO();
				inventoryEntryVO.setId(resultSet.getString("ID"));
				inventoryEntryVO.setStockItemName(resultSet.getString("STOCK_ITEM_NAME"));
				inventoryEntryVO.setAmount(resultSet.getString("AMOUNT"));
				inventoryEntryVO.setRate(resultSet.getString("RATE"));
				inventoryEntryVO.setBilledQuantity(resultSet.getString("BILLED_QTY"));
				inventoryEntryVO.setVoucherKey(resultSet.getString("VOUCHER_KEY"));
				
				inventoryEntryVOs.add(inventoryEntryVO);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return inventoryEntryVOs;
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
	
	//not in use
	public List<StockMaster> getTallyStockMaster(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyStockMaster In");
		
		List<StockMaster> stockMasters = new ArrayList<>();
		StockMaster stockMaster = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();System.out.println("connection : " + connection);
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_MASTER);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockMaster = new StockMaster();
				stockMaster.setVoucherType(resultSet.getString(Constants.VOUCHER_TYPE));
				stockMaster.setAction(resultSet.getString(Constants.VOUCHER_ACTION));
				stockMaster.setDateAlt(resultSet.getString(Constants.DATE_ALT));
				stockMaster.setDateEnt(resultSet.getString(Constants.DATE_ENT));
				stockMaster.setVoucherTypeName(resultSet.getString(Constants.VOUCHER_TYPE_NAME));
				stockMaster.setVoucherNumber(resultSet.getString(Constants.VOUCHER_NUMBER));
				stockMaster.setVoucherKey(resultSet.getString(Constants.VOUCHER_KEY));
				stockMaster.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockMaster.setPersistedView(resultSet.getString(Constants.PERSISTED_VIEW));
				stockMaster.setAlterId(resultSet.getString(Constants.ALTER_ID));
				stockMaster.setMasterId(resultSet.getString(Constants.MASTER_ID));
				stockMaster.setOprDate(resultSet.getString(Constants.OPR_DATE));
				stockMaster.setRealWeight(resultSet.getString(Constants.REEL_WEIGHT));
				stockMaster.setStartTime(resultSet.getString(Constants.START_TIME));
				stockMaster.setRewindStart(resultSet.getString(Constants.REWIND_START));
				stockMaster.setRewindEnd(resultSet.getString(Constants.REWIND_END));
				stockMaster.setOperatedBy(resultSet.getString(Constants.OPERATED_BY));
				stockMaster.setForeman1(resultSet.getString(Constants.FOREMAN1));
				stockMaster.setForeman2(resultSet.getString(Constants.FOREMAN2));
				
				stockMasters.add(stockMaster);
					
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyStockMaster Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockMasters;
	}
	
	//not in use
	public List<StockDetail> getTallyStockDetail(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyStockDetail In");
		
		List<StockDetail> stockDetails = new ArrayList<>();
		StockDetail stockDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_DETAIL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockDetail = new StockDetail();
				stockDetail.setStockDetailsId(resultSet.getString(Constants.STOCK_DETAILS_ID));
				stockDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				//stockDetail.setRate(resultSet.getString(Constants.DB_RATE));
				stockDetail.setAmount(resultSet.getString(Constants.DB_AMOUNT));
				//stockDetail.setBilledQty(resultSet.getString(Constants.BILLED_QTY));
				stockDetail.setActualQty(resultSet.getString(Constants.ACTUAL_QTY));
				stockDetail.setStatus(resultSet.getString(Constants.STATUS));
				stockDetail.setVoucherKey(resultSet.getString(Constants.VOUCHER_KEY));
				
				stockDetails.add(stockDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyStockDetail Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockDetails;
	}
	
	//not in use
	public List<StockItemDetail> getTallyStockItemDetail(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyStockItemDetail In");
		
		List<StockItemDetail> stockItemDetails = new ArrayList<>();
		StockItemDetail stockItemDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_ITEM_DETAIL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockItemDetail = new StockItemDetail();
				
				stockItemDetail.setStockItemDetailsId(resultSet.getString(Constants.STOCK_ITEM_DETAILS_ID));
				stockItemDetail.setGsmTgt(resultSet.getString(Constants.GSM_TGT));
				stockItemDetail.setGsmAct(resultSet.getString(Constants.GSM_ACT));
				stockItemDetail.setBfTgt(resultSet.getString(Constants.BF_TGT));
				stockItemDetail.setBfAct(resultSet.getString(Constants.BF_ACT));
				stockItemDetail.setSizeAct(resultSet.getString(Constants.SIZE_ACT));
				stockItemDetail.setReelLen(resultSet.getString(Constants.REEL_LEN));
				stockItemDetail.setJoints(resultSet.getString(Constants.JOINTS));
				stockItemDetail.setRealDia(resultSet.getString(Constants.REEL_DIA));
				stockItemDetail.setMoist(resultSet.getString(Constants.MOIST));
				stockItemDetail.setSizeTgt1(resultSet.getString(Constants.SIZE_TGT1));
				stockItemDetail.setSizeAct1(resultSet.getString(Constants.SIZE_ACT1));
				stockItemDetail.setLength1(resultSet.getString(Constants.LENGTH1));
				stockItemDetail.setTemp(resultSet.getString(Constants.TEMP));
				stockItemDetail.setUnits(resultSet.getString(Constants.UNITS));
				stockItemDetail.setVoucherKey(resultSet.getString(Constants.VOUCHER_KEY));
				stockItemDetail.setStockDetailsId(resultSet.getString(Constants.STOCK_DETAILS_ID));
				
				stockItemDetails.add(stockItemDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getTallyStockItemDetail Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockItemDetails;
	}
	
	public List<StockGSMDetail> getStockGSMDetailLast7Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMDetailLast7Days In");
		
		List<StockGSMDetail> stockGSMDetails = new ArrayList<>();
		StockGSMDetail stockGSMDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_GSM_LAST_7_DAYS);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockGSMDetail = new StockGSMDetail();
				
				stockGSMDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockGSMDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				//stockGSMDetail.setGsmTgt(null != resultSet.getString(Constants.GSM_TGT) ? Double.parseDouble(resultSet.getString(Constants.GSM_TGT).trim()) : 0);
				//stockGSMDetail.setGsmAct(null != resultSet.getString(Constants.GSM_ACT) ? Double.parseDouble(resultSet.getString(Constants.GSM_ACT).trim()) : 0);
				
				stockGSMDetail.setGsmTgt(resultSet.getDouble(Constants.GSM_TGT));
				stockGSMDetail.setGsmAct(resultSet.getDouble(Constants.GSM_ACT));
				
				
				stockGSMDetails.add(stockGSMDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMDetailLast7Days Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockGSMDetails;
	}

	public List<StockGSMDetail> getStockGSMDetailLast30Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMDetailLast30Days In");
		
		List<StockGSMDetail> stockGSMDetails = new ArrayList<>();
		StockGSMDetail stockGSMDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_GSM_LAST_30_DAYS);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockGSMDetail = new StockGSMDetail();
				
				stockGSMDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockGSMDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				//stockGSMDetail.setGsmTgt(null != resultSet.getString(Constants.GSM_TGT) ? Double.parseDouble(resultSet.getString(Constants.GSM_TGT).trim()) : 0);
				//stockGSMDetail.setGsmAct(null != resultSet.getString(Constants.GSM_ACT) ? Double.parseDouble(resultSet.getString(Constants.GSM_ACT).trim()) : 0);
				
				stockGSMDetail.setGsmTgt(resultSet.getDouble(Constants.GSM_TGT));
				stockGSMDetail.setGsmAct(resultSet.getDouble(Constants.GSM_ACT));
				
				
				stockGSMDetails.add(stockGSMDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMDetailLast30Days Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockGSMDetails;
	}


	public List<StockGSMDetail> getStockGSMDetail(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMDetail In");
		
		List<StockGSMDetail> stockGSMDetails = new ArrayList<>();
		StockGSMDetail stockGSMDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_GSM_DETAIL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockGSMDetail = new StockGSMDetail();
				
				stockGSMDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockGSMDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				//stockGSMDetail.setGsmTgt(null != resultSet.getString(Constants.GSM_TGT) ? Double.parseDouble(resultSet.getString(Constants.GSM_TGT).trim()) : 0);
				//stockGSMDetail.setGsmAct(null != resultSet.getString(Constants.GSM_ACT) ? Double.parseDouble(resultSet.getString(Constants.GSM_ACT).trim()) : 0);
				
				stockGSMDetail.setGsmTgt(resultSet.getDouble(Constants.GSM_TGT));
				stockGSMDetail.setGsmAct(resultSet.getDouble(Constants.GSM_ACT));
				
				
				stockGSMDetails.add(stockGSMDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMDetail Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockGSMDetails;
	}

	public List<StockBFDetail> getStockBFDetail(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFDetail In");
		
		List<StockBFDetail> stockBFDetails = new ArrayList<>();
		StockBFDetail stockBFDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_BF_DETAIL);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockBFDetail = new StockBFDetail();
				
				stockBFDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockBFDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				stockBFDetail.setBfTgt(resultSet.getDouble(Constants.BF_TGT));
				stockBFDetail.setBfAct(resultSet.getDouble(Constants.BF_ACT));
				
				
				stockBFDetails.add(stockBFDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFDetail Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockBFDetails;
	}
	
	public List<StockBFDetail> getStockBFDetailLast7Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFDetailLast7Days In");
		
		List<StockBFDetail> stockBFDetails = new ArrayList<>();
		StockBFDetail stockBFDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_BF_LAST_7_DAYS);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockBFDetail = new StockBFDetail();
				
				stockBFDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockBFDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				stockBFDetail.setBfTgt(resultSet.getDouble(Constants.BF_TGT));
				stockBFDetail.setBfAct(resultSet.getDouble(Constants.BF_ACT));
				
				
				stockBFDetails.add(stockBFDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFDetailLast7Days Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockBFDetails;
	}

	public List<StockBFDetail> getStockBFDetailLast30Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFDetailLast30Days In");
		
		List<StockBFDetail> stockBFDetails = new ArrayList<>();
		StockBFDetail stockBFDetail = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCK_BF_LAST_30_DAYS);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockBFDetail = new StockBFDetail();
				
				stockBFDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockBFDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				stockBFDetail.setBfTgt(resultSet.getDouble(Constants.BF_TGT));
				stockBFDetail.setBfAct(resultSet.getDouble(Constants.BF_ACT));
				
				
				stockBFDetails.add(stockBFDetail);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFDetailLast30Days Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockBFDetails;
	}
	
	public List<Stocks> getStocks(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocks In");
		
		List<Stocks> stockss = new ArrayList<>();
		Stocks stocks = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_STOCKS);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stocks = new Stocks();
				
				stocks.setVoucherNumber(resultSet.getString(Constants.VOUCHER_NUMBER));
				stocks.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stocks.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				stocks.setRate(Utility.formatRate(resultSet.getString(Constants.DB_RATE)));
				stocks.setAmount(resultSet.getString(Constants.DB_AMOUNT));
				stocks.setBilledQty(Utility.formatQty(resultSet.getString(Constants.BILLED_QTY)));
				
				stocks.setGsmTgt(resultSet.getString(Constants.GSM_TGT));
				stocks.setGsmAct(resultSet.getString(Constants.GSM_ACT));
				stocks.setBfTgt(resultSet.getString(Constants.BF_TGT));
				stocks.setBfAct(resultSet.getString(Constants.BF_ACT));
				stocks.setSizeAct(resultSet.getString(Constants.SIZE_ACT));
				stocks.setReelLen(resultSet.getString(Constants.REEL_LEN));
				stocks.setJoints(resultSet.getString(Constants.JOINTS));
				stocks.setRealDia(resultSet.getString(Constants.REEL_DIA));
				stocks.setMoist(resultSet.getString(Constants.MOIST));
				stocks.setSizeTgt1(resultSet.getString(Constants.SIZE_TGT1));
				stocks.setSizeAct1(resultSet.getString(Constants.SIZE_ACT1));
				stocks.setLength1(resultSet.getString(Constants.LENGTH1));
				stocks.setTemp(resultSet.getString(Constants.TEMP));
				stocks.setUnits(resultSet.getString(Constants.UNITS));
				stocks.setVoucherKey(resultSet.getString(Constants.VOUCHER_KEY));
				stocks.setStockItemDetailsId(resultSet.getString(Constants.STOCK_ITEM_DETAILS_ID));
				
				stockss.add(stocks);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocks Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return stockss;
	}

	public StockStatistics getStocksStatistics(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocksStatistics In");
		
		StockStatistics statistics = new StockStatistics();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_YEAR);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockYear(resultSet.getString(1));
			}
			
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_QUARTER);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockQuarter(resultSet.getString(2));
			}
			
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_MONTH);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockMonth(resultSet.getString(3));
			}
			
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_WEEK);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockWeek(resultSet.getString(2));
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocksStatistics Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return statistics;
	}
	
	public List<ProductionDashboardChart> getProductionDashboardChart(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart In");
		
		List<ProductionDashboardChart> productionDashboardCharts = new ArrayList<>();
		ProductionDashboardChart productionDashboardChart = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_DASHBOARD_CHART);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				productionDashboardChart = new ProductionDashboardChart();
				
				productionDashboardChart.setDate(resultSet.getString(1));
				productionDashboardChart.setQuantity(null != resultSet.getString(2) ? Integer.parseInt(resultSet.getString(2).trim()) : 0);
				
				productionDashboardCharts.add(productionDashboardChart);
				
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return productionDashboardCharts;
	}
}
