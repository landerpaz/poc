package com.india.tamilnadu.vo;

import java.util.List;

public class UserManager {

	private List<User> users;
	private List<Role> roles;
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
