package com.india.tamilnadu.tally.dto;

import java.util.List;

import com.india.tamilnadu.tally.vo.DayBookMasterVO;

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
