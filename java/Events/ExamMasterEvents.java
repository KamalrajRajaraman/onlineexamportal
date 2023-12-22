package com.vastpro.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

public class ExamMasterEvents {
	
	public static final String module = ExamMasterEvents.class.getName();

	public static String createExam(HttpServletRequest request, HttpServletResponse response) {

		
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		// Getting Add Exam Form data which is sent as Json Object from React
		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		String examName = (String) request.getAttribute(CommonConstant.EXAM_NAME);
		String description = (String) request.getAttribute(CommonConstant.DESCRIPTION);
		String creationDate = (String) request.getAttribute(CommonConstant.CREATION_DATE);
		String expirationDate = (String) request.getAttribute(CommonConstant.EXPIRATION_DATE);
		String noOfQuestions = (String) request.getAttribute(CommonConstant.NO_OF_QUESTIONS);
		String durationMinutes = (String) request.getAttribute(CommonConstant.DURATION_MINUTES);
		String passPercentage = (String) request.getAttribute(CommonConstant.PASS_PERCENTAGE);
		String questionsRandomized = (String) request.getAttribute(CommonConstant.QUESTIONS_RANDOMIZED);
		String answersMust = (String) request.getAttribute(CommonConstant.ANSWER_MUST);
		String enableNegativeMark = (String) request.getAttribute(CommonConstant.ENABLE_NEGATIVE_MARK);
		String negativeMarkValue = (String) request.getAttribute(CommonConstant.NEGATIVE_MARK_VALUE);
		
		
		//creating map to pass required context to the service called 
		Map<String, Object> addExamContext = UtilMisc.toMap(
				CommonConstant.EXAM_ID, examId,
				CommonConstant.EXAM_NAME,examName,
				CommonConstant.DESCRIPTION, description,
				CommonConstant.CREATION_DATE, creationDate,
				CommonConstant.EXPIRATION_DATE, expirationDate,
				CommonConstant.NO_OF_QUESTIONS, noOfQuestions,
				CommonConstant.DURATION_MINUTES, durationMinutes,
				CommonConstant.PASS_PERCENTAGE, passPercentage,
				CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized,
				CommonConstant.ANSWER_MUST, answersMust,
				CommonConstant.ENABLE_NEGATIVE_MARK, enableNegativeMark,
				CommonConstant.NEGATIVE_MARK_VALUE,negativeMarkValue,
				CommonConstant.USER_LOGIN, userLogin);
		
		// creating ExamMaster Record by calling createExam service 
		Map<String, Object> createExamResp = null;
		try {
			
			createExamResp = dispatcher.runSync("createExam", addExamContext);

			
			Debug.logInfo("=======Creating ExamMAster record in event using service createExam=========", module);
		} catch (GenericServiceException e) {

			String errMsg = "Unable to create new records in ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		
		if (ServiceUtil.isSuccess(createExamResp)) {
			request.setAttribute("result", createExamResp.get(CommonConstant.RESPONSE_MESSAGE));
			
			
			//constructing exam as mapObject
			Map<String, Object> exam = new HashMap<>();			
			exam.put(CommonConstant.EXAM_ID,createExamResp.get(CommonConstant.EXAM_ID) );
			exam.put(CommonConstant.EXAM_NAME,createExamResp.get(CommonConstant.EXAM_NAME) );
			exam.put(CommonConstant.DESCRIPTION,createExamResp.get(CommonConstant.DESCRIPTION) );
			exam.put(CommonConstant.CREATION_DATE,createExamResp.get(CommonConstant.CREATION_DATE) );
			exam.put(CommonConstant.EXPIRATION_DATE,createExamResp.get(CommonConstant.EXPIRATION_DATE) );
			exam.put(CommonConstant.NO_OF_QUESTIONS,createExamResp.get(CommonConstant.NO_OF_QUESTIONS) );
			exam.put(CommonConstant.DURATION_MINUTES,createExamResp.get(CommonConstant.DURATION_MINUTES) );
			exam.put(CommonConstant.PASS_PERCENTAGE,createExamResp.get(CommonConstant.PASS_PERCENTAGE) );
			exam.put(CommonConstant.QUESTIONS_RANDOMIZED,createExamResp.get(CommonConstant.QUESTIONS_RANDOMIZED) );
			exam.put(CommonConstant.ANSWER_MUST,createExamResp.get(CommonConstant.ANSWER_MUST) );
			exam.put(CommonConstant.ENABLE_NEGATIVE_MARK,createExamResp.get(CommonConstant.ENABLE_NEGATIVE_MARK) );
			exam.put(CommonConstant.NEGATIVE_MARK_VALUE,createExamResp.get(CommonConstant.NEGATIVE_MARK_VALUE) );
		
			request.setAttribute("exam", exam);
			
		}


		return CommonConstant.SUCCESS;

	}

	public static String findAllExams(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> examList = null;

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);

		try {
			examList = dispatcher.runSync("findAllExams", findExamContext);
			if (ServiceUtil.isSuccess(examList)) {
				request.setAttribute("examList", examList.get("examList"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrieve  records  from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

	}

	public static String findExamById(HttpServletRequest request, HttpServletResponse response) {
		

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String examId = request.getParameter(CommonConstant.EXAM_ID);

		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstant.EXAM_ID, examId);
		
		
		Map<String, Object> examResult = null;
		try {
			examResult = dispatcher.runSync("findExamById", findExamContext);
			if (ServiceUtil.isSuccess(examResult)) {
				request.setAttribute("result", examResult.get(CommonConstant.RESPONSE_MESSAGE));
				request.setAttribute("exam", examResult.get("exam"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrieve  records  from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

	}

	public static String deleteExam(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String examId = request.getParameter(CommonConstant.EXAM_ID);

		Map<String, Object> deleteExamContext = new HashMap<>();
		deleteExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		deleteExamContext.put(CommonConstant.EXAM_ID, examId);
		Map<String, Object> serviceResultMap = null;
		try {
			serviceResultMap = dispatcher.runSync("deleteExam", deleteExamContext);
			
		} catch (GenericServiceException e) {
			String errMsg = "Unable to delete  record  from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		
		if (ServiceUtil.isSuccess(serviceResultMap)) {
			request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
			request.setAttribute("resultMap", serviceResultMap);
		}

		return CommonConstant.SUCCESS;
	}
	
	
	public static String createUserAttemptTopicMaster(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
	  
		//Retrieving data from front end
		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		String partyId = (String) request.getAttribute(CommonConstant.PARTY_ID);
		String performanceId= (String) request.getAttribute(CommonConstant.PERFORMANCE_ID);	
		if(UtilValidate.isEmpty(examId)) {
			examId = request.getParameter(CommonConstant.EXAM_ID);
		}
		if(UtilValidate.isEmpty(partyId)) {
			partyId = request.getParameter(CommonConstant.PARTY_ID);
		}
		if(UtilValidate.isEmpty(performanceId)) {
			performanceId = request.getParameter(CommonConstant.PERFORMANCE_ID);
		}
		
		
		Map<String, Object> attemptMasterCtx = new HashMap<String, Object>();
		attemptMasterCtx.put(CommonConstant.USER_LOGIN, userLogin);
		attemptMasterCtx.put(CommonConstant.EXAM_ID, examId);
		attemptMasterCtx.put(CommonConstant.PARTY_ID, partyId);
		attemptMasterCtx.put(CommonConstant.PERFORMANCE_ID, performanceId);
		
		//Getting noOfQuestions from  ExamMaster entity 
		Map<String, Object> noOfQuestionResp = null;
		if (UtilValidate.isEmpty(performanceId)) {
			
				try {
					noOfQuestionResp = dispatcher.runSync("findNoOfQuestionCountByExamID", attemptMasterCtx);
				} catch (GenericServiceException e) {
					String errMsg = "Unable to retrieve  noOfQuestions from ExamMaster entity: " + e.toString();
					request.setAttribute("_ERROR_MESSAGE_", errMsg);
					request.setAttribute("result", CommonConstant.ERROR);
					return CommonConstant.ERROR;
				}
				
				//creating  UserAttemptMaster record 
				if(ServiceUtil.isSuccess(noOfQuestionResp)) {
					Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstant.NO_OF_QUESTIONS);							
					attemptMasterCtx.put(CommonConstant.NO_OF_QUESTIONS, noOfQuestion);
					Map<String, Object> createUserAttemptMasterResp=null;
					 try {
						 createUserAttemptMasterResp = dispatcher.runSync("createUserAttemptMaster", attemptMasterCtx);
					} catch (GenericServiceException e) {
						String errMsg = "Unable to create records in UserAttemptMaster entity: " + e.toString();
						request.setAttribute("_ERROR_MESSAGE_", errMsg);
						request.setAttribute("result", CommonConstant.ERROR);
						return CommonConstant.ERROR;
					}
					 
					 if(ServiceUtil.isSuccess(createUserAttemptMasterResp)) {
						 performanceId =  String.valueOf(createUserAttemptMasterResp.get(CommonConstant.PERFORMANCE_ID));
						 
						 //findAllExamTopicMappingRecordsByExamId
						 String eventResp = ExamTopicMappingMasterEvents.findAllExamTopicMappingRecordsByExamId(request, response);
						
						 //creating UserAttemptTopicMaster 
						 if(CommonConstant.SUCCESS.equals(eventResp)) {
							 
							 List<Map<String, Object>>  examTopicMappingRespList  =  (List<Map<String, Object>>) request.getAttribute("examTopicMappingRecordList");
							 List< Map<String, Object>> topicList = new LinkedList<>();
							 Map<String, Object> createUserAttemptTopicMasterResp = null;
							 
							 for(Map<String,Object> examTopicMappingRecord:examTopicMappingRespList) {
								 String topicId=(String) examTopicMappingRecord.get(CommonConstant.TOPIC_ID);
								 String topicPassPercentage = (String) examTopicMappingRecord.get(CommonConstant.TOPIC_PASS_PERCENTAGE);
								 String totalQuestionsInThisTopic =(String) examTopicMappingRecord.get(CommonConstant.QUESTION_PER_EXAM);
								 attemptMasterCtx.put(CommonConstant.PERFORMANCE_ID, performanceId);
								 attemptMasterCtx.put(CommonConstant.TOPIC_ID, topicId);
								 attemptMasterCtx.put(CommonConstant.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
								 attemptMasterCtx.put(CommonConstant.TOTAL_QUESTIONS_IN_THIS_TOPIC, totalQuestionsInThisTopic);
								
								 try {
									 createUserAttemptTopicMasterResp=dispatcher.runSync("createUserAttemptTopicMaster", attemptMasterCtx);
								} catch (GenericServiceException e) {
									String errMsg = "Unable to create records in UserAttemptTopicMaster entity: " + e.toString();
									request.setAttribute("_ERROR_MESSAGE_", errMsg);
									request.setAttribute("result", CommonConstant.ERROR);
									return CommonConstant.ERROR;
								}
								 
								 
								 if(ServiceUtil.isSuccess(createUserAttemptTopicMasterResp)){
									 
									 topicList.add(UtilMisc.toMap(
											 CommonConstant.TOPIC_ID, createUserAttemptTopicMasterResp.get(CommonConstant.TOPIC_ID),
											 CommonConstant.TOTAL_QUESTIONS_IN_THIS_TOPIC, createUserAttemptTopicMasterResp.get(CommonConstant.TOTAL_QUESTIONS_IN_THIS_TOPIC)
											 ));
								 }	 
							 }
							 if(UtilValidate.isNotEmpty(topicList)) {
								 
								 attemptMasterCtx.put("topicList", topicList);
								 Map<String, Object> findQuestionsByTopicIdsResp=null;
								 try {
									 findQuestionsByTopicIdsResp = dispatcher.runSync("findQuestionsByTopicIds", attemptMasterCtx );
								} catch (GenericServiceException e) {
									String errMsg = "Unable to retrive QuestionIds from QuestionMaster entity: " + e.toString();
									request.setAttribute("_ERROR_MESSAGE_", errMsg);
									request.setAttribute("result", CommonConstant.ERROR);
									return CommonConstant.ERROR;									
								}
								 
								 List<String> randomQuestions = new LinkedList<>();
								 
								 if(ServiceUtil.isSuccess(findQuestionsByTopicIdsResp)){
									 
									
									List<Map<String, Object>> topicWiseQuestions =(List<Map<String, Object>>) findQuestionsByTopicIdsResp.get("topicList");
									for(Map<String, Object> topic:topicList) {
										
										Integer totalQuestionsInThisTopic =(Integer) topic.get(CommonConstant.TOTAL_QUESTIONS_IN_THIS_TOPIC);
										List<String> questionIdList = (List<String>) topic.get("questionIdList");
										
										Random rd = new Random();
										
										for (int i = 0; i < totalQuestionsInThisTopic; i++) {
											if(UtilValidate.isNotEmpty(questionIdList)) {
												int rand = rd.nextInt(questionIdList.size());
												randomQuestions.add(questionIdList.get(rand));
												questionIdList.remove(rand);
											}
										}
										
									}
									request.setAttribute("selectedQuestion", randomQuestions);		
											
								}
							 }
						 }	 
					 }
				}
		}	
		return CommonConstant.SUCCESS;
	}
	
	
}
