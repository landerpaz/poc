package com.india.tamilnadu.tally.bc;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.ProductionDashboardChart;
import com.india.tamilnadu.tally.vo.ProductionSummary;
import com.india.tamilnadu.tally.vo.ProductionSummaryByYear;
import com.india.tamilnadu.tally.vo.SalesSummary;
import com.india.tamilnadu.tally.vo.SalesSummaryByYear;
import com.india.tamilnadu.tally.vo.StockBFDetail;
import com.india.tamilnadu.tally.vo.StockDetail;
import com.india.tamilnadu.tally.vo.StockGSMDetail;
import com.india.tamilnadu.tally.vo.StockItemDetail;
import com.india.tamilnadu.tally.vo.StockMaster;
import com.india.tamilnadu.tally.vo.StockStatistics;
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
	
	public List<StockGSMDetail> getGSMDataLast7Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getGSMDataLast7Days In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockGSMDetail> stockGSMDetails = tallyDAO.getStockGSMDetailLast7Days(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getGSMDataLast7Days Out");
		
		return stockGSMDetails;
	}

	public List<StockGSMDetail> getGSMDataLast30Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getGSMDataLast30Days In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockGSMDetail> stockGSMDetails = tallyDAO.getStockGSMDetailLast30Days(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getGSMDataLast30Days Out");
		
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
	
	public List<StockBFDetail> getBFDataLast7days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getBFDataLast7days In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockBFDetail> stockBFDetails = tallyDAO.getStockBFDetailLast7Days(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getBFDataLast7days Out");
		
		return stockBFDetails;
	}

	public List<StockBFDetail> getBFDataLast30Days(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getBFDataLast30Days In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<StockBFDetail> stockBFDetails = tallyDAO.getStockBFDetailLast30Days(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getBFDataLast30Days Out");
		
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
	
	public StockStatistics getStocksStatistics(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocksStatistics In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		
		//get weekly, monthly, quarterly and yealy from stock_master, stocker_details and stock_item_details tables
		//StockStatistics statistics = tallyDAO.getStocksStatistics(tallyInputDTO);
		
		//get monthly, quarterly and yealy from history_data table
		StockStatistics statistics = tallyDAO.getSummaryFromHistoryData(tallyInputDTO);
		
		statistics.setProductionSummaryByMonth(getProductionSummary(tallyInputDTO));
		statistics.setSalesSummaryByMonth(getSalesSummary(tallyInputDTO));
		statistics.setProductionSummaryByYear(getProductionSummaryByYear(tallyInputDTO));
		statistics.setSalesSummaryByYear(getSalesSummaryByYear(tallyInputDTO));
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocksStatistics Out");
		
		return statistics;
	}
	
	public List<ProductionDashboardChart> getProductionDashboardChart(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<ProductionDashboardChart> productionDashboardCharts = tallyDAO.getProductionDashboardChart(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart Out");
		
		return productionDashboardCharts;
	}

	private List<ProductionSummary> getProductionSummary(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionSummary In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<ProductionSummary> productionSummaries = tallyDAO.getProductionSummary(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionSummary Out");
		
		return productionSummaries;
	}
	
	private List<SalesSummary> getSalesSummary(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesSummary In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<SalesSummary> salesSummaries = tallyDAO.getSalesSummary(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesSummary Out");
		
		return salesSummaries;
	}
	
	private List<ProductionSummaryByYear> getProductionSummaryByYear(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionSummaryByYear In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<ProductionSummaryByYear> productionSummaries = tallyDAO.getProductionSummaryByYear(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionSummaryByYear Out");
		
		return productionSummaries;
	}
	
	private List<SalesSummaryByYear> getSalesSummaryByYear(TallyInputDTO tallyInputDTO) throws Exception {
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesSummaryByYear In");
		
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<SalesSummaryByYear> salesSummaries = tallyDAO.getSalesSummaryByYear(tallyInputDTO);
		
		LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesSummaryByYear Out");
		
		return salesSummaries;
	}
}
