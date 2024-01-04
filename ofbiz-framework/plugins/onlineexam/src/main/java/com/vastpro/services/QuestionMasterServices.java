package com.vastpro.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class QuestionMasterServices {

	public static final String module = QuestionMasterServices.class.getName();

	public static Map<String, Object> findAllQuestions(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();
		List<Map<String, Object>>questionList = new LinkedList<>();

		Delegator delegator = dctx.getDelegator();
		List<GenericValue> questionGenericValueList = null;
		try {
			questionGenericValueList = EntityQuery.use(delegator).from(CommonConstants.QUESTION_MASTER).queryList();
		} 
		catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from QuestionMaster entity ........" + module);
		}
		
		
		for (GenericValue questionGenericValue : questionGenericValueList) {
			Map<String, Object> question = getQuestionFromGenericValue(questionGenericValue);			
			questionList.add( question);
		}
		returnSucces.put("questionList", questionList);
		return returnSucces;

	}

	public static Map<String, Object> findQuestion(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> serviceResultMap = ServiceUtil.returnSuccess();	
		Delegator delegator = dctx.getDelegator();
		
		Long questionId = (Long) context.get(CommonConstants.QUESTION_ID);
		
		GenericValue questionMasterGV = null;
		try {			
			questionMasterGV = EntityQuery.use(delegator)
									.from("QuestionMaster")
									.where(CommonConstants.QUESTION_ID,questionId)
									.queryOne();
		
		} 
		catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from QuestionMaster entity ........" + module);
		}

		if(UtilValidate.isNotEmpty(questionMasterGV)) {		
			Map<String, Object> questionMap = getQuestionFromGenericValue(questionMasterGV);
			serviceResultMap.put("question", questionMap);
		}
		else {
			serviceResultMap = ServiceUtil.returnError("Failed fetching question from DB");
		}
		
		
		return serviceResultMap;

	}
	
	//Helper Method 
	public static Map<String, Object> getQuestionFromGenericValue(GenericValue genericValue){	
		Long questionId =  genericValue.getLong(CommonConstants.QUESTION_ID);
		String questionDetail = genericValue.getString(CommonConstants.QUESTION_DETAIL);
		String optionA =genericValue.getString(CommonConstants.OPTION_A);
		String optionB =genericValue.getString(CommonConstants.OPTION_B);
		String optionC = genericValue.getString(CommonConstants.OPTION_C);
		String optionD = genericValue.getString(CommonConstants.OPTION_D);
		String optionE = genericValue.getString(CommonConstants.OPTION_E);
		String answer = genericValue.getString(CommonConstants.ANSWER);
		Long numAnswers = genericValue.getLong(CommonConstants.NUM_ANSWERS);
		String questionType =genericValue.getString(CommonConstants.QUESTION_TYPE);
		Integer difficultyLevel =genericValue.getInteger(CommonConstants.DIFFICULTY_LEVEL);
		BigDecimal answerValue =genericValue.getBigDecimal(CommonConstants.ANSWER_VALUE);
		String topicId = genericValue.getString(CommonConstants.TOPIC_ID);
		BigDecimal negativeMarkValue = genericValue.getBigDecimal(CommonConstants.NEGATIVE_MARK_VALUE);

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
