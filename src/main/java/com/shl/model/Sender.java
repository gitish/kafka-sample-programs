package com.shl.model;

public class Sender {
	String name;
	//set by configuration
	String email;
	//set by configuration
	String pass;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return "sshail@sapient.com";
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPass() {
		return "Nove2016";
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
}
