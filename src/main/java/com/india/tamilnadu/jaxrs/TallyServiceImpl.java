package com.india.tamilnadu.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.HttpServletRequestSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.bc.TallyDayBookBC;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.User;
import com.india.tamilnadu.util.SaxParserHandler;
import com.india.tamilnadu.util.TallyBean;
import com.india.tamilnadu.util.TallyRequestContext;
import com.india.tamilnadu.util.Utility;

/*import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;*/

import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;
import static com.india.tamilnadu.util.Constants.LOG_DATA_FORMAT;

public class TallyServiceImpl implements TallyService {

	private final Logger LOG = LoggerFactory.getLogger(TallyServiceImpl.class);
	
	
	public Response login(User user) {
		
		Response response = new Response();
		response.setStatus("200");
		
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
	
	public List getDayBook() {
		System.out.println("...invoking getTallyDayBook");
		
		TallyDayBookBC dayBookBC = new TallyDayBookBC();
		List dayBookList = dayBookBC.getTallyDayBookData(new TallyInputDTO());
				
		System.out.println("Number of day book entries : " + (null == dayBookList? "0" : dayBookList.size()));
		
		return dayBookList;
	}
	
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
