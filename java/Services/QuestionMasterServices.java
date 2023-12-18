package com.vastpro.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class QuestionMasterServices {

	public static final String module = QuestionMasterServices.class.getName();

	public static Map<String, Object> findAllQuestions(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();
		List<Map<String, Object>>questionList = new LinkedList<>();

		Delegator delegator = dctx.getDelegator();
		List<GenericValue> questionGenericValueList = null;
		try {
			questionGenericValueList = EntityQuery.use(delegator).from(CommonConstant.QUESTION_MASTER).queryList();
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from QuestionMaster entity ........" + module);
		}
		
		
		for (GenericValue questionGenericValue : questionGenericValueList) {
			
			
			Long questionId =  questionGenericValue.getLong(CommonConstant.QUESTION_ID);
			String questionDetail = questionGenericValue.getString(CommonConstant.QUESTION_DETAIL);
			String optionA =questionGenericValue.getString(CommonConstant.OPTION_A);
			String optionB =questionGenericValue.getString(CommonConstant.OPTION_B);
			String optionC = questionGenericValue.getString(CommonConstant.OPTION_C);
			String optionD = questionGenericValue.getString(CommonConstant.OPTION_D);
			String optionE = questionGenericValue.getString(CommonConstant.OPTION_E);
			String answer = questionGenericValue.getString(CommonConstant.ANSWER);
			Long numAnswers = questionGenericValue.getLong(CommonConstant.NUM_ANSWERS);
			String questionType =questionGenericValue.getString(CommonConstant.QUESTION_TYPE);
			Integer difficultyLevel =questionGenericValue.getInteger(CommonConstant.DIFFICULTY_LEVEL);
			BigDecimal answerValue =questionGenericValue.getBigDecimal(CommonConstant.ANSWER_VALUE);
			String topicId = questionGenericValue.getString(CommonConstant.TOPIC_ID);
			BigDecimal negativeMarkValue = questionGenericValue.getBigDecimal(CommonConstant.NEGATIVE_MARK_VALUE);

			Map<String, Object> question = new HashMap<>();
			question.put(CommonConstant.QUESTION_ID, questionId);
			question.put(CommonConstant.QUESTION_DETAIL, questionDetail);
			question.put(CommonConstant.OPTION_A, optionA);
			question.put(CommonConstant.OPTION_B, optionB);
			question.put(CommonConstant.OPTION_C, optionC);
			question.put(CommonConstant.OPTION_D, optionD);
			question.put(CommonConstant.OPTION_E, optionE);
			question.put(CommonConstant.ANSWER, answer);
			question.put(CommonConstant.NUM_ANSWERS, numAnswers);
			question.put(CommonConstant.QUESTION_TYPE, questionType);
			question.put(CommonConstant.DIFFICULTY_LEVEL, difficultyLevel);
			question.put(CommonConstant.ANSWER_VALUE, answerValue);
			question.put(CommonConstant.TOPIC_ID, topicId);
			question.put(CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue);
			
			
			questionList.add( question);
		}
		returnSucces.put("questionList", questionList);
		return returnSucces;

	}

}
