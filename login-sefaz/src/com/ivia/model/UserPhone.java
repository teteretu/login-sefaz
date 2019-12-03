package com.ivia.model;

import java.io.Serializable;

public class UserPhone implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phoneNumber;
	private String userEmail;
	private int ddd;
	private String tpPhone;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public int getDdd() {
		return ddd;
	}
	public void setDdd(int ddd) {
		this.ddd = ddd;
	}
	public String getTpPhone() {
		return tpPhone;
	}
	public void setTpPhone(String tpPhone) {
		this.tpPhone = tpPhone;
	}

}