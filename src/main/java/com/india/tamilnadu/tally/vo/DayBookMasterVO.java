package com.india.tamilnadu.tally.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DayBookMaster")
public class DayBookMasterVO {

	private String voucherType;
	private String voucherDate;
	private String voucherNumber;
	private String partyLedgerName;
	private String voucherKey;
	private String effectiveDate;
	private String masterId;
	private String checkFlag;
	
	private List<LedgerEntryVO> ledgerEntryVOs;
	private List<InventoryEntryVO> inventoryEntryVOs;
	
	public List<LedgerEntryVO> getLedgerEntryVOs() {
		return ledgerEntryVOs;
	}
	public void setLedgerEntryVOs(List<LedgerEntryVO> ledgerEntryVOs) {
		this.ledgerEntryVOs = ledgerEntryVOs;
	}
	public List<InventoryEntryVO> getInventoryEntryVOs() {
		return inventoryEntryVOs;
	}
	public void setInventoryEntryVOs(List<InventoryEntryVO> inventoryEntryVOs) {
		this.inventoryEntryVOs = inventoryEntryVOs;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getPartyLedgerName() {
		return partyLedgerName;
	}
	public void setPartyLedgerName(String partyLedgerName) {
		this.partyLedgerName = partyLedgerName;
	}
	public String getVoucherKey() {
		return voucherKey;
	}
	public void setVoucherKey(String voucherKey) {
		this.voucherKey = voucherKey;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getMasterId() {
		return masterId;
	}
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	
}
