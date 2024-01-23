package com.vastpro.validator;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class RegisterValidator {

	@NotEmpty(message="firstName is empty", groups= {RegisterCheck.class})
	private String firstName;
	
	@NotEmpty(message="lastName is empty", groups= {RegisterCheck.class})
	private String lastName;
	
	@Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$", message = "Invalid userId format", groups= {RegisterCheck.class})
	@NotEmpty(message="userLoginId is empty", groups= {RegisterCheck.class})
	private String userLoginId;
	
	@Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",message = "Invalid password format",groups = {RegisterCheck.class})
	@NotEmpty(message="currentPassword is empty", groups= {RegisterCheck.class})
	private String currentPassword;
	
	
	@NotEmpty(message="currentPasswordVerify is empty", groups= {RegisterCheck.class})
	private String currentPasswordVerify;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getCurrentPasswordVerify() {
		return currentPasswordVerify;
	}

	public void setCurrentPasswordVerify(String currentPasswordVerify) {
		this.currentPasswordVerify = currentPasswordVerify;
	}
	
}
