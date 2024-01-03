package com.vastpro.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class QuestionMasterValidator {
	@NotEmpty(message = "Question Detail is Empty",groups = {QuestionMasterCheck.class})
	private String questionDetail;
	
	@NotEmpty(message = "option A is Empty",groups = {QuestionMasterCheck.class})
	private String optionA;
	
	@NotEmpty(message = "option B is Empty",groups = {QuestionMasterCheck.class})
	private String optionB;
	
	@NotEmpty(message = "option C is Empty",groups = {QuestionMasterCheck.class})
	private String optionC;
	
	@NotEmpty(message = "option D is Empty",groups = {QuestionMasterCheck.class})
	private String optionD;
	
	@NotEmpty(message = "option E is Empty",groups = {QuestionMasterCheck.class})
	private String optionE;
	
	@NotEmpty(message = "Answer is Empty",groups = {QuestionMasterCheck.class})
	private String answer;
	
	@NotEmpty(message = "Num answer is Empty",groups = {QuestionMasterCheck.class})
	private String numAnswers;
	
	@NotEmpty(message = "Question type is Empty",groups = {QuestionMasterCheck.class})
	private String questionType;
	
	@NotEmpty(message = "Difficulty level is Empty",groups = {QuestionMasterCheck.class})
	private String difficultyLevel;
	
	@NotEmpty(message = "Answer value is Empty",groups = {QuestionMasterCheck.class})
	private String answerValue;
	
	@NotEmpty(message = "Topic Id is Empty",groups = {QuestionMasterCheck.class})
	private String topicId;
	
	@NotEmpty(message = "Negative mark value is Empty",groups = {QuestionMasterCheck.class})
	private String negativeMarkValue;

	public String getQuestionDetail() {
		return questionDetail;
	}

	public void setQuestionDetail(String questionDetail) {
		this.questionDetail = questionDetail;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public String getOptionE() {
		return optionE;
	}

	public void setOptionE(String optionE) {
		this.optionE = optionE;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getNumAnswers() {
		return numAnswers;
	}

	public void setNumAnswers(String numAnswers) {
		this.numAnswers = numAnswers;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getNegativeMarkValue() {
		return negativeMarkValue;
	}

	public void setNegativeMarkValue(String negativeMarkValue) {
		this.negativeMarkValue = negativeMarkValue;
	}
	
	
}
