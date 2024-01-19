package com.vastpro.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;
import com.vastpro.onlineexam.helper.OnlineExamHelper;

public class UserExamMappingMasterServices {
	public static final String module = UserExamMappingMasterServices.class.getName();

	/**
	 * This method is used to retrieve Exams for particular partyId from entity
	 * 
	 * @param DispatchContext
	 * @param Map<String,     Object>
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> findAllExamForPartyId(DispatchContext dctx,
			Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		// Get and store the partyId which was sent from the event
		String partyId = (String) context.get(CommonConstants.PARTY_ID);
		List<GenericValue> userExamMappingGenericValueList = null;
		List<Map<String, Object>> examList = new LinkedList<Map<String, Object>>();
		try {

			// Query to fetch all the exams assigned to the partyId
			userExamMappingGenericValueList = EntityQuery.use(delegator).from("UserExamMappingViewEntity")
					.where(CommonConstants.PARTY_ID, partyId).cache().queryList();

		} catch (GenericEntityException e) {
			Debug.logError(e.getMessage(), module);
			// returning an error message to the event
			return ServiceUtil
					.returnError("Error in fetching record from UserExamMappingViewEntity entity ..... " + e + module);
		}

		// If the Retrieved userExamMappingGenericValueList is empty
		if (UtilValidate.isEmpty(userExamMappingGenericValueList)) {
			
			String errMsg = "Retreived record list from UserExamMappingViewEntity is null or empty";
			Debug.logWarning(errMsg, module);
			return ServiceUtil.returnMessage(CommonConstants.RESPOND_EMPTY, errMsg + module);
		}
		
		if (UtilValidate.isNotEmpty(userExamMappingGenericValueList)) {

			// Creating a list, containing the map of exams
			for (GenericValue userExamMappingGenericValue : userExamMappingGenericValueList) {

				Map<String, Object> userExamMappingRecord = OnlineExamHelper.populateUserExamMappingMapFromGenericValue(userExamMappingGenericValue);
				userExamMappingRecord.put(CommonConstants.EXAM_NAME,
						
						userExamMappingGenericValue.getString(CommonConstants.EXAM_NAME));
				examList.add(userExamMappingRecord);
			}
			// adding the list of exams to the result map
			result.put(CommonConstants.EXAM_LIST, examList);

		}

		return result;

	}

	public static Map<String, Object> findUserExamMappingRecord(DispatchContext dctx,
			Map<Object, ? extends Object> context) {

		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		String examId = (String) context.get(CommonConstants.EXAM_ID);
		String partyId = (String) context.get(CommonConstants.PARTY_ID);

		GenericValue userExamMappingGenericValue = null;
		try {
			userExamMappingGenericValue = EntityQuery.use(delegator).from(CommonConstants.USER_EXAM_MAPPING_MASTER)
					.where(CommonConstants.EXAM_ID, examId, CommonConstants.PARTY_ID, partyId).cache().queryOne();
		} catch (GenericEntityException e) {
			Debug.logError(e.getMessage(), module);
			return ServiceUtil
					.returnError("Error in fetching record from UserExamMappingViewEntity entity ..... " + e + module);

		}

		// If userExamMappingGenericValue is empty
		if (UtilValidate.isEmpty(userExamMappingGenericValue)) {
			String errMsg = "Retrieved userExamMappingGenericValue  is empty or null";
			Debug.logError(errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}

		Map<String, Object> userExamMappingRecord = OnlineExamHelper.populateUserExamMappingMapFromGenericValue(userExamMappingGenericValue);

		resultMap.put(CommonConstants.USER_EXAM_MAPPING_RECORD, userExamMappingRecord);

		return resultMap;

	}

}
