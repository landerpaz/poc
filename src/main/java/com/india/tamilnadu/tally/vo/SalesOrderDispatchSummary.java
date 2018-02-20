package com.india.tamilnadu.tally.vo;

import java.util.List;

public class SalesOrderDispatchSummary {

	private String company;
	private String orderNumber;
	private String totalReel;
	private String totalReelInStock;
	private String totalWeight;
	private List<SalesOrderDispatch> salesOrderDispatchs;
	
	public String getTotalReelInStock() {
		return totalReelInStock;
	}
	public void setTotalReelInStock(String totalReelInStock) {
		this.totalReelInStock = totalReelInStock;
	}
	public String getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
	public String getTotalReel() {
		return totalReel;
	}
	public void setTotalReel(String totalReel) {
		this.totalReel = totalReel;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public List<SalesOrderDispatch> getSalesOrderDispatchs() {
		return salesOrderDispatchs;
	}
	public void setSalesOrderDispatchs(List<SalesOrderDispatch> salesOrderDispatchs) {
		this.salesOrderDispatchs = salesOrderDispatchs;
	}
}
