package com.vastpro.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class ExamMasterValidator {
	
	

	@NotEmpty(message = "Exam Name is unavailable", groups = { ExamMasterCheck.class })
	private String examName;

	@NotEmpty(message = "Description is unavailable", groups = { ExamMasterCheck.class })
	private String description;

	@NotEmpty(message = "Creation Date is unavailable", groups = { ExamMasterCheck.class })
	private String creationDate;

	@NotEmpty(message = "Expiration Date is unavailable", groups = { ExamMasterCheck.class })
	private String expirationDate;

	@NotEmpty(message = "Number of Questions is unavailable", groups = { ExamMasterCheck.class })
	private String noOfQuestions;

	@NotEmpty(message = "Duration is unavailable", groups = { ExamMasterCheck.class })
	private String durationMinutes;

	@NotEmpty(message = "Pass Percentage is unavailable", groups = { ExamMasterCheck.class })
	private String passPercentage;

	@NotEmpty(message = "Questions Randomized is unavailable", groups = { ExamMasterCheck.class })
	private String questionsRandomized;

	@NotEmpty(message = "Answers must is unavailable", groups = { ExamMasterCheck.class })
	private String answersMust;

	@NotEmpty(message = "Enable negative mark is unavailable", groups = { ExamMasterCheck.class })
	private String enableNegativeMark;

	@NotEmpty(message = "Negatve mark value is unavailable", groups = { ExamMasterCheck.class })
	private String negativeMarkValue;



	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getNoOfQuestions() {
		return noOfQuestions;
	}

	public void setNoOfQuestions(String noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public String getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(String durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getPassPercentage() {
		return passPercentage;
	}

	public void setPassPercentage(String passPercentage) {
		this.passPercentage = passPercentage;
	}

	public String getQuestionsRandomized() {
		return questionsRandomized;
	}

	public void setQuestionsRandomized(String questionsRandomized) {
		this.questionsRandomized = questionsRandomized;
	}

	public String getAnswersMust() {
		return answersMust;
	}

	public void setAnswersMust(String answersMust) {
		this.answersMust = answersMust;
	}

	public String getEnableNegativeMark() {
		return enableNegativeMark;
	}

	public void setEnableNegativeMark(String enableNegativeMark) {
		this.enableNegativeMark = enableNegativeMark;
	}

	public String getNegativeMarkValue() {
		return negativeMarkValue;
	}

	public void setNegativeMarkValue(String negativeMarkValue) {
		this.negativeMarkValue = negativeMarkValue;
	}

}
