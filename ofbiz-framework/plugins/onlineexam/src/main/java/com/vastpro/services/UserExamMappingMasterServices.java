package com.vastpro.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class UserExamMappingMasterServices {
	public static final String module = UserExamMappingMasterServices.class.getName();
	
	/**
	 * This method is used to retrieve Exams for particular partyId from entity
	 * @param DispatchContext
	 * @param Map<String, Object>
	 * @return
	 * 		Map<String, Object>
	 */
	public static Map<String, Object> showExamsForPartyId(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		
		//Get and store the partyId which was sent from the event
		String partyId = (String) context.get(CommonConstants.PARTY_ID);
		List<GenericValue> examGenericValueList = null;
		List<Map<String, Object>> examList= new LinkedList<Map<String,Object>>();
		try {
			
			//Query to fetch all the exams assigned to the partyId
			examGenericValueList = EntityQuery.use(delegator).from("UserExamMappingViewEntity")
					.where(CommonConstants.PARTY_ID, partyId).queryList();

		} catch (GenericEntityException e) {
			
			//returning an error message to the event
			return ServiceUtil.returnError(
					"Error in fetching record from UserExamMappingViewEntity entity ..... " + e + module);
		}
		
		if(UtilValidate.isEmpty(examGenericValueList)) {
			//If examGenericValueList is empty
			String errMsg = "Retrieved Exam list is empty or null";
			Debug.logError(errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		if (UtilValidate.isNotEmpty(examGenericValueList)) {
			
			//Creating a list, containing the map of exams
			for (GenericValue examGenericValue : examGenericValueList) {
				String examId = examGenericValue.getString(CommonConstants.EXAM_ID);
				String examName = examGenericValue.getString(CommonConstants.EXAM_NAME);
				
				//Creating a map to store the exam details
				Map<String, Object> exam = UtilMisc.toMap(CommonConstants.EXAM_ID, examId,
						CommonConstants.EXAM_NAME, examName);
				examList.add(exam); 
			}
			//adding the list of exams to the result map
			result.put("examList", examList);
			
		}
		
		return result;

	}

}
