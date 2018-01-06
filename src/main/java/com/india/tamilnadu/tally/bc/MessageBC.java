package com.india.tamilnadu.tally.bc;

import java.util.List;

import com.india.tamilnadu.dao.AuthenticationDAO;
import com.india.tamilnadu.dao.TallyDAO;
import com.india.tamilnadu.vo.Message;
import com.india.tamilnadu.vo.User;

public class MessageBC {

	public void addMessage(String trackingId, Message message) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		tallyDAO.addMessage(message);
	}
	
	public List<Message> getMessage(String companyId, String trackingId) throws Exception {
		
		TallyDAO tallyDAO = new TallyDAO();
		return tallyDAO.getMessage(companyId);
	}
}
