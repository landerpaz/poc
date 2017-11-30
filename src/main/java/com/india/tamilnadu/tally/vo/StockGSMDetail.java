package com.india.tamilnadu.tally.vo;

public class StockGSMDetail {

	private String  voucherEffectiveDate;
	private String stockItemName;
	private double gsmTgt;
	private double gsmAct;
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
	public double getGsmTgt() {
		return gsmTgt;
	}
	public void setGsmTgt(double gsmTgt) {
		this.gsmTgt = gsmTgt;
	}
	public double getGsmAct() {
		return gsmAct;
	}
	public void setGsmAct(double gsmAct) {
		this.gsmAct = gsmAct;
	}
	
}
