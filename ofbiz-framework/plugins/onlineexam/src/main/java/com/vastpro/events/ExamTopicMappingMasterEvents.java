package com.vastpro.events;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;
import com.vastpro.validator.ExamTopicMappingCheck;
import com.vastpro.validator.ExamTopicMappingValidator;
import com.vastpro.validator.HibernateHelper;


public class ExamTopicMappingMasterEvents {

	public static final String module = ExamTopicMappingMasterEvents.class.getName();
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String createExamTopicMappingMasterRecord(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(CommonConstant.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstant.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstant.DISPATCHER);

		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Locale locale = UtilHttp.getLocale(request);

		ExamTopicMappingValidator examTopicMappingForm 
			= HibernateHelper.populateBeanFromMap(combinedMap,ExamTopicMappingValidator.class);
		
		Set<ConstraintViolation<ExamTopicMappingValidator>> checkValidationErrors
			= HibernateHelper.checkValidationErrors(examTopicMappingForm, ExamTopicMappingCheck.class);

		boolean hasFormErrors = HibernateHelper
								.validateFormSubmission
								(delegator,	
									checkValidationErrors,
									request, 
									locale, 
									"MandatoryFieldErrMsg", 
									CommonConstant.RESOURCE_ERROR,false);
		
		request.setAttribute("hasFormErrors", hasFormErrors);
		
		if(hasFormErrors) {
			Debug.logError( "Invalid Input", module);
			String errMsg = "Invalid Input";
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;		
		}
		
		//Getting records from front End 
		String examId = (String) combinedMap.get(CommonConstant.EXAM_ID);
		String topicId = (String) combinedMap.get(CommonConstant.TOPIC_ID);
		String percentage = (String) combinedMap.get(CommonConstant.PERCENTAGE);
		String topicPassPercentage = (String) combinedMap.get(CommonConstant.TOPIC_PASS_PERCENTAGE);
		

		// creating map to pass required context to the service called
		Map<String, Object> addTopicToExamContextMap = new HashMap<>();
		addTopicToExamContextMap.put(CommonConstant.EXAM_ID, examId);
		addTopicToExamContextMap.put(CommonConstant.TOPIC_ID, topicId);
		addTopicToExamContextMap.put(CommonConstant.PERCENTAGE, percentage);
		addTopicToExamContextMap.put(CommonConstant.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
		addTopicToExamContextMap.put(CommonConstant.USER_LOGIN, userLogin);

		//Retrieving noOfQuestion from ExamMaster Entity to calculate questionPerExam
		Map<String, Object> noOfQuestionResp = null;
		try {
			noOfQuestionResp = dispatcher.runSync("findNoOfQuestionCountByExamID", addTopicToExamContextMap);
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute findNoOfQuestionCountByExamID service", module);
			String errMsg = "Failed to execute findNoOfQuestionCountByExamID service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;

		}

		if (ServiceUtil.isSuccess(noOfQuestionResp)) {

			// questionPerExam calculated using the business logic
			Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstant.NO_OF_QUESTIONS);
			Integer percentageOfTopic = Integer.parseInt(percentage);
			Long questionPerExam = ((noOfQuestion * percentageOfTopic) / 100);
			addTopicToExamContextMap.put(CommonConstant.QUESTION_PER_EXAM, questionPerExam);

			// creating examTopicMapping records
			Map<String, Object> serviceResultMap = null;
			try {
				serviceResultMap = dispatcher.runSync("addTopicToExam", addTopicToExamContextMap);

			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute addTopicToExam service", module);
				String errMsg = "Failed to execute addTopicToExam service : " + e.getMessage();
				request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
				return CommonConstant.ERROR;
			}

			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
				GenericValue insertedRecordGenericValue = null;
				try {
					insertedRecordGenericValue = EntityQuery.use(delegator).from("ExamTopicMappingViewEntity")
							.where(CommonConstant.EXAM_ID, examId, CommonConstant.TOPIC_ID, topicId).queryOne();
				} catch (GenericEntityException e) {
					Debug.logError(e, "Failed to retrieve records from ExamTopicMappingViewEntity ", module);
					String errMsg = "Failed to retrieve records from ExamTopicMappingViewEntity : " + e.getMessage();
					request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
					return CommonConstant.ERROR;
				}
				if (UtilValidate.isNotEmpty(insertedRecordGenericValue)) {

					examId = insertedRecordGenericValue.getString(CommonConstant.EXAM_ID);
					String examName = insertedRecordGenericValue.getString(CommonConstant.EXAM_NAME);
					topicId = insertedRecordGenericValue.getString(CommonConstant.TOPIC_ID);
					String topicName = insertedRecordGenericValue.getString(CommonConstant.TOPIC_NAME);
					percentage = insertedRecordGenericValue.getString(CommonConstant.PERCENTAGE);
					topicPassPercentage = insertedRecordGenericValue.getString(CommonConstant.TOPIC_PASS_PERCENTAGE);
					questionPerExam = insertedRecordGenericValue.getLong(CommonConstant.QUESTION_PER_EXAM);

					Map<String, Object> examTopicMappingRecord = new HashMap<>();
					examTopicMappingRecord.put(CommonConstant.EXAM_ID, examId);
					examTopicMappingRecord.put(CommonConstant.EXAM_NAME, examName);
					examTopicMappingRecord.put(CommonConstant.TOPIC_ID, topicId);
					examTopicMappingRecord.put(CommonConstant.TOPIC_NAME, topicName);
					examTopicMappingRecord.put(CommonConstant.PERCENTAGE, percentage);
					examTopicMappingRecord.put(CommonConstant.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
					examTopicMappingRecord.put(CommonConstant.QUESTION_PER_EXAM, questionPerExam);

					request.setAttribute("examTopicMappingMasterRecord", examTopicMappingRecord);
				}
			}
		}
		return CommonConstant.SUCCESS;
	}

	public static String findAllExamTopicMappingRecordsByExamId(HttpServletRequest request,
			HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		if (examId == null) {
			examId = request.getParameter(CommonConstant.EXAM_ID);
		}

		Map<String, Object> findAllContextMap = new HashMap<>();
		findAllContextMap.put(CommonConstant.EXAM_ID, examId);
		findAllContextMap.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> serviceResultMap = null;

		try {
			serviceResultMap = dispatcher.runSync("findExamTopicMappingRecords", findAllContextMap);
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute findExamTopicMappingRecords service", module);
			String errMsg = "Failed to execute findExamTopicMappingRecords service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		if (ServiceUtil.isSuccess(serviceResultMap)) {
			
			request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
			request.setAttribute("examTopicMappingRecordList", serviceResultMap.get("examTopicMappingRecordList"));
		}

		return CommonConstant.SUCCESS;
	}
}
