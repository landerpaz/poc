package com.india.tamilnadu.tally.vo;

import java.util.List;

public class SalesOrderConsolidated {

	private List<SalesOrderPlannedSummary> salesOrderPlannedSummaries;
	private List<SalesOrder> salesOrdersBf;
	private List<SalesOrder> salesOrdersBfGsm;
	private List<SalesOrder> salesOrdersBfGsmSize;
	
	
	
	public List<SalesOrderPlannedSummary> getSalesOrderPlannedSummaries() {
		return salesOrderPlannedSummaries;
	}
	public void setSalesOrderPlannedSummaries(List<SalesOrderPlannedSummary> salesOrderPlannedSummaries) {
		this.salesOrderPlannedSummaries = salesOrderPlannedSummaries;
	}
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
	
	
}
