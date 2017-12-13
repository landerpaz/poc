package com.india.tamilnadu.tally.vo;

import java.util.List;

public class StockStatistics {

	/*private String stockWeek;
	private String stockMonth;
	private String stockQuarter;
	private String stockYear;*/
	
	private String monthlySales;
	private String quarterlySales;
	private String yearlySales;
	
	private String monthlyProduction;
	private String quarterlyProduction;
	private String yearlyProduction;
	
	private List<ProductionSummary> productionSummaryByMonth;
	private List<SalesSummary> salesSummaryByMonth;
	private List<ProductionSummaryByYear> productionSummaryByYear;
	private List<SalesSummaryByYear> salesSummaryByYear;
	
	public List<ProductionSummaryByYear> getProductionSummaryByYear() {
		return productionSummaryByYear;
	}
	public void setProductionSummaryByYear(List<ProductionSummaryByYear> productionSummaryByYear) {
		this.productionSummaryByYear = productionSummaryByYear;
	}
	public List<SalesSummaryByYear> getSalesSummaryByYear() {
		return salesSummaryByYear;
	}
	public void setSalesSummaryByYear(List<SalesSummaryByYear> salesSummaryByYear) {
		this.salesSummaryByYear = salesSummaryByYear;
	}
	public List<ProductionSummary> getProductionSummaryByMonth() {
		return productionSummaryByMonth;
	}
	public void setProductionSummaryByMonth(List<ProductionSummary> productionSummaryByMonth) {
		this.productionSummaryByMonth = productionSummaryByMonth;
	}
	public List<SalesSummary> getSalesSummaryByMonth() {
		return salesSummaryByMonth;
	}
	public void setSalesSummaryByMonth(List<SalesSummary> salesSummaryByMonth) {
		this.salesSummaryByMonth = salesSummaryByMonth;
	}
	public String getMonthlySales() {
		return monthlySales;
	}
	public void setMonthlySales(String monthlySales) {
		this.monthlySales = monthlySales;
	}
	public String getQuarterlySales() {
		return quarterlySales;
	}
	public void setQuarterlySales(String quarterlySales) {
		this.quarterlySales = quarterlySales;
	}
	public String getYearlySales() {
		return yearlySales;
	}
	public void setYearlySales(String yearlySales) {
		this.yearlySales = yearlySales;
	}
	public String getMonthlyProduction() {
		return monthlyProduction;
	}
	public void setMonthlyProduction(String monthlyProduction) {
		this.monthlyProduction = monthlyProduction;
	}
	public String getQuarterlyProduction() {
		return quarterlyProduction;
	}
	public void setQuarterlyProduction(String quarterlyProduction) {
		this.quarterlyProduction = quarterlyProduction;
	}
	public String getYearlyProduction() {
		return yearlyProduction;
	}
	public void setYearlyProduction(String yearlyProduction) {
		this.yearlyProduction = yearlyProduction;
	}
	
}
