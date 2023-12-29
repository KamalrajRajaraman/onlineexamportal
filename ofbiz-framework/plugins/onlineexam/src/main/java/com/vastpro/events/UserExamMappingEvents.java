package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

//Event class for UserExamMappingMaster operation
public class UserExamMappingEvents {
	public static final String module = UserExamMappingEvents.class.getName();
	
	//Method for create a record in UserExamMappingMaster entity
	public static String createUserExamMappingRecord(HttpServletRequest request, HttpServletResponse response) {
		
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		Map<String, Object> serviceResultMap = null;
		
		//Getting all details from the request as a attribute
		String partyId = (String) request.getAttribute(CommonConstant.PARTY_ID);
		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		String allowedAttempts = (String) request.getAttribute(CommonConstant.ALLOWED_ATTEMPTS);
		String noOfAttempts = (String) request.getAttribute(CommonConstant.NO_OF_ATTEMPTS);
		String lastPerformanceDate = (String) request.getAttribute(CommonConstant.LAST_PERFORMANCE_DATE);
		String timeoutDays = (String) request.getAttribute(CommonConstant.TIMEOUT_DAYS);
		String passwordChangesAuto = (String) request.getAttribute(CommonConstant.PASSWORD_CHANGES_AUTO);
		String canSplitExams = (String) request.getAttribute(CommonConstant.CAN_SPLIT_EXAMS);
		String canSeeDetailedResults = (String) request.getAttribute(CommonConstant.CAN_SEE_DETAILED_RESULTS);
		String maxSplitAttempts = (String) request.getAttribute(CommonConstant.MAX_SPLIT_ATTEMPTS);

		//Creating a map with all the details and userLogin object
		Map<String, Object> createUserExamMappingRecordMap = UtilMisc.toMap(
				CommonConstant.PARTY_ID, partyId ,
				CommonConstant.EXAM_ID, examId,
				CommonConstant.ALLOWED_ATTEMPTS,allowedAttempts,
				CommonConstant.NO_OF_ATTEMPTS,noOfAttempts,
				CommonConstant.LAST_PERFORMANCE_DATE, lastPerformanceDate,
				CommonConstant.TIMEOUT_DAYS, timeoutDays,
				CommonConstant.PASSWORD_CHANGES_AUTO, passwordChangesAuto, 
				CommonConstant.CAN_SPLIT_EXAMS,canSplitExams,
				CommonConstant.CAN_SEE_DETAILED_RESULTS, canSeeDetailedResults,
				CommonConstant.MAX_SPLIT_ATTEMPTS, maxSplitAttempts, 
				CommonConstant.USER_LOGIN, userLogin);

		//calling createUserExamMappingRecord service 
		try {
			serviceResultMap = dispatcher.runSync("createUserExamMappingRecord", createUserExamMappingRecordMap);
			Debug.logInfo("=======Creating UserExamMappingMaster record in event using service addExamToUser=========",
					module);
		} catch (GenericServiceException e) {
			//If any exception occur in service, set error as a result in request object
			Debug.logError(e, "Failed to execute createUserExamMappingRecord service", module);
			String errMsg = "Failed to execute createUserExamMappingRecord service : " + e.getMessage();
			request.setAttribute(CommonConstant.ERROR, errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		
		//checking if the createUserExamMappingRecord service is success or not
		if (ServiceUtil.isSuccess(createUserExamMappingRecordMap)) {
			request.setAttribute("createUserExamMappingRecordMap", serviceResultMap);
		}
		return CommonConstant.SUCCESS;
	}
	
	//Event for showing exams for the particular user based on partyId
	public static String showExamsForPartyId(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> examResult = null;

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		
		//checking partyId comes as a parameter
		String partyId = request.getParameter(CommonConstant.PARTY_ID);
		
		//checking partyId comes as a attriubute
		if(UtilValidate.isEmpty(partyId)) {
			partyId = (String) request.getAttribute(CommonConstant.PARTY_ID);
		}
		
		//If the partyId didn't came as both parameter and attribute then take it from the session
		if(UtilValidate.isEmpty(partyId)) {
			partyId = (String) request.getSession().getAttribute(CommonConstant.PARTY_ID);
			
		}
		
		//create a map with partyId, userLogin object
		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstant.PARTY_ID, partyId);

		//calling showExamsForPartyId service for showing exams
		try {
			examResult = dispatcher.runSync("showExamsForPartyId", findExamContext);
			if (ServiceUtil.isSuccess(examResult)) {
				request.setAttribute("examList", examResult.get("examList"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			//If any exception occur in service, set error as a result in request object
			Debug.logError(e, "Failed to execute showExamsForPartyId service", module);
			String errMsg = "Failed to execute showExamsForPartyId service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

		
				
	}

}
