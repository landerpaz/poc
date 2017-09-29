package com.india.tamilnadu.tally.bc;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.jaxrs.TallyServiceImpl;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.DayBookMasterVO;
import com.india.tamilnadu.tally.vo.InventoryEntryVO;
import com.india.tamilnadu.tally.vo.LedgerEntryVO;
import com.india.tamilnadu.util.Constants;

import static com.india.tamilnadu.util.Constants.TALLYMESSAGE_COUNT_EXP;
import static com.india.tamilnadu.util.Constants.TALLYMESSAGE_COUNT_EXP_TINY;
import static com.india.tamilnadu.util.Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE;
import static com.india.tamilnadu.util.Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY;
import static com.india.tamilnadu.util.Constants.VCHTYPE;
import static com.india.tamilnadu.util.Constants.ACTION;
import static com.india.tamilnadu.util.Constants.DATE;
import static com.india.tamilnadu.util.Constants.VOUCHERTYPENAME;
import static com.india.tamilnadu.util.Constants.VOUCHERNUMBER;
import static com.india.tamilnadu.util.Constants.PARTYLEDGERNAME;
import static com.india.tamilnadu.util.Constants.VOUCHERKEY;
import static com.india.tamilnadu.util.Constants.EFFECTIVEDATE;
import static com.india.tamilnadu.util.Constants.PERSISTEDVIEW;
import static com.india.tamilnadu.util.Constants.ALTERID;
import static com.india.tamilnadu.util.Constants.MASTERID;
import static com.india.tamilnadu.util.Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1;
import static com.india.tamilnadu.util.Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1_TINY;
import static com.india.tamilnadu.util.Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_2;
import static com.india.tamilnadu.util.Constants.VOUCHER_ALLLEDGERENTRIES_LIST;
import static com.india.tamilnadu.util.Constants.LEDGERNAME;
import static com.india.tamilnadu.util.Constants.LOG_BASE_FORMAT;
import static com.india.tamilnadu.util.Constants.LOG_DATA_FORMAT;
import static com.india.tamilnadu.util.Constants.AMOUNT;
import static com.india.tamilnadu.util.Constants.ISPARTYLEDGER;
import static com.india.tamilnadu.util.Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_1;
import static com.india.tamilnadu.util.Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_1_TINY;
import static com.india.tamilnadu.util.Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_2;
import static com.india.tamilnadu.util.Constants.VOUCHER_LEDGERENTRIES_LIST;
import static com.india.tamilnadu.util.Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_1;
import static com.india.tamilnadu.util.Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_1_TINY;
import static com.india.tamilnadu.util.Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_2;
import static com.india.tamilnadu.util.Constants.VOUCHER_INVENTORYENTRIES_LIST;
import static com.india.tamilnadu.util.Constants.RATE;
import static com.india.tamilnadu.util.Constants.BILLEDQTY;
import static com.india.tamilnadu.util.Constants.STOCKITEMNAME;

public class TallyDayBookBC {
	
	private final Logger LOG = LoggerFactory.getLogger(TallyDayBookBC.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TallyDayBookBC tallyDayBookBC = new TallyDayBookBC();
		
		//Response response = tallyDayBookBC.addTallyDayBookData(new TallyInputDTO());
		tallyDayBookBC.getTallyDayBookData(new TallyInputDTO());
		
		
	}
	
	public Response updateTallyDayBookData(TallyInputDTO tallyInputDTO) {
		TallyDAO tallyDAO = new TallyDAO(); 
		return tallyDAO.updateDayBookMasterFlag(tallyInputDTO);
	}
	
