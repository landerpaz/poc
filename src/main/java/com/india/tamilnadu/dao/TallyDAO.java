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
import com.india.tamilnadu.tally.vo.ProductionSummary;
import com.india.tamilnadu.tally.vo.ProductionSummaryByYear;
import com.india.tamilnadu.tally.vo.SalesOrder;
import com.india.tamilnadu.tally.vo.SalesOrderDispatch;
import com.india.tamilnadu.tally.vo.SalesOrderPlanned;
import com.india.tamilnadu.tally.vo.SalesSummary;
import com.india.tamilnadu.tally.vo.SalesSummaryByYear;
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
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Message;
import com.india.tamilnadu.vo.User;

public class TallyDAO implements BaseDAO {
	
	private final Logger LOG = LoggerFactory.getLogger(TallyDayBookBC.class);
	
	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement preparedStatement = null;
	
	public List<Tally> getTallySummary(TallyInputDTO tallyInputDTO) {
		
		Tally tally = null;
		List<Tally> tallySummaryList =  new ArrayList<Tally>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_TALLY_SUMMARY);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				//System.out.println(resultSet.getString("report_value1") + " : " + resultSet.getString("report_value2"));
				
				tally = new Tally();
				tally.setTallySummaryIid(resultSet.getString("tally_summary_id"));
				tally.setReportId(resultSet.getString("report_id"));
				tally.setReportKey(resultSet.getString("report_key"));
				tally.setReportValue1(Integer.toString((int)(Math.abs(Double.parseDouble(resultSet.getString("report_value1"))))));
				tally.setReportValue2(Integer.toString((int)(Math.abs(Double.parseDouble(resultSet.getString("report_value2"))))));
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
			
			//DELETE
			int parameterIndex = 1;
			preparedStatement = connection.prepareStatement(Constants.DB_DELETE_TALLY_SUMMARY);
			preparedStatement.setString(parameterIndex++, context.getCompanyId());
			preparedStatement.executeUpdate();
			
			//ADD
			preparedStatement = connection.prepareStatement(Constants.DB_ADD_TALLY_SUMMARY);
			
