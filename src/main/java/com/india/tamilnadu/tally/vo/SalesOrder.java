package com.india.tamilnadu.tally.vo;

public class SalesOrder {

	private String id;
	private String orderNumber;
	private String voucherKey;
	private String orderDate;
	private String company;
	private String bf;
	private String gsm;
	private String size;
	private String weight;
	private String reel;
	private String orderStatus;
	private String altered;
	
	
	public String getAltered() {
		return altered;
	}
	public void setAltered(String altered) {
		this.altered = altered;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getBf() {
		return bf;
	}
	public void setBf(String bf) {
		this.bf = bf;
	}
	public String getGsm() {
		return gsm;
	}
	public void setGsm(String gsm) {
		this.gsm = gsm;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getReel() {
		return reel;
	}
	public void setReel(String reel) {
		this.reel = reel;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherKey() {
		return voucherKey;
	}
	public void setVoucherKey(String voucherKey) {
		this.voucherKey = voucherKey;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getSize() {
		return size;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
