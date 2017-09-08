package com.india.tamilnadu.jaxrs;

import java.util.List;
import com.india.tamilnadu.dao.ProductsDAO;
import com.india.tamilnadu.dao.SuppliersDAO;
import com.india.tamilnadu.dto.Response;

public class TallyServiceImpl implements TallyService {
	
	public Response addTallyData(String tallyData) {
		System.out.println("...adding tally data");
				
		System.out.println("Tally Data : " + tallyData);
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage(tallyData);
		return response;
	}
	
}
