package com.vastpro.events;

import java.math.BigDecimal;
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
import org.apache.ofbiz.base.util.UtilProperties;
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
	 * Method for create a record in ExamTopicMappingMaster entity
	 * 
	 * @param request
	 * @param response
	 * @return String
	 */
	public static String createExamTopicMappingMasterRecord(HttpServletRequest request, HttpServletResponse response) {
		
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
		
		request.setAttribute(CommonConstants.HAS_FORM_ERROR, hasFormErrors);
		
		if(hasFormErrors) {
			
			String errMsg = "Invalid Input";
			Debug.logError(errMsg, module);
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
		
		/*Calculation for finding available topic to exam Percentage.
		 * While Mapping topic to exam,topic to exam percentage should not be more than 100
		 * and Topic to exam percentage should not be more than available percentage that can be added
		 * */
		//retrieving List of percentage from ExamTopicMappingMaster for given examID and TopicId 
		
		List<GenericValue> examTopicMasterGV  =null;
		try {
			examTopicMasterGV  = EntityQuery
								.use(delegator)
								.from(CommonConstants.EXAM_TOPIC_MAPPING_MASTER)
								.where(CommonConstants.EXAM_ID,examId).cache()
								.queryList();

		} catch (GenericEntityException e) {
			String errMsg = "Failed retrieve records from ExamTopicMappingMaster for given examID and TopicId : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		
		boolean isUpdateExamTopicMappingMaster =false;
		BigDecimal existingPercentage =null;

		if(UtilValidate.isNotEmpty(examTopicMasterGV)) {
			//Total topic to exam percentage is calculated 
			BigDecimal totalPercentageAdded = new BigDecimal(0);
			for(GenericValue examTopicMasterRecord :examTopicMasterGV) {
				String topicIdfromETMM = examTopicMasterRecord.getString(CommonConstants.TOPIC_ID);
				if(topicId.equals(topicIdfromETMM)) {
					isUpdateExamTopicMappingMaster=true;
					existingPercentage =examTopicMasterRecord.getBigDecimal(CommonConstants.PERCENTAGE);			
				}
				totalPercentageAdded = totalPercentageAdded.add(examTopicMasterRecord.getBigDecimal(CommonConstants.PERCENTAGE));			
			}
			
			//Normally total percentage will be 100
			BigDecimal totalPercentage = new BigDecimal(100);
			if(isUpdateExamTopicMappingMaster) {
				totalPercentageAdded = totalPercentageAdded.subtract(existingPercentage);
			}
			
			//Available percentage is calculated by subtracting totalPercentageAdded from totalPercentage
			BigDecimal availablePercentage = totalPercentage.subtract(totalPercentageAdded);
			
			
			
			/*Before converting BigDecimal to Double,
			checked availablePercentage is not empty,to avoid NullPointerException*/
			if(UtilValidate.isNotEmpty(availablePercentage)) {
				
				double availablePercent = availablePercentage.doubleValue();
				
				//If No more  Topic can be added  to exam ,returning error
				if(availablePercent==0) {
					String errMsg = UtilProperties.getMessage(CommonConstants.RESOURCE_ERROR,"MaxTopicError", locale);
					Debug.logError( errMsg, module);	
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
					
				}//if available percentage is less than topic percentage should be added, returning error
				else if (availablePercent < Double.valueOf(percentage)) {
					String errMsg = "Available Percentage for adding new topic is less than the topic percentage you trying to add ";
					Debug.logError( errMsg, module);
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);			
					return CommonConstants.ERROR;
				}
			}
			else {
				//when availablePercentage is empty or null,error is returned
				String errMsg = "Available Percentage calculated is null or empty";
				Debug.logError( errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);			
				return CommonConstants.ERROR;
				
			}
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
			noOfQuestionResp = dispatcher.runSync(CommonConstants.FIND_NO_OF_QUESTION_COUNT_BY_EXAM_ID, addTopicToExamContextMap);
			Debug.logInfo("Successfully executed findNoOfQuestionCountByExamId Service", module);
		} catch (GenericServiceException e) {
			
			String errMsg = "Failed to execute findNoOfQuestionCountByExamId service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;

		}
		
		//Returned and terminated the method if Response from findNoOfQuestionCountByExamId service is error 
		if(ServiceUtil.isError(noOfQuestionResp)) {
			String errMsg = "Error while executing findNoOfQuestionCountByExamId service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;		
		}
		
		//if Response from findNoOfQuestionCountByExamID service is success 
		if (ServiceUtil.isSuccess(noOfQuestionResp)) {

			// questionPerExam calculated using the business logic
			Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstants.NO_OF_QUESTIONS);
			Double percentageOfTopic = Double.parseDouble(percentage);
			Long questionPerExam = (long) ((noOfQuestion * percentageOfTopic) / 100);
			addTopicToExamContextMap.put(CommonConstants.QUESTION_PER_EXAM, questionPerExam);

			
			
			// creating examTopicMapping records
			Map<String, Object> addTopicToExamResp = null;
			if(isUpdateExamTopicMappingMaster) {
				try {
					addTopicToExamResp = dispatcher.runSync(CommonConstants.UPDATE_EXAM_TOPIC_MAPPING_MASTER, addTopicToExamContextMap);
					Debug.logInfo("Successfully executed updateExamTopicMappingMaster Service", module);
				} catch (GenericServiceException e) {
					
					String errMsg = "Failed to execute updateExamTopicMappingMaster service : " + e.getMessage();
					Debug.logError(e, errMsg, module);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					return CommonConstants.ERROR;
				}
				
				
			}else {
				
				try {
					addTopicToExamResp = dispatcher.runSync(CommonConstants.ADD_TOPIC_TO_EXAM, addTopicToExamContextMap);
					Debug.logInfo("Successfully executed addTopicToExam Service", module);
					
				} catch (GenericServiceException e) {
					String errMsg = "Failed to execute addTopicToExam service : " + e.getMessage();
					Debug.logError(e, errMsg, module);
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
				}
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
					insertedRecordGenericValue = EntityQuery.use(delegator).from(CommonConstants.EXAM_TOPIC_MAPPING_VIEW_ENTITY)
							.where(CommonConstants.EXAM_ID, examId, CommonConstants.TOPIC_ID, topicId).queryOne();
					Debug.logInfo("Successfully retrieved  records from ExamTopicMappingViewEntity", module);
				} catch (GenericEntityException e) {
					String errMsg = "Failed to retrieve records from ExamTopicMappingViewEntity : " + e.getMessage();
					Debug.logError(e, errMsg, module);
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

					request.setAttribute(CommonConstants.EXAM_TOPIC_MAPPING_MASTER_RECORD, examTopicMappingRecord);
				}
			}
		}
		return CommonConstants.SUCCESS;
	}

	public static String findAllExamTopicMappingRecordsByExamId(HttpServletRequest request,
			HttpServletResponse response) {

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
	
	

}
