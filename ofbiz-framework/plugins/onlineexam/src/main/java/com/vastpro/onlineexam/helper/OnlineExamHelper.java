package com.vastpro.onlineexam.helper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class OnlineExamHelper {
	
	private static final String module = OnlineExamHelper.class.getName();
	
	
	/**
	 *Helps to find All ExamTopicMappingRecords By ExamId 
	 * @param request
	 * @return
	 */
	public static String findAllExamTopicMappingRecordsByExamId(HttpServletRequest request) {


		String examId = (String) request.getAttribute(CommonConstants.EXAM_ID);
		if (examId == null) {
			examId = request.getParameter(CommonConstants.EXAM_ID);
		}
		
		//If examId is null or Empty ,error returned
		if(UtilValidate.isEmpty(examId)) {
			String errMsg = "examId is Empty";
			Debug.logError( errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
		    return CommonConstants.ERROR;		
		}
		
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		Map<String, Object> findAllContextMap = new HashMap<>();
		findAllContextMap.put(CommonConstants.EXAM_ID, examId);
		findAllContextMap.put(CommonConstants.USER_LOGIN, userLogin);
		
		//Executes findExamTopicMappingRecords service (one examId is related with many topicId)
		Map<String, Object> findExamTopicMappingResp = null;
		try {
			findExamTopicMappingResp = dispatcher.runSync("findExamTopicMappingRecords", findAllContextMap);
			Debug.logInfo("Successfully executed findExamTopicMappingRecords Service", module);
		} catch (GenericServiceException e) {
			String errMsg = "Failed to execute findExamTopicMappingRecords service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		
		String responseMessage = (String) findExamTopicMappingResp.get(CommonConstants.RESPONSE_MESSAGE);
		if(responseMessage.equals(CommonConstants.RESPOND_EMPTY)) {
			String errMsg = (String) findExamTopicMappingResp.get(CommonConstants.SUCCESS_MESSAGE);
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		//if findExamTopicMappingRecords Service result is success ,examTopicMappingRecordList is set in request
		if (ServiceUtil.isSuccess(findExamTopicMappingResp)) {
			
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.EXAM_TOPIC_MAPPING_RECORD_LIST, findExamTopicMappingResp.get(CommonConstants.EXAM_TOPIC_MAPPING_RECORD_LIST));
		}
		else {
			//if findExamTopicMappingRecords Service result is error ,error message is set in request and return error
			String errMsg = "Error returned while executing findExamTopicMappingRecords";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
			
		}
		
		return CommonConstants.SUCCESS;
	}
	
	
	public static  Map<String,Object> populateUserExamMappingMapFromGenericValue(GenericValue userExamMappingGV){
		
		Map<String, Object> userExamMappingRecord = new HashedMap<>();
		userExamMappingRecord.put(CommonConstants.PARTY_ID,
				userExamMappingGV.getString(CommonConstants.PARTY_ID));
		userExamMappingRecord.put(CommonConstants.EXAM_ID,
				userExamMappingGV.getString(CommonConstants.EXAM_ID));
		userExamMappingRecord.put(CommonConstants.ALLOWED_ATTEMPTS,
				userExamMappingGV.getLong(CommonConstants.ALLOWED_ATTEMPTS));
		userExamMappingRecord.put(CommonConstants.NO_OF_ATTEMPTS,
				userExamMappingGV.getLong(CommonConstants.NO_OF_ATTEMPTS));
		userExamMappingRecord.put(CommonConstants.LAST_PERFORMANCE_DATE,
				userExamMappingGV.getTimestamp(CommonConstants.LAST_PERFORMANCE_DATE));
		userExamMappingRecord.put(CommonConstants.TIMEOUT_DAYS,
				userExamMappingGV.getLong(CommonConstants.TIMEOUT_DAYS));
		userExamMappingRecord.put(CommonConstants.PASSWORD_CHANGES_AUTO,
				userExamMappingGV.getString(CommonConstants.PASSWORD_CHANGES_AUTO));
		userExamMappingRecord.put(CommonConstants.CAN_SPLIT_EXAMS,
				userExamMappingGV.getString(CommonConstants.CAN_SPLIT_EXAMS));
		userExamMappingRecord.put(CommonConstants.CAN_SEE_DETAILED_RESULTS,
				userExamMappingGV.getString(CommonConstants.CAN_SEE_DETAILED_RESULTS));
		userExamMappingRecord.put(CommonConstants.MAX_SPLIT_ATTEMPTS,
				userExamMappingGV.getInteger(CommonConstants.MAX_SPLIT_ATTEMPTS));	
		return userExamMappingRecord;
		
	}

}
