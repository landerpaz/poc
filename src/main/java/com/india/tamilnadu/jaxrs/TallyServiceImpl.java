package com.india.tamilnadu.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.container.AsyncResponse;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.HttpServletRequestSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.security.bc.AuthenticationBC;
import com.india.tamilnadu.security.util.JWTHelper;
import com.india.tamilnadu.tally.bc.MessageBC;
import com.india.tamilnadu.tally.bc.ReceiptBC;
import com.india.tamilnadu.tally.bc.SalesBC;
import com.india.tamilnadu.tally.bc.SalesOrderBC;
import com.india.tamilnadu.tally.bc.TallyDayBookBC;
import com.india.tamilnadu.tally.bc.TallyStockBC;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.Result;
import com.india.tamilnadu.tally.vo.SalesOrder;
import com.india.tamilnadu.tally.vo.SalesOrderConsolidated;
import com.india.tamilnadu.tally.vo.StockStatistics;
import com.india.tamilnadu.util.SaxParserHandler;
import com.india.tamilnadu.util.TallyBean;
import com.india.tamilnadu.util.TallyRequestContext;
import com.india.tamilnadu.util.Utility;
import com.india.tamilnadu.util.bc.MailBC;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.LoginUser;
import com.india.tamilnadu.vo.Message;
import com.india.tamilnadu.vo.Role;
import com.india.tamilnadu.vo.User;
import com.india.tamilnadu.vo.UserManager;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;
import static com.india.tamilnadu.util.Constants.LOG_DATA_FORMAT;

public class TallyServiceImpl implements TallyService {

	private final Logger LOG = LoggerFactory.getLogger(TallyServiceImpl.class);
	
	//public void addAttachments(MultipartBody body) {
	public void addAttachments(String body) {
		//System.out.println(body.getType());
		System.out.println("In");
		System.out.println(body);
	}
	
	public Response userLogin(String userAgent, Login login) {
		
		System.out.println("user agent : " + userAgent);
		
		String trackingID = Utility.getRandomNumber();
		
		LOG.info(LOG_BASE_FORMAT, trackingID, "userLogin In");
		LOG.info(LOG_BASE_FORMAT, trackingID, "userAgent : " + userAgent);
		
		Response response = new Response();
		response.setStatus("200");
		response.setStatusMessage("AUTH_FAILED");
		
		try {
			
			if(null == login || null == login.getEmail() || null == login.getPassword()) {
				return response;
			}
			
			//authenticate user
			AuthenticationBC authenticationBC = new AuthenticationBC();
			User user = authenticationBC.authenticate(login.getEmail(), 
					Utility.hashPassword(login.getPassword().toCharArray(), login.getEmail().getBytes(), 2, 256), trackingID, userAgent);
			if(user.isAuthenticate()) {
				response.setStatusMessage("AUTH_SUCCESS");
				response.setRole(user.getRole());
				response.setToken(user.getToken());
				response.setFirstName(user.getFirstName());
				response.setLastName(user.getLastName());
				response.setCompanyId(user.getCompanyId());
				response.setCompanyName(user.getCompanyName());
			}
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "userLogin Out");
		
		} catch (AuthenticationException e) {
			LOG.error(LOG_DATA_FORMAT, trackingID, "exception captured in authenticate", e.getMessage());
			
			response.setStatus("401");
			response.setStatusMessage("AUTH ERROR");
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingID, "exception captured in authenticate", e.getMessage());
			e.printStackTrace();
			
