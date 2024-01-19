package com.vastpro.onlineexam.helper;

import java.math.BigDecimal;
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
	
	/**
	 * Create a userExamMapping Record Map if userExamMappingMaster Generic Value is given
	 * @param userExamMappingGV
	 * @return
	 * @author Kamalraj
	 */
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
	
	/**
	 * findExamById method is run findExamById service and returns the result as Map Object
	 * @param dispatcher
	 * @param findExamByIdContext
	 * @return
	 */
	public static Map<String,Object> findExamById(LocalDispatcher dispatcher , Map<String,? extends Object> findExamByIdContext){
		Map<String, Object> findExamByIdResp = null;
		try {
			findExamByIdResp = dispatcher.runSync(CommonConstants.FIND_EXAM_BY_ID, findExamByIdContext);
			Debug.logInfo("Successfully executed findExamById service", module);
		} catch (GenericServiceException e) {			
			 String errMsg = "Failed to execute findExamById service : " + e.getMessage();
			 return ServiceUtil.returnError(errMsg);
             
       
		}
		return findExamByIdResp;
		
	}
	
	/**
	 * findTopicById method is run findTopicById service and returns the result as Map Object
	 * @param dispatcher
	 * @param findExamByIdContext
	 * @return
	 * 
	 * @author Kamalraj
	 */
	public static Map<String,Object> findTopicById(LocalDispatcher dispatcher , Map<String,? extends Object> findExamByIdContext){
		Map<String, Object> findTopicByIdResp = null;
		try {
			findTopicByIdResp = dispatcher.runSync(CommonConstants.FIND_TOPIC_BY_ID, findExamByIdContext);
			Debug.logInfo("Successfully executed findExamById service", module);
		} catch (GenericServiceException e) {			
			 String errMsg = "Failed to execute findExamById service : " + e.getMessage();
			 return ServiceUtil.returnError(errMsg);
             
       
		}
		return findTopicByIdResp;
		
	}
	
	
	/**
	 * This helper method is used to convert Question GV to Map
	 * @param GenericValue 
	 * @return Map<String,Object>	
	 */
	public static Map<String, Object> getQuestionFromGenericValue(GenericValue genericValue) {
		//Extract question fields from genericValue object 
		Long questionId = genericValue.getLong(CommonConstants.QUESTION_ID);
		String questionDetail = genericValue.getString(CommonConstants.QUESTION_DETAIL);
		String optionA = genericValue.getString(CommonConstants.OPTION_A);
		String optionB = genericValue.getString(CommonConstants.OPTION_B);
		String optionC = genericValue.getString(CommonConstants.OPTION_C);
		String optionD = genericValue.getString(CommonConstants.OPTION_D);
		String optionE = genericValue.getString(CommonConstants.OPTION_E);
		String answer = genericValue.getString(CommonConstants.ANSWER);
		Long numAnswers = genericValue.getLong(CommonConstants.NUM_ANSWERS);
		String questionType = genericValue.getString(CommonConstants.QUESTION_TYPE);
		Integer difficultyLevel = genericValue.getInteger(CommonConstants.DIFFICULTY_LEVEL);
		BigDecimal answerValue = genericValue.getBigDecimal(CommonConstants.ANSWER_VALUE);
		String topicId = genericValue.getString(CommonConstants.TOPIC_ID);
		BigDecimal negativeMarkValue = genericValue.getBigDecimal(CommonConstants.NEGATIVE_MARK_VALUE);

		//Construct a map with the fields
		Map<String, Object> question = new HashMap<>();
		question.put(CommonConstants.QUESTION_ID, questionId);
		question.put(CommonConstants.QUESTION_DETAIL, questionDetail);
		question.put(CommonConstants.OPTION_A, optionA);
		question.put(CommonConstants.OPTION_B, optionB);
		question.put(CommonConstants.OPTION_C, optionC);
		question.put(CommonConstants.OPTION_D, optionD);
		question.put(CommonConstants.OPTION_E, optionE);
		question.put(CommonConstants.ANSWER, answer);
		question.put(CommonConstants.NUM_ANSWERS, numAnswers);
		question.put(CommonConstants.QUESTION_TYPE, questionType);
		question.put(CommonConstants.DIFFICULTY_LEVEL, difficultyLevel);
		question.put(CommonConstants.ANSWER_VALUE, answerValue);
		question.put(CommonConstants.TOPIC_ID, topicId);
		question.put(CommonConstants.NEGATIVE_MARK_VALUE, negativeMarkValue);
		
		return question;
	}

	

}
