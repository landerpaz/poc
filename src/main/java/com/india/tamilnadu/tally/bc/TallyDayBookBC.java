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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.dto.Response;
import com.india.tamilnadu.tally.dto.TallyInputDTO;
import com.india.tamilnadu.tally.vo.DayBookMasterVO;
import com.india.tamilnadu.tally.vo.InventoryEntryVO;
import com.india.tamilnadu.tally.vo.LedgerEntryVO;
import com.india.tamilnadu.util.Constants;

public class TallyDayBookBC {

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

	public Response addTallyDayBookData(TallyInputDTO tallyInputDTO) {
		
		Response response = new Response();
		
		System.out.println("Parsing xml request data......");
		long startTime = System.currentTimeMillis();
	    
		//get data from xml file
		//List<DayBookMasterVO> dayBookMasterVOs = addFromFile(tallyInputDTO);
		
		//get data from xml file
		List<DayBookMasterVO> dayBookMasterVOs = addFromRequest(tallyInputDTO);
		
	    long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Parsing xml request data completed!  " + elapsedTime);
		
		//insert data in DB
		if(null != dayBookMasterVOs && dayBookMasterVOs.size() > 0) {
		
			System.out.println("Inserting data in DB......");
			startTime = System.currentTimeMillis();
			
			tallyInputDTO.setDayBookMasterVOs(dayBookMasterVOs);
			TallyDAO tallyDAO = new TallyDAO();
			response = tallyDAO.addTallyDayBook(tallyInputDTO);
			
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("Inserting data in DB completed!    " + elapsedTime);
			
		}
		
		return response;
		
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
            	dayBookMasterVO.setVoucherType(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VCHTYPE, index));
            	dayBookMasterVO.setVoucherAction(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.ACTION, index));
            	dayBookMasterVO.setVoucherDate(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.DATE, index));
            	dayBookMasterVO.setVoucherTypeName(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERTYPENAME, index));
            	dayBookMasterVO.setVoucherNumber(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERNUMBER, index));
            	dayBookMasterVO.setPartyLedgerName(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.PARTYLEDGERNAME, index));
            	dayBookMasterVO.setVoucherKey(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERKEY, index));
            	dayBookMasterVO.setEffectiveDate(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.EFFECTIVEDATE, index));
            	dayBookMasterVO.setPersistedView(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.PERSISTEDVIEW, index));
            	dayBookMasterVO.setAlterId(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.ALTERID, index));
            	dayBookMasterVO.setMasterId(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.MASTERID, index));
            	
            	
            	//get allledgerentries
            	int allLedgerEntriesListCount = getCount(doc, xpath, new StringBuilder(Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1).append(index).append(Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("allLedgerEntriesListCount : " + allLedgerEntriesListCount);
            	
            	List<LedgerEntryVO> ledgerEntryVOs = new ArrayList<>();
            	for(int subIndex=1; subIndex<=allLedgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.ISPARTYLEDGER, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            	}
            	
            	
            	int ledgerEntriesListCount = getCount(doc, xpath, new StringBuilder(Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_1).append(index).append(Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("ledgerEntriesListCount : " + ledgerEntriesListCount);
            	
            	for(int subIndex=1; subIndex<=ledgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            		
            	}
            	
            	//set ledger list in master vo in one place for both allledgerentrieslist and ledgerentrieslist, because either one will be available not both for same tallmessage
            	dayBookMasterVO.setLedgerEntryVOs(ledgerEntryVOs);
            	
            	
            	//get inventoryentries
            	int inventoryEntriesListCount = getCount(doc, xpath, new StringBuilder(Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_1).append(index).append(Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("inventoryEntriesListCount : " + inventoryEntriesListCount);
            	
            	List<InventoryEntryVO> inventoryEntryVOs = new ArrayList<>();
            	
            	for(int subIndex=1; subIndex<=inventoryEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.STOCKITEMNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.RATE, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.BILLEDQTY, index, subIndex));
            		
            		System.out.println("----------------------");*/
            		
            		inventoryEntryVO = new InventoryEntryVO();
            		inventoryEntryVO.setStockItemName(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.STOCKITEMNAME, index, subIndex));
            		inventoryEntryVO.setRate(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.RATE, index, subIndex));
            		inventoryEntryVO.setAmount(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		inventoryEntryVO.setBilledQuantity(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.BILLEDQTY, index, subIndex));
            		
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
            	dayBookMasterVO.setVoucherType(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VCHTYPE, index));
            	dayBookMasterVO.setVoucherAction(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.ACTION, index));
            	dayBookMasterVO.setVoucherDate(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.DATE, index));
            	dayBookMasterVO.setVoucherTypeName(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERTYPENAME, index));
            	dayBookMasterVO.setVoucherNumber(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERNUMBER, index));
            	dayBookMasterVO.setPartyLedgerName(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.PARTYLEDGERNAME, index));
            	dayBookMasterVO.setVoucherKey(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHERKEY, index));
            	dayBookMasterVO.setEffectiveDate(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.EFFECTIVEDATE, index));
            	dayBookMasterVO.setPersistedView(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.PERSISTEDVIEW, index));
            	dayBookMasterVO.setAlterId(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.ALTERID, index));
            	dayBookMasterVO.setMasterId(getPrimaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.MASTERID, index));
            	
            	
            	//get allledgerentries
            	int allLedgerEntriesListCount = getCount(doc, xpath, new StringBuilder(Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_1).append(index).append(Constants.VOUCHER_ALLLEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("allLedgerEntriesListCount : " + allLedgerEntriesListCount);
            	
            	List<LedgerEntryVO> ledgerEntryVOs = new ArrayList<>();
            	for(int subIndex=1; subIndex<=allLedgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.ISPARTYLEDGER, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_ALLLEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            	}
            	
            	
            	int ledgerEntriesListCount = getCount(doc, xpath, new StringBuilder(Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_1).append(index).append(Constants.VOUCHER_LEDGERENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("ledgerEntriesListCount : " + ledgerEntriesListCount);
            	
            	for(int subIndex=1; subIndex<=ledgerEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		System.out.println("----------------------");*/
            		
            		ledgerEntryVO = new LedgerEntryVO();
            		ledgerEntryVO.setLedgerName(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.LEDGERNAME, index, subIndex));
            		ledgerEntryVO.setAmount(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_LEDGERENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		ledgerEntryVOs.add(ledgerEntryVO);
            		
            	}
            	
            	//set ledger list in master vo in one place for both allledgerentrieslist and ledgerentrieslist, because either one will be available not both for same tallmessage
            	dayBookMasterVO.setLedgerEntryVOs(ledgerEntryVOs);
            	
            	
            	//get inventoryentries
            	int inventoryEntriesListCount = getCount(doc, xpath, new StringBuilder(Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_1).append(index).append(Constants.VOUCHER_INVENTORYENTRIES_LIST_COUNT_2).toString());
            	
            	//System.out.println("inventoryEntriesListCount : " + inventoryEntriesListCount);
            	
            	List<InventoryEntryVO> inventoryEntryVOs = new ArrayList<>();
            	
            	for(int subIndex=1; subIndex<=inventoryEntriesListCount; subIndex++) {
            		/*System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.STOCKITEMNAME, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.RATE, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		System.out.println(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.BILLEDQTY, index, subIndex));
            		
            		System.out.println("----------------------");*/
            		
            		inventoryEntryVO = new InventoryEntryVO();
            		inventoryEntryVO.setStockItemName(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.STOCKITEMNAME, index, subIndex));
            		inventoryEntryVO.setRate(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.RATE, index, subIndex));
            		inventoryEntryVO.setAmount(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.AMOUNT, index, subIndex));
            		inventoryEntryVO.setBilledQuantity(getSecondaryData(doc, xpath, Constants.ENVELOPE_BODY_DATA_TALLYMESSAGE, Constants.VOUCHER_INVENTORYENTRIES_LIST, Constants.BILLEDQTY, index, subIndex));
            		
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
