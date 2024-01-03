package com.vastpro.validator;

import org.hibernate.validator.constraints.NotEmpty;
/**
 * ExamTopicMappingValidator is used in Hibernate vaidation as bean class
 * @author KAMALRAJ
 *
 */
public class ExamTopicMappingValidator {
	
	@NotEmpty(message = "examId is empty",groups = {ExamTopicMappingCheck.class})
	private String examId;
	
	@NotEmpty(message = "topicId is empty",groups = {ExamTopicMappingCheck.class})
	private String topicId;
	
	@NotEmpty(message = "percentage is empty",groups = {ExamTopicMappingCheck.class})
	private String percentage;
	
	@NotEmpty(message = "topicPassPercentage is empty",groups = {ExamTopicMappingCheck.class})
	private String topicPassPercentage;
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getTopicPassPercentage() {
		return topicPassPercentage;
	}
	public void setTopicPassPercentage(String topicPassPercentage) {
		this.topicPassPercentage = topicPassPercentage;
	}
	
	

}