	public List<DayBookMasterVO> getTallyDayBookData(TallyInputDTO tallyInputDTO) {
		
		System.out.println("Retreiving Tally day book data from DB......");
		long startTime = System.currentTimeMillis();
	    
		//get data from DB
		TallyDAO tallyDAO = new TallyDAO();
		List<DayBookMasterVO> dayBookMasterVOs = tallyDAO.getTallyDayBookMaster();
		List<LedgerEntryVO> ledgerEntryVOs = tallyDAO.getTallyDayBookLedgerEntries();
		List<InventoryEntryVO> inventoryEntryVOs = tallyDAO.getTallyDayBookInventoryEntries();
		
	    long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Retreiving Tally day book data from DB completed!  " + elapsedTime);
		
		System.out.println("dayBookMasterVOs size : " + (null != dayBookMasterVOs ? dayBookMasterVOs.size() : 0));
		System.out.println("ledgerEntryVOs size : " + (null != ledgerEntryVOs ? ledgerEntryVOs.size() : 0));
		System.out.println("inventoryEntryVOs size : " + (null != inventoryEntryVOs ? inventoryEntryVOs.size() : 0));
		
		System.out.println("Framing Tally day book data......");
		startTime = System.currentTimeMillis();
	    
		//group ledgerentries and inventoryentries for each voucherkey and load them in  daybookmaster
		List<DayBookMasterVO> dayBookMasterVOsResult = frameTallyDayBookdataFromLedgerAndInv(dayBookMasterVOs, ledgerEntryVOs, inventoryEntryVOs);
		
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		System.out.println("Framing Tally day book data completed!  " + elapsedTime);
		
		
		return dayBookMasterVOsResult;
		
	}
	
