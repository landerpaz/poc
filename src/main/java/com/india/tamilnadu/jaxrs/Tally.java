package com.india.tamilnadu.jaxrs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Tally")
public class Tally {

	private String tallySummaryIid;
	private String reportId;
	private String reportName;
	private String reportKey;
	private String reportValue1;
	private String reportValue2;
	private String createdTime;
	private String checkFlag;
	
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getTallySummaryIid() {
		return tallySummaryIid;
	}
	public void setTallySummaryIid(String tallySummaryIid) {
		this.tallySummaryIid = tallySummaryIid;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportKey() {
		return reportKey;
	}
	public void setReportKey(String reportKey) {
		this.reportKey = reportKey;
	}
	public String getReportValue1() {
		return reportValue1;
	}
	public void setReportValue1(String reportValue1) {
		this.reportValue1 = reportValue1;
	}
	public String getReportValue2() {
		return reportValue2;
	}
	public void setReportValue2(String reportValue2) {
		this.reportValue2 = reportValue2;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	
}