			response.setStatus("500");
			response.setStatusMessage("SYSTEM ERROR");
		}
		
		return response;
	}

	public Response login(Login login) {
		
		String trackingID = Utility.getRandomNumber();
		
		LOG.info(LOG_BASE_FORMAT, trackingID, "login In");
		
		Response response = new Response();
		response.setStatus("200");
		response.setStatusMessage("AUTH_FAILED");
		
		
		try {
			
			if(null == login || null == login.getEmail() || null == login.getPassword()) {
				return response;
			}
			
			if(login.getEmail().equals("kms") && login.getPassword().equals("Spak#007")) {
			
				response.setStatus("200");
				response.setStatusMessage("AUTH_SUCCESS");
				response.setRole("associate");
				response.setToken("2468");
			
			} else if(login.getEmail().equals("admin") && login.getPassword().equals("Spak#007")) {
			
				response.setStatus("200");
				response.setStatusMessage("AUTH_SUCCESS");
				response.setRole("admin");
				response.setToken("1357");
			
			}
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "login Out");
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return response;
	}
	
	public javax.ws.rs.core.Response getTallySummary(String token, String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List tallySummaryList = new ArrayList<>();
		String trackingID = null;
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			trackingID = Utility.getRandomNumber();
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "getTallySummary In");
			
			//Message message = PhaseInterceptorChain.getCurrentMessage();
			//HttpServletRequestSnapshot request = (HttpServletRequestSnapshot)message.get(AbstractHTTPDestination.HTTP_REQUEST);
		    //System.out.println("11111111 : " + request.getHeader("User-Agent"));
		    //HttpSession  session = request.getSession();
		    //session.setAttribute("Session : " + session);
	    
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(trackingID);
			tallyInputDTO.setCompanyId(companyId);
			
			TallyDAO tallyDAO = new TallyDAO();
			tallySummaryList = tallyDAO.getTallySummary(tallyInputDTO);
					
			System.out.println("getTallySummary : Number of tally summary : " + (null == tallySummaryList? "0" : tallySummaryList.size()));
			
		} catch (Exception e) {
			
			LOG.error(LOG_DATA_FORMAT, trackingID, "exception captured in getTallySummary", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
		
		return javax.ws.rs.core.Response.ok(tallySummaryList).build();
		
	}
	
	public Response addTallyData(String tallyData, String companyId) {
		System.out.println("...adding tally data");
				
		//This companyId will come through URL
		//String companyId = "Spak";
		
		TallyBean tallyBean = null;
		System.out.println("Tally Data : " + tallyData);
		
		tallyData = tallyData.replaceAll("<DSPCLDRAMTA></DSPCLDRAMTA>", "<DSPCLDRAMTA>0</DSPCLDRAMTA>");
		tallyData = tallyData.replaceAll("<DSPCLCRAMTA></DSPCLCRAMTA>", "<DSPCLCRAMTA>0</DSPCLCRAMTA>");
		
		System.out.println("Tally Data formatted : " + tallyData);
		
		//parse xml into list of strings
		
		if(null != tallyData) {
			System.out.println("Parse tally xml data...");
			
			SaxParserHandler saxParser = new SaxParserHandler();
			tallyBean = saxParser.parseXml(tallyData);
			
			System.out.println("key : " + tallyBean.getKeys());
			System.out.println("value1 : " + tallyBean.getValues1());
			System.out.println("value2 : " + tallyBean.getValues2());
			
			System.out.println("key size : " + tallyBean.getKeys().size());
			System.out.println("value1 size : " + tallyBean.getValues1().size());
			System.out.println("value2 size : " + tallyBean.getValues2().size());
		}
		
		//insert parsed data in DB
		if(null != tallyBean && null != tallyBean.getKeys() && null != tallyBean.getValues1() && null != tallyBean.getValues2()) {
			
			System.out.println("Insert tally summary data in DB...");
			
			TallyRequestContext context = new TallyRequestContext();
			context.setKeys(tallyBean.getKeys());
			context.setValues1(tallyBean.getValues1());
			context.setValues2(tallyBean.getValues2());
			context.setCheckFlag(false);
			context.setCompanyId(companyId);
			
			TallyDAO tallyDAO = new TallyDAO();
			int reportId = tallyDAO.getNextValueForReportId();
			context.setReportId(reportId);
			tallyDAO.addTallySummary(context);
		}
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage(tallyData);
		return response;
	}
	
	public javax.ws.rs.core.Response updateTallySummary(String token, Tally tally, String companyId) {
		System.out.println("...update tally data");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			TallyDAO tallyDAO = new TallyDAO();
			tally.setCompanyId(companyId);
			tally.setTrackingId(Utility.getRandomNumber());
			response = tallyDAO.updateTallySummary(tally);
			
		} catch (Exception e) {
			//response.setStatus("Failed");
			//response.setStatusMessage("Failed");
			
			LOG.error(LOG_DATA_FORMAT, tally.getTrackingId(), "exception captured in updateTallySummary", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
			
		}
		
		return javax.ws.rs.core.Response.ok(response).build();
	}
	
	public javax.ws.rs.core.Response updateDayBookFlag(String token, String companyId, String voucherKey) {
		System.out.println("...update day book flag");
		System.out.println("voucherKey : " + voucherKey);
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		String trackingID = null;
		
		try {
			
			trackingID = Utility.getRandomNumber();
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "updateDayBookFlag In");
			LOG.info(LOG_BASE_FORMAT, trackingID, "updateDayBookFlag : token : " + token);
			
			String scope = JWTHelper.validateJWT(token);
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "updateDayBookFlag : scope : " + scope);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setVoucherKey(voucherKey);
			tallyInputDTO.setCompanyId(companyId);
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookBC.updateTallyDayBookData(tallyInputDTO);
			
		} catch (Exception e) {
			//commented for JWT implementation
			//response.setStatus("Failed");
			//response.setStatusMessage("Failed");
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
		
		return javax.ws.rs.core.Response.ok(response).build();
	}
	
	/**
	 *This method fetches day book data from DB 
	 * 
	 * 
	 **/
	public javax.ws.rs.core.Response getDayBook(String token, String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List dayBookList = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getDayBook In");
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookList = dayBookBC.getTallyDayBookData(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == dayBookList? "0" : dayBookList.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getDayBook Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getDayBook", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(dayBookList).build();
	}
	
	public javax.ws.rs.core.Response getDayBookJWT(String token, String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List dayBookList = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			System.out.println("token : " + token);
			String scope = JWTHelper.validateJWT(token);
			System.out.println("scope : " + scope);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getDayBook In");
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookList = dayBookBC.getTallyDayBookData(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == dayBookList? "0" : dayBookList.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getDayBook Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
			return javax.ws.rs.core.Response.ok(dayBookList).build();
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getDayBook", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
		
	}
	
	/**
	 * This method receives whole set of tally book data and add it in DB
	 * 
	 * 
	 * */
	public Response addDayBook(String dayBook) {
		System.out.println("...adding day book");
				
		System.out.println("Day book Data : " + dayBook.length());
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		//if(true) return new Response();
		
		try {
		
			dayBook = dayBook.replaceAll("&#", "");
			dayBook = dayBook.replaceAll("#&", "");
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setDayBook(dayBook);
			tallyInputDTO.setTiny(false);
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookBC.addTallyDayBookData(tallyInputDTO);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * This method receives single tally book data and add it in DB
	 * In this case Client jar will split the tally data received from tally and send to this method one tally message tag at a time
	 * 
	 * */
	public Response addTinyDayBook(String dayBook) {
		
		TallyInputDTO tallyInputDTO = new TallyInputDTO();
		Response response = new Response();
		long startTime = System.currentTimeMillis();
		
		tallyInputDTO.setTrackingID(Utility.getRandomNumber());
		response.setStatus("200");
		response.setStatusMessage("Success");
		
		LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTinyDayBook In");
		LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "addTinyDayBook", "request_length:" + dayBook.length());
		
		//if(true) return new Response();
		
		try {
			
			dayBook = dayBook.replaceAll("&#", "");
			dayBook = dayBook.replaceAll("#&", "");
			
			tallyInputDTO.setDayBook(dayBook);
			tallyInputDTO.setTiny(true);
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookBC.addTallyDayBookData(tallyInputDTO);
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in addTinyDayBook", e.getMessage());
			e.printStackTrace();
			response.setStatus("200");
			response.setStatusMessage("TechnicalError:" + e.getMessage());
		}
		
		LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "addTinyDayBook Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		return response;
	}

	/**
	 *This method fetches stock data from DB 
	 * 
	 * 
	 **/
	public List getStock() {
		
		TallyInputDTO tallyInputDTO = null;
		List stockMasters = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStock In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockMasters = stockBC.getStockData(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stocks : "  + (null == stockMasters? "0" : stockMasters.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStock Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStock", e.getMessage());
			e.printStackTrace();
		}
	
		return stockMasters;
	}
	
	public List getStockGraph(String name, String companyId, String startDate, String endDate) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockGSMs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setName(name);
			tallyInputDTO.setStartDate(startDate);
			tallyInputDTO.setEndDate(endDate);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGraph In");
			
			TallyStockBC stockBC = new TallyStockBC();
			
			if (name.equals("gsm")) {
				stockGSMs = stockBC.getGSMData(tallyInputDTO);
			} else if (name.equals("bf")) {
				stockGSMs = stockBC.getBFData(tallyInputDTO);
			} else {
				LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "not a valid name");
			}
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock : "  + (null == stockGSMs? "0" : stockGSMs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockGraph Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockGraph", e.getMessage());
			e.printStackTrace();
		}
	
		return stockGSMs;
	}

	public List getStockGSM(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockGSMs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			tallyInputDTO.setCompanyId(companyId);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSM In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockGSMs = stockBC.getGSMData(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock GSMs: "  + (null == stockGSMs? "0" : stockGSMs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSM Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockGSM", e.getMessage());
			e.printStackTrace();
		}
	
		return stockGSMs;
	}

	public List getStockGSMLast7Days(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockGSMs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMLast7Days In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockGSMs = stockBC.getGSMDataLast7Days(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock GSMs: "  + (null == stockGSMs? "0" : stockGSMs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMLast7Days Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockGSMLast7Days", e.getMessage());
			e.printStackTrace();
		}
	
		return stockGSMs;
	}

	public List getStockGSMLast30Days(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockGSMs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMLast30Days In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockGSMs = stockBC.getGSMDataLast30Days(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock GSMs: "  + (null == stockGSMs? "0" : stockGSMs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockGSMLast30Days Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockGSMLast30Days", e.getMessage());
			e.printStackTrace();
		}
	
		return stockGSMs;
	}

	public List getStockBF(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockBFs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			tallyInputDTO.setCompanyId(companyId);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBF In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockBFs = stockBC.getBFData(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock BFs: "  + (null == stockBFs? "0" : stockBFs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockBF Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockBF", e.getMessage());
			e.printStackTrace();
		}
	
		return stockBFs;
	}
	
	public List getStockBFLast7Days(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockBFs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFLast7Days In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockBFs = stockBC.getBFDataLast7days(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock BFs: "  + (null == stockBFs? "0" : stockBFs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFLast7Days Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockBFLast7Days", e.getMessage());
			e.printStackTrace();
		}
	
		return stockBFs;
	}

	public List getStockBFLast30Days(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockBFs = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFLast30Days In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockBFs = stockBC.getBFDataLast30Days(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock BFs: "  + (null == stockBFs? "0" : stockBFs.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStockBFLast30Days Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStockBFLast30Days", e.getMessage());
			e.printStackTrace();
		}
	
		return stockBFs;
	}

	public List getStocks(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List stockss = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			tallyInputDTO.setCompanyId(companyId);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getStocks In");
			
			TallyStockBC stockBC = new TallyStockBC();
			stockss = stockBC.getStocks(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock : "  + (null == stockss? "0" : stockss.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getStocks Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getStocks", e.getMessage());
			e.printStackTrace();
		}
	
		return stockss;
	}

	public javax.ws.rs.core.Response getProductionStatistics(String token, String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		StockStatistics statistics = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			tallyInputDTO.setCompanyId(companyId);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionStatistics In");
			
			TallyStockBC stockBC = new TallyStockBC();
			statistics = stockBC.getStocksStatistics(tallyInputDTO);
					
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getProductionStatistics Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getProductionStatistics", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(statistics).build();
	}
	
	public List getProductionDashboardChart(String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List productionDashboardCharts = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			tallyInputDTO.setCompanyId(companyId);
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart In");
			
			TallyStockBC stockBC = new TallyStockBC();
			productionDashboardCharts = stockBC.getProductionDashboardChart(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day stock BFs: "  + (null == productionDashboardCharts? "0" : productionDashboardCharts.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getProductionDashboardChart Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getProductionDashboardChart", e.getMessage());
			e.printStackTrace();
		}
	
		return productionDashboardCharts;
	}
	
	public javax.ws.rs.core.Response getUsers(String token, String companyId) {
		
		UserManager userManager = new UserManager();
		List<User> users = new ArrayList<>();
		List<Role> roles = new ArrayList<>();
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "getUsers In");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			AuthenticationBC authenticationBC = new AuthenticationBC();
			users = authenticationBC.getUsers(companyId, trackingId);
			roles = authenticationBC.getRoles(trackingId);
			
			userManager.setUsers(users);
			userManager.setRoles(roles);
			
			LOG.debug(LOG_BASE_FORMAT, trackingId, "getUsers: Number of users: "  + (null == users? "0" : users.size()));
			LOG.info(LOG_DATA_FORMAT, trackingId, "getUsers : getUsers Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in getUsers", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("getUsers : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(userManager).build();
	}
	
	public javax.ws.rs.core.Response updateUser(String token, String companyId, User user) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "updateUser In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			AuthenticationBC authenticationBC = new AuthenticationBC();
			user.setCompanyId(companyId);
			authenticationBC.updateUser(trackingId, user);
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "updateUser : updateUser Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in updateUser", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}
	
	public javax.ws.rs.core.Response addUser(LoginUser loginUser) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "addUser In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			//String scope = JWTHelper.validateJWT(token);
			
			AuthenticationBC authenticationBC = new AuthenticationBC();
			//loginUser.setCompanyId(companyId);
			loginUser.setPassword(new String(Utility.hashPassword(loginUser.getPassword().toCharArray(), loginUser.getEmail().getBytes(), 2, 256)));
			authenticationBC.addUser(trackingId, loginUser);
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "addUser : addUser Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in updateUser", e.getMessage());
			e.printStackTrace();
			
			/*if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}*/
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				response.setStatus("Duplicate");
				response.setStatusMessage("Duplicate");
				return javax.ws.rs.core.Response.ok(response).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}

	public javax.ws.rs.core.Response addMessage(String token, String companyId, Message message) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "addMessage In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			MessageBC messageBC = new MessageBC();
			message.setCompanyId(companyId);
			messageBC.addMessage(trackingId, message);
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "addMessage : addMessage Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in updateUser", e.getMessage());
			e.printStackTrace();
			
			/*if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}*/
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				response.setStatus("Duplicate");
				response.setStatusMessage("Duplicate");
				return javax.ws.rs.core.Response.ok(response).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}
	
	public javax.ws.rs.core.Response getMessage(String token, String companyId) {
		
		UserManager userManager = new UserManager();
		List<Message> messages = new ArrayList<>();
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "getMessage In");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			MessageBC messageBC = new MessageBC();
			messages = messageBC.getMessage(companyId, trackingId);
			
			LOG.debug(LOG_BASE_FORMAT, trackingId, "getMessage: Number of users: "  + (null == messages? "0" : messages.size()));
			LOG.info(LOG_DATA_FORMAT, trackingId, "getMessage : getMessage Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in getMessage", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("getMessage : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(messages).build();
	}
	
	public javax.ws.rs.core.Response getSalesOrderByBf(String token, String companyId) {
		
		UserManager userManager = new UserManager();
		List<SalesOrder> salesOrders = new ArrayList<>();
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "getSalesOrderByBf In");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrders = salesOrderBC.getSalesOrderByBf(companyId, trackingId);
			
			LOG.debug(LOG_BASE_FORMAT, trackingId, "getSalesOrderByBf: Number of users: "  + (null == salesOrders? "0" : salesOrders.size()));
			LOG.info(LOG_DATA_FORMAT, trackingId, "getSalesOrderByBf : getMessage Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in getSalesOrderByBf", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("getSalesOrderByBfGsm : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesOrders).build();
	}
	
	public javax.ws.rs.core.Response getSalesOrderByBfGsm(String token, String companyId) {
			
			UserManager userManager = new UserManager();
			List<SalesOrder> salesOrders = new ArrayList<>();
			String trackingId = Utility.getRandomNumber();
			long startTime = System.currentTimeMillis();
			
			LOG.info(LOG_BASE_FORMAT, trackingId, "getMessage In");
			
			try {
				
				String scope = JWTHelper.validateJWT(token);
				
				SalesOrderBC salesOrderBC = new SalesOrderBC();
				salesOrders = salesOrderBC.getSalesOrderByBfGsm(companyId, trackingId);
				
				LOG.debug(LOG_BASE_FORMAT, trackingId, "getSalesOrderByBfGsm: Number of users: "  + (null == salesOrders? "0" : salesOrders.size()));
				LOG.info(LOG_DATA_FORMAT, trackingId, "getSalesOrderByBfGsm : getMessage Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
			} catch (Exception e) {
				LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in getSalesOrderByBfGsm", e.getMessage());
				e.printStackTrace();
				
				if(e.getMessage().contains("getSalesOrderByBfGsm : JWT signature does not match")) { 
					return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
				}
				
				return javax.ws.rs.core.Response.serverError().build();
			}
		
			return javax.ws.rs.core.Response.ok(salesOrders).build();
		}
	
	public javax.ws.rs.core.Response getSalesOrderByBfGsmSize(String token, String companyId) {
		
		UserManager userManager = new UserManager();
		List<SalesOrder> salesOrders = new ArrayList<>();
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "getSalesOrderByBfGsmSize In");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrders = salesOrderBC.getSalesOrderByBfGsmSize(companyId, trackingId);
			
			LOG.debug(LOG_BASE_FORMAT, trackingId, "getSalesOrderByBfGsmSize: Number of users: "  + (null == salesOrders? "0" : salesOrders.size()));
			LOG.info(LOG_DATA_FORMAT, trackingId, "getSalesOrderByBfGsmSize : getMessage Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in getSalesOrderByBfGsmSize", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("getSalesOrderByBfGsmSize : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesOrders).build();
	}
	
	public javax.ws.rs.core.Response getSalesOrder(String token, String status, String companyId) {
		
		UserManager userManager = new UserManager();
		List<SalesOrder> salesOrders = new ArrayList<>();
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "getMessage In");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrders = salesOrderBC.getSalesOrder(companyId, trackingId, status);
			
			LOG.debug(LOG_BASE_FORMAT, trackingId, "getMessage: Number of users: "  + (null == salesOrders? "0" : salesOrders.size()));
			LOG.info(LOG_DATA_FORMAT, trackingId, "getMessage : getMessage Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in getMessage", e.getMessage());
			e.printStackTrace();
			
			if(null != e && null != e.getMessage() && e.getMessage().contains("getMessage : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesOrders).build();
	}

	public javax.ws.rs.core.Response deleteSalesOrder(String token, String type, String companyId, String id) {
		System.out.println("...update sales order order_status flag");
		System.out.println("id : " + id);
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		String trackingID = null;
		
		try {
			
			trackingID = Utility.getRandomNumber();
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "deleteSalesOrder In");
			LOG.info(LOG_BASE_FORMAT, trackingID, "deleteSalesOrder : token : " + token);
			
			String scope = JWTHelper.validateJWT(token);
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "deleteSalesOrder : scope : " + scope);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setId(id);
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setType(type);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrderBC.deleteSalesOrder(tallyInputDTO);
			
		} catch (Exception e) {
			//commented for JWT implementation
			//response.setStatus("Failed");
			//response.setStatusMessage("Failed");
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
		
		return javax.ws.rs.core.Response.ok(response).build();
	}

	/*public javax.ws.rs.core.Response updateSalesOrders(String token, String companyId, String salesOrders) {
		
		
		System.out.println("salesOrders....................." + salesOrders);
		
	
		return javax.ws.rs.core.Response.ok("").build();
	}*/


	public javax.ws.rs.core.Response createSalesOrderPlan(String token, String companyId, List<SalesOrder> salesOrders) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "createSalesOrderPlan In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setSalesOrders(salesOrders);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrderBC.updateSalesOrders(tallyInputDTO, null); //passing batch number as null as new batch number will be generated in DAO
			
			for(SalesOrder salesOrder : salesOrders) {
				System.out.println(salesOrder.getId());
				System.out.println(salesOrder.getOrderStatus());
				System.out.println(salesOrder.getAltered());
				
			}
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "createSalesOrderPlan : createSalesOrderPlan Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in createSalesOrderPlan", e.getMessage());
			e.printStackTrace();
			
			//if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				//return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			//}
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				response.setStatus("Duplicate");
				response.setStatusMessage("Duplicate");
				return javax.ws.rs.core.Response.ok(response).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}

	public javax.ws.rs.core.Response updateSalesOrderPlan(String token, String companyId, String batchNumber, List<SalesOrder> salesOrders) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "updateSalesOrderPlan In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setSalesOrders(salesOrders);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrderBC.updateSalesOrders(tallyInputDTO, batchNumber); 
			
			for(SalesOrder salesOrder : salesOrders) {
				System.out.println(salesOrder.getId());
				System.out.println(salesOrder.getOrderStatus());
				System.out.println(salesOrder.getAltered());
				
			}
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "updateSalesOrderPlan : updateSalesOrderPlan Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in updateSalesOrderPlan", e.getMessage());
			e.printStackTrace();
			
			//if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				//return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			//}
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				response.setStatus("Duplicate");
				response.setStatusMessage("Duplicate");
				return javax.ws.rs.core.Response.ok(response).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}

	/*public javax.ws.rs.core.Response updateSalesOrders(String token, String companyId, SalesOrderConsolidated salesOrderConsolidated) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "updateSalesOrders In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setSalesOrders(salesOrderConsolidated.getSalesOrders());
			tallyInputDTO.setConsBf(salesOrderConsolidated.getConsBf());
			tallyInputDTO.setConsBfGsm(salesOrderConsolidated.getConsBfGsm());
			tallyInputDTO.setConsBfGsmSize(salesOrderConsolidated.getConsBfGsmSize());
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrderBC.updateSalesOrders(tallyInputDTO);
			
			for(SalesOrder salesOrder : salesOrderConsolidated.getSalesOrders()) {
				System.out.println(salesOrder.getId());
				System.out.println(salesOrder.getOrderStatus());
				System.out.println(salesOrder.getAltered());
				
			}
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "updateSalesOrders : updateSalesOrders Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in updateSalesOrders", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("updateUser : JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			if(null != e && e.getMessage().contains("Duplicate")) {
				response.setStatus("Duplicate");
				response.setStatusMessage("Duplicate");
				return javax.ws.rs.core.Response.ok(response).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}*/

	/**
	 *This method fetches day book data from DB 
	 * 
	 * 
	 **/
