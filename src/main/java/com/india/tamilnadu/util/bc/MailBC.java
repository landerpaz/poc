package com.india.tamilnadu.util.bc;

import java.util.List;

import com.india.tamilnadu.tally.vo.Result;
import com.india.tamilnadu.util.MailUtil;

public class MailBC {
	
	public void senMail(String trackingId, String fileName, String status, String to, List<Result> results) throws Exception {
		MailUtil mailUtil = new MailUtil();
		mailUtil.sendMail(fileName, to, status, results);
	}
}
