package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class UserExamMappingEvents {
	public static final String module = UserExamMappingEvents.class.getName();
	

	public static String createUserExamMappingRecord(HttpServletRequest request, HttpServletResponse response) {
		
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");
		Map<String, Object> serviceResultMap = null;
		
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

		try {
			serviceResultMap = dispatcher.runSync("createUserExamMappingRecord", createUserExamMappingRecordMap);
			

			Debug.logInfo("=======Creating UserExamMappingMaster record in event using service addExamToUser=========",
					module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to create new records in ExamMaster entity: " + e.toString();
			request.setAttribute(CommonConstant.ERROR, errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		
		if (ServiceUtil.isSuccess(createUserExamMappingRecordMap)) {
			request.setAttribute("createUserExamMappingRecordMap", serviceResultMap);
		}
		return CommonConstant.SUCCESS;
	}
	

	public static String showExamsForPartyId(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> examResult = null;

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String partyId = (String) request.getAttribute(CommonConstant.PARTY_ID);

		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstant.PARTY_ID, partyId);

		try {
			examResult = dispatcher.runSync("showExamsForPartyId", findExamContext);
			if (ServiceUtil.isSuccess(examResult)) {
				request.setAttribute("examList", examResult.get("examList"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrieve  records  from UserExamMappingMasterview  entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

		
				
	}

}
