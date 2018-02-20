package com.india.tamilnadu.tally.bc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.SalesOrder;
import com.india.tamilnadu.tally.vo.SalesOrderConsolidated;
import com.india.tamilnadu.tally.vo.SalesOrderDispatch;
import com.india.tamilnadu.tally.vo.SalesOrderDispatchSummary;
import com.india.tamilnadu.tally.vo.SalesOrderPlanned;
import com.india.tamilnadu.tally.vo.SalesOrderPlannedSummary;
import com.india.tamilnadu.util.Utility;


public class SalesOrderBC {

	public List<SalesOrder> getSalesOrder( String companyId, String trackingId, String status) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getSalesOrder(companyId, status);
	}
	
	public List<SalesOrder> getSalesOrderByBf( String companyId, String trackingId) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getSalesOrderByBf(companyId);
	}

	public List<SalesOrder> getSalesOrderByBfGsm( String companyId, String trackingId) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getSalesOrderByBfGsm(companyId);
	}
	
	public List<SalesOrder> getSalesOrderByBfGsmSize( String companyId, String trackingId) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getSalesOrderByBfGsmSize(companyId);
	}

	public Response deleteSalesOrder(TallyInputDTO tallyInputDTO) {
		TallyDAO tallyDAO = new TallyDAO(); 
		return tallyDAO.deleteSalesOrder(tallyInputDTO);
	}
	
	public Response updateSalesOrders(TallyInputDTO tallyInputDTO, String batchNumber) {
		TallyDAO tallyDAO = new TallyDAO(); 
		return tallyDAO.updateSalesOrders(tallyInputDTO, batchNumber);
	}
	
	/*public List<SalesOrderPlannedSummary> getSalesOrdersPlanned(TallyInputDTO tallyInputDTO) throws Exception {
		TallyDAO tallyDAO = new TallyDAO(); 
		List<SalesOrderPlanned> salesOrderPlanneds = tallyDAO.getSalesOrdersPlanned(tallyInputDTO.getCompanyId());
		List<SalesOrderPlanned> tempList = new ArrayList<SalesOrderPlanned>();
		List<SalesOrderPlannedSummary> salesOrderPlannedSummaries = new ArrayList<SalesOrderPlannedSummary>();
		
		SalesOrderPlannedSummary orderPlannedSummary = null;
		
		String batchNumber = null;
		boolean firstTime = true;
		
		for(SalesOrderPlanned salesOrderPlanned : salesOrderPlanneds) {
			
			
			if(firstTime) {
				batchNumber = salesOrderPlanned.getBatchNumber();
				orderPlannedSummary = new SalesOrderPlannedSummary();
				orderPlannedSummary.setBatchNumber(batchNumber);
				orderPlannedSummary.setCreatedDate(salesOrderPlanned.getCreatedDate());
				firstTime = false;
			}
			
			if(!batchNumber.equals(salesOrderPlanned.getBatchNumber())) {
				batchNumber = salesOrderPlanned.getBatchNumber();
				
				orderPlannedSummary.setSalesOrderPlanneds(tempList);
				salesOrderPlannedSummaries.add(orderPlannedSummary);
				
				orderPlannedSummary = new SalesOrderPlannedSummary();
				orderPlannedSummary.setBatchNumber(batchNumber);
				orderPlannedSummary.setCreatedDate(salesOrderPlanned.getCreatedDate());
				
				tempList = new ArrayList<>();
				tempList.add(salesOrderPlanned);
				
			} else {
				tempList.add(salesOrderPlanned);
			}
		}
		
		if(!CollectionUtils.isEmpty(tempList)) {
			orderPlannedSummary.setSalesOrderPlanneds(tempList);
			salesOrderPlannedSummaries.add(orderPlannedSummary);
		}
		
		return salesOrderPlannedSummaries;
	}*/
	
	public List<SalesOrderPlannedSummary> getSalesOrdersPlanned(TallyInputDTO tallyInputDTO) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO(); 
		List<SalesOrderPlanned> salesOrderPlanneds = tallyDAO.getSalesOrdersPlanned(tallyInputDTO.getCompanyId());
		
		List<SalesOrderPlanned> tempList = new ArrayList<SalesOrderPlanned>();
		List<SalesOrderPlannedSummary> salesOrderPlannedSummaries = new ArrayList<SalesOrderPlannedSummary>();
		
		SalesOrderPlannedSummary orderPlannedSummary = null;
		
		String batchNumber = null;
		boolean firstTime = true;
		
		for(SalesOrderPlanned salesOrderPlanned : salesOrderPlanneds) {
			
			
			if(firstTime) {
				batchNumber = salesOrderPlanned.getBatchNumber();
				orderPlannedSummary = new SalesOrderPlannedSummary();
				orderPlannedSummary.setBatchNumber(batchNumber);
				orderPlannedSummary.setCreatedDate(salesOrderPlanned.getCreatedDate());
				firstTime = false;
			}
			
			if(!batchNumber.equals(salesOrderPlanned.getBatchNumber())) {
				batchNumber = salesOrderPlanned.getBatchNumber();
				
				orderPlannedSummary.setSalesOrderPlanneds(tempList);
				salesOrderPlannedSummaries.add(orderPlannedSummary);
				
				orderPlannedSummary = new SalesOrderPlannedSummary();
				orderPlannedSummary.setBatchNumber(batchNumber);
				orderPlannedSummary.setCreatedDate(salesOrderPlanned.getCreatedDate());
				
				tempList = new ArrayList<>();
				tempList.add(salesOrderPlanned);
				
			} else {
				tempList.add(salesOrderPlanned);
			}
		}
		
		if(!CollectionUtils.isEmpty(tempList)) {
			orderPlannedSummary.setSalesOrderPlanneds(tempList);
			salesOrderPlannedSummaries.add(orderPlannedSummary);
		}
		
		List<SalesOrderPlannedSummary> salesOrderPlannedSummariesResult = new ArrayList<>();
		
		for(SalesOrderPlannedSummary salesOrderPlannedSummary : salesOrderPlannedSummaries) {
			
			List<SalesOrder> salesOrdersBf = tallyDAO.getSalesOrderPlannedByBf(tallyInputDTO.getCompanyId(), salesOrderPlannedSummary.getBatchNumber());
			List<SalesOrder> salesOrdersBfGsm = tallyDAO.getSalesOrderPlannedByBfGsm(tallyInputDTO.getCompanyId(), salesOrderPlannedSummary.getBatchNumber());
			List<SalesOrder> salesOrdersBfGsmItem = tallyDAO.getSalesOrderPlannedByBfGsmSize(tallyInputDTO.getCompanyId(), salesOrderPlannedSummary.getBatchNumber());
			
			salesOrderPlannedSummary.setSalesOrdersBf(salesOrdersBf);
			salesOrderPlannedSummary.setSalesOrdersBfGsm(salesOrdersBfGsm);
			salesOrderPlannedSummary.setSalesOrdersBfGsmSize(salesOrdersBfGsmItem);
			
			salesOrderPlannedSummariesResult.add(salesOrderPlannedSummary);
		}
		
		//return salesOrderPlannedSummaries;
		return salesOrderPlannedSummariesResult;
	}
	
	public List<SalesOrderDispatchSummary> getSalesOrdersDispatched(TallyInputDTO tallyInputDTO) throws Exception {
		TallyDAO tallyDAO = new TallyDAO(); 
		List<SalesOrderDispatch> SalesOrderDispatchs = tallyDAO.getSalesOrdersDispatch(tallyInputDTO);
		List<SalesOrderDispatch> tempList = new ArrayList<SalesOrderDispatch>();
		List<SalesOrderDispatchSummary> SalesOrderDispatchSummaries = new ArrayList<SalesOrderDispatchSummary>();
		
		SalesOrderDispatchSummary orderDispatchSummary = null;
		
		//String batchNumber = null;
		String orderNumber = null;
		String company = null;
		boolean firstTime = true;
		double totalReel = 0;
		double totalReelInStock = 0;
		double totalWeight = 0;
		
		for(SalesOrderDispatch SalesOrderDispatch : SalesOrderDispatchs) {
			
			
			if(firstTime) {
				orderNumber = SalesOrderDispatch.getOrderNumber();
				company = SalesOrderDispatch.getCompany();
				
				orderDispatchSummary = new SalesOrderDispatchSummary();
				orderDispatchSummary.setOrderNumber(orderNumber);
				orderDispatchSummary.setCompany(company);
				firstTime = false;
			}
			
			if(!orderNumber.equals(SalesOrderDispatch.getOrderNumber()) || !company.equals(SalesOrderDispatch.getCompany())) {
				/*orderNumber = SalesOrderDispatch.getOrderNumber();
				company = SalesOrderDispatch.getCompany();
				totalReel += Double.parseDouble(SalesOrderDispatch.getReel());
				*/
				
				orderDispatchSummary.setTotalReel(Double.toString(totalReel));
				orderDispatchSummary.setTotalReelInStock(Double.toString(totalReelInStock));
				orderDispatchSummary.setTotalWeight(Double.toString(totalWeight));
				totalReel = 0;
				totalReelInStock = 0;
				totalWeight = 0;
				orderDispatchSummary.setSalesOrderDispatchs(tempList);
				SalesOrderDispatchSummaries.add(orderDispatchSummary);
				
				orderDispatchSummary = new SalesOrderDispatchSummary();
				orderNumber = SalesOrderDispatch.getOrderNumber();
				company = SalesOrderDispatch.getCompany();
				totalReel += Double.parseDouble(SalesOrderDispatch.getReel());
				totalReelInStock += Double.parseDouble(StringUtils.isNotEmpty(SalesOrderDispatch.getReelInStock()) ? SalesOrderDispatch.getReelInStock() : "0");
				totalWeight += Double.parseDouble(SalesOrderDispatch.getWeight());
				
				orderDispatchSummary.setOrderNumber(orderNumber);
				orderDispatchSummary.setCompany(company);
				//orderDispatchSummary.setTotalReel(Double.toString(totalReel));
				
				tempList = new ArrayList<>();
				tempList.add(SalesOrderDispatch);
				
			} else {
				tempList.add(SalesOrderDispatch);
				totalReel += Double.parseDouble(SalesOrderDispatch.getReel());
				totalReelInStock += Double.parseDouble(StringUtils.isNotEmpty(SalesOrderDispatch.getReelInStock()) ? SalesOrderDispatch.getReelInStock() : "0");
				totalWeight += Double.parseDouble(SalesOrderDispatch.getWeight());
			}
		}
		
		if(!CollectionUtils.isEmpty(tempList)) {
			orderDispatchSummary.setTotalReel(Double.toString(totalReel));
			orderDispatchSummary.setTotalReelInStock(Double.toString(totalReelInStock));
			//orderDispatchSummary.setTotalWeight(Double.toString(totalWeight));
			orderDispatchSummary.setTotalWeight(Utility.roundToTwoDecimal(totalWeight));
			
			totalReel = 0;
			totalReelInStock = 0;
			totalWeight = 0;
			orderDispatchSummary.setSalesOrderDispatchs(tempList);
			SalesOrderDispatchSummaries.add(orderDispatchSummary);
		}
		
		return SalesOrderDispatchSummaries;
	}
	
	public Response updateSalesOrderPlannedReel(TallyInputDTO tallyInputDTO) {
		TallyDAO tallyDAO = new TallyDAO(); 
		return tallyDAO.updateSalesOrderPlannedReel(tallyInputDTO);
	}
	
	public Response deleteSalesOrdersPlanned(TallyInputDTO tallyInputDTO) {
		TallyDAO tallyDAO = new TallyDAO(); 
		return tallyDAO.deleteSalesOrderPlanned(tallyInputDTO);
	}
	
}
