package com.vastpro.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
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
import com.vastpro.validator.ExamMasterCheck;
import com.vastpro.validator.ExamMasterValidator;
import com.vastpro.validator.HibernateHelper;

public class ExamMasterEvents {
	
	public static final String module = ExamMasterEvents.class.getName();
	
	public static String createExam(HttpServletRequest request, HttpServletResponse response) {	
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Map<String,Object> combinedMap=UtilHttp.getCombinedMap(request);	
		Locale locale= request.getLocale();
		
		//Hibernate Validation for creating exam
		ExamMasterValidator createExamForm =HibernateHelper.populateBeanFromMap(combinedMap, ExamMasterValidator.class);	
		Set<ConstraintViolation<ExamMasterValidator>> createExamvalidationErrors =HibernateHelper.checkValidationErrors(createExamForm, ExamMasterCheck.class);
		Boolean hasformErrors= HibernateHelper.validateFormSubmission(delegator, createExamvalidationErrors , request, locale, "InvalidErrMsg", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute(CommonConstants.HAS_FORM_ERROR, hasformErrors);
		if(hasformErrors){
			
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			Debug.logError("Errors in  createExam Form Validation ", module);
			return CommonConstants.ERROR;		
		}
		
		//If Has No Hiberate Validation Error createExam Service is called 
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		
		String examId = (String) combinedMap.get(CommonConstants.EXAM_ID);
		String examName = (String) combinedMap.get(CommonConstants.EXAM_NAME);
		String description = (String) combinedMap.get(CommonConstants.DESCRIPTION);
		String creationDate = (String) combinedMap.get(CommonConstants.CREATION_DATE);
		String expirationDate = (String) combinedMap.get(CommonConstants.EXPIRATION_DATE);
		String noOfQuestions = (String) combinedMap.get(CommonConstants.NO_OF_QUESTIONS);
		String durationMinutes = (String) combinedMap.get(CommonConstants.DURATION_MINUTES);
		String passPercentage = (String) combinedMap.get(CommonConstants.PASS_PERCENTAGE);
		String questionsRandomized = (String) combinedMap.get(CommonConstants.QUESTIONS_RANDOMIZED);
		String answersMust = (String) combinedMap.get(CommonConstants.ANSWER_MUST);
		String enableNegativeMark = (String) combinedMap.get(CommonConstants.ENABLE_NEGATIVE_MARK);
		String negativeMarkValue = (String) combinedMap.get(CommonConstants.NEGATIVE_MARK_VALUE);
	
		//creating map to pass required context to the service called 
		Map<String, Object> addExamContext = UtilMisc.toMap(
				CommonConstants.EXAM_ID, examId,
				CommonConstants.EXAM_NAME,examName,
				CommonConstants.DESCRIPTION, description,
				CommonConstants.CREATION_DATE, creationDate,
				CommonConstants.EXPIRATION_DATE, expirationDate,
				CommonConstants.NO_OF_QUESTIONS, noOfQuestions,
				CommonConstants.DURATION_MINUTES, durationMinutes,
				CommonConstants.PASS_PERCENTAGE, passPercentage,
				CommonConstants.QUESTIONS_RANDOMIZED, questionsRandomized,
				CommonConstants.ANSWER_MUST, answersMust,
				CommonConstants.ENABLE_NEGATIVE_MARK, enableNegativeMark,
				CommonConstants.NEGATIVE_MARK_VALUE,negativeMarkValue,
				CommonConstants.USER_LOGIN, userLogin);
		
		// creating ExamMaster Record by calling createExam service 
		Map<String, Object> createExamResp = null;
		try {
			createExamResp = dispatcher.runSync(CommonConstants.CREATE_EXAM, addExamContext);	
			Debug.logInfo("Successfully executed createExam Service", module);
		} catch (GenericServiceException e) {
			 Debug.logError(e, "Failed to execute createExam service", module);
			 String errMsg = "Failed to execute createExam service : " + e.getMessage();
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;
		}
		
		if (ServiceUtil.isSuccess(createExamResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);		
			//constructing exam as mapObject
			Map<String, Object> exam = new HashMap<>();			
			exam.put(CommonConstants.EXAM_ID,createExamResp.get(CommonConstants.EXAM_ID) );
			exam.put(CommonConstants.EXAM_NAME,createExamResp.get(CommonConstants.EXAM_NAME) );
			exam.put(CommonConstants.DESCRIPTION,createExamResp.get(CommonConstants.DESCRIPTION) );
			exam.put(CommonConstants.CREATION_DATE,createExamResp.get(CommonConstants.CREATION_DATE) );
			exam.put(CommonConstants.EXPIRATION_DATE,createExamResp.get(CommonConstants.EXPIRATION_DATE) );
			exam.put(CommonConstants.NO_OF_QUESTIONS,createExamResp.get(CommonConstants.NO_OF_QUESTIONS) );
			exam.put(CommonConstants.DURATION_MINUTES,createExamResp.get(CommonConstants.DURATION_MINUTES) );
			exam.put(CommonConstants.PASS_PERCENTAGE,createExamResp.get(CommonConstants.PASS_PERCENTAGE) );
			exam.put(CommonConstants.QUESTIONS_RANDOMIZED,createExamResp.get(CommonConstants.QUESTIONS_RANDOMIZED) );
			exam.put(CommonConstants.ANSWER_MUST,createExamResp.get(CommonConstants.ANSWER_MUST) );
			exam.put(CommonConstants.ENABLE_NEGATIVE_MARK,createExamResp.get(CommonConstants.ENABLE_NEGATIVE_MARK) );
			exam.put(CommonConstants.NEGATIVE_MARK_VALUE,createExamResp.get(CommonConstants.NEGATIVE_MARK_VALUE) );		
			request.setAttribute("exam", exam);
			
		}
		else {
			 String errMsg = "Error while executing createExam service";
			 Debug.logError(errMsg, module);	
			 request.setAttribute(CommonConstants.RESULT_MAP,createExamResp);
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;		
		}
		return CommonConstants.SUCCESS;

	}
	
	//Method to retrieve all the exams from ExamMaster entity
	public static String findAllExams(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		//Creation of a map to send the context to the service 
		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstants.USER_LOGIN, userLogin);

		//Calling the service which in return provides the list of exams
		Map<String, Object> findAllExamsResp = null;
		try {
			findAllExamsResp = dispatcher.runSync("findAllExams", findExamContext);	
			Debug.logInfo("Successfully executed findAllExams service", module);
		} catch (GenericServiceException e) {
			 Debug.logError(e, "Failed to execute findAllExams service", module);
			 String errMsg = "Failed to execute findAllExams service : " + e.getMessage();
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;
		}
		//If the service returns success result and  examList is added to the request
		if (ServiceUtil.isSuccess(findAllExamsResp)) {	
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute("examList", findAllExamsResp.get("examList"));
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = "Error while executing findAllExams service";
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,findAllExamsResp);
			return CommonConstants.ERROR;
		}
		return CommonConstants.SUCCESS;

	}

	//Method to find an exam by using examId from ExamMaster entity
	public static String findExamById(HttpServletRequest request, HttpServletResponse response) {
		
		
		//Getting the examId and other required attributed from the request
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		String examId = request.getParameter(CommonConstants.EXAM_ID);

		//Map is created to sent it as context to  the service called
		Map<String, Object> findExamByIdContext = new HashMap<>();
		findExamByIdContext.put(CommonConstants.USER_LOGIN, userLogin);
		findExamByIdContext.put(CommonConstants.EXAM_ID, examId);
		
		//Calling the findExamById  service which  return the exam detail for given the examId
		Map<String, Object> findExamByIdResp = null;
		try {
			findExamByIdResp = dispatcher.runSync("findExamById", findExamByIdContext);
			Debug.logInfo("Successfully executed findExamById service", module);
		} catch (GenericServiceException e) {
			
			 Debug.logError(e, "Failed to execute findExamById service", module);
			 String errMsg = "Failed to execute findExamById service : " + e.getMessage();
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;
       
		}
		
		//If the service returns success result and exam record is added to the request
		if (ServiceUtil.isSuccess(findExamByIdResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute("exam", findExamByIdResp.get("exam"));
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = "Error while executing findExamById service";
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg); 
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,findExamByIdResp);	
			return CommonConstants.ERROR;
		}
		
		return CommonConstants.SUCCESS;
	}

	//Method to delete an exam from ExamMaster entity
	public static String deleteExam(HttpServletRequest request, HttpServletResponse response) {
		
		//Getting the examId and other required objects from the request	
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		String examId = request.getParameter(CommonConstants.EXAM_ID);
		
	
		//Creation of a map to send the context to the service 
		Map<String, Object> deleteExamContext = new HashMap<>();
		deleteExamContext.put(CommonConstants.USER_LOGIN, userLogin);
		deleteExamContext.put(CommonConstants.EXAM_ID, examId);
		
		Map<String, Object> deleteExamResp = null;
		
		//Calling the service which deletes the exam
		try {
			deleteExamResp = dispatcher.runSync("deleteExam", deleteExamContext);
			Debug.logInfo("Successfully executed deleteExam service", module);
			
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute deleteExam service", module);
			String errMsg = "Failed to execute deleteExam service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;

		}
		//If the exam is deleted successfully, the response Map object  is set in request
		if (ServiceUtil.isSuccess(deleteExamResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.RESULT_MAP, deleteExamResp);
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = "Error while executing deleteExam service";
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg); 
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,deleteExamResp);
			return CommonConstants.ERROR;
		}

		return CommonConstants.SUCCESS;
	}
	
	//Method to retrieve all the exams from ExamMaster entity
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








