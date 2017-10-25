package com.india.tamilnadu.jaxrs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.security.bc.AuthenticationBC;
import com.india.tamilnadu.security.util.JWTHelper;
import com.india.tamilnadu.tally.bc.TallyDayBookBC;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.util.SaxParserHandler;
import com.india.tamilnadu.util.TallyBean;
import com.india.tamilnadu.util.TallyRequestContext;
import com.india.tamilnadu.util.Utility;
import com.india.tamilnadu.vo.Login;
import com.india.tamilnadu.vo.User;

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;
import static com.india.tamilnadu.util.Constants.LOG_DATA_FORMAT;

public class TallyServiceImpl implements TallyService {

	private final Logger LOG = LoggerFactory.getLogger(TallyServiceImpl.class);
	
	public Response userLogin(Login login) {
		
		String trackingID = Utility.getRandomNumber();
		
		LOG.info(LOG_BASE_FORMAT, trackingID, "userLogin In");
		
		Response response = new Response();
		response.setStatus("200");
		response.setStatusMessage("AUTH_FAILED");
		
		try {
			
			if(null == login || null == login.getEmail() || null == login.getPassword()) {
				return response;
			}
			
			//authenticate user
			AuthenticationBC authenticationBC = new AuthenticationBC();
			User user = authenticationBC.authenticate(login, trackingID);
			if(user.isAuthenticate()) {
				response.setStatusMessage("AUTH_SUCCESS");
				response.setRole(user.getRole());
				response.setToken(user.getToken());
				response.setFirstName(user.getFirstName());
				response.setLastName(user.getLastName());				
			}
			
			LOG.info(LOG_BASE_FORMAT, trackingID, "userLogin Out");
		
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
	
	public List getTallySummary() {
		
		String trackingID = Utility.getRandomNumber();
		
		LOG.info(LOG_BASE_FORMAT, trackingID, "getTallySummary In");
		
		/*Message message = PhaseInterceptorChain.getCurrentMessage();
	    HttpServletRequestSnapshot request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
	    request.getSession().
	    //HttpSession  session = request.getSession();
	    //session.setAttribute("Session : " + session);
*/	    
		
		TallyDAO tallyDAO = new TallyDAO();
		List tallySummaryList = tallyDAO.getTallySummary();
				
		System.out.println("Number of tally summary : " + (null == tallySummaryList? "0" : tallySummaryList.size()));
		
		return tallySummaryList;
	}
	
	public Response addTallyData(String tallyData) {
		System.out.println("...adding tally data");
				
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
			context.setReportName("REQUEST_XML_TRAIL_BALANCE");
			context.setKeys(tallyBean.getKeys());
			context.setValues1(tallyBean.getValues1());
			context.setValues2(tallyBean.getValues2());
			context.setCheckFlag(false);
			
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
	
	public Response updateTallySummary(Tally tally) {
		System.out.println("...update tally data");
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			TallyDAO tallyDAO = new TallyDAO();
			response = tallyDAO.updateTallySummary(tally);
			
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setStatusMessage("Failed");
		}
		
		return response;
	}
	
	public Response updateDayBookFlag(String voucherKey) {
		System.out.println("...update day book flag");
		System.out.println("voucherKey : " + voucherKey);
		
		Response response = new Response();
		response.setStatus("Success");
		response.setStatusMessage("Success");
		
		try {
			TallyInputDTO tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setVoucherKey(voucherKey);
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookBC.updateTallyDayBookData(tallyInputDTO);
			
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setStatusMessage("Failed");
		}
		
		return response;
	}
	
	/**
	 *This method fetches day book data from DB 
	 * 
	 * 
	 **/
	public List getDayBook() {
		
		TallyInputDTO tallyInputDTO = null;
		List dayBookList = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
			tallyInputDTO.setTrackingID(Utility.getRandomNumber());
			
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "getDayBook In");
			
			TallyDayBookBC dayBookBC = new TallyDayBookBC();
			dayBookList = dayBookBC.getTallyDayBookData(tallyInputDTO);
					
			LOG.debug(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "Number of day book entries : "  + (null == dayBookList? "0" : dayBookList.size()));
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "getDayBook Out", "time_elapsed:" + (startTime - System.currentTimeMillis()));
		
		} catch (Exception e) {
			LOG.error(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "exception captured in getDayBook", e.getMessage());
			e.printStackTrace();
		}
	
		return dayBookList;
	}
	
	public javax.ws.rs.core.Response getDayBookJWT(String token) {
		
		TallyInputDTO tallyInputDTO = null;
		List dayBookList = null;
		long startTime = System.currentTimeMillis();
		
		try {
			
			tallyInputDTO = new TallyInputDTO();
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
	
}
