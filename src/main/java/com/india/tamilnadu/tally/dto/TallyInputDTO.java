package com.india.tamilnadu.tally.dto;

import java.util.List;

import com.india.tamilnadu.tally.vo.DayBookMasterVO;
import com.india.tamilnadu.tally.vo.Receipt;
import com.india.tamilnadu.tally.vo.Sales;
import com.india.tamilnadu.tally.vo.SalesOrder;

public class TallyInputDTO {

	private String trackingID;
	private String dayBook;
	private List<DayBookMasterVO> dayBookMasterVOs;
	private String voucherKey;
	private boolean tiny;
	private String companyId;
	private String name;
	private String startDate;
	private String endDate;
	private String id;
	private List<SalesOrder> salesOrders;
	private String batchNo;
	private String consBf;
	private String consBfGsm;
	private String consBfGsmSize;
	private String reel;
	private String altered;
	private String salesOrderedPlannedId;
	private String weight;
	private String type;
	private List<Sales> salesList;
	private List<Receipt> receiptList;
	private boolean selectAll;
	
	public List<Sales> getSalesList() {
		return salesList;
	}
	public void setSalesList(List<Sales> salesList) {
		this.salesList = salesList;
	}
	public List<Receipt> getReceiptList() {
		return receiptList;
	}
	public void setReceiptList(List<Receipt> receiptList) {
		this.receiptList = receiptList;
	}
	public boolean isSelectAll() {
		return selectAll;
	}
	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getAltered() {
		return altered;
	}
	public void setAltered(String altered) {
		this.altered = altered;
	}
	public String getSalesOrderedPlannedId() {
		return salesOrderedPlannedId;
	}
	public void setSalesOrderedPlannedId(String salesOrderedPlannedId) {
		this.salesOrderedPlannedId = salesOrderedPlannedId;
	}
	public String getReel() {
		return reel;
	}
	public void setReel(String reel) {
		this.reel = reel;
	}
	public String getConsBf() {
		return consBf;
	}
	public void setConsBf(String consBf) {
		this.consBf = consBf;
	}
	public String getConsBfGsm() {
		return consBfGsm;
	}
	public void setConsBfGsm(String consBfGsm) {
		this.consBfGsm = consBfGsm;
	}
	public String getConsBfGsmSize() {
		return consBfGsmSize;
	}
	public void setConsBfGsmSize(String consBfGsmSize) {
		this.consBfGsmSize = consBfGsmSize;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public List<SalesOrder> getSalesOrders() {
		return salesOrders;
	}
	public void setSalesOrders(List<SalesOrder> salesOrders) {
		this.salesOrders = salesOrders;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public boolean isTiny() {
		return tiny;
	}
	public void setTiny(boolean tiny) {
		this.tiny = tiny;
	}
	public String getVoucherKey() {
		return voucherKey;
	}
	public void setVoucherKey(String voucherKey) {
		this.voucherKey = voucherKey;
	}
	public List<DayBookMasterVO> getDayBookMasterVOs() {
		return dayBookMasterVOs;
	}
	public void setDayBookMasterVOs(List<DayBookMasterVO> dayBookMasterVOs) {
		this.dayBookMasterVOs = dayBookMasterVOs;
	}
	public String getTrackingID() {
		return trackingID;
	}
	public void setTrackingID(String trackingID) {
		this.trackingID = trackingID;
	}
	public String getDayBook() {
		return dayBook;
	}
	public void setDayBook(String dayBook) {
		this.dayBook = dayBook;
	}
	
	
}
