package com.india.tamilnadu.tally.vo;

import java.util.List;

public class SalesOrderPlannedSummary {

	private String batchNumber;
	private String createdDate;
	private List<SalesOrderPlanned> salesOrderPlanneds;
	private List<SalesOrder> salesOrdersBf;
	private List<SalesOrder> salesOrdersBfGsm;
	private List<SalesOrder> salesOrdersBfGsmSize;
	
	public List<SalesOrder> getSalesOrdersBf() {
		return salesOrdersBf;
	}
	public void setSalesOrdersBf(List<SalesOrder> salesOrdersBf) {
		this.salesOrdersBf = salesOrdersBf;
	}
	public List<SalesOrder> getSalesOrdersBfGsm() {
		return salesOrdersBfGsm;
	}
	public void setSalesOrdersBfGsm(List<SalesOrder> salesOrdersBfGsm) {
		this.salesOrdersBfGsm = salesOrdersBfGsm;
	}
	public List<SalesOrder> getSalesOrdersBfGsmSize() {
		return salesOrdersBfGsmSize;
	}
	public void setSalesOrdersBfGsmSize(List<SalesOrder> salesOrdersBfGsmSize) {
		this.salesOrdersBfGsmSize = salesOrdersBfGsmSize;
	}
	public List<SalesOrderPlanned> getSalesOrderPlanneds() {
		return salesOrderPlanneds;
	}
	public void setSalesOrderPlanneds(List<SalesOrderPlanned> salesOrderPlanneds) {
		this.salesOrderPlanneds = salesOrderPlanneds;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
