package com.vastpro.validator;

import org.hibernate.validator.constraints.NotEmpty;

public class TopicMasterValidator {
	
	@NotEmpty(message="TopicName is empty", groups= {TopicMasterCheck.class})
	private  String topicName;

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	
	
	
	
}
