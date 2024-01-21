
package com.vastpro.services;

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
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class OnlineExamServices {

	public static final String module = OnlineExamServices.class.getName();

	/**
	 * This method is used to retrieve all the users from UserMaster view entity
	 * 
	 * @param DispatchContext
	 * @param Map<String,     ? extends Object>
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> findAllUsers(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> resultMap = ServiceUtil.returnSuccess();

		Delegator delegator = dctx.getDelegator();

		List<Map<String, Object>> userList = new LinkedList<>();
		List<GenericValue> GenericValueList = null;
		Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
		EntityExpr roleTypeCondition = EntityCondition.makeCondition(CommonConstants.ROLE_TYPE_ID, EntityOperator.EQUALS,CommonConstants.PERSON_ROLE);
		EntityExpr disabledDateTimeCondition1 = EntityCondition.makeCondition(CommonConstants.DISABLED_DATE_TIME, EntityOperator.EQUALS,null);
		EntityExpr disabledDateTimeCondition2= EntityCondition.makeCondition(CommonConstants.DISABLED_DATE_TIME, EntityOperator.GREATER_THAN_EQUAL_TO,currentDateTime);
		EntityExpr disabledDateTimeOrCondition = EntityCondition.makeCondition(disabledDateTimeCondition1,EntityOperator.OR,disabledDateTimeCondition2);
		EntityExpr whereCondition = EntityCondition.makeCondition(roleTypeCondition,EntityOperator.AND,disabledDateTimeOrCondition);
		try {
			// Retrieve all users based on roleTypeId
			GenericValueList = EntityQuery.use(delegator).from(CommonConstants.USER_MASTER)
					.where(whereCondition).cache().queryList();

		} catch (GenericEntityException e) {
			// If Exception occurred return error map
			String errMsg = "Exception in fetching record from UserMaster view entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}

		if (UtilValidate.isEmpty(GenericValueList)) {
			// If Retrieved GenericValueList is empty or null
			String errMsg = "Retrieved user List from UserMaster view entity is empty or null";
			Debug.logError(errMsg ,module);
			return ServiceUtil.returnMessage( CommonConstants.RESPOND_EMPTY, errMsg + module );
		
		}

		if (UtilValidate.isNotEmpty(GenericValueList)) {
			// If GenericValueList is not empty, iterate the list.
			for (GenericValue genericValue : GenericValueList) {
				String firstName = genericValue.getString(CommonConstants.FIRST_NAME);
				String lastName = genericValue.getString(CommonConstants.LAST_NAME);
				String partyId = genericValue.getString(CommonConstants.PARTY_ID);
				String roleTypeId = genericValue.getString(CommonConstants.ROLE_TYPE_ID);
				String userLoginId = genericValue.getString(CommonConstants.USER_LOGIN_ID);

				// Construct a map with required values
				Map<String, Object> user = new HashMap<>();
				user.put(CommonConstants.FIRST_NAME, firstName);
				user.put(CommonConstants.LAST_NAME, lastName);
				user.put(CommonConstants.PARTY_ID, partyId);
				user.put(CommonConstants.ROLE_TYPE_ID, roleTypeId);
				user.put(CommonConstants.USER_LOGIN_ID, userLoginId);
				userList.add(user);
			}
			resultMap.put(CommonConstants.USER_LIST, userList);

		}

		return resultMap;

	}

	public static Map<String, Object> findAnswerAndTopicIdForQuestionId(DispatchContext dctx,
			Map<String, ? extends Object> context) {

		Map<String, Object> resultMap = ServiceUtil.returnSuccess();

		Delegator delegator = dctx.getDelegator();

		String questionId = (String) context.get(CommonConstants.QUESTION_ID);

		GenericValue questionDetail = null;
		try {
			// Retrieve all users based on roleTypeId
			questionDetail = EntityQuery.use(delegator).from(CommonConstants.QUESTION_MASTER)
					.where(CommonConstants.QUESTION_ID, questionId).cache().queryOne();

		} catch (GenericEntityException e) {
			// If Exception occurred return error map
			String errMsg = "Exception in fetching record from QuestionMaster entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}

		if (UtilValidate.isEmpty(questionDetail)) {
			String errMsg = "Retrieved record from QuestionMaster entity is empty or null";
			Debug.logError(errMsg ,module);
			return ServiceUtil.returnMessage( CommonConstants.RESPOND_EMPTY, errMsg + module );

		}

		String topicId = questionDetail.getString(CommonConstants.TOPIC_ID);
		String answer = questionDetail.getString(CommonConstants.ANSWER);

		resultMap.put(CommonConstants.TOPIC_ID, topicId);
		resultMap.put(CommonConstants.ANSWER, answer);

		// resultMap.put("questionDetail", questionDetail);

		return resultMap;
	}

}
