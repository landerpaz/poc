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

	public void addTallyDayBook(TallyInputDTO tallyInputDTO) throws Exception {
		
		/*Response response = new Response();
		response.setStatus(Constants.RESPONSE_STATUS_SUCCESS);
		response.setStatusMessage(Constants.RESPONSE_MESSAGE_PRODUCT_ADD_SUCCESS);*/
		
		PreparedStatement ledgerPreparedStatement = null;
		PreparedStatement inventoryPreparedStatement = null;
		
		try {
			
			connection = DatabaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			
			//delete data from table
			preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_LEDGER);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_INVENTORY);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement(Constants.DB_DELETE_DAYBOOK_MASTER);
			preparedStatement.execute();
			
			//insert data into table
			for(DayBookMasterVO dayBookMasterVO : tallyInputDTO.getDayBookMasterVOs()) {
			
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
					inventoryPreparedStatement.setString(parameterIndex++, inventoryEntryVO.getAmount());
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

	public List<DayBookMasterVO> getTallyDayBookMaster() {
		
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
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting tally summry from DB...");
			e.printStackTrace();
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

}
