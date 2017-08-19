package com.india.tamilnadu.jaxrs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Supplier")
public class Supplier {

	private String supplierID;
	private String name;
	private String phone;
	
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
