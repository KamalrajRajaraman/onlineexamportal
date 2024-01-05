package com.vastpro.events;

import java.util.HashMap;
import java.util.List;
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

import com.vastpro.constants.CommonConstants;
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
		//Hibernate Validation for create ExamTopicMappingMaster Record
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
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
									CommonConstants.RESOURCE_ERROR,false);
		
		request.setAttribute("hasFormErrors", hasFormErrors);
		
		if(hasFormErrors) {
			Debug.logError( "Invalid Input", module);
			String errMsg = "Invalid Input";
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;		
		}
		
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		//Getting records from front End 
		String examId = (String) combinedMap.get(CommonConstants.EXAM_ID);
		String topicId = (String) combinedMap.get(CommonConstants.TOPIC_ID);
		String percentage = (String) combinedMap.get(CommonConstants.PERCENTAGE);
		String topicPassPercentage = (String) combinedMap.get(CommonConstants.TOPIC_PASS_PERCENTAGE);
		
		 List<GenericValue> percentageList  =null;
		try {
				percentageList  = EntityQuery
								.use(delegator)
								.select(CommonConstants.PERCENTAGE)
								.from(CommonConstants.EXAM_TOPIC_MAPPING_MASTER)
								.where(CommonConstants.EXAM_ID,examId)
								.queryList();

		} catch (GenericEntityException e) {
			Debug.logError(e, "Failed retrive List of percentage from EXamTopicMappingMaster for given examID and TopicId ", module);
			String errMsg = "Failed retrive List of percentage from EXamTopicMappingMaster for given examID and TopicId : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		for(GenericValue percent :percentageList) {
			
			
		}
		
		
		// creating map to pass required context to the service called
		Map<String, Object> addTopicToExamContextMap = new HashMap<>();
		addTopicToExamContextMap.put(CommonConstants.EXAM_ID, examId);
		addTopicToExamContextMap.put(CommonConstants.TOPIC_ID, topicId);
		addTopicToExamContextMap.put(CommonConstants.PERCENTAGE, percentage);
		addTopicToExamContextMap.put(CommonConstants.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
		addTopicToExamContextMap.put(CommonConstants.USER_LOGIN, userLogin);

		//Retrieving noOfQuestion from ExamMaster Entity to calculate questionPerExam
		Map<String, Object> noOfQuestionResp = null;
		try {
			noOfQuestionResp = dispatcher.runSync("findNoOfQuestionCountByExamID", addTopicToExamContextMap);
			Debug.logInfo("Successfully executed findNoOfQuestionCountByExamID Service", module);
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute findNoOfQuestionCountByExamID service", module);
			String errMsg = "Failed to execute findNoOfQuestionCountByExamID service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;

		}
		
		//Returned and terminated the method if Response from findNoOfQuestionCountByExamId service is error 
		if(ServiceUtil.isError(noOfQuestionResp)) {
			String errMsg = "Error while executing findNoOfQuestionCountByExamID service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;		
		}
		
		//if Response from findNoOfQuestionCountByExamID service is success 
		if (ServiceUtil.isSuccess(noOfQuestionResp)) {

			// questionPerExam calculated using the business logic
			Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstants.NO_OF_QUESTIONS);
			Integer percentageOfTopic = Integer.parseInt(percentage);
			Long questionPerExam = ((noOfQuestion * percentageOfTopic) / 100);
			addTopicToExamContextMap.put(CommonConstants.QUESTION_PER_EXAM, questionPerExam);

			// creating examTopicMapping records
			Map<String, Object> addTopicToExamResp = null;
			try {
				addTopicToExamResp = dispatcher.runSync("addTopicToExam", addTopicToExamContextMap);
				Debug.logInfo("Successfully executed addTopicToExam Service", module);
				
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute addTopicToExam service", module);
				String errMsg = "Failed to execute addTopicToExam service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			//if error occurred while executing addTopicToExam service ,return is returned (method terminated)
			if(ServiceUtil.isError(addTopicToExamResp)) {
				String errMsg = "Error while executing addTopicToExam service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;		
			}
			

			if (ServiceUtil.isSuccess(addTopicToExamResp)) {
				request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
				GenericValue insertedRecordGenericValue = null;
				try {
					insertedRecordGenericValue = EntityQuery.use(delegator).from("ExamTopicMappingViewEntity")
							.where(CommonConstants.EXAM_ID, examId, CommonConstants.TOPIC_ID, topicId).queryOne();
					Debug.logInfo("Successfully retrieved  records from ExamTopicMappingViewEntity", module);
				} catch (GenericEntityException e) {
					Debug.logError(e, "Failed to retrieve records from ExamTopicMappingViewEntity ", module);
					String errMsg = "Failed to retrieve records from ExamTopicMappingViewEntity : " + e.getMessage();
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
				}
				//if Records retrieved  from ExamTopicMappingView is null or empty error is returned 
				if(UtilValidate.isEmpty(insertedRecordGenericValue)) {
					String errMsg = "Error - records revieved from ExamTopicMappingViewEntity is empty" ;
					Debug.logError(errMsg, module);
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
				}
				/*if Records retrieved  from ExamTopicMappingView is not null or not empty ,
				examTopicMappingRecord map is created and set in request*/
				if (UtilValidate.isNotEmpty(insertedRecordGenericValue)) {

					examId = insertedRecordGenericValue.getString(CommonConstants.EXAM_ID);
					String examName = insertedRecordGenericValue.getString(CommonConstants.EXAM_NAME);
					topicId = insertedRecordGenericValue.getString(CommonConstants.TOPIC_ID);
					String topicName = insertedRecordGenericValue.getString(CommonConstants.TOPIC_NAME);
					percentage = insertedRecordGenericValue.getString(CommonConstants.PERCENTAGE);
					topicPassPercentage = insertedRecordGenericValue.getString(CommonConstants.TOPIC_PASS_PERCENTAGE);
					questionPerExam = insertedRecordGenericValue.getLong(CommonConstants.QUESTION_PER_EXAM);

					Map<String, Object> examTopicMappingRecord = new HashMap<>();
					examTopicMappingRecord.put(CommonConstants.EXAM_ID, examId);
					examTopicMappingRecord.put(CommonConstants.EXAM_NAME, examName);
					examTopicMappingRecord.put(CommonConstants.TOPIC_ID, topicId);
					examTopicMappingRecord.put(CommonConstants.TOPIC_NAME, topicName);
					examTopicMappingRecord.put(CommonConstants.PERCENTAGE, percentage);
					examTopicMappingRecord.put(CommonConstants.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
					examTopicMappingRecord.put(CommonConstants.QUESTION_PER_EXAM, questionPerExam);

					request.setAttribute("examTopicMappingMasterRecord", examTopicMappingRecord);
				}
			}
		}
		return CommonConstants.SUCCESS;
	}

	public static String findAllExamTopicMappingRecordsByExamId(HttpServletRequest request,
			HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String examId = (String) request.getAttribute(CommonConstants.EXAM_ID);
		if (examId == null) {
			examId = request.getParameter(CommonConstants.EXAM_ID);
		}

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
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
			
		}

		return CommonConstants.SUCCESS;
	}
}
