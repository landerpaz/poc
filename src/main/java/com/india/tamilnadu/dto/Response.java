package com.india.tamilnadu.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response {

	private String status;
	private String statusMessage;
	private String role;
	private String token;
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
