package com.india.tamilnadu.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.india.tamilnadu.dao.AuthenticationDAO;
import com.india.tamilnadu.tally.vo.InventoryEntryVO;

public class Utility {

	public static String getCurrentQuarter() {
		
		int quarter = (Calendar.getInstance().get(Calendar.MONTH) / 3); // 0 to 3
		String[] mQuarterKey = {"('January', 'Febrarury', 'March')", "('April', 'May', 'June')", "('July', 'August', 'September')", "('October', 'November', 'December')"};
		return mQuarterKey[quarter];
	}
	
	public static String getCurrentMonth() {
		
		String[] months = { "January", "Febrarury", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH);
		
		return months[month];
		
	}

	public static Date getCurrentdate() {
		
		Date date = new java.sql.Date(new java.util.Date().getTime());
		
		return date;
		
	}
	
	public static String getCurrentFinancialYear() {
		
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		
		if (now.get(Calendar.MONTH) >= 4 && now.get(Calendar.MONTH) <= 12) {
			return Integer.toString(now.get(Calendar.YEAR));
		} else {
			return Integer.toString(now.get(Calendar.YEAR) - 1);
		}
		
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
		//System.out.println(removeDecimal("121231.00"));
		//System.out.println(Math.abs(-1232131.99));
		//System.out.println(Long.toString(Math.round(Math.abs(Double.parseDouble("-23123.99")))));
		//System.out.println(getCurrentQuarter());
		
		/*String password = "Spak#007";
		String salt = "spak3"; //user name 
		String pwd1 = new String(hashPassword(password.toCharArray(), salt.getBytes(), 2, 256));
		String pwd2 = new String(hashPassword(password.toCharArray(), salt.getBytes(), 2, 256));
		System.out.println(new String(hashPassword(password.toCharArray(), salt.getBytes(), 2, 256)));
		//System.out.println(pwd1.equals(pwd2));
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		
		try {
			authenticationDAO.insertPwd(salt, new String(hashPassword(password.toCharArray(), salt.getBytes(), 2, 256)));
			//authenticationDAO.updatePwd(new String(hashPassword(password.toCharArray(), salt.getBytes(), 2, 256)), salt);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
		
		
		Double price = 5.10234234324;
		DecimalFormat format = new DecimalFormat("0.00");
		System.out.println(format.format(price));
		
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
	
	public static int removeDecimal(String qty) {
		
		System.out.println("qty : " + qty);
		
		try {
			if(null != qty && qty.trim().length() > 1 && qty.contains(".")) {
				
				String temps[] = qty.split(".00");
				
				System.out.println(temps.length);
				
				String temp = (qty.split(".00"))[0];
				
				return Integer.parseInt(temp);
			} 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return 0;
	}

	public static String removeDecimalAndMinus(String qty) {
		
		//System.out.println("qty : " + qty);
		
		try {
			if(null != qty && qty.trim().length() > 1 && qty.contains(".00")) {
				
				String temp = (qty.split(".00"))[0];
				
				return Integer.toString(Math.abs(Integer.parseInt(temp)));
			} 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return Long.toString(Math.round(Math.abs(Double.parseDouble(qty))));
	}
	
	public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
		 
       try {
           SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
           PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
           SecretKey key = skf.generateSecret( spec );
           byte[] res = key.getEncoded( );
           return res;
 
       } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
           throw new RuntimeException( e );
       }
   }
	
   public static String zeroTruncating(Double value) {
	   String result = null;
	   try {
		   DecimalFormat format = new DecimalFormat("0.##");
		   result = format.format(value);
	   } catch (Exception e) {
		// TODO: handle exception
	   }
	   return result;
   }
   
   public static String roundToTwoDecimal(Double value) {
	   String result = null;
	   try {
		   DecimalFormat format = new DecimalFormat("0.00");
		   result = format.format(value);
	   } catch (Exception e) {
		// TODO: handle exception
	   }
	   return result;
   }
   
   public static String getBatchNumber() {
	   DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	   java.util.Date date = new java.util.Date();
	   return dateFormat.format(date);
   }
   
   public static String getReel(String size, double weight) {
		String reel =  null;
		
		if (null != size) {
			
			double sizeValue = Double.parseDouble(size);
			//double weightValue = Double.parseDouble(weight);
			
			double temp = (weight * 1000) / (sizeValue * 10);
			//System.out.println("temp : " + temp);
			int valueX = (int) Math.round(temp);
			reel = Integer.toString(valueX);
		}
		
		return reel;
		
	}
   
   public static String calculateDispatchReal(double real, double realInStock) {
		
	return Double.toString(real - realInStock);	
	
		
	}
}
