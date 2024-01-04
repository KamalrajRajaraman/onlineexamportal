package com.vastpro.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class UserExamMappingValidator {
	
	@NotEmpty(message="Party Id is missing", groups= {CreateUserExamMappingCheck.class,FindUserExamMappingCheck.class})
	private String partyId;
	
	@NotEmpty(message="examId is missing", groups= {CreateUserExamMappingCheck.class})
	private String examId;
	
	@NotEmpty(message="allowedAttempts is missing", groups= {CreateUserExamMappingCheck.class})
	private String allowedAttempts;
	
	@NotEmpty(message="noOfAttempts  is missing", groups= {CreateUserExamMappingCheck.class})
	private String noOfAttempts;
	
	@NotEmpty(message="lastPerformanceDate is missing", groups= {CreateUserExamMappingCheck.class})
	private String lastPerformanceDate;
	
	@NotEmpty(message="timeoutDays is missing", groups= {CreateUserExamMappingCheck.class})
	private String timeoutDays;
	
	@NotEmpty(message="passwordChangesAuto is missing", groups= {CreateUserExamMappingCheck.class})
	private String passwordChangesAuto;
	
	@NotEmpty(message="canSplitExams is missing", groups= {CreateUserExamMappingCheck.class})
	private String canSplitExams;
	
	@NotEmpty(message="canSeeDetailedResults  is missing", groups= {CreateUserExamMappingCheck.class})
	private String canSeeDetailedResults;
	
	@NotEmpty(message="maxSplitAttempts is missing", groups= {CreateUserExamMappingCheck.class})
	private String maxSplitAttempts;

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getAllowedAttempts() {
		return allowedAttempts;
	}

	public void setAllowedAttempts(String allowedAttempts) {
		this.allowedAttempts = allowedAttempts;
	}

	public String getNoOfAttempts() {
		return noOfAttempts;
	}

	public void setNoOfAttempts(String noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}

	public String getLastPerformanceDate() {
		return lastPerformanceDate;
	}

	public void setLastPerformanceDate(String lastPerformanceDate) {
		this.lastPerformanceDate = lastPerformanceDate;
	}

	public String getTimeoutDays() {
		return timeoutDays;
	}

	public void setTimeoutDays(String timeoutDays) {
		this.timeoutDays = timeoutDays;
	}

	public String getPasswordChangesAuto() {
		return passwordChangesAuto;
	}

	public void setPasswordChangesAuto(String passwordChangesAuto) {
		this.passwordChangesAuto = passwordChangesAuto;
	}

	public String getCanSplitExams() {
		return canSplitExams;
	}

	public void setCanSplitExams(String canSplitExams) {
		this.canSplitExams = canSplitExams;
	}

	public String getCanSeeDetailedResults() {
		return canSeeDetailedResults;
	}

	public void setCanSeeDetailedResults(String canSeeDetailedResults) {
		this.canSeeDetailedResults = canSeeDetailedResults;
	}

	public String getMaxSplitAttempts() {
		return maxSplitAttempts;
	}

	public void setMaxSplitAttempts(String maxSplitAttempts) {
		this.maxSplitAttempts = maxSplitAttempts;
	}
	
}
