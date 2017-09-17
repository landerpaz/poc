package com.india.tamilnadu.util;


import java.io.File;
import java.io.StringBufferInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParser {
	
	public static void main(String a[]) {
		SaxParserHandlerTest saxParser = new SaxParserHandlerTest();
		TallyBean tallyBean = saxParser.parseXml();
		System.out.println("key : " + tallyBean.getKeys());
		//System.out.println("value : " + tallyBean.getValues());
	}

	/*public void parseXml() {

	      try {
	    	 
	    	  String xmlData = "<?xml version = \"1.0\"?><class><student rollno = \"393\"><firstname>dinkar</firstname><lastname>kad</lastname><nickname>dinkar</nickname><marks>85</marks></student></class>";
	    	  
	    	   <student rollno = "393">
	    	      <firstname>dinkar</firstname>
	    	      <lastname>kad</lastname>
	    	      <nickname>dinkar</nickname>
	    	      <marks>85</marks>
	    	   </student>
	    	   
	    	   <student rollno = "493">
	    	      <firstname>Vaneet</firstname>
	    	      <lastname>Gupta</lastname>
	    	      <nickname>vinni</nickname>
	    	      <marks>95</marks>
	    	   </student>
	    	   
	    	   <student rollno = "593">
	    	      <firstname>jasvir</firstname>
	    	      <lastname>singn</lastname>
	    	      <nickname>jazz</nickname>
	    	      <marks>90</marks>
	    	   </student>
	    	   
	         SAXParserFactory factory = SAXParserFactory.newInstance();
	         SAXParser saxParser = factory.newSAXParser();
	         SaxParserHandler userhandler = new SaxParserHandler();
	         saxParser.parse(new StringBufferInputStream(xmlData), userhandler);     
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }*/
}