	private List<DayBookMasterVO> frameTallyDayBookdataFromLedgerAndInv(List<DayBookMasterVO> dayBookMasterVOs, List<LedgerEntryVO> ledgerEntryVOs, List<InventoryEntryVO> inventoryEntryVOs) {
		
		List<DayBookMasterVO> dayBookMasterVOsResult = new ArrayList<>();
		List<LedgerEntryVO> ledgerEntryVOsLocal = null;
		List<InventoryEntryVO> inventoryEntryVOsLocal = null;
		
		try {
			
			
			for(DayBookMasterVO dayBookMasterVO : dayBookMasterVOs) {
			
				//add ledgerentries
				ledgerEntryVOsLocal = new ArrayList<>();
				for(LedgerEntryVO ledgerEntryVO : ledgerEntryVOs) {
					if(ledgerEntryVO.getVoucherKey().equals(dayBookMasterVO.getVoucherKey())) {
						ledgerEntryVOsLocal.add(ledgerEntryVO);
					}
				}
				dayBookMasterVO.setLedgerEntryVOs(ledgerEntryVOsLocal);
				
				//add inventoryentries
				inventoryEntryVOsLocal = new ArrayList<>();
				for(InventoryEntryVO enInventoryEntryVO : inventoryEntryVOs) {
					if(enInventoryEntryVO.getVoucherKey().equals(dayBookMasterVO.getVoucherKey())) {
						inventoryEntryVOsLocal.add(enInventoryEntryVO);
					}
				}
				dayBookMasterVO.setInventoryEntryVOs(inventoryEntryVOsLocal);
				
				dayBookMasterVOsResult.add(dayBookMasterVO);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return dayBookMasterVOsResult;
	}

	public void addTallyDayBookData(TallyInputDTO tallyInputDTO) throws Exception {
		
		long startTime = System.currentTimeMillis();
	   
		//Response response = new Response();
		
		LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBookData In");
		LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBookData, xml parsing started");
		 
		//get data from xml file
		//List<DayBookMasterVO> dayBookMasterVOs = addFromFile(tallyInputDTO);
		
		//get data from tally response
		List<DayBookMasterVO> dayBookMasterVOs = addFromRequest(tallyInputDTO);
		
		LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBookData, xml parsing completed", "time_elapsed:" + (startTime - System.currentTimeMillis()));
	    
		//insert data in DB
		if(null != dayBookMasterVOs && dayBookMasterVOs.size() > 0) {
		
			startTime = System.currentTimeMillis();
			LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBookData, insert data in DB started");
			
			startTime = System.currentTimeMillis();
			
			tallyInputDTO.setDayBookMasterVOs(dayBookMasterVOs);
			TallyDAO tallyDAO = new TallyDAO();
			//response = tallyDAO.addTallyDayBook(tallyInputDTO);
			tallyDAO.addTallyDayBook(tallyInputDTO);
			
			LOG.info(LOG_DATA_FORMAT, tallyInputDTO.getTrackingID(), "addTallyDayBookData, insert data in DB completed", "time_elapsed:" + (startTime - System.currentTimeMillis()));
			
		}
		
		//return response;
		
	}
	
	private List<DayBookMasterVO> addFromFile(TallyInputDTO tallyInputVO) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		DayBookMasterVO dayBookMasterVO = null;
		LedgerEntryVO ledgerEntryVO = null;
		InventoryEntryVO inventoryEntryVO = null;
		List<DayBookMasterVO> dayBookMasterVOs = new ArrayList<>();
		
		try {
			
			builder = factory.newDocumentBuilder();
            //doc = builder.parse("/Users/ashokarulsamy/Downloads/2.xml");
            //doc = builder.parse("/Users/ashokarulsamy/Downloads/day_book.xml");
            doc = builder.parse("/Users/ashokarulsamy/Downloads/test_file_1.xml");
			//doc = builder.parse("/Users/ashokarulsamy/Downloads/db_923-2.xml");
			
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();

            int tallyMessageCount = getCount(doc, xpath, Constants.TALLYMESSAGE_COUNT_EXP);
            
            System.out.println("tallyMessageCount : " + tallyMessageCount);
            
            for(int index=1; index<=tallyMessageCount; index++) {
            	/*System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VCHTYPE, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.ACTION, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.DATE, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERTYPENAME, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERNUMBER, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.PARTYLEDGERNAME, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERKEY, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.EFFECTIVEDATE, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.PERSISTEDVIEW, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.ALTERID, index));
            	System.out.println(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.MASTERID, index));
            	System.out.println("----------------------");*/
            	
            	dayBookMasterVO = new DayBookMasterVO();
            	dayBookMasterVO.setVoucherType(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VCHTYPE, index));
            	dayBookMasterVO.setVoucherAction(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, ACTION, index));
            	dayBookMasterVO.setVoucherDate(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, DATE, index));
            	dayBookMasterVO.setVoucherTypeName(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERTYPENAME, index));
            	dayBookMasterVO.setVoucherNumber(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERNUMBER, index));
            	dayBookMasterVO.setPartyLedgerName(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, PARTYLEDGERNAME, index));
            	dayBookMasterVO.setVoucherKey(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERKEY, index));
            	dayBookMasterVO.setEffectiveDate(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, EFFECTIVEDATE, index));
            	dayBookMasterVO.setPersistedView(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, PERSISTEDVIEW, index));
            	dayBookMasterVO.setAlterId(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, ALTERID, index));
            	dayBookMasterVO.setMasterId(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, MASTERID, index));
            	
            	
            	//get allledgerentries
            	int allLedgerEntriesListCount = getCount(doc, xpath, new StringBuilder(VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1).append(index).append(VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("allLedgerEntriesListCount : " + allLedgerEntriesListCount);
            	
            	List<LedgerEntryVO> ledgerEntryVOs = new ArrayList<>();
            	for(int subIndex=1; subIndex<=allLedgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, ISPARTYLEDGER, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            	}
            	
            	
            	int ledgerEntriesListCount = getCount(doc, xpath, new StringBuilder(VOUCHER_LEDGERENTRIES_LIST_COUNT_1).append(index).append(VOUCHER_LEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("ledgerEntriesListCount : " + ledgerEntriesListCount);
            	
            	for(int subIndex=1; subIndex<=ledgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            		
            	}
            	
            	//set ledger list in master vo in one place for both allledgerentrieslist and ledgerentrieslist, because either one will be available not both for same tallmessage
            	dayBookMasterVO.setLedgerEntryVOs(ledgerEntryVOs);
            	
            	
            	//get inventoryentries
            	int inventoryEntriesListCount = getCount(doc, xpath, new StringBuilder(VOUCHER_INVENTORYENTRIES_LIST_COUNT_1).append(index).append(VOUCHER_INVENTORYENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("inventoryEntriesListCount : " + inventoryEntriesListCount);
            	
            	List<InventoryEntryVO> inventoryEntryVOs = new ArrayList<>();
            	
            	for(int subIndex=1; subIndex<=inventoryEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, STOCKITEMNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, RATE, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, BILLEDQTY, index, subIndex));
            		
            		System.out.println("----------------------");*/
            		
            		inventoryEntryVO = new InventoryEntryVO();
            		inventoryEntryVO.setStockItemName(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, STOCKITEMNAME, index, subIndex));
            		inventoryEntryVO.setRate(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, RATE, index, subIndex));
            		inventoryEntryVO.setAmount(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, AMOUNT, index, subIndex));
            		inventoryEntryVO.setBilledQuantity(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, BILLEDQTY, index, subIndex));
            		
            		inventoryEntryVOs.add(inventoryEntryVO);
            	}
            	
            	dayBookMasterVO.setInventoryEntryVOs(inventoryEntryVOs);
            	dayBookMasterVOs.add(dayBookMasterVO);
            	
            	//System.out.println("########################################################");
            	
            }
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		return dayBookMasterVOs;
		
	}
	
	private List<DayBookMasterVO> addFromRequest(TallyInputDTO tallyInputDTO) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		DayBookMasterVO dayBookMasterVO = null;
		LedgerEntryVO ledgerEntryVO = null;
		InventoryEntryVO inventoryEntryVO = null;
		List<DayBookMasterVO> dayBookMasterVOs = new ArrayList<>();
		
		try {
			
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(tallyInputDTO.getDayBook()));
            doc = builder.parse(is);
			
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();

            int tallyMessageCount = getCount(doc, xpath, tallyInputDTO.isTiny() ? TALLYMESSAGE_COUNT_EXP_TINY : TALLYMESSAGE_COUNT_EXP);
            
            if(tallyMessageCount < 1) {
            	LOG.info(LOG_BASE_FORMAT, tallyInputDTO.getTrackingID(), "addFromRequest, valid tally message count is 0, so xml parsing cancelled");
            }
            
            for(int index=1; index<=tallyMessageCount; index++) {
            	/*System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VCHTYPE, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, ACTION, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, DATE, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERTYPENAME, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERNUMBER, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, PARTYLEDGERNAME, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERKEY, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, EFFECTIVEDATE, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, PERSISTEDVIEW, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, ALTERID, index));
            	System.out.println(getPrimaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, MASTERID, index));
            	System.out.println("----------------------");*/
            	
            	dayBookMasterVO = new DayBookMasterVO();
            	dayBookMasterVO.setVoucherType(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VCHTYPE, index));
            	dayBookMasterVO.setVoucherAction(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, ACTION, index));
            	dayBookMasterVO.setVoucherDate(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, DATE, index));
            	dayBookMasterVO.setVoucherTypeName(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERTYPENAME, index));
            	dayBookMasterVO.setVoucherNumber(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERNUMBER, index));
            	dayBookMasterVO.setPartyLedgerName(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, PARTYLEDGERNAME, index));
            	dayBookMasterVO.setVoucherKey(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHERKEY, index));
            	dayBookMasterVO.setEffectiveDate(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, EFFECTIVEDATE, index));
            	dayBookMasterVO.setPersistedView(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, PERSISTEDVIEW, index));
            	dayBookMasterVO.setAlterId(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, ALTERID, index));
            	dayBookMasterVO.setMasterId(getPrimaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, MASTERID, index));
            	
            	
            	//get allledgerentries
            	int allLedgerEntriesListCount = getCount(doc, xpath, new StringBuilder(tallyInputDTO.isTiny() ? VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1_TINY : VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1).append(index).append(VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("allLedgerEntriesListCount : " + allLedgerEntriesListCount);
            	
            	List<LedgerEntryVO> ledgerEntryVOs = new ArrayList<>();
            	for(int subIndex=1; subIndex<=allLedgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, ISPARTYLEDGER, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_ALLLEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            	}
            	
            	
            	int ledgerEntriesListCount = getCount(doc, xpath, new StringBuilder(tallyInputDTO.isTiny() ? VOUCHER_LEDGERENTRIES_LIST_COUNT_1_TINY : VOUCHER_LEDGERENTRIES_LIST_COUNT_1).append(index).append(VOUCHER_LEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("ledgerEntriesListCount : " + ledgerEntriesListCount);
            	
            	for(int subIndex=1; subIndex<=ledgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_LEDGERENTRIES_LIST, AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            		
            	}
            	
            	//set ledger list in master vo in one place for both allledgerentrieslist and ledgerentrieslist, because either one will be available not both for same tallmessage
            	dayBookMasterVO.setLedgerEntryVOs(ledgerEntryVOs);
            	
            	
            	//get inventoryentries
            	int inventoryEntriesListCount = getCount(doc, xpath, new StringBuilder(tallyInputDTO.isTiny() ? VOUCHER_INVENTORYENTRIES_LIST_COUNT_1_TINY : VOUCHER_INVENTORYENTRIES_LIST_COUNT_1).append(index).append(VOUCHER_INVENTORYENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("inventoryEntriesListCount : " + inventoryEntriesListCount);
            	
            	List<InventoryEntryVO> inventoryEntryVOs = new ArrayList<>();
            	
            	for(int subIndex=1; subIndex<=inventoryEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, STOCKITEMNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, RATE, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, BILLEDQTY, index, subIndex));
            		
            		System.out.println("----------------------");*/
            		
            		inventoryEntryVO = new InventoryEntryVO();
            		inventoryEntryVO.setStockItemName(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, STOCKITEMNAME, index, subIndex));
            		inventoryEntryVO.setRate(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, RATE, index, subIndex));
            		inventoryEntryVO.setAmount(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, AMOUNT, index, subIndex));
            		inventoryEntryVO.setBilledQuantity(getSecondaryData(doc, xpath, tallyInputDTO.isTiny() ? ENVELOPE_BODY_DATA_TALLYMESSAGE_TINY : ENVELOPE_BODY_DATA_TALLYMESSAGE, VOUCHER_INVENTORYENTRIES_LIST, BILLEDQTY, index, subIndex));
            		
            		inventoryEntryVOs.add(inventoryEntryVO);
            	}
            	
            	dayBookMasterVO.setInventoryEntryVOs(inventoryEntryVOs);
            	dayBookMasterVOs.add(dayBookMasterVO);
            	
            	//System.out.println("########################################################");
            	
            }
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		return dayBookMasterVOs;
	}
	
	private static int getCount(Document doc, XPath xpath, String expression) {
        String count = null;
        try {
        	//System.out.println(expression);
            XPathExpression expr = xpath.compile(expression);
            count = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return null != count ? Integer.parseInt(count) : 0;
    }
	
	private static String getPrimaryData(Document doc, XPath xpath, String expressionPrefix, String expressionSuffix, int index) {
        
		String result = null;
        
        try {
            XPathExpression expr = xpath.compile(new StringBuilder(expressionPrefix).append(index).append(expressionSuffix).toString());
            result = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return null != result ? result.trim() : result;
    }
	
	private static String getSecondaryData(Document doc, XPath xpath, String expressionPrefix, String expression, String expressionSuffix, int index, int subIndex) {
        
		String result = null;
        
        try {
            XPathExpression expr = xpath.compile(new StringBuilder(expressionPrefix).append(index).append(expression).append(subIndex).append(expressionSuffix).toString());
            result = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return null != result ? result.trim() : result;
    }

}
