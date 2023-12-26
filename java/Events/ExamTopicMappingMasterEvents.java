package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class ExamTopicMappingMasterEvents {
	
	public static final String module = ExamTopicMappingMasterEvents.class.getName();
	
	public static String createExamTopicMappingMasterRecord(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		
		//Getting records from front End 
		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		String topicId = (String) request.getAttribute(CommonConstant.TOPIC_ID);
		String percentage = (String) request.getAttribute(CommonConstant.PERCENTAGE);
		String topicPassPercentage = (String) request.getAttribute(CommonConstant.TOPIC_PASS_PERCENTAGE);
		if (examId == null) {
			examId = request.getParameter(CommonConstant.EXAM_ID);
		}
		if (topicId == null) {
			topicId = request.getParameter(CommonConstant.TOPIC_ID);
		}
		if (percentage == null) {
			percentage = request.getParameter(CommonConstant.PERCENTAGE);
		}
		if (topicPassPercentage == null) {
			topicPassPercentage = request.getParameter(CommonConstant.TOPIC_PASS_PERCENTAGE);
		}

		
		//creating map to pass required context to the service called 
		Map<String, Object> addTopicToExamContextMap = new HashMap<>();
		addTopicToExamContextMap.put(CommonConstant.EXAM_ID, examId);
		addTopicToExamContextMap.put(CommonConstant.TOPIC_ID, topicId);
		addTopicToExamContextMap.put(CommonConstant.PERCENTAGE, percentage);
		addTopicToExamContextMap.put(CommonConstant.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
		addTopicToExamContextMap.put(CommonConstant.USER_LOGIN, userLogin);
		
		
		//Retrieving noOfQuestion from ExamMaster Entity to calculate questionPerExam 
		Map<String, Object>  noOfQuestionResp= null;
		try {
			noOfQuestionResp = dispatcher.runSync("findNoOfQuestionCountByExamID", addTopicToExamContextMap );
		} catch (GenericServiceException e) {
			
			String errMsg = "Unable to retrieve  noOfQuestions from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;

		}
		
		
		if (ServiceUtil.isSuccess(noOfQuestionResp)) {
			
			
			//questionPerExam   calculated using the business logic
			Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstant.NO_OF_QUESTIONS);			
			Integer percentageOfTopic   = Integer.parseInt(percentage);
			Long questionPerExam =  ((noOfQuestion*percentageOfTopic)/100);
			addTopicToExamContextMap.put(CommonConstant.QUESTION_PER_EXAM, questionPerExam);
			
			//creating examTopicMapping records
			Map<String, Object> serviceResultMap = null;
			try {
				serviceResultMap = dispatcher.runSync("addTopicToExam", addTopicToExamContextMap);
				
			} catch (GenericServiceException e) {
				String errMsg = "Unable to create  record  in ExamTopicMappingMaster entity: " + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				request.setAttribute("result", CommonConstant.ERROR);
				return CommonConstant.ERROR;
			}
			
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
				GenericValue insertedRecordGenericValue =null;
				try {
					insertedRecordGenericValue	 = EntityQuery.use(delegator)
					.from("ExamTopicMappingViewEntity")
					.where(CommonConstant.EXAM_ID,examId,CommonConstant.TOPIC_ID,topicId)
					.queryOne();
				} catch (GenericEntityException e) {
					e.printStackTrace();
				}
				if(UtilValidate.isNotEmpty(insertedRecordGenericValue)) {

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
	
	
	public static String findAllExamTopicMappingRecordsByExamId(HttpServletRequest request, HttpServletResponse response) {
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
				String errMsg = "Unable to fetch  records  from ExamTopicMappingMaster entity: " + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				request.setAttribute("result", CommonConstant.ERROR);
				return CommonConstant.ERROR;
			}
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
				request.setAttribute("examTopicMappingRecordList",serviceResultMap.get("examTopicMappingRecordList"));
				
			}
		
		
		return CommonConstant.SUCCESS;
	}
}