/*	public javax.ws.rs.core.Response getSalesOrdersPlanned(String token, String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List salesOrdersPlannedSummary = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesOrdersPlanned In");
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrdersPlannedSummary = salesOrderBC.getSalesOrdersPlanned(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == salesOrdersPlannedSummary? "0" : salesOrdersPlannedSummary.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getSalesOrdersPlanned Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getSalesOrdersPlanned", e.getMessage());
			e.printStackTrace();
			
			if(e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesOrdersPlannedSummary).build();
	}*/
	
	public javax.ws.rs.core.Response getSalesOrdersPlanned(String token, String companyId) {
		
		TallyInputDTO tallyInputDTO = null;
		List salesOrdersPlannedSummary = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesOrdersPlanned In");
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrdersPlannedSummary = salesOrderBC.getSalesOrdersPlanned(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == salesOrdersPlannedSummary? "0" : salesOrdersPlannedSummary.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getSalesOrdersPlanned Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getSalesOrdersPlanned", e.getMessage());
			e.printStackTrace();
			
			if(null != e && null != e.getMessage() && e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesOrdersPlannedSummary).build();
	}
	
	/**
	 *This method fetches day book data from DB 
	 * 
	 * 
	 **/
	public javax.ws.rs.core.Response getSalesOrdersDispatch(String token, String companyId, String batchNo) {
		
		TallyInputDTO tallyInputDTO = null;
		List salesOrdersDispatch = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setBatchNo(batchNo);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSalesOrdersDispatch In");
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrdersDispatch = salesOrderBC.getSalesOrdersDispatched(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == salesOrdersDispatch? "0" : salesOrdersDispatch.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getSalesOrdersDispatch Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getSalesOrdersDispatch", e.getMessage());
			e.printStackTrace();
			
			if(null != e && null != e.getMessage() && e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesOrdersDispatch).build();
	}
	
	public javax.ws.rs.core.Response updateSalesOrderPlannedReel(String token, String companyId, String id, String reel) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "updateSalesOrderPlannedReel In");
		LOG.info(LOG_BASE_FORMAT, token, "token : " + token);
		LOG.info(LOG_BASE_FORMAT, token, "companyId : " + companyId);
		LOG.info(LOG_BASE_FORMAT, token, "reel : " + reel);
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setId(id);
			tallyInputDTO.setReel(reel);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrderBC.updateSalesOrderPlannedReel(tallyInputDTO);
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "updateSalesOrders : updateSalesOrderPlannedReel Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in updateSalesOrderPlannedReel", e.getMessage());
			e.printStackTrace();
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}
	
	public javax.ws.rs.core.Response deleteSalesOrdersPlanned(String token, String companyId, String id, String salesOrderPlannedId, String altered, String weight) {
		
		String trackingId = Utility.getRandomNumber();
		long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "deleteSalesOrdersPlanned In");
		LOG.info(LOG_BASE_FORMAT, trackingId, "companyId : " + companyId);
		LOG.info(LOG_BASE_FORMAT, trackingId, "id : " + id);
		LOG.info(LOG_BASE_FORMAT, trackingId, "salesOrderPlannedId : " + salesOrderPlannedId);
		LOG.info(LOG_BASE_FORMAT, trackingId, "weight : " + weight);
		LOG.info(LOG_BASE_FORMAT, trackingId, "altered : " + altered);
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setId(id);
			tallyInputDTO.setSalesOrderedPlannedId(salesOrderPlannedId);
			tallyInputDTO.setWeight(weight);
			tallyInputDTO.setAltered(altered);
			
			SalesOrderBC salesOrderBC = new SalesOrderBC();
			salesOrderBC.deleteSalesOrdersPlanned(tallyInputDTO); 
			
			LOG.info(LOG_DATA_FORMAT, trackingId, "deleteSalesOrdersPlanned : deleteSalesOrdersPlanned Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in deleteSalesOrdersPlanned", e.getMessage());
			e.printStackTrace();
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}
	
	public javax.ws.rs.core.Response getSales(String token, String companyId, String id) {
		
		TallyInputDTO tallyInputDTO = null;
		List salesList = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			if(null != id && id.equals("all")) {
				tallyInputDTO.setSelectAll(true);
			} else {
				tallyInputDTO.setSelectAll(false);
			}
			tallyInputDTO.setId(id);
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getSales In");
			
			SalesBC salesBC = new SalesBC();
			salesList = salesBC.getSales(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == salesList? "0" : salesList.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getSales Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getSales", e.getMessage());
			e.printStackTrace();
			
			if(null != e && null != e.getMessage() && e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(salesList).build();
	}
	
	public javax.ws.rs.core.Response getReceipts(String token, String companyId, String id) {
		
		TallyInputDTO tallyInputDTO = null;
		List receiptsList = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			tallyInputDTO = new TallyInputDTO();
			if(null != id && id.equals("all")) {
				tallyInputDTO.setSelectAll(true);
			} else {
				tallyInputDTO.setSelectAll(false);
			}
			tallyInputDTO.setId(id);
			tallyInputDTO.setCompanyId(companyId);
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getReceipts In");
			
			ReceiptBC receiptBC = new ReceiptBC();
			receiptsList = receiptBC.getReceipts(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == receiptsList? "0" : receiptsList.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getReceipts Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getReceipts", e.getMessage());
			e.printStackTrace();
			
			if(null != e && null != e.getMessage() && e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(receiptsList).build();
	}

	public javax.ws.rs.core.Response sendMail(String token, String companyId, String fileName, String status, String to, List<Result> results) {
		
		String trackingId = Utility.getRandomNumber();
		//long startTime = System.currentTimeMillis();
		
		LOG.info(LOG_BASE_FORMAT, trackingId, "sendMail In");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			
			String scope = JWTHelper.validateJWT(token);
			
			new Thread() {
				public void run() {
					try {
						MailBC mailBC = new MailBC();
						mailBC.senMail(trackingId, fileName, status, to, results);
					} catch (Exception e) {
						LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in sendMail", e.getMessage());
					}
				}
			}.start();
			
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in sendMail", e.getMessage());
			e.printStackTrace();
			
			if(null != e && null != e.getMessage() && e.getMessage().contains("JWT signature does not match")) { 
				return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
			}
			
			return javax.ws.rs.core.Response.serverError().build();
		}
	
		return javax.ws.rs.core.Response.ok(response).build();
	}


	public void sendMailAsync(AsyncResponse asyncResponse, String token, String companyId, String fileName, String status, String to) {
			
			String trackingId = Utility.getRandomNumber();
			//long startTime = System.currentTimeMillis();
			
			LOG.info(LOG_BASE_FORMAT, trackingId, "sendMail In");
			
			Response response = new Response();
			response.setStatus("Success");
			response.setStatusMessage("Success");
			
			try {
				
				String scope = JWTHelper.validateJWT(token);
				
				//MailBC mailBC = new MailBC();
				//mailBC.senMail(trackingId, fileName, status, to);
				
				int i = 0;
				while(i < 10) {
					Thread.sleep(1000);
					System.out.println("Wait...");
					i++;
				}
				
			} catch (Exception e) {
				LOG.error(LOG_DATA_FORMAT, trackingId, "exception captured in sendMail", e.getMessage());
				e.printStackTrace();
				
				if(null != e && null != e.getMessage() && e.getMessage().contains("JWT signature does not match")) { 
					//return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
				}
				
				//return javax.ws.rs.core.Response.serverError().build();
			}
		
			//return javax.ws.rs.core.Response.ok(response).build();
		}
}
