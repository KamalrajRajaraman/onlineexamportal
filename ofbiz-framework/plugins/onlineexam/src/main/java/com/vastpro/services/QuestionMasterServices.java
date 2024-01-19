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
import com.vastpro.onlineexam.helper.OnlineExamHelper;

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
			Debug.logWarning(errMsg, module);
			return ServiceUtil.returnMessage(CommonConstants.RESPOND_EMPTY,errMsg + module);
		}
		
		
		if(UtilValidate.isNotEmpty(questionGenericValueList)) {
			for (GenericValue questionGenericValue : questionGenericValueList) {
				Map<String, Object> question =OnlineExamHelper.getQuestionFromGenericValue(questionGenericValue);
				questionList.add(question);
			}
		}
		serviceResultMap.put(CommonConstants.QUESTION_LIST, questionList);
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
			questionMasterGV = EntityQuery.use(delegator).from(CommonConstants.QUESTION_MASTER)
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
		
		Map<String, Object> questionMap = OnlineExamHelper.getQuestionFromGenericValue(questionMasterGV);
		serviceResultMap.put(CommonConstants.QUESTION, questionMap);
		return serviceResultMap;

	}

	

}
