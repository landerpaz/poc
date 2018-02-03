package com.india.tamilnadu.tally.bc;

import java.util.List;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.SalesOrder;


public class SalesOrderBC {

	public List<SalesOrder> getSalesOrder( String companyId, String trackingId) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getSalesOrder(companyId);
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
	
	
}
