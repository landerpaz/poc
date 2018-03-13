package com.india.tamilnadu.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.india.tamilnadu.tally.vo.Result;

public class MailUtil {
	
	public static void main(String a[]) {
		MailUtil mailUtil = new MailUtil();
		List<Result> results = new ArrayList<>();
		/*Result result = new Result();
		result.setVoucherType("Stock");
		result.setVoucherKey("12345");
		result.setStatus("SUCCESS");
		results.add(result);
		result = new Result();
		result.setVoucherType("Day");
		result.setVoucherKey("671524");
		result.setStatus("FAILED");
		results.add(result);*/
		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(results));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mailUtil.sendMail("File", "ashok.arulsamy@gmail.com", "Success", results);
	}

	//public void sendMail(int caseId,String subject, String adminMailId, String cc, String body) {
	public void sendMail(String fileName, String to, String status, List<Result> results) {
		
		final String username = "bizdaz.notification@gmail.com";
		final String password = "bizdaz007";
		String from = "bizdaz.notification@gmail.com";
		//String to = "ashok.arulsamy@gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(new StringBuilder(fileName).append(" - ").append(status).toString());
			//message.setText("");
			message.setContent(Utility.objectToString(results), "text/html; charset=utf-8");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
