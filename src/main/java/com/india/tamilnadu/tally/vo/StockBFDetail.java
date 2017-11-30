package com.india.tamilnadu.tally.vo;

public class StockBFDetail {

	private String  voucherEffectiveDate;
	private String stockItemName;
	private double bfTgt;
	private double bfAct;
	private String batchName;
	
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getVoucherEffectiveDate() {
		return voucherEffectiveDate;
	}
	public void setVoucherEffectiveDate(String voucherEffectiveDate) {
		this.voucherEffectiveDate = voucherEffectiveDate;
	}
	public String getStockItemName() {
		return stockItemName;
	}
	public void setStockItemName(String stockItemName) {
		this.stockItemName = stockItemName;
	}
	public double getBfTgt() {
		return bfTgt;
	}
	public void setBfTgt(double bfTgt) {
		this.bfTgt = bfTgt;
	}
	public double getBfAct() {
		return bfAct;
	}
	public void setBfAct(double bfAct) {
		this.bfAct = bfAct;
	}
	
	
}
