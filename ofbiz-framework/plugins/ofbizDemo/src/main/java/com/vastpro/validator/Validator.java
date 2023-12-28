package com.vastpro.validator;


import org.hibernate.validator.constraints.NotEmpty;

public class Validator {
	
	@NotEmpty(message ="Username is empty",groups= {Loggable.class})
	@javax.validation.constraints.Pattern(regexp = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$",message = "Invalid Email Format",groups = {Loggable.class})
	private String username;
	
	@NotEmpty(message="Password is empty",groups= {Loggable.class})
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
