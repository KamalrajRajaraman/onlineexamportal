package com.vastpro.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

import groovy.xml.Entity;

public class QuestionMasterServices {

	public static final String module = QuestionMasterServices.class.getName();

	/**
	 * This method is used to retrieve all the Questions from QuestionMaster entity
	 * @param DispatchContext 
	 * @param Map<String, ? extends Object>
	 * @return Map<String,Object>	
	 */
	
	public static Map<String, Object> findAllQuestions(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> serviceResultMap = ServiceUtil.returnSuccess();
		List<Map<String, Object>> questionList = new LinkedList<>();

		Delegator delegator = dctx.getDelegator();
		List<GenericValue> questionGenericValueList = null;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		
		EntityCondition condition1 = EntityCondition.makeCondition(CommonConstants.EXPIRATION_DATE, EntityOperator.EQUALS, null);
		EntityCondition condition2 = EntityCondition.makeCondition(CommonConstants.EXPIRATION_DATE, EntityOperator.GREATER_THAN, currentTime );
		EntityCondition condition = EntityCondition.makeCondition(condition1, EntityOperator.OR, condition2);
		try {
			questionGenericValueList = EntityQuery.use(delegator).from(CommonConstants.QUESTION_MASTER).where(condition).queryList();
		} 
		catch (GenericEntityException e) {
			//If Exception occurred return error map
			String errMsg = "Error in fetching record from QuestionMaster entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}

		if (UtilValidate.isEmpty(questionGenericValueList)) {
			String errMsg = "Retreived question List from Question Master entity is null or empty";
			Debug.logError(errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		
		if(UtilValidate.isNotEmpty(questionGenericValueList)) {
			for (GenericValue questionGenericValue : questionGenericValueList) {
				Map<String, Object> question = getQuestionFromGenericValue(questionGenericValue);
				questionList.add(question);
			}
		}
		serviceResultMap.put("questionList", questionList);
		return serviceResultMap;

	}

	/**
	 * This method is used to retrieve a question based on questionId from QuestionMaster entity
	 * @param DispatchContext 
	 * @param Map<String, ? extends Object>
	 * @return Map<String,Object>	
	 */
	public static Map<String, Object> findQuestion(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> serviceResultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		Long questionId = (Long) context.get(CommonConstants.QUESTION_ID);

		GenericValue questionMasterGV = null;
		try {
			//Execute the query to find question
			questionMasterGV = EntityQuery.use(delegator).from("QuestionMaster")
					.where(CommonConstants.QUESTION_ID, questionId).queryOne();

		} catch (GenericEntityException e) {
			//If Exception occurred return error map
			String errMsg = "Error in fetching record from QuestionMaster entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}

		if (UtilValidate.isEmpty(questionMasterGV)) {
			//If Exception occurred return error map
			String errMsg = "Retrieved Question from QuestionMaster entity is null or empty";
			Debug.logError(errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		Map<String, Object> questionMap = getQuestionFromGenericValue(questionMasterGV);
		serviceResultMap.put("question", questionMap);

		return serviceResultMap;

	}

	
	/**
	 * This helper method is used to convert Question GV to Map
	 * @param GenericValue 
	 * @return Map<String,Object>	
	 */
	public static Map<String, Object> getQuestionFromGenericValue(GenericValue genericValue) {
		//Extract question fields from genericValue object 
		Long questionId = genericValue.getLong(CommonConstants.QUESTION_ID);
		String questionDetail = genericValue.getString(CommonConstants.QUESTION_DETAIL);
		String optionA = genericValue.getString(CommonConstants.OPTION_A);
		String optionB = genericValue.getString(CommonConstants.OPTION_B);
		String optionC = genericValue.getString(CommonConstants.OPTION_C);
		String optionD = genericValue.getString(CommonConstants.OPTION_D);
		String optionE = genericValue.getString(CommonConstants.OPTION_E);
		String answer = genericValue.getString(CommonConstants.ANSWER);
		Long numAnswers = genericValue.getLong(CommonConstants.NUM_ANSWERS);
		String questionType = genericValue.getString(CommonConstants.QUESTION_TYPE);
		Integer difficultyLevel = genericValue.getInteger(CommonConstants.DIFFICULTY_LEVEL);
		BigDecimal answerValue = genericValue.getBigDecimal(CommonConstants.ANSWER_VALUE);
		String topicId = genericValue.getString(CommonConstants.TOPIC_ID);
		BigDecimal negativeMarkValue = genericValue.getBigDecimal(CommonConstants.NEGATIVE_MARK_VALUE);

		//Construct a map with the fields
		Map<String, Object> question = new HashMap<>();
		question.put(CommonConstants.QUESTION_ID, questionId);
		question.put(CommonConstants.QUESTION_DETAIL, questionDetail);
		question.put(CommonConstants.OPTION_A, optionA);
		question.put(CommonConstants.OPTION_B, optionB);
		question.put(CommonConstants.OPTION_C, optionC);
		question.put(CommonConstants.OPTION_D, optionD);
		question.put(CommonConstants.OPTION_E, optionE);
		question.put(CommonConstants.ANSWER, answer);
		question.put(CommonConstants.NUM_ANSWERS, numAnswers);
		question.put(CommonConstants.QUESTION_TYPE, questionType);
		question.put(CommonConstants.DIFFICULTY_LEVEL, difficultyLevel);
		question.put(CommonConstants.ANSWER_VALUE, answerValue);
		question.put(CommonConstants.TOPIC_ID, topicId);
		question.put(CommonConstants.NEGATIVE_MARK_VALUE, negativeMarkValue);
		
		return question;
	}

}
