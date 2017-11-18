package com.india.tamilnadu.jaxrs;

import java.util.List;


import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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
	@Path("/tally/")
	Response updateTallySummary(Tally tally);
	
	@GET
	@Path("/daybook/")
	List<DayBookMasterVO> getDayBook();
	
	@POST
	@Path("/daybook/")
	Response addDayBook(String dayBook);
	
	@PUT
	@Path("/daybook/{voucherKey}")
	Response updateDayBookFlag(@PathParam("voucherKey") String voucherKey);
	
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
	@Path("/daybookjwt/")
	javax.ws.rs.core.Response getDayBookJWT(@HeaderParam("Authorization") String token);
	
	/*@GET
	@Path("/stock/")
	List<DayBookMasterVO> getStock();*/
	
	@GET
	@Path("/stock/gsm/")
	List<StockGSMDetail> getStockGSM();
	
	@GET
	@Path("/stock/gsm/7")
	List<StockGSMDetail> getStockGSMLast7Days();
	
	@GET
	@Path("/stock/gsm/30")
	List<StockGSMDetail> getStockGSMLast30Days();
	
	@GET
	@Path("/stock/bf/")
	List<StockBFDetail> getStockBF();
	
	@GET
	@Path("/stock/bf/7")
	List<StockBFDetail> getStockBFLast7Days();
	
	@GET
	@Path("/stock/bf/30")
	List<StockBFDetail> getStockBFLast30Days();
	
	@GET
	@Path("/stocks")
	List<StockBFDetail> getStocks();
	
	@GET
	@Path("/stocks/productionstatistics")
	StockStatistics getProductionStatistics();
	
	@GET
	@Path("/stocks/productiondashboardchart")
	List<ProductionDashboardChart> getProductionDashboardChart();
	
}