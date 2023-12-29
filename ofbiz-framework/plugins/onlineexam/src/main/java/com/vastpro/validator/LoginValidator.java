package com.vastpro.validator;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginValidator {
	
	@NotEmpty(message = "USERNAME is Empty",groups = {Loggable.class})
	@Pattern(regexp ="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",message = "Invalid USERNAME format",groups = {Loggable.class})
	private String USERNAME;
	
	@NotEmpty(message ="PASSWORD is Empty",groups = {Loggable.class})
	@Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",message = "Invalid PASSWORD format",groups = {Loggable.class})
	private String PASSWORD;

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String USERNAME) {
		this.USERNAME = USERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String PASSWORD) {
		this.PASSWORD = PASSWORD;
	}
	
	

}