			parameterIndex = 1;
			for(int index=0; index<context.getKeys().size(); index++) {
				preparedStatement.setInt(parameterIndex++, context.getReportId());
				//preparedStatement.setString(parameterIndex++, context.getReportName());
				preparedStatement.setString(parameterIndex++, context.getKeys().get(index));
				preparedStatement.setString(parameterIndex++, context.getValues1().get(index));
				preparedStatement.setString(parameterIndex++, context.getValues2().get(index));
				preparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
				preparedStatement.setBoolean(parameterIndex++, context.isCheckFlag());
				preparedStatement.setString(parameterIndex++, context.getCompanyId());
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
			System.out.println("tally company id : " + tally.getCompanyId());
			
			int parameterIndex = 1;
			preparedStatement.setInt(parameterIndex++, Integer.parseInt(tally.getTallySummaryIid()));
			preparedStatement.setInt(parameterIndex++, Integer.parseInt(tally.getReportId()));
			preparedStatement.setString(parameterIndex++, tally.getCompanyId());
			
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
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			
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
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherNumber());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getPartyLedgerName());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getEffectiveDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getMasterId());
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
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getVoucherNumber());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getPartyLedgerName());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getEffectiveDate());
				preparedStatement.setString(parameterIndex++, dayBookMasterVO.getMasterId());
				preparedStatement.setBoolean(parameterIndex++, false);
				preparedStatement.setDate(parameterIndex++, Utility.getCurrentdate());
				preparedStatement.setString(parameterIndex++, null);
				preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
				
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
					ledgerPreparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
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
					inventoryPreparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
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
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				dayBookMasterVO = new DayBookMasterVO();
				dayBookMasterVO.setVoucherKey(resultSet.getString("VOUCHER_KEY"));
				dayBookMasterVO.setVoucherType(resultSet.getString("VCH_TYPE"));
				dayBookMasterVO.setVoucherDate(resultSet.getString("VOUCHER_DATE_FORMATTED"));
				dayBookMasterVO.setVoucherNumber(resultSet.getString("VOUCHER_NUMBER"));
				dayBookMasterVO.setPartyLedgerName(resultSet.getString("PARTY_LEDGER_NAME"));
				dayBookMasterVO.setEffectiveDate(resultSet.getString("EFFECTIVE_DATE"));
				dayBookMasterVO.setMasterId(resultSet.getString("MASTER_ID"));
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
	
	public List<LedgerEntryVO> getTallyDayBookLedgerEntries(String companyId) {
		
		List<LedgerEntryVO> ledgerEntryVOs = new ArrayList<>();
		LedgerEntryVO ledgerEntryVO = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_DAYBOOK_LEDGER);
			preparedStatement.setString(1, companyId);
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

	public List<InventoryEntryVO> getTallyDayBookInventoryEntries(String companyId) {
		
		List<InventoryEntryVO> inventoryEntryVOs = new ArrayList<>();
		InventoryEntryVO inventoryEntryVO = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_DAYBOOK_INVENTORY);
			preparedStatement.setString(1, companyId);
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
				stockItemDetail.setSizeAct(resultSet.getString(Constants.SIZE_ACT));
				stockItemDetail.setSizeTgt(resultSet.getString(Constants.SIZE_TGT));
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
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			preparedStatement.setDate(2, Utility.convertStringToDate(tallyInputDTO.getStartDate()));
			preparedStatement.setDate(3, Utility.convertStringToDate(tallyInputDTO.getEndDate()));
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockGSMDetail = new StockGSMDetail();
				
				stockGSMDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockGSMDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				//stockGSMDetail.setGsmTgt(null != resultSet.getString(Constants.GSM_TGT) ? Double.parseDouble(resultSet.getString(Constants.GSM_TGT).trim()) : 0);
				//stockGSMDetail.setGsmAct(null != resultSet.getString(Constants.GSM_ACT) ? Double.parseDouble(resultSet.getString(Constants.GSM_ACT).trim()) : 0);
				
				stockGSMDetail.setGsmTgt(resultSet.getDouble(Constants.GSM_TGT));
				stockGSMDetail.setGsmAct(resultSet.getDouble(Constants.GSM_ACT));
				stockGSMDetail.setBatchName(resultSet.getString("BATCH_NAME"));
				
				
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
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			preparedStatement.setDate(2, Utility.convertStringToDate(tallyInputDTO.getStartDate()));
			preparedStatement.setDate(3, Utility.convertStringToDate(tallyInputDTO.getEndDate()));
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				stockBFDetail = new StockBFDetail();
				
				stockBFDetail.setVoucherEffectiveDate(resultSet.getString(Constants.EFFECTIVE_DATE));
				stockBFDetail.setStockItemName(resultSet.getString(Constants.STOCK_ITEM_NAME));
				stockBFDetail.setBfTgt(resultSet.getDouble(Constants.BF_TGT));
				stockBFDetail.setBfAct(resultSet.getDouble(Constants.BF_ACT));
				stockBFDetail.setBatchName(resultSet.getString("BATCH_NAME"));
				
				
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
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
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
				stocks.setCobb(resultSet.getString(Constants.COBB));
				stocks.setReelLen(resultSet.getString(Constants.REEL_LEN));
				stocks.setJoints(resultSet.getString(Constants.JOINTS));
				stocks.setRealDia(resultSet.getString(Constants.REEL_DIA));
				stocks.setMoist(resultSet.getString(Constants.MOIST));
				stocks.setSizeAct(resultSet.getString(Constants.SIZE_ACT));
				stocks.setSizeTgt(resultSet.getString(Constants.SIZE_TGT));
				stocks.setUnits(resultSet.getString(Constants.UNITS));
				stocks.setVoucherKey(resultSet.getString(Constants.VOUCHER_KEY));
				stocks.setStockItemDetailsId(resultSet.getString(Constants.STOCK_ITEM_DETAILS_ID));
				stocks.setBatchName(resultSet.getString(Constants.BATCH_NAME));
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

	/*public StockStatistics getStocksStatistics(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocksStatistics In");
		
		StockStatistics statistics = new StockStatistics();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_YEAR);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockYear(resultSet.getString(1));
			}
			
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_QUARTER);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockQuarter(resultSet.getString(2));
			}
			
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_MONTH);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setStockMonth(resultSet.getString(3));
			}
			
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_WEEK);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
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
	}*/
	
	public List<ProductionDashboardChart> getProductionDashboardChart(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart In");
		
		List<ProductionDashboardChart> productionDashboardCharts = new ArrayList<>();
		ProductionDashboardChart productionDashboardChart = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_PRODUCTION_DASHBOARD_CHART);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
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
	
	public List<ProductionSummary> getProductionSummary(TallyInputDTO tallyInputDTO) {
		
		ProductionSummary productionSummary = null;
		List<ProductionSummary> productionSummaries =  new ArrayList<ProductionSummary>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_PRODUCTION_SUMMARY);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			preparedStatement.setString(2, "PROD");
			preparedStatement.setString(3, "Yes");
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				productionSummary = new ProductionSummary();
				productionSummary.setMonth(resultSet.getString("MONTH"));
				//productionSummary.setAmount(Integer.toString(Double.valueOf(resultSet.getString("AMOUNT")).intValue()));
				productionSummary.setAmount(resultSet.getString("AMOUNT"));
				
				productionSummaries.add(productionSummary);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return productionSummaries;
	}
	
	public List<SalesSummary> getSalesSummary(TallyInputDTO tallyInputDTO) {
		
		SalesSummary salesSummary = null;
		List<SalesSummary> salesSummaries =  new ArrayList<SalesSummary>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_PRODUCTION_SUMMARY);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			preparedStatement.setString(2, "SALES");
			preparedStatement.setString(3, "Yes");
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				salesSummary = new SalesSummary();
				salesSummary.setMonth(resultSet.getString("MONTH"));
				//salesSummary.setAmount(Integer.toString(Double.valueOf(resultSet.getString("AMOUNT")).intValue()));
				salesSummary.setAmount(resultSet.getString("AMOUNT"));
				
				salesSummaries.add(salesSummary);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return salesSummaries;
	}
	
	public List<ProductionSummaryByYear> getProductionSummaryByYear(TallyInputDTO tallyInputDTO) {
		
		ProductionSummaryByYear productionSummaryByYear = null;
		List<ProductionSummaryByYear> productionSummaries =  new ArrayList<ProductionSummaryByYear>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_PRODUCTION_SUMMARY_BY_YEAR);
			preparedStatement.setString(1, "PROD");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				productionSummaryByYear = new ProductionSummaryByYear();
				productionSummaryByYear.setYear(resultSet.getString(1));
				productionSummaryByYear.setAmount(resultSet.getString(2));
				
				productionSummaries.add(productionSummaryByYear);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return productionSummaries;
	}

	

	public List<SalesSummaryByYear> getSalesSummaryByYear(TallyInputDTO tallyInputDTO) {
		
		SalesSummaryByYear salesSummaryByYear = null;
		List<SalesSummaryByYear> salesSummaries =  new ArrayList<SalesSummaryByYear>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_PRODUCTION_SUMMARY_BY_YEAR);
			preparedStatement.setString(1, "SALES");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				salesSummaryByYear = new SalesSummaryByYear();
				salesSummaryByYear.setYear(resultSet.getString(1));
				salesSummaryByYear.setAmount(resultSet.getString(2));
				
				salesSummaries.add(salesSummaryByYear);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return salesSummaries;
	}
	
	public StockStatistics getSummaryFromHistoryData(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSummaryFromHistoryData In");
		
		StockStatistics statistics = new StockStatistics();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_CURRENT_YEAR_SUMMARY);
			preparedStatement.setString(1, "SALES");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setYearlySales(resultSet.getString(1));
			}
			
			preparedStatement = connection.prepareStatement(new StringBuilder(Constants.DB_GET_CURRENT_QUARTER_SUMMARY).append(Utility.getCurrentQuarter()).toString());
			preparedStatement.setString(1, "SALES");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setQuarterlySales(resultSet.getString(1));
			}
	
			preparedStatement = connection.prepareStatement(Constants.DB_GET_CURRENT_MONTH_SUMMARY);
			preparedStatement.setString(1, "SALES");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			preparedStatement.setString(5, Utility.getCurrentMonth());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setMonthlySales(resultSet.getString(1));
			}
			
			//production
			preparedStatement = connection.prepareStatement(Constants.DB_GET_CURRENT_YEAR_SUMMARY);
			preparedStatement.setString(1, "PROD");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setYearlyProduction(resultSet.getString(1));
			}
			
			//System.out.println(new StringBuilder(Constants.DB_GET_CURRENT_QUARTER_SUMMARY).append(Utility.getCurrentQuarter()).toString());
			preparedStatement = connection.prepareStatement(new StringBuilder(Constants.DB_GET_CURRENT_QUARTER_SUMMARY).append(Utility.getCurrentQuarter()).toString());
			preparedStatement.setString(1, "PROD");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setQuarterlyProduction(resultSet.getString(1));
			}		
			preparedStatement = connection.prepareStatement(Constants.DB_GET_CURRENT_MONTH_SUMMARY);
			preparedStatement.setString(1, "PROD");
			preparedStatement.setString(2, "Yes");
			preparedStatement.setString(3, tallyInputDTO.getCompanyId());
			preparedStatement.setString(4, Utility.getCurrentFinancialYear());
			preparedStatement.setString(5, Utility.getCurrentMonth());
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				statistics.setMonthlyProduction(resultSet.getString(1));
			}
			
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSummaryFromHistoryData Out");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return statistics;
	}

	public void addMessage(Message message) throws Exception {
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_ADD_MESSAGE);
			preparedStatement.setString(1, message.getMessage());
			preparedStatement.setString(2, message.getUrl());
			preparedStatement.setString(3, message.getRole());
			preparedStatement.setString(4, "active");
			preparedStatement.setDate(5, Utility.getCurrentdate());
			preparedStatement.setString(6, message.getCompanyId());
			preparedStatement.setString(7, message.getTitle());
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in adding messages in DB...");
			e.printStackTrace();
			
			throw new RuntimeException(e);
			
		} finally {
			closeResources();
		}
	}
	
	public List<Message> getMessage(String companyId) throws Exception {
		
		Message message = null;
		List<Message> messages = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_MESSAGE);
			preparedStatement.setString(1, companyId);
			resultSet = preparedStatement.executeQuery();
		
			while(resultSet.next()) {
				
				message = new Message();
				message.setMessage(resultSet.getString(1));
				message.setUrl(resultSet.getString(2));
				message.setCreatedDate(resultSet.getString(3));
				message.setRole(resultSet.getString(4));
				message.setTitle(resultSet.getString(5));
				
				messages.add(message);
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting messages from DB...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return messages;
	}
	
	public List<SalesOrder> getSalesOrder(String companyId, String status) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER);
			
			if(status.equals("active")) {
				preparedStatement.setInt(1, 0);
			} else { //status = inactive
				preparedStatement.setInt(1, 1);
			}
			
			preparedStatement.setString(2, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setId(resultSet.getString(index++));
				salesOrder.setOrderNumber(resultSet.getString(index++));
				salesOrder.setVoucherKey(resultSet.getString(index++));
				salesOrder.setCompany(resultSet.getString(index++));
				salesOrder.setOrderDate(resultSet.getString(index++));
				//salesOrder.setBf(resultSet.getString(index++));
				//salesOrder.setGsm(resultSet.getString(index++));
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setSize(resultSet.getString(index++));
				salesOrder.setWeight(resultSet.getString(index++));
				salesOrder.setReel(resultSet.getString(index++));
				salesOrder.setOrderStatus(resultSet.getString(index++));
				salesOrder.setAltered(resultSet.getString(index++));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrder DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public List<SalesOrder> getSalesOrderByBfGsmSize(String companyId) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER_BY_BF_GSM_SIZE);
			preparedStatement.setString(1, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setSize(resultSet.getString(index++));
				salesOrder.setWeight(resultSet.getString(index++));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderByBfGsmSize DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public List<SalesOrder> getSalesOrderByBf(String companyId) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER_BY_BF);
			preparedStatement.setString(1, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setWeight(resultSet.getString(index++));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderByBF DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public List<SalesOrder> getSalesOrderByBfGsm(String companyId) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER_BY_BF_GSM);
			preparedStatement.setString(1, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setWeight(resultSet.getString(index++));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderByBFAndGsm DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public Response deleteSalesOrder(TallyInputDTO tallyInputDTO) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_SALES_ORDER);
			
			System.out.println("Update query : " + Constants.DB_UPDATE_SALES_ORDER + ":" + tallyInputDTO.getId() + ":" + tallyInputDTO.getCompanyId());
			
			int parameterIndex = 1;
			
			if(tallyInputDTO.getType().equals("restore")) {
				preparedStatement.setInt(parameterIndex++, 0);
			} else { //type = remove
				preparedStatement.setInt(parameterIndex++, 1);
			}
			
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getId());
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			// TODO: handle exception
			System.out.println("Error in updating sales_orders order_status flag field in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			closeResources();
		}
		
		return response;
	}
	
	public Response updateSalesOrders(TallyInputDTO tallyInputDTO, String batchNumber) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		PreparedStatement preparedStatementModified = null;
		PreparedStatement preparedStatementPlanned = null;
		
		try {
			
			if(null == batchNumber) {
				batchNumber = Utility.getBatchNumber();
			}
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_COMPLETED_SALES_ORDERS); //SALES_ORDERS
			preparedStatementModified = connection.prepareStatement(Constants.DB_UPDATE_MODIFIED_SALES_ORDERS); //SALES_ORDERS
			preparedStatementPlanned = connection.prepareStatement(Constants.DB_ADD_SALES_ORDERS_PLANNED); //SALES_ORDERS_PLANNED
			
			System.out.println("Update query : " + Constants.DB_UPDATE_COMPLETED_SALES_ORDERS + ":" + tallyInputDTO.getCompanyId());
			System.out.println("Update query : " + Constants.DB_UPDATE_MODIFIED_SALES_ORDERS + ":" + tallyInputDTO.getCompanyId());
			System.out.println("Update query : " + Constants.DB_ADD_SALES_ORDERS_PLANNED + ":" + tallyInputDTO.getCompanyId());
			
			List<SalesOrder> salesOrders = tallyInputDTO.getSalesOrders();
			int parameterIndex = 1;
			
			connection.setAutoCommit(false);
			
			//New weight = actual ordered weight - planned weight
			//weight = planned weight
			
			for(SalesOrder salesOrder : salesOrders) {
				if(salesOrder.getAltered().equals("1")) { //sales order modified
					preparedStatementModified.setDouble(parameterIndex++, Double.parseDouble(salesOrder.getNewWeight())); //pending weight , yet to be planned (original weight - planned weight)
					preparedStatementModified.setDouble(parameterIndex++, Double.parseDouble(Utility.getReel(salesOrder.getSize(), Double.parseDouble(salesOrder.getNewWeight()))));
					preparedStatementModified.setString(parameterIndex++, salesOrder.getId());
					preparedStatementModified.setString(parameterIndex++, tallyInputDTO.getCompanyId());
					preparedStatementModified.executeUpdate();
					
				} else { //sales order not modified
					preparedStatement.setString(parameterIndex++, salesOrder.getId());
					preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
					preparedStatement.executeUpdate();
				}
				
				parameterIndex = 1;
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getId());
				preparedStatementPlanned.setString(parameterIndex++, batchNumber);
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getVoucherKey());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getOrderDate());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getCompany());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getSize());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getWeight()); //planned weight.
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getOrderStatus());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getAltered());
				preparedStatementPlanned.setString(parameterIndex++, tallyInputDTO.getCompanyId());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getOrderNumber());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getBf());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getGsm());
				//preparedStatementPlanned.setString(parameterIndex++, Utility.getReel(salesOrder.getSize(), Double.parseDouble(salesOrder.getWeight())));
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getReel());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getReelInStock());
				
				preparedStatementPlanned.executeUpdate();
				
				parameterIndex = 1;
			}
			
			connection.commit();
			
		} catch (Exception e) {
			
			try {
				connection.rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
			// TODO: handle exception
			System.out.println("Error in updating sales_orders order_status flag field in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			
			try {
				if(null != preparedStatementModified) {
					preparedStatementModified.close();
				}
				if(null != preparedStatementPlanned) {
					preparedStatementPlanned.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			closeResources();
		}
		
		return response;
	}

	
	/*public Response updateSalesOrders(TallyInputDTO tallyInputDTO) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		PreparedStatement preparedStatementModified = null;
		PreparedStatement preparedStatementPlanned = null;
		PreparedStatement preparedStatementPlannedJSON = null;
		
		try {
			
			String batchNumber = Utility.getBatchNumber();
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_COMPLETED_SALES_ORDERS);
			preparedStatementModified = connection.prepareStatement(Constants.DB_UPDATE_MODIFIED_SALES_ORDERS);
			preparedStatementPlanned = connection.prepareStatement(Constants.DB_ADD_SALES_ORDERS_PLANNED);
			
			preparedStatementPlannedJSON = connection.prepareStatement(Constants.DB_ADD_SALES_ORDERS_PLANNED_JSON);
			
			System.out.println("Update query : " + Constants.DB_UPDATE_COMPLETED_SALES_ORDERS + ":" + tallyInputDTO.getCompanyId());
			System.out.println("Update query : " + Constants.DB_UPDATE_MODIFIED_SALES_ORDERS + ":" + tallyInputDTO.getCompanyId());
			System.out.println("Update query : " + Constants.DB_ADD_SALES_ORDERS_PLANNED + ":" + tallyInputDTO.getCompanyId());
			
			List<SalesOrder> salesOrders = tallyInputDTO.getSalesOrders();
			int parameterIndex = 1;
			
			connection.setAutoCommit(false);
			
			for(SalesOrder salesOrder : salesOrders) {
				if(salesOrder.getAltered().equals("1")) { //sales order modified
					preparedStatementModified.setDouble(parameterIndex++, Double.parseDouble(salesOrder.getNewWeight())); //pending weight , yet to be planned (original weight - planned weight)
					preparedStatementModified.setDouble(parameterIndex++, Double.parseDouble(Utility.getReel(salesOrder.getSize(), Double.parseDouble(salesOrder.getNewWeight()))));
					preparedStatementModified.setString(parameterIndex++, salesOrder.getId());
					preparedStatementModified.setString(parameterIndex++, tallyInputDTO.getCompanyId());
					preparedStatementModified.executeUpdate();
					
				} else { //sales order not modified
					preparedStatement.setString(parameterIndex++, salesOrder.getId());
					preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
					preparedStatement.executeUpdate();
				}
				
				parameterIndex = 1;
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getId());
				preparedStatementPlanned.setString(parameterIndex++, batchNumber);
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getVoucherKey());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getOrderDate());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getCompany());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getSize());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getWeight()); //planned weight.
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getOrderStatus());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getAltered());
				preparedStatementPlanned.setString(parameterIndex++, tallyInputDTO.getCompanyId());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getOrderNumber());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getBf());
				preparedStatementPlanned.setString(parameterIndex++, salesOrder.getGsm());
				preparedStatementPlanned.setString(parameterIndex++, Utility.getReel(salesOrder.getSize(), Double.parseDouble(salesOrder.getWeight())));
				
				preparedStatementPlanned.executeUpdate();
				
				parameterIndex = 1;
			}
			
			preparedStatementPlannedJSON.setString(parameterIndex++, batchNumber);
			preparedStatementPlannedJSON.setString(parameterIndex++, "BF"); //json type
			preparedStatementPlannedJSON.setString(parameterIndex++, tallyInputDTO.getConsBf()); //json value
			preparedStatementPlannedJSON.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			
			preparedStatementPlannedJSON.executeUpdate();
			
			parameterIndex = 1;
			preparedStatementPlannedJSON.setString(parameterIndex++, batchNumber);
			preparedStatementPlannedJSON.setString(parameterIndex++, "GSM"); //json type
			preparedStatementPlannedJSON.setString(parameterIndex++, tallyInputDTO.getConsBf()); //json value
			preparedStatementPlannedJSON.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			
			preparedStatementPlannedJSON.executeUpdate();
			
			parameterIndex = 1;
			preparedStatementPlannedJSON.setString(parameterIndex++, batchNumber);
			preparedStatementPlannedJSON.setString(parameterIndex++, "SIZE"); //json type
			preparedStatementPlannedJSON.setString(parameterIndex++, tallyInputDTO.getConsBf()); //json value
			preparedStatementPlannedJSON.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			
			preparedStatementPlannedJSON.executeUpdate();
			
			
			connection.commit();
			
		} catch (Exception e) {
			
			try {
				connection.rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
			// TODO: handle exception
			System.out.println("Error in updating sales_orders order_status flag field in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			
			try {
				if(null != preparedStatementModified) {
					preparedStatementModified.close();
				}
				if(null != preparedStatementPlanned) {
					preparedStatementPlanned.close();
				}
				if(null != preparedStatementPlannedJSON) {
					preparedStatementPlanned.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			closeResources();
		}
		
		return response;
	}*/

	public List<SalesOrderPlanned> getSalesOrdersPlanned(String companyId) throws Exception {
		
		SalesOrderPlanned salesOrderPlanned = null;
		List<SalesOrderPlanned> salesOrderPlanneds = new ArrayList<SalesOrderPlanned>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDERS_PLANNED);
			preparedStatement.setString(1, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrderPlanned = new SalesOrderPlanned();
				
				salesOrderPlanned.setSalesOrderPlannedId(resultSet.getString(index++));
				salesOrderPlanned.setId(resultSet.getString(index++));
				salesOrderPlanned.setBatchNumber(resultSet.getString(index++));
				salesOrderPlanned.setVoucherKey(resultSet.getString(index++));
				salesOrderPlanned.setOrderDate(resultSet.getString(index++));
				salesOrderPlanned.setCompany(resultSet.getString(index++));
				salesOrderPlanned.setSize(resultSet.getString(index++));
				salesOrderPlanned.setWeight(resultSet.getString(index++));
				salesOrderPlanned.setOrderStatus(resultSet.getString(index++));
				salesOrderPlanned.setAltered(resultSet.getString(index++));
				salesOrderPlanned.setCreatedDate(resultSet.getString(index++));
				salesOrderPlanned.setModifiedDate(resultSet.getString(index++));
				salesOrderPlanned.setCompanyId(resultSet.getString(index++));
				salesOrderPlanned.setOrderNumber(resultSet.getString(index++));
				salesOrderPlanned.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrderPlanned.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrderPlanned.setReel(resultSet.getString(index++));
				salesOrderPlanned.setReelInStock(resultSet.getString(index++));
				salesOrderPlanneds.add(salesOrderPlanned);
				
				index = 1;
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderPlanned DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrderPlanneds;
	}
	
	public List<SalesOrderDispatch> getSalesOrdersDispatch(TallyInputDTO tallyInputDTO) throws Exception {
		
		SalesOrderDispatch salesOrderDispatch = null;
		List<SalesOrderDispatch> salesOrderDispatchs = new ArrayList<SalesOrderDispatch>();
		double reel = 0.0;
		double reelInstock = 0.0;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDERS_DISPATCH);
			preparedStatement.setString(1, tallyInputDTO.getCompanyId());
			preparedStatement.setString(2, tallyInputDTO.getBatchNo());
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrderDispatch = new SalesOrderDispatch();
				
				salesOrderDispatch.setCompany(resultSet.getString(index++));
				salesOrderDispatch.setOrderNumber(resultSet.getString(index++));
				salesOrderDispatch.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrderDispatch.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrderDispatch.setSize(resultSet.getString(index++));
				salesOrderDispatch.setWeight(resultSet.getString(index++));
				//salesOrderDispatch.setReel(resultSet.getString(index++));
				//salesOrderDispatch.setReelInStock(resultSet.getString(index++));
				
				reel = resultSet.getDouble(index++);
				reelInstock = resultSet.getDouble(index++);
				salesOrderDispatch.setReel(Utility.calculateDispatchReal(reel, reelInstock));
				salesOrderDispatch.setReelInStock(Double.toString(reelInstock));
				
				salesOrderDispatchs.add(salesOrderDispatch);
				
				index = 1;
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderPlanned DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrderDispatchs;
	}
	
	
	public List<SalesOrder> getSalesOrderPlannedByBf(String companyId, String batchNo) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER_PLANNED_BY_BF);
			preparedStatement.setString(1, batchNo);
			preparedStatement.setString(2, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setWeight(resultSet.getString(index++));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderByBF DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public List<SalesOrder> getSalesOrderPlannedByBfGsm(String companyId, String batchNo) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER_PLANNED_BY_BF_GSM);
			preparedStatement.setString(1, batchNo);
			preparedStatement.setString(2, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setWeight(resultSet.getString(index++));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderByBFAndGsm DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public List<SalesOrder> getSalesOrderPlannedByBfGsmSize(String companyId, String batchNo) throws Exception {
		
		SalesOrder salesOrder = null;
		List<SalesOrder> salesOrders = new ArrayList<>();
		double reel = 0.0;
		double reelInstock = 0.0;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_GET_SALES_ORDER_PLANNED_BY_BF_GSM_SIZE);
			preparedStatement.setString(1, batchNo);
			preparedStatement.setString(2, companyId);
			resultSet = preparedStatement.executeQuery();
		
			int index = 1;
			
			while(resultSet.next()) {
				
				salesOrder = new SalesOrder();
				salesOrder.setBf(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setGsm(Utility.zeroTruncating(resultSet.getDouble(index++)));
				salesOrder.setSize(resultSet.getString(index++));
				salesOrder.setWeight(resultSet.getString(index++));
				
				reel = resultSet.getDouble(index++);
				reelInstock = resultSet.getDouble(index++);
				salesOrder.setReel(Utility.calculateDispatchReal(reel, reelInstock));
				salesOrder.setReelInStock(Double.toString(reelInstock));
				salesOrders.add(salesOrder);
				
				index = 1;
				
			} 
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getSalesOrderByBfGsmSize DAO...");
			e.printStackTrace();
			//throw new Exception("Server error");
			throw new RuntimeException(e);
		} finally {
			closeResources();
		}
		
		return salesOrders;
	}
	
	public Response updateSalesOrderPlannedReel(TallyInputDTO tallyInputDTO) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_SALES_ORDERS_PLANNED_REEL);
			
			System.out.println("Update query : " + Constants.DB_UPDATE_SALES_ORDERS_PLANNED_REEL);
			
			int parameterIndex = 1;
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getReel());
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getId());
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			// TODO: handle exception
			System.out.println("Error in updating sales order planned reel field in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
		} finally {
			closeResources();
		}
		
		return response;
	}
	
	public Response deleteSalesOrderPlanned(TallyInputDTO tallyInputDTO) {
		
		Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			
			preparedStatement = connection.prepareStatement(Constants.DB_DELETE_SALES_ORDER_PLANNED);
			
			System.out.println("Update query : " + Constants.DB_DELETE_SALES_ORDER_PLANNED + ":" + tallyInputDTO.getId() + ":" + tallyInputDTO.getCompanyId());
			
			int parameterIndex = 1;
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getSalesOrderedPlannedId());
			preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
			
			preparedStatement.executeUpdate();
			
			//
			if(tallyInputDTO.getAltered().equals("1")) {
				preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_MODIFIED_SALES_ORDERS_RESTORED);
				
				System.out.println("Update query : " + Constants.DB_UPDATE_MODIFIED_SALES_ORDERS_RESTORED + ":" + tallyInputDTO.getId() + ":" + tallyInputDTO.getCompanyId());
				
				parameterIndex = 1;
				preparedStatement.setDouble(parameterIndex++, Double.parseDouble(tallyInputDTO.getWeight()));
				preparedStatement.setString(parameterIndex++, tallyInputDTO.getId());
				preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
				
				preparedStatement.executeUpdate();
			} else {
				preparedStatement = connection.prepareStatement(Constants.DB_UPDATE_COMPLETED_SALES_ORDERS_RESTORED);
				
				System.out.println("Update query : " + Constants.DB_UPDATE_COMPLETED_SALES_ORDERS_RESTORED + ":" + tallyInputDTO.getId() + ":" + tallyInputDTO.getCompanyId());
				
				parameterIndex = 1;
				preparedStatement.setString(parameterIndex++, tallyInputDTO.getId());
				preparedStatement.setString(parameterIndex++, tallyInputDTO.getCompanyId());
				
				preparedStatement.executeUpdate();
			}
			
			connection.commit();
			
		} catch (Exception e) {
			
			// TODO: handle exception
			System.out.println("Error in deleting sales_orders_planned in DB...");
			e.printStackTrace();
			
			response.setStatus(Constants.RESPONSE_STATUS_FAILED);
			response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_FAILED);
			
			try {
				if(null != connection) {connection.rollback();}
			}catch (Exception ex) {
				// TODO: handle exception
			}
		} finally {
			closeResources();
		}
		
		return response;
	}
}
