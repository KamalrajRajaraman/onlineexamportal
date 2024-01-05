package com.vastpro.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class UserAttemptMasterValidator {
	
	@NotEmpty(message = "examId is missing",groups = {UserAttemptAnswerMasterCheck.class})
	private String examId;
	
	@NotEmpty(message = "partyId is missing",groups = {UserAttemptAnswerMasterCheck.class})
	private String partyId;
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	

}
