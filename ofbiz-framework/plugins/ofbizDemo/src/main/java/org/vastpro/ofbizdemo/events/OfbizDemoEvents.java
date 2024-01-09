package org.vastpro.ofbizdemo.events;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;


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
import com.vastpro.onlineexam.helper.OnlineExamHelper;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.UserAttemptAnswerMasterCheck;
import com.vastpro.validator.UserAttemptMasterValidator;
public class OfbizDemoEvents {

 public static final String module = OfbizDemoEvents.class.getName();

    public static String createOfbizDemoEvent(HttpServletRequest request, HttpServletResponse response) {
    	
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String ofbizDemoTypeId = request.getParameter("ofbizDemoTypeId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (UtilValidate.isEmpty(firstName) || UtilValidate.isEmpty(lastName)) {
            String errMsg = "First Name and Last Name are required fields on the form and can't be empty.";
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String comments = request.getParameter("comments");

        try {
            Debug.logInfo("=======Creating OfbizDemo record in event using service createOfbizDemoByGroovyService=========", module);
            dispatcher.runSync("createOfbizDemoByGroovyService", UtilMisc.toMap("ofbizDemoTypeId", ofbizDemoTypeId,
                    "firstName", firstName, "lastName", lastName, "comments", comments, "userLogin", userLogin));
        } catch (GenericServiceException e) {
            String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        request.setAttribute("_EVENT_MESSAGE_", "OFBiz Demo created succesfully.");
        return "success";
    }
}





class UserAttemptEvents {
	public static final String module = UserAttemptEvents.class.getName();

	public static String createUserAttemptMaster(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		String errMsg = null;

		// Retrieving data from front end
		Locale locale = UtilHttp.getLocale(request);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		String examId = (String) combinedMap.get(CommonConstants.EXAM_ID);
		String performanceId = (String) combinedMap.get(CommonConstants.PERFORMANCE_ID);
		String partyId = userLogin.getString(CommonConstants.PARTY_ID);

		// Map to provide context to the service
		Map<String, Object> attemptMasterCtx = new HashMap<String, Object>();
		attemptMasterCtx.put(CommonConstants.USER_LOGIN, userLogin);
		attemptMasterCtx.put(CommonConstants.EXAM_ID, examId);
		attemptMasterCtx.put(CommonConstants.PARTY_ID, partyId);
		attemptMasterCtx.put(CommonConstants.PERFORMANCE_ID, performanceId);

		UserAttemptMasterValidator userAttemptMasterValidator = HibernateHelper.populateBeanFromMap(attemptMasterCtx,
				UserAttemptMasterValidator.class);
		Set<ConstraintViolation<UserAttemptMasterValidator>> userAttemptMasterValidationErrors = HibernateHelper
				.checkValidationErrors(userAttemptMasterValidator, UserAttemptAnswerMasterCheck.class);
		boolean hasValidationError = HibernateHelper.validateFormSubmission(delegator,
				userAttemptMasterValidationErrors, request, locale, "MissingRequiredFieldsErrMsg",
				CommonConstants.RESOURCE_ERROR, false);

		if (hasValidationError) {
			errMsg = UtilProperties.getMessage(CommonConstants.RESOURCE_ERROR, "MissingRequiredFieldsErrMsg", locale);
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		List<Map<String, Object>> insertedQuestions = new LinkedList<>();
		if (UtilValidate.isEmpty(performanceId)) {

			// calculated attemptNumber by counting no. of records in UserAttemptMaster for
			// PartyId and examId
			long prevAttempt = 0;
			try {
				prevAttempt = EntityQuery.use(delegator).select("count(*)").from(CommonConstants.USER_ATTEMPT_MASTER)
						.where(CommonConstants.PARTY_ID, partyId, CommonConstants.EXAM_ID, examId).queryCount();
			} catch (GenericEntityException e) {
				Debug.logError(e, "Unable to retrieve previous no of Attempt from UserAttemptMaster", module);
				errMsg = "Unable to retrieve previous no of Attempt from UserAttemptMaster : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			long attemptNumber = prevAttempt + 1;
			attemptMasterCtx.put(CommonConstants.ATTEMPT_NUMBER, attemptNumber);

			// Getting noOfQuestions from ExamMaster entity
			Map<String, Object> noOfQuestionResp = null;

			try {
				noOfQuestionResp = dispatcher.runSync("findNoOfQuestionCountByExamId", attemptMasterCtx);
			} catch (GenericServiceException e) {

				Debug.logError(e, "Failed to execute findNoOfQuestionCountByExamID service", module);
				errMsg = "Failed to execute findNoOfQuestionCountByExamID service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}
			
			if(ServiceUtil.isError(noOfQuestionResp)){
				errMsg = "Error while executing findNoOfQuestionCountByExamID service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
				
			}

			// creating UserAttemptMaster record
			if (ServiceUtil.isSuccess(noOfQuestionResp)) {
				Long noOfQuestion = (Long) noOfQuestionResp.get(CommonConstants.NO_OF_QUESTIONS);
				attemptMasterCtx.put(CommonConstants.NO_OF_QUESTIONS, noOfQuestion);
				Map<String, Object> createUserAttemptMasterResp = null;
				try {

					// Call the service to create the entries provided through the map
					createUserAttemptMasterResp = dispatcher.runSync("createUserAttemptMaster", attemptMasterCtx);
				} catch (GenericServiceException e) {

					Debug.logError(e, "Failed to execute createUserAttemptMaster service", module);
					errMsg = "Failed to execute createUserAttemptMaster service : " + e.getMessage();
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;

				}

				if (ServiceUtil.isSuccess(createUserAttemptMasterResp)) {

					// Converting the Generic value to String
					performanceId = String.valueOf(createUserAttemptMasterResp.get(CommonConstants.PERFORMANCE_ID));

					// findAllExamTopicMappingRecordsByExamId
					String eventResp = OnlineExamHelper.findAllExamTopicMappingRecordsByExamId(request);

					// creating UserAttemptTopicMaster
					if (CommonConstants.SUCCESS.equals(eventResp)) {

						List<Map<String, Object>> examTopicMappingRespList = (List<Map<String, Object>>) request.getAttribute("examTopicMappingRecordList");
						List<Map<String, Object>> topicList = new LinkedList<>();
						Map<String, Object> createUserAttemptTopicMasterResp = null;

						// The objects in the list are added to a map
						for (Map<String, Object> examTopicMappingRecord : examTopicMappingRespList) {
							String topicId = (String) examTopicMappingRecord.get(CommonConstants.TOPIC_ID);
							String topicPassPercentage = (String) examTopicMappingRecord
									.get(CommonConstants.TOPIC_PASS_PERCENTAGE);
							String totalQuestionsInThisTopic = (String) examTopicMappingRecord
									.get(CommonConstants.QUESTION_PER_EXAM);
							attemptMasterCtx.put(CommonConstants.PERFORMANCE_ID, performanceId);
							attemptMasterCtx.put(CommonConstants.TOPIC_ID, topicId);
							attemptMasterCtx.put(CommonConstants.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
							attemptMasterCtx.put(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC,
									totalQuestionsInThisTopic);

							try {
								// The service to create entries in the UserAttemptTopicMaster is called
								createUserAttemptTopicMasterResp = dispatcher.runSync("createUserAttemptTopicMaster",
										attemptMasterCtx);
							} catch (GenericServiceException e) {

								Debug.logError(e, "Failed to execute createUserAttemptTopicMaster service", module);
								errMsg = "Failed to execute createUserAttemptTopicMaster service : " + e.getMessage();
								request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
								request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
								return CommonConstants.ERROR;

							}

							if (ServiceUtil.isSuccess(createUserAttemptTopicMasterResp)) {

								topicList.add(UtilMisc.toMap(CommonConstants.TOPIC_ID,
										createUserAttemptTopicMasterResp.get(CommonConstants.TOPIC_ID),
										CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC, createUserAttemptTopicMasterResp
												.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC)));
							}
						}

						if (UtilValidate.isNotEmpty(topicList)) {

							attemptMasterCtx.put("topicList", topicList);

							Map<String, Object> findQuestionsByTopicIdsResp = null;
							try {
								findQuestionsByTopicIdsResp = dispatcher.runSync("findQuestionsByTopicIds",
										attemptMasterCtx);
							} catch (GenericServiceException e) {

								Debug.logError(e, "Failed to execute findQuestionsByTopicIds service", module);
								errMsg = "Failed to execute findQuestionsByTopicIds service : " + e.getMessage();
								request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
								request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
								return CommonConstants.ERROR;

							}

							// Selecting random Questions
							List<String> randomQuestions = new LinkedList<>();

							if (ServiceUtil.isSuccess(findQuestionsByTopicIdsResp)) {
								List<Map<String, Object>> topicWiseQuestions = (List<Map<String, Object>>) findQuestionsByTopicIdsResp.get("topicList");
								for (Map<String, Object> topic : topicWiseQuestions) {

									Integer totalQuestionsInThisTopic = (Integer) topic
											.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC);
									List<String> questionIdList = (List<String>) topic.get("questionIdList");

									Random rd = new Random();
									// checking totalQuestionsInThisTopic is not null
									if (UtilValidate.isNotEmpty(totalQuestionsInThisTopic)) {

										for (int i = 0; i < totalQuestionsInThisTopic; i++) {
											if (UtilValidate.isNotEmpty(questionIdList)) {
												int rand = rd.nextInt(questionIdList.size());
												randomQuestions.add(questionIdList.get(rand));
												questionIdList.remove(rand);
											}
										}
									}

								}
								// Creating UserAttemptAnswerMaster Records
								Integer sequenceNum = 1;						
								for (String questionId : randomQuestions) {
									Map<String, Object> createUAAMSResp = null;
									try {

										createUAAMSResp = dispatcher.runSync("createUserAttemptAnswerMasterService",
												UtilMisc.toMap(CommonConstants.QUESTION_ID, questionId,
														CommonConstants.PERFORMANCE_ID, performanceId,
														CommonConstants.SEQUENCE_NUM, sequenceNum++, "userLogin",
														userLogin));

									} catch (GenericServiceException e) {

										Debug.logError(e,
												"Failed to execute createUserAttemptAnswerMasterService service",
												module);
										errMsg = "Failed to execute createUserAttemptAnswerMasterService service : "
												+ e.getMessage();
										request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
										request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
										return CommonConstants.ERROR;

									}

									if (ServiceUtil.isSuccess(createUAAMSResp)) {
										Debug.logInfo("=======Created UserAttemptAnswerMaster record  using service createUserAttemptAnswerMaster=========",module);
										insertedQuestions.add(UtilMisc.toMap(CommonConstants.PERFORMANCE_ID,
												createUAAMSResp.get(CommonConstants.PERFORMANCE_ID),
												CommonConstants.SEQUENCE_NUM,
												createUAAMSResp.get(CommonConstants.SEQUENCE_NUM),
												CommonConstants.QUESTION_ID,
												createUAAMSResp.get(CommonConstants.QUESTION_ID)));
										
										request.getSession().setAttribute(CommonConstants.PERFORMANCE_ID, performanceId);
										
										//update noOfAttempts in userExamMapping Master
										attemptMasterCtx.put(CommonConstants.NO_OF_ATTEMPTS,attemptNumber);
										try {
											dispatcher.runAsync("updateUserExamMappingRecord", attemptMasterCtx);
										} catch (GenericServiceException e) {
											Debug.logError(e,"Failed to execute updateUserExamMappingRecord service",module);
											errMsg = "Failed to execute updateUserExamMappingRecord service : "+ e.getMessage();
											request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
											request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
											return CommonConstants.ERROR;			
										}	
										
									}
								}	
							}
						}
					}
				}
			}
		}else {
			
			 List<GenericValue> UserAttemptAnswerMasterGVList =null;
			try {
				UserAttemptAnswerMasterGVList = EntityQuery	
				.use(delegator)
				.from(CommonConstants.USER_ATTEMPT_ANSWER_MASTER)
				.where(CommonConstants.PERFORMANCE_ID,Integer.valueOf(performanceId)).queryList();
				
			} catch (GenericEntityException e) {
				Debug.logError(e,"Failed to retrieve Records from UserAttemptAnswerMaster service",module);
				errMsg = "Failed to retrieve Records from UserAttemptAnswerMaster service : "+ e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;				
			}
			
			if(UtilValidate.isNotEmpty(UserAttemptAnswerMasterGVList)) {
				for(GenericValue UserAttemptAnswerMasterGV :UserAttemptAnswerMasterGVList ) {
					insertedQuestions.add(UtilMisc.toMap(CommonConstants.PERFORMANCE_ID,
							UserAttemptAnswerMasterGV.getString(CommonConstants.PERFORMANCE_ID),
							CommonConstants.SEQUENCE_NUM,
							UserAttemptAnswerMasterGV.getString(CommonConstants.SEQUENCE_NUM),
							CommonConstants.QUESTION_ID,
							UserAttemptAnswerMasterGV.getString(CommonConstants.QUESTION_ID)));
				}
			}
			
			
		}
		
		
		
		
		if (UtilValidate.isNotEmpty(insertedQuestions)) {

			Map<String, Object> RetriveQuestions = new HashMap<>();
			RetriveQuestions.put(CommonConstants.USER_LOGIN, userLogin);

			for (Map<String, Object> question : insertedQuestions) {
				RetriveQuestions.put(CommonConstants.QUESTION_ID,
						question.get(CommonConstants.QUESTION_ID));
				Map<String, Object> resultMap = null;
				try {
					resultMap = dispatcher.runSync("findQuestionById", RetriveQuestions);
				} catch (GenericServiceException e) {

					Debug.logError(e, "Failed to execute findQuestionById service", module);
					errMsg = "Failed to execute findQuestionById service : " + e.getMessage();
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;

				}

				if (ServiceUtil.isSuccess(resultMap)) {
					request.setAttribute("result",
							resultMap.get(CommonConstants.RESPONSE_MESSAGE));
					Map<String, Object> questionResult = (Map<String, Object>) resultMap.get("question");
					question.put(CommonConstants.QUESTION_ID,
							questionResult.get(CommonConstants.QUESTION_ID));
					question.put(CommonConstants.QUESTION_DETAIL,
							questionResult.get(CommonConstants.QUESTION_DETAIL));
					question.put(CommonConstants.OPTION_A,
							questionResult.get(CommonConstants.OPTION_A));
					question.put(CommonConstants.OPTION_B,
							questionResult.get(CommonConstants.OPTION_B));
					question.put(CommonConstants.OPTION_C,
							questionResult.get(CommonConstants.OPTION_C));
					question.put(CommonConstants.OPTION_D,
							questionResult.get(CommonConstants.OPTION_D));
					question.put(CommonConstants.OPTION_E,
							questionResult.get(CommonConstants.OPTION_E));
					question.put(CommonConstants.QUESTION_TYPE,
							questionResult.get(CommonConstants.QUESTION_TYPE));
					question.put(CommonConstants.TOPIC_ID,
							questionResult.get(CommonConstants.TOPIC_ID));

				}
			}
		}
		request.setAttribute("selectedQuestion", insertedQuestions);
		
		return CommonConstants.SUCCESS;
	}
	
	public static String updateAnswerInUserAttemptAnswerMaster(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Map<String, Object> updateAnswerInUserAttemptAnswerMasterResp = null;

		try {
			updateAnswerInUserAttemptAnswerMasterResp = dispatcher.runSync("updateUserAttemptAnswerMaster",
					combinedMap);

		} catch (GenericServiceException e) {
			// If any exception occur in service, set error as a result in request object
			String errMsg = "Failed to execute updateAnswerInUserAttemptAnswerMaster service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			return CommonConstants.ERROR;
		}

		if (ServiceUtil.isError(updateAnswerInUserAttemptAnswerMasterResp)) {
			String errMsg = "Error while updating answer in UserAttemptAnswerMaster";
			Debug.logInfo(errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESPONSE_MESSAGE, errMsg);
			return CommonConstants.ERROR;
		}

		request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
		return CommonConstants.SUCCESS;
	}

	public static String updateQuestionForReviewInUserAttemptAnswerMaster(HttpServletRequest request,
			HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Map<String, Object> updateQuestionForReviewInUserAttemptAnswerMasterResp = null;

		try {
			updateQuestionForReviewInUserAttemptAnswerMasterResp = dispatcher.runSync("updateUserAttemptAnswerMaster",
					combinedMap);

		} catch (GenericServiceException e) {
			// If any exception occur in service, set error as a result in request object
			String errMsg = "Failed to execute updateQuestionForReviewInUserAttemptAnswerMaster service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			return CommonConstants.ERROR;
		}

		if (ServiceUtil.isError(updateQuestionForReviewInUserAttemptAnswerMasterResp)) {
			String errMsg = "Error while updating Mark for review in UserAttemptAnswerMaster";
			Debug.logInfo(errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESPONSE_MESSAGE, errMsg);
			return CommonConstants.ERROR;
		}
		request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
		return CommonConstants.SUCCESS;
	}

//	public static String evaluateUserAttemptAnswerMaster(HttpServletRequest request, HttpServletResponse response) {
//
//		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
//
//		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
//		Map<String, Object> findAnswerAndTopicIdForQuestionIdResp = null;
//		Map<String, Object> findAnswerAndTopicIdForQuestionIdServiceMap = null;
//		Set<String> TopicIdSet = null;
//		int totalCorrect = 0;
//
//		List<Map<String, Object>> userAttemptAnswerList = (List<Map<String, Object>>) request.getAttribute("questions");
//		
//		List<Map<String, Object>> userAttemptAnswerList1 = (List<Map<String, Object>>) combinedMap
//				.get("questions");
//		//userAttemptAnswerList
//
//		for (Map<String, Object> userAttemptAnswer : userAttemptAnswerList) {
//
//			Boolean correct = false;
//			String questionId = (String) userAttemptAnswer.get(CommonConstants.QUESTION_ID);
//			String submittedAnswer = (String) userAttemptAnswer.get(CommonConstants.SUBMITTED_ANSWER);
//			findAnswerAndTopicIdForQuestionIdServiceMap.put(CommonConstants.QUESTION_ID, questionId);
//			findAnswerAndTopicIdForQuestionIdServiceMap.put(CommonConstants.SUBMITTED_ANSWER, submittedAnswer);
//			try {
//
//				// Returns the topic id and and the correct answer for the question
//				findAnswerAndTopicIdForQuestionIdResp = dispatcher.runSync("findAnswerAndTopicIdForQuestionId",
//						findAnswerAndTopicIdForQuestionIdServiceMap);
//			} catch (GenericServiceException e) {
//				// If any exception occur in service, set error as a result in request object
//				String errMsg = "Failed to execute findAnswerAndTopicIdForQuestionId service";
//				Debug.logError(errMsg, module);
//				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//				return CommonConstants.ERROR;
//			}
//			if (UtilValidate.isNotEmpty(findAnswerAndTopicIdForQuestionIdResp)) {
//				String topicId = (String) findAnswerAndTopicIdForQuestionIdResp.get(CommonConstants.TOPIC_ID);
//				// Adds the topicIds to a Set to prevent duplicates
//				TopicIdSet.add(topicId);
//
//				// Adds the topicId for the question in the request list
//				userAttemptAnswer.put(CommonConstants.TOPIC_ID, topicId);
//				String answer = (String) findAnswerAndTopicIdForQuestionIdResp.get(CommonConstants.ANSWER);
//				if (answer.equals(submittedAnswer)) {
//					correct = true;
//					totalCorrect++;
//				}
//				// validates the answer and sets the value to the request list
//				userAttemptAnswer.put("correct", correct);
//			}
//		}
//		return CommonConstants.SUCCESS;
//
//	}
//	
	

}

