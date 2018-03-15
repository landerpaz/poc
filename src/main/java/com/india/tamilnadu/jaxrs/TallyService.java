package com.india.tamilnadu.jaxrs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.container.AsyncResponse;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.vo.DayBookMasterVO;
import com.india.tamilnadu.tally.vo.ProductionDashboardChart;
import com.india.tamilnadu.tally.vo.Result;
import com.india.tamilnadu.tally.vo.SalesOrder;
import com.india.tamilnadu.tally.vo.StockBFDetail;
import com.india.tamilnadu.tally.vo.StockGSMDetail;
import com.india.tamilnadu.tally.vo.StockStatistics;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Message;
import com.india.tamilnadu.vo.User;

@Produces({ "application/xml", "application/json" })
public interface TallyService {

	/*@POST
	@Path("/tally/")
	Response addTallyData(String tallyData);*/
	
	@POST
	@Path("/tally/{companyId}")
	Response addTallyData(String tallyData, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/tally/{companyId}")
	javax.ws.rs.core.Response getTallySummary(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@PUT
	@Path("/tally/{companyId}")
	javax.ws.rs.core.Response updateTallySummary(@HeaderParam("Authorization") String token, Tally tally, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/daybook/{companyId}")
	javax.ws.rs.core.Response getDayBook(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@POST
	@Path("/daybook/")
	Response addDayBook(String dayBook);
	
	@PUT
	@Path("/daybook/{companyId}/{voucherKey}")
	javax.ws.rs.core.Response updateDayBookFlag(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("voucherKey") String voucherKey);
	
	@POST
	@Path("/daybooktiny/")
	Response addTinyDayBook(String dayBook);
	
	@POST
	@Path("/login/")
	Response login(Login login);
	
	@POST
	@Path("/userLogin/")
	Response userLogin(@HeaderParam("User-Agent") String userAgent, Login login);
	
	@GET
	@Path("/daybookjwt/{companyId}")
	javax.ws.rs.core.Response getDayBookJWT(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	/*@GET
	@Path("/stock/")
	List<DayBookMasterVO> getStock();*/
	
	@GET
	@Path("/stock/{name}/{companyId}/{startDate}/{endDate}")
	List<StockGSMDetail> getStockGraph(@PathParam("name") String name, @PathParam("companyId") String companyId, 
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate);
	
	@GET
	@Path("/stock/gsm/{companyId}")
	List<StockGSMDetail> getStockGSM(@PathParam("companyId") String companyId);
	
	//not in use
	@GET
	@Path("/stock/gsm/7/{companyId}")
	List<StockGSMDetail> getStockGSMLast7Days(@PathParam("companyId") String companyId);
	
	//not in use
	@GET
	@Path("/stock/gsm/30/{companyId}")
	List<StockGSMDetail> getStockGSMLast30Days(@PathParam("companyId") String companyId);
	
	@GET
	@Path("/stock/bf/{companyId}")
	List<StockBFDetail> getStockBF(@PathParam("companyId") String companyId);
	
	//not in use
	@GET
	@Path("/stock/bf/7/{companyId}")
	List<StockBFDetail> getStockBFLast7Days(@PathParam("companyId") String companyId);
	
	//not in use
	@GET
	@Path("/stock/bf/30/{companyId}")
	List<StockBFDetail> getStockBFLast30Days(@PathParam("companyId") String companyId);
	
	@GET
	@Path("/stocks/{companyId}")
	List<StockBFDetail> getStocks(@PathParam("companyId") String companyId);
	
	@GET
	@Path("/stocks/productionstatistics/{companyId}")
	javax.ws.rs.core.Response getProductionStatistics(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/stocks/productiondashboardchart/{companyId}")
	List<ProductionDashboardChart> getProductionDashboardChart(@PathParam("companyId") String companyId);
	
	@POST
	@Path("/stocks/upload")
	//public void addAttachments(MultipartBody body);
	public void addAttachments(String body);
	
	@GET
	@Path("/tally/users/{companyId}")
	javax.ws.rs.core.Response getUsers(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@PUT
	@Path("/tally/users/{companyId}")
	javax.ws.rs.core.Response updateUser(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, User user);
	
	@POST
	@Path("/tally/users")
	javax.ws.rs.core.Response addUser(LoginUser loginUser);
	
	@POST
	@Path("/tally/message/{companyId}")
	javax.ws.rs.core.Response addMessage(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, Message message);
	
	@GET
	@Path("/tally/message/{companyId}")
	javax.ws.rs.core.Response getMessage(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/tally/salesOrder/{status}/{companyId}")
	javax.ws.rs.core.Response getSalesOrder(@HeaderParam("Authorization") String token, @PathParam("status") String status, @PathParam("companyId") String companyId);
		
	@GET
	@Path("/tally/salesOrderByBf/{companyId}")
	javax.ws.rs.core.Response getSalesOrderByBf(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/tally/salesOrderByBfGsm/{companyId}")
	javax.ws.rs.core.Response getSalesOrderByBfGsm(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/tally/salesOrderByBfGsmSize/{companyId}")
	javax.ws.rs.core.Response getSalesOrderByBfGsmSize(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);

	@PUT
	@Path("/tally/salesOrder/{type}/{companyId}/{id}")
	javax.ws.rs.core.Response deleteSalesOrder(@HeaderParam("Authorization") String token, @PathParam("type") String type, @PathParam("companyId") String companyId, @PathParam("id") String id);
	
	@POST
	@Path("/tally/salesOrders/{companyId}")
	javax.ws.rs.core.Response createSalesOrderPlan(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, 
			List<SalesOrder> salesOrders);
	
	@POST
	@Path("/tally/salesOrders/{companyId}/{batchNumber}")
	javax.ws.rs.core.Response updateSalesOrderPlan(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, 
			@PathParam("batchNumber") String batchNumber, List<SalesOrder> salesOrders);
	
	@GET
	@Path("/tally/salesOrdersPlanned/{companyId}")
	javax.ws.rs.core.Response getSalesOrdersPlanned(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/tally/salesOrdersDispatch/{companyId}/{batchNo}")
	javax.ws.rs.core.Response getSalesOrdersDispatch(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("batchNo") String batchNo);
	
	@PUT
	//@Path("/tally/salesOrdersPlannedReel/{companyId}/{id}/{reel}")
	@Path("/tally/salesOrdersPlannedReel/{companyId}/{id}{reel : (/reel)?}")
	javax.ws.rs.core.Response updateSalesOrderPlannedReel(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("id") String id, @PathParam("reel") String reel);
	
	@PUT
	@Path("/tally/salesOrdersPlanned/{companyId}/{id}/{salesOrderPlannedId}/{altered}/{weight}")
	javax.ws.rs.core.Response deleteSalesOrdersPlanned(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, 
			@PathParam("id") String id, @PathParam("salesOrderPlannedId") String salesOrderPlannedId, @PathParam("altered") String altered, @PathParam("weight") String weight);
	
	@GET
	@Path("/tally/sales/{companyId}/{id}")
	javax.ws.rs.core.Response getSales(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("id") String id);
	
	
	@GET
	@Path("/tally/receipt/{companyId}/{id}")
	javax.ws.rs.core.Response getReceipts(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("id") String id);
	
	@POST
	@Path("/tally/mail/{companyId}/{fileName}/{status}/{to}")
	javax.ws.rs.core.Response sendMail(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("fileName") String fileName, @PathParam("status") String status, @PathParam("to") String to, List<Result> results);
	
	@POST
	@Path("/tally/mail/{companyId}/{fileName}/{status}/{to}")
	public void sendMailAsync(@Suspended AsyncResponse reponse, @HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("fileName") String fileName, @PathParam("status") String status, @PathParam("to") String to);
	
	@GET
	@Path("/tally/customer/{companyId}")
	javax.ws.rs.core.Response getCustomers(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/tally/customerDetail/{companyId}/{id}")
	javax.ws.rs.core.Response getCustomerDetail(@HeaderParam("Authorization") String token, @PathParam("companyId") String companyId, @PathParam("id") String id);
	
}