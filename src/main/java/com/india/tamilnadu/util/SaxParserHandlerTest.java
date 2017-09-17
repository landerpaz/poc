package com.india.tamilnadu.util;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SaxParserHandlerTest {
	
	List<String> keys = new ArrayList<>();
	List<String> values = new ArrayList<>();
 	
	public TallyBean parseXml() {

		TallyBean tallyBean = new TallyBean();
   	 
  	  
	      try {
	    	  
	    	  String xmlData = "<class><student rollno = \"393\"><firstname>dinkar</firstname><lastname>kad</lastname><nickname>dinkar</nickname><marks>85</marks></student>"
	    			  + "<student rollno = \"394\"><firstname>dinkar1</firstname><lastname>kad1</lastname><nickname>dinkar1</nickname><marks>85</marks></student>"
	    			  + "<student rollno = \"391\"><firstname>dinkar0</firstname><lastname>kad0</lastname><nickname>dinkar</nickname><marks>85</marks></student></class>";
	    	  
	    	  
	    	  
	    	   /*<student rollno = "393">
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
	    	   </student>*/
	    	   
	         SAXParserFactory factory = SAXParserFactory.newInstance();
	         SAXParser saxParser = factory.newSAXParser();
	         SaxParserHandlerTest userhandler = new SaxParserHandlerTest();
	         saxParser.parse(new StringBufferInputStream(xmlData), defaultHandler);   
	         
	         tallyBean.setKeys(keys);
	         //tallyBean.setValues(values);
	         
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      
	      return tallyBean;
	   }
	
	
	DefaultHandler defaultHandler = new DefaultHandler(){


   boolean bFirstName = false;
   boolean bLastName = false;
   boolean bNickName = false;
   boolean bMarks = false;

   @Override
   public void startElement(
      String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
      
      if (qName.equalsIgnoreCase("student")) {
         String rollNo = attributes.getValue("rollno");
         System.out.println("Roll No : " + rollNo);
      } else if (qName.equalsIgnoreCase("firstname")) {
         bFirstName = true;
      } else if (qName.equalsIgnoreCase("lastname")) {
         bLastName = true;
      } else if (qName.equalsIgnoreCase("nickname")) {
         bNickName = true;
      }
      else if (qName.equalsIgnoreCase("marks")) {
         bMarks = true;
      }
   }

   @Override
   public void endElement(String uri, 
      String localName, String qName) throws SAXException {
      
      if (qName.equalsIgnoreCase("student")) {
         System.out.println("End Element :" + qName);
      }
   }

   @Override
   public void characters(char ch[], int start, int length) throws SAXException {

      if (bFirstName) {
         System.out.println("First Name: " + new String(ch, start, length));
         keys.add(new String(ch, start, length));
         bFirstName = false;
      } else if (bLastName) {
         System.out.println("Last Name: " + new String(ch, start, length));
         values.add(new String(ch, start, length));
         bLastName = false;
      } else if (bNickName) {
         System.out.println("Nick Name: " + new String(ch, start, length));
         bNickName = false;
      } else if (bMarks) {
         System.out.println("Marks: " + new String(ch, start, length));
         bMarks = false;
      }
   }
   
	};
}
