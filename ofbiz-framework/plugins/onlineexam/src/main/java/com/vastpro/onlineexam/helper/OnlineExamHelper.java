package com.vastpro.onlineexam.helper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.common.authentication.AuthHelper;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class OnlineExamHelper {
	
	private static final String module = OnlineExamHelper.class.getName();
	
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
			Debug.logError(e, "Failed to execute findExamTopicMappingRecords service", module);
			String errMsg = "Failed to execute findExamTopicMappingRecords service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		//if findExamTopicMappingRecords Service result is success ,examTopicMappingRecordList is set in request
		if (ServiceUtil.isSuccess(findExamTopicMappingResp)) {
			
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute("examTopicMappingRecordList", findExamTopicMappingResp.get("examTopicMappingRecordList"));
		}
		else {
			//if findExamTopicMappingRecords Service result is error ,error message is set in request and return error
			String errMsg = "Error returned while executing findExamTopicMappingRecords";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants.RESULT_MAP, findExamTopicMappingResp);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
			
		}

		return CommonConstants.SUCCESS;
	}

}
