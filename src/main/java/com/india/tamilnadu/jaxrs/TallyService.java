package com.india.tamilnadu.jaxrs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.vo.DayBookMasterVO;
import com.india.tamilnadu.tally.vo.ProductionDashboardChart;
import com.india.tamilnadu.tally.vo.StockBFDetail;
import com.india.tamilnadu.tally.vo.StockGSMDetail;
import com.india.tamilnadu.tally.vo.StockStatistics;
import com.india.tamilnadu.vo.Login;

@Produces({ "application/xml", "application/json" })
public interface TallyService {

	@POST
	@Path("/tally/")
	Response addTallyData(String tallyData);
	
	@GET
	@Path("/tally/")
	List<Tally> getTallySummary();
	
	@PUT
	@Path("/tally/{companyId}")
	Response updateTallySummary(Tally tally, @PathParam("companyId") String companyId);
	
	@GET
	@Path("/daybook/{companyId}")
	List<DayBookMasterVO> getDayBook(@PathParam("companyId") String companyId);
	
	@POST
	@Path("/daybook/")
	Response addDayBook(String dayBook);
	
	@PUT
	@Path("/daybook/{companyId}/{voucherKey}")
	Response updateDayBookFlag(@PathParam("companyId") String companyId, @PathParam("voucherKey") String voucherKey);
	
	@POST
	@Path("/daybooktiny/")
	Response addTinyDayBook(String dayBook);
	
	@POST
	@Path("/login/")
	Response login(Login login);
	
	@POST
	@Path("/userLogin/")
	Response userLogin(Login login);
	
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
	StockStatistics getProductionStatistics(@PathParam("companyId") String companyId);
	
	@GET
	@Path("/stocks/productiondashboardchart/{companyId}")
	List<ProductionDashboardChart> getProductionDashboardChart(@PathParam("companyId") String companyId);
	
	@POST
	@Path("/stocks/upload")
	//public void addAttachments(MultipartBody body);
	public void addAttachments(String body);
	
}