package com.india.tamilnadu.tally.bc;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.StockBFDetail;
import com.india.tamilnadu.tally.vo.StockDetail;
import com.india.tamilnadu.tally.vo.StockGSMDetail;
import com.india.tamilnadu.tally.vo.StockItemDetail;
import com.india.tamilnadu.tally.vo.StockMaster;
import com.india.tamilnadu.tally.vo.Stocks;

public class TallyStockBC {
	
	private final Logger LOG = LoggerFactory.getLogger(TallyStockBC.class);
	
	public static void main(String[] args) {
		try {
			TallyStockBC tallyStockBC = new TallyStockBC();
			List<StockMaster> stockMasters = tallyStockBC.getStockData(new TallyInputDTO());
			System.out.println("Count : " + stockMasters.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<StockMaster> getStockData(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockData In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockMaster> stockMasters = tallyDAO.getTallyStockMaster(tallyInputDTO);
		List<StockDetail> stockDetails = tallyDAO.getTallyStockDetail(tallyInputDTO);
		List<StockItemDetail> stockItemDetails = tallyDAO.getTallyStockItemDetail(tallyInputDTO);
		
		//System.out.println("Stock item detail : " + stockItemDetails.size());
		   
		//format data
		List<StockMaster> stockMastersResult = frameTallyDayBookdataFromLedgerAndInv(stockMasters, stockDetails, stockItemDetails);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockData Out");
		
		return stockMastersResult;
	}
	
	private List<StockMaster> frameTallyDayBookdataFromLedgerAndInv(List<StockMaster> stockMasters, List<StockDetail> stockDetails, List<StockItemDetail> stockItemDetails) throws Exception {
		
		List<StockMaster> stockMastersResult = new ArrayList<>();
		List<StockDetail> stockDetailsLocal = null;
		List<StockItemDetail> stockItemDetailsLocal = null;
		
		//add stock master
		for(StockMaster stockMaster : stockMasters) {
	
			//add stock detail
			stockDetailsLocal = new ArrayList<>();
			for(StockDetail stockDetail : stockDetails) {
				if(stockDetail.getVoucherKey().equals(stockMaster.getVoucherKey())) {
					
					//add stock item detail
					stockItemDetailsLocal = new ArrayList<>();
					for(StockItemDetail stockItemDetail : stockItemDetails) {
						if(stockItemDetail.getStockDetailsId().equals(stockDetail.getStockDetailsId())) {
							stockItemDetailsLocal.add(stockItemDetail);
						}
					}
					
					stockDetail.setStockItemDetails(stockItemDetailsLocal);
					stockDetailsLocal.add(stockDetail);
				}
			}
			
			stockMaster.setStockDetails(stockDetailsLocal);
			stockMastersResult.add(stockMaster);
		}	
			
		return stockMastersResult;
	}
	
	public List<StockGSMDetail> getGSMData(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getGSMData In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockGSMDetail> stockGSMDetails = tallyDAO.getStockGSMDetail(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getGSMData Out");
		
		return stockGSMDetails;
	}
	
	public List<StockBFDetail> getBFData(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getBFData In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockBFDetail> stockBFDetails = tallyDAO.getStockBFDetail(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getBFData Out");
		
		return stockBFDetails;
	}
	
	public List<Stocks> getStocks(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocks In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<Stocks> stockss = tallyDAO.getStocks(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocks Out");
		
		return stockss;
	}
}
