package com.india.tamilnadu.jaxrs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.AsyncResponse;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.vo.ProductionDashboardChart;
import com.india.tamilnadu.tally.vo.Result;
import com.india.tamilnadu.tally.vo.SalesOrder;
import com.india.tamilnadu.tally.vo.StockBFDetail;
import com.india.tamilnadu.tally.vo.StockGSMDetail;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Message;
import com.india.tamilnadu.vo.User;

@Produces({ "application/xml", "application/json" })
public interface CustomerService {

	@POST
	@Path("/external/customer/register")
	javax.ws.rs.core.Response customerRegistration(LoginUser loginUser);
	
	@POST
	@Path("/external/customer/userLogin")
	Response userLogin(@HeaderParam("User-Agent") String userAgent, Login login);
	
	/*@GET
	@Path("/tally/{companyId}")
	javax.ws.rs.core.Response getTallySummary(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@PUT
	@Path("/tally/{companyId}")
	javax.ws.rs.core.Response updateTallySummary(@HeaderParam("Authorization") String token, Tally tally, @PathParam("companyId") String companyId);
*/
}
