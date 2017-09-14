package com.india.tamilnadu.util;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserHandler {
	
	List<String> keys = new ArrayList<>();
	List<String> values1 = new ArrayList<>();
	List<String> values2 = new ArrayList<>();
 	
	public TallyBean parseXml(String xmlData) {

		TallyBean tallyBean = new TallyBean();
	  
		try {
	    	   
	         SAXParserFactory factory = SAXParserFactory.newInstance();
	         SAXParser saxParser = factory.newSAXParser();
	         SaxParserHandler userhandler = new SaxParserHandler();
	         saxParser.parse(new StringBufferInputStream(xmlData), defaultHandler);   
	         
	         tallyBean.setKeys(keys);
	         tallyBean.setValues1(values1);
	         tallyBean.setValues2(values2);
	         
	         
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	      
        return tallyBean;
	}
	
	
	DefaultHandler defaultHandler = new DefaultHandler(){

	   boolean DSPDISPNAME = false;
	   boolean DSPCLDRAMTA = false;
	   boolean DSPCLCRAMTA = false;
	   
	  
	   @Override
	   public void startElement(
	      String uri, String localName, String qName, Attributes attributes)
	      throws SAXException {
	      
	      if (qName.equalsIgnoreCase("DSPDISPNAME")) {
	    	  DSPDISPNAME = true;
	      } else if (qName.equalsIgnoreCase("DSPCLDRAMTA")) {
	    	  DSPCLDRAMTA = true;
	      } else if (qName.equalsIgnoreCase("DSPCLCRAMTA")) {
	    	  DSPCLCRAMTA = true;
	      } 
	   }
	
	   @Override
	   public void endElement(String uri, 
	      String localName, String qName) throws SAXException {
	      
	      if (qName.equalsIgnoreCase("ENVELOPE")) {
	         System.out.println("End Element :" + qName);
	      }
	   }
	
	   @Override
	   public void characters(char ch[], int start, int length) throws SAXException {
	
	      if (DSPDISPNAME) {
	         System.out.println("DSPDISPNAME: " + new String(ch, start, length));
	         keys.add(new String(ch, start, length));
	         DSPDISPNAME = false;
	      } else if (DSPCLDRAMTA) {
		      System.out.println("DSPCLDRAMTA: " + new String(ch, start, length));
		      values1.add(new String(ch, start, length));
		      DSPCLDRAMTA = false;
	      } else if (DSPCLCRAMTA) {
	    	  System.out.println("DSPCLCRAMTA: " + new String(ch, start, length));
	    	  values2.add(new String(ch, start, length));
	    	  DSPCLCRAMTA = false;
	      } 
	   }
   
	};
}
