package com.india.tamilnadu.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.india.tamilnadu.tally.vo.InventoryEntryVO;

public class Utility {

	public static Date getCurrentdate() {
		
		Date date = new java.sql.Date(new java.util.Date().getTime());
		
		return date;
		
	}
	
	public static String getRandomNumber() {
		
		return UUID.randomUUID().toString();
		
	}
	
	public static void formatInventory(InventoryEntryVO inventoryEntryVO) {
		
		if(null != inventoryEntryVO) {
			if(null != inventoryEntryVO.getRate() && inventoryEntryVO.getRate().contains("/Ton")) {
				inventoryEntryVO.setRate(inventoryEntryVO.getRate().replace("/Ton", ""));
			} 
			
			if(null != inventoryEntryVO.getBilledQuantity() && inventoryEntryVO.getBilledQuantity().contains("=")) {
				inventoryEntryVO.setBilledQuantity((inventoryEntryVO.getBilledQuantity().split("="))[0]);
			}
		}
		
	}
	
	/*public static String formatQty(String qty) {
		
		if(null != qty && qty.contains("=")) {
			return (qty.split("="))[0].trim();
		} 
		
		return "";
	}*/
	
	public static double formatQty(String qty) {
		
		if(null != qty && qty.contains("Kgs")) {
			return Double.parseDouble((qty.split("Kgs"))[0].trim());
		} 
		
		return 0;
	}

	public static double formatRate(String rate) {
		
		if(null != rate && rate.contains("/Kgs")) {
			return Double.parseDouble(rate.trim().replace("/Kgs", ""));
		} 
		
		return 0;
	}

	public static void main(String[] a) {
		//System.out.println(formatQty("392.000 Kgs = 0.3920 Ton"));
		System.out.println(convertStringToDate(""));
	}
	
	public static Date convertStringToDate(String inputDate) {
		
	   //inputDate="2014-12-31";
	   SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	   java.util.Date date = null;
	   
		try {
			date = sdf1.parse(inputDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Date(date.getTime()); 
		   
	}

}
