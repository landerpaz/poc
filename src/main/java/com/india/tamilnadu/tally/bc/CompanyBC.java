package com.india.tamilnadu.tally.bc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.Company;
import com.india.tamilnadu.tally.vo.Customer;
import com.india.tamilnadu.tally.vo.CustomerDetail;
import com.india.tamilnadu.tally.vo.Receipt;
import com.india.tamilnadu.tally.vo.Sales;
import com.india.tamilnadu.tally.vo.SalesOrder;
import com.india.tamilnadu.tally.vo.SalesOrderConsolidated;
import com.india.tamilnadu.tally.vo.SalesOrderDispatch;
import com.india.tamilnadu.tally.vo.SalesOrderDispatchSummary;
import com.india.tamilnadu.tally.vo.SalesOrderPlanned;
import com.india.tamilnadu.tally.vo.SalesOrderPlannedSummary;
import com.india.tamilnadu.util.Utility;


public class CompanyBC {

	public List<Customer> getCustomer(TallyInputDTO tallyInputDTO) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getCustomers(tallyInputDTO);
	}
	
	public void addCompany(Company company, String trackingID) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		tallyDAO.addCompany(company, trackingID);
	}
}
