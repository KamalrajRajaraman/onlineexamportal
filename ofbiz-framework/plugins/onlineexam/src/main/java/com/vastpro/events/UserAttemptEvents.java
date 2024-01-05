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
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class UserAttemptEvents {
	public static final String module = ExamMasterEvents.class.getName();
	
	public static String createUserAttemptMaster(HttpServletRequest request, HttpServletResponse response) {
		
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
	  
		//Retrieving data from front end
		String examId = (String) request.getAttribute(CommonConstants.EXAM_ID);
		String partyId = (String) request.getAttribute(CommonConstants.PARTY_ID);
		String performanceId= (String) request.getAttribute(CommonConstants.PERFORMANCE_ID);	
		
		if(UtilValidate.isEmpty(examId)) {
			examId = request.getParameter(CommonConstants.EXAM_ID);
		}
		if(UtilValidate.isEmpty(partyId)) {
			partyId = request.getParameter(CommonConstants.PARTY_ID);
		}
		if(UtilValidate.isEmpty(performanceId)) {
			performanceId = request.getParameter(CommonConstants.PERFORMANCE_ID);
		}
		
		if(UtilValidate.isEmpty(partyId)) {
			partyId = (String) request.getSession().getAttribute(CommonConstants.PARTY_ID);
		}
		
		//calculated attemptNumber by counting no. of records in UserAttemptMaster for PartyId and examId
		long prevAttempt =0;	
		try {
			prevAttempt = EntityQuery.use(delegator)
			.select("count(*)")
			.from(CommonConstants.USER_ATTEMPT_MASTER)
			.where(CommonConstants.PARTY_ID,partyId,CommonConstants.EXAM_ID,examId)
			.queryCount();
		} catch (GenericEntityException e) {
			Debug.logError(e, "Unable to retrieve previous no of Attempt from UserAttemptMaster", module);
			String errMsg = "Unable to retrieve previous no of Attempt from UserAttemptMaster : " + e.getMessage();
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		long attemptNumber = prevAttempt+1;
		
		//Map to provide context to the service
		Map<String, Object> attemptMasterCtx = new HashMap<String, Object>();
		attemptMasterCtx.put(CommonConstants.USER_LOGIN, userLogin);
		attemptMasterCtx.put(CommonConstants.EXAM_ID, examId);
		attemptMasterCtx.put(CommonConstants.PARTY_ID, partyId);
		attemptMasterCtx.put(CommonConstants.PERFORMANCE_ID, performanceId);
		attemptMasterCtx.put(CommonConstants.ATTEMPT_NUMBER, attemptNumber);
		
		//Getting noOfQuestions from  ExamMaster entity 
		Map<String, Object> noOfQuestionResp = null;
		if (UtilValidate.isEmpty(performanceId)) {
			
				try {
					noOfQuestionResp = dispatcher.runSync("findNoOfQuestionCountByExamID", attemptMasterCtx);
				} catch (GenericServiceException e) {
					
					Debug.logError(e, "Failed to execute findNoOfQuestionCountByExamID service", module);
					String errMsg = "Failed to execute findNoOfQuestionCountByExamID service : " + e.getMessage();
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
		            return CommonConstants.ERROR;
		            
				}
				
				//creating  UserAttemptMaster record 
				if(ServiceUtil.isSuccess(noOfQuestionResp)) {
					Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstants.NO_OF_QUESTIONS);							
					attemptMasterCtx.put(CommonConstants.NO_OF_QUESTIONS, noOfQuestion);
					Map<String, Object> createUserAttemptMasterResp=null;
					 try {
						 
						 //Call the service to create the entries provided through the map
						 createUserAttemptMasterResp = dispatcher.runSync("createUserAttemptMaster", attemptMasterCtx);
					} catch (GenericServiceException e) {
						
						Debug.logError(e, "Failed to execute createUserAttemptMaster service", module);
						String errMsg = "Failed to execute createUserAttemptMaster service : " + e.getMessage();
						request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
						request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			            return CommonConstants.ERROR;
						
					}
					 
					 if(ServiceUtil.isSuccess(createUserAttemptMasterResp)) {
						 
						//Converting the Generic value to String
						 performanceId =  String.valueOf(createUserAttemptMasterResp.get(CommonConstants.PERFORMANCE_ID));
						 
						 //findAllExamTopicMappingRecordsByExamId
						 String eventResp = ExamTopicMappingMasterEvents.findAllExamTopicMappingRecordsByExamId(request, response);
						
						 //creating UserAttemptTopicMaster 
						 if(CommonConstants.SUCCESS.equals(eventResp)) {
							 
							 List<Map<String, Object>>  examTopicMappingRespList  =  (List<Map<String, Object>>) request.getAttribute("examTopicMappingRecordList");
							 List< Map<String, Object>> topicList = new LinkedList<>();
							 Map<String, Object> createUserAttemptTopicMasterResp = null;
							 
							 //The objects in the list are added to a map
							 for(Map<String,Object> examTopicMappingRecord:examTopicMappingRespList) {
								 String topicId=(String) examTopicMappingRecord.get(CommonConstants.TOPIC_ID);
								 String topicPassPercentage = (String) examTopicMappingRecord.get(CommonConstants.TOPIC_PASS_PERCENTAGE);
								 String totalQuestionsInThisTopic =(String) examTopicMappingRecord.get(CommonConstants.QUESTION_PER_EXAM);
								 attemptMasterCtx.put(CommonConstants.PERFORMANCE_ID, performanceId);
								 attemptMasterCtx.put(CommonConstants.TOPIC_ID, topicId);
								 attemptMasterCtx.put(CommonConstants.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
								 attemptMasterCtx.put(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC, totalQuestionsInThisTopic);
								
								 try {
									 //The service to create entries in the UserAttemptTopicMaster is called
									 createUserAttemptTopicMasterResp=dispatcher.runSync("createUserAttemptTopicMaster", attemptMasterCtx);
								} catch (GenericServiceException e) {
									
									Debug.logError(e, "Failed to execute createUserAttemptTopicMaster service", module);
									String errMsg = "Failed to execute createUserAttemptTopicMaster service : " + e.getMessage();
									 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
									 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
						            return CommonConstants.ERROR;
									
								}
								 
								 
								 if(ServiceUtil.isSuccess(createUserAttemptTopicMasterResp)){
									 
									 topicList.add(UtilMisc.toMap(
											 CommonConstants.TOPIC_ID, createUserAttemptTopicMasterResp.get(CommonConstants.TOPIC_ID),
											 CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC, createUserAttemptTopicMasterResp.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC)
											 ));
								 }	 
							 }
							 
							 if(UtilValidate.isNotEmpty(topicList)) {
								 
								 attemptMasterCtx.put("topicList", topicList);
								 Map<String, Object> findQuestionsByTopicIdsResp=null;
								 try {
									 findQuestionsByTopicIdsResp = dispatcher.runSync("findQuestionsByTopicIds", attemptMasterCtx );
								} catch (GenericServiceException e) {
									
									Debug.logError(e, "Failed to execute findQuestionsByTopicIds service", module);
									String errMsg = "Failed to execute findQuestionsByTopicIds service : " + e.getMessage();
									request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
									request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
						            return CommonConstants.ERROR;
																		
								}
								 
								 //Selecting random Questions 
								 List<String> randomQuestions = new LinkedList<>();
								 
								 if(ServiceUtil.isSuccess(findQuestionsByTopicIdsResp)){									
									List<Map<String, Object>> topicWiseQuestions =(List<Map<String, Object>>) findQuestionsByTopicIdsResp.get("topicList");
									for(Map<String, Object> topic:topicWiseQuestions) {
									
										Integer totalQuestionsInThisTopic =(Integer) topic.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC);
										List<String> questionIdList = (List<String>) topic.get("questionIdList");
										
										Random rd = new Random();
										//checking totalQuestionsInThisTopic  is not null
										if(UtilValidate.isNotEmpty(totalQuestionsInThisTopic)) {
											
											for (int i = 0; i < totalQuestionsInThisTopic; i++) {
												if(UtilValidate.isNotEmpty(questionIdList)) {
													int rand = rd.nextInt(questionIdList.size());
													randomQuestions.add(questionIdList.get(rand));
													questionIdList.remove(rand);
												}
											}
										}
										
									}	
									//Creating UserAttemptAnswerMaster Records
									Integer sequenceNum = 1;
									List<Map<String,Object>> insertedQuestions = new LinkedList<>();
									for(String questionId: randomQuestions) {
										Map<String, Object> createUAAMSResp =null;
										try {
											
											createUAAMSResp = dispatcher.runSync("createUserAttemptAnswerMasterService" , 
													UtilMisc.toMap(CommonConstants.QUESTION_ID, questionId, 
															CommonConstants.PERFORMANCE_ID, performanceId,
															CommonConstants.SEQUENCE_NUM,sequenceNum++, "userLogin" , userLogin
													));
											
										}
										catch (GenericServiceException e) {
											
											Debug.logError(e, "Failed to execute createUserAttemptAnswerMasterService service", module);
											String errMsg = "Failed to execute createUserAttemptAnswerMasterService service : " + e.getMessage();
											request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
											request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
								            return CommonConstants.ERROR;
								            
										}
										
										if (ServiceUtil.isSuccess(createUAAMSResp)) {
											insertedQuestions.add(UtilMisc.toMap(
													CommonConstants.PERFORMANCE_ID,createUAAMSResp.get(CommonConstants.PERFORMANCE_ID),
													CommonConstants.SEQUENCE_NUM,createUAAMSResp.get(CommonConstants.SEQUENCE_NUM),
													CommonConstants.QUESTION_ID,createUAAMSResp.get(CommonConstants.QUESTION_ID)
													));
											
											Debug.logInfo("=======Created UserAttemptAnswerMaster record  using service createUserAttemptAnswerMaster=========", module);
										}
									}
									
									if(UtilValidate.isNotEmpty(insertedQuestions)) {
										
										Map<String, Object> RetriveQuestions = new HashMap<>();
										RetriveQuestions.put(CommonConstants.USER_LOGIN, userLogin);
										
										for(Map<String,Object> question :insertedQuestions) {	
											RetriveQuestions.put(CommonConstants.QUESTION_ID, question.get(CommonConstants.QUESTION_ID));
											Map<String, Object> resultMap = null;
											try {
												resultMap = dispatcher.runSync("findQuestionById", RetriveQuestions);
											} catch (GenericServiceException e) {			
												
												Debug.logError(e, "Failed to execute findQuestionById service", module);
												String errMsg = "Failed to execute findQuestionById service : " + e.getMessage();
												 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
												 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
									            return CommonConstants.ERROR;
												
											}

											if (ServiceUtil.isSuccess(resultMap)) {
												request.setAttribute("result", resultMap.get(CommonConstants.RESPONSE_MESSAGE));
												Map<String,Object> questionResult  = (Map<String, Object>) resultMap.get("question");
												question.put(CommonConstants.QUESTION_ID, questionResult.get(CommonConstants.QUESTION_ID));
												question.put(CommonConstants.QUESTION_DETAIL, questionResult.get(CommonConstants.QUESTION_DETAIL));
												question.put(CommonConstants.OPTION_A, questionResult.get(CommonConstants.OPTION_A));
												question.put(CommonConstants.OPTION_B, questionResult.get(CommonConstants.OPTION_B));
												question.put(CommonConstants.OPTION_C, questionResult.get(CommonConstants.OPTION_C));
												question.put(CommonConstants.OPTION_D, questionResult.get(CommonConstants.OPTION_D));
												question.put(CommonConstants.OPTION_E, questionResult.get(CommonConstants.OPTION_E));
												question.put(CommonConstants.QUESTION_TYPE, questionResult.get(CommonConstants.QUESTION_TYPE));				
												question.put(CommonConstants.TOPIC_ID, questionResult.get(CommonConstants.TOPIC_ID));
											
											}			
										}
									}									
									request.setAttribute("selectedQuestion", insertedQuestions);				
								}
							 }
						 }	 
					 }
				}
		}	
		return CommonConstants.SUCCESS;
	}	

}
