package com.vastpro.events;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

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

public class UserAttemptEvents {
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
		Integer performanceId = (Integer) combinedMap.get(CommonConstants.PERFORMANCE_ID);
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

			// To check allowedAttempt and noOfAttempt ,retrieve allowedAttempt and
			// noOfAttempt from UserExamMappingMaster
			Map<String, Object> findUserExamMappingRecordResp = null;

			try {
				findUserExamMappingRecordResp = dispatcher.runSync("findUserExamMappingRecord", attemptMasterCtx);
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute findUserExamMappingRecord service", module);
				errMsg = "Failed to execute findUserExamMappingRecord service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

			if (ServiceUtil.isError(findUserExamMappingRecordResp)) {
				errMsg = "Error while executing findUserExamMappingRecord service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT_MAP, findUserExamMappingRecordResp);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			Map<String, Object> userExamMappingRecord = (Map<String, Object>) findUserExamMappingRecordResp
					.get("userExamMappingRecord");
			Long allowedAttempt = (Long) userExamMappingRecord.get(CommonConstants.ALLOWED_ATTEMPTS);
			Long noOfAttempt = (Long) userExamMappingRecord.get(CommonConstants.NO_OF_ATTEMPTS);

			// if noOfAttempt is greater or equal allowed attempt return error
			if (noOfAttempt >= allowedAttempt) {
				errMsg = "Error: User has reached Maximum Allowed Attempt";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			// if noOfAttempt less than allowed attempt
			Long attemptNumber = noOfAttempt + 1;
			attemptMasterCtx.put(CommonConstants.ATTEMPT_NUMBER, attemptNumber);

			// Calling the findExamById service which return the exam detail for given the
			// examId
			Map<String, Object> findExamByIdResp = null;
			try {
				findExamByIdResp = dispatcher.runSync("findExamById", attemptMasterCtx);
				Debug.logInfo("Successfully executed findExamById service", module);
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute findExamById service", module);
				errMsg = "Failed to execute findExamById service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			if (ServiceUtil.isError(findExamByIdResp)) {
				errMsg = "Error while executing findExamById service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT_MAP, findExamByIdResp);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			// If the findNoOfQuestionCountByExamId Service is executed successfully
			Map<String, Object> exam = (Map<String, Object>) findExamByIdResp.get("exam");
			request.setAttribute("durationMinutes", exam.get(CommonConstants.DURATION_MINUTES));
			// creating UserAttemptMaster record

			Long noOfQuestion = (Long) exam.get(CommonConstants.NO_OF_QUESTIONS);
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
			if (ServiceUtil.isError(createUserAttemptMasterResp)) {
				errMsg = "Error while executing createUserAttemptMaster service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				request.setAttribute(CommonConstants.RESULT_MAP, createUserAttemptMasterResp);
				return CommonConstants.ERROR;

			}

			// If the createUserAttemptMaster Service is executed successfully
			// Converting the Generic value to String
			
			performanceId = (Integer) createUserAttemptMasterResp.get(CommonConstants.PERFORMANCE_ID);
			// findAllExamTopicMappingRecordsByExamId
			String eventResp = OnlineExamHelper.findAllExamTopicMappingRecordsByExamId(request);

			if (!CommonConstants.SUCCESS.equals(eventResp)) {
				errMsg = "Error while executing findAllExamTopicMappingRecordsByExamId method in OnlineExamHelper ";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			// If the findAllExamTopicMappingRecordsByExamId method in OnlineExamHelper
			// executed successfully
			// creating UserAttemptTopicMaster

			List<Map<String, Object>> examTopicMappingRespList = (List<Map<String, Object>>) request
					.getAttribute("examTopicMappingRecordList");
			List<Map<String, Object>> topicList = new LinkedList<>();
			Map<String, Object> createUserAttemptTopicMasterResp = null;

			// The objects in the list are added to a map
			for (Map<String, Object> examTopicMappingRecord : examTopicMappingRespList) {

				String topicId = (String) examTopicMappingRecord.get(CommonConstants.TOPIC_ID);
				String topicPassPercentage = (String) examTopicMappingRecord.get(CommonConstants.TOPIC_PASS_PERCENTAGE);
				String totalQuestionsInThisTopic = (String) examTopicMappingRecord
						.get(CommonConstants.QUESTION_PER_EXAM);
				attemptMasterCtx.put(CommonConstants.PERFORMANCE_ID, performanceId);
				attemptMasterCtx.put(CommonConstants.TOPIC_ID, topicId);
				attemptMasterCtx.put(CommonConstants.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
				attemptMasterCtx.put(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC, totalQuestionsInThisTopic);

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
							CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC,
							createUserAttemptTopicMasterResp.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC)));
				}
			}

			if (UtilValidate.isEmpty(topicList)) {
				errMsg = "Error : created topicList is empty ,after executing createUserAttemptTopicMaster service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			// If TopicList is Not Empty
			attemptMasterCtx.put("topicList", topicList);

			Map<String, Object> findQuestionsByTopicIdsResp = null;
			try {
				findQuestionsByTopicIdsResp = dispatcher.runSync("findQuestionsByTopicIds", attemptMasterCtx);
			} catch (GenericServiceException e) {

				Debug.logError(e, "Failed to execute findQuestionsByTopicIds service", module);
				errMsg = "Failed to execute findQuestionsByTopicIds service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			if (ServiceUtil.isError(findQuestionsByTopicIdsResp)) {
				errMsg = "Error while executing findQuestionsByTopicIds service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				request.setAttribute(CommonConstants.RESULT_MAP, findQuestionsByTopicIdsResp);
				return CommonConstants.ERROR;

			}

			// If the findQuestionsByTopicIds Service is executed successfully
			// Selecting random Questions
			List<String> randomQuestions = new LinkedList<>();

			List<Map<String, Object>> topicWiseQuestions = (List<Map<String, Object>>) findQuestionsByTopicIdsResp
					.get("topicList");
			for (Map<String, Object> topic : topicWiseQuestions) {

				Integer totalQuestionsInThisTopic = (Integer) topic.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC);
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

			if (UtilValidate.isEmpty(randomQuestions)) {
				errMsg = "Error: randamQuestion list created is empty or null";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			// Creating UserAttemptAnswerMaster Records
			Integer sequenceNum = 1;
			// isFlagged field default value is 0
			Integer isFlagged = 0;
			for (String questionId : randomQuestions) {
				Map<String, Object> createUAAMSResp = null;
				try {

					createUAAMSResp = dispatcher.runSync("createUserAttemptAnswerMasterService",
							UtilMisc.toMap(CommonConstants.QUESTION_ID, questionId, CommonConstants.PERFORMANCE_ID,
									performanceId, CommonConstants.SEQUENCE_NUM, sequenceNum++,
									CommonConstants.IS_FLAGGED, isFlagged, CommonConstants.USER_LOGIN, userLogin));

				} catch (GenericServiceException e) {

					Debug.logError(e, "Failed to execute createUserAttemptAnswerMasterService service", module);
					errMsg = "Failed to execute createUserAttemptAnswerMasterService service : " + e.getMessage();
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;

				}

				if (ServiceUtil.isError(createUAAMSResp)) {
					errMsg = "Error while executing createUserAttemptAnswerMasterService service";
					Debug.logError(errMsg, module);
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					request.setAttribute(CommonConstants.RESULT_MAP, createUAAMSResp);
					return CommonConstants.ERROR;

				}
				// If the UserAttemptAnswerMaster Service is executed successfully
				Debug.logInfo(
						"=======Created UserAttemptAnswerMaster record  using service createUserAttemptAnswerMaster=========",
						module);
				insertedQuestions.add(UtilMisc.toMap(CommonConstants.PERFORMANCE_ID,
						createUAAMSResp.get(CommonConstants.PERFORMANCE_ID), CommonConstants.SEQUENCE_NUM,
						createUAAMSResp.get(CommonConstants.SEQUENCE_NUM), CommonConstants.QUESTION_ID,
						createUAAMSResp.get(CommonConstants.QUESTION_ID), CommonConstants.IS_FLAGGED,
						createUAAMSResp.get(CommonConstants.IS_FLAGGED)));

				request.getSession().setAttribute(CommonConstants.PERFORMANCE_ID, performanceId);

			}
			// update noOfAttempts and lastPerformanceDate in userExamMapping Master
			Timestamp lastPerformanceDate = new Timestamp(System.currentTimeMillis());

			userExamMappingRecord.put(CommonConstants.NO_OF_ATTEMPTS, attemptNumber);
			userExamMappingRecord.put(CommonConstants.LAST_PERFORMANCE_DATE, lastPerformanceDate);
			try {
				Map<String, Object> updateUserExamMappingRecordResp = dispatcher.runSync("updateUserExamMappingRecord",
						userExamMappingRecord);
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute updateUserExamMappingRecord service", module);
				errMsg = "Failed to execute updateUserExamMappingRecord service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

		} else {

			List<GenericValue> UserAttemptAnswerMasterGVList = null;
			try {
				UserAttemptAnswerMasterGVList = EntityQuery.use(delegator)
						.from(CommonConstants.USER_ATTEMPT_ANSWER_MASTER)
						.where(CommonConstants.PERFORMANCE_ID, Integer.valueOf(performanceId)).queryList();

			} catch (GenericEntityException e) {
				Debug.logError(e, "Failed to retrieve Records from UserAttemptAnswerMaster service", module);
				errMsg = "Failed to retrieve Records from UserAttemptAnswerMaster service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

			if (UtilValidate.isNotEmpty(UserAttemptAnswerMasterGVList)) {
				for (GenericValue UserAttemptAnswerMasterGV : UserAttemptAnswerMasterGVList) {
					insertedQuestions.add(UtilMisc.toMap(CommonConstants.PERFORMANCE_ID,
							UserAttemptAnswerMasterGV.getString(CommonConstants.PERFORMANCE_ID),
							CommonConstants.SEQUENCE_NUM,
							UserAttemptAnswerMasterGV.getLong(CommonConstants.SEQUENCE_NUM),
							CommonConstants.QUESTION_ID,
							UserAttemptAnswerMasterGV.getString(CommonConstants.QUESTION_ID),
							CommonConstants.IS_FLAGGED,
							UserAttemptAnswerMasterGV.getInteger(CommonConstants.IS_FLAGGED),
							CommonConstants.SUBMITTED_ANSWER,
							UserAttemptAnswerMasterGV.getString(CommonConstants.SUBMITTED_ANSWER)));
				}
			}

		}

		if (UtilValidate.isNotEmpty(insertedQuestions)) {

			Map<String, Object> RetriveQuestions = new HashMap<>();
			RetriveQuestions.put(CommonConstants.USER_LOGIN, userLogin);

			for (Map<String, Object> question : insertedQuestions) {
				RetriveQuestions.put(CommonConstants.QUESTION_ID, question.get(CommonConstants.QUESTION_ID));
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
					request.setAttribute("result", resultMap.get(CommonConstants.RESPONSE_MESSAGE));
					Map<String, Object> questionResult = (Map<String, Object>) resultMap.get("question");
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
		Collections.sort(insertedQuestions, (o1, o2) -> Long.compare((Long) o1.get(CommonConstants.SEQUENCE_NUM),
				(Long) o2.get(CommonConstants.SEQUENCE_NUM)));
		request.setAttribute("selectedQuestion", insertedQuestions);

		return CommonConstants.SUCCESS;
	}

	public static String updateAnswerInUserAttemptAnswerMaster(HttpServletRequest request,
			HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Map<String, Object> updateAnswerInUserAttemptAnswerMasterResp = null;

		if (combinedMap.get("questionType").equals("02")) {
			List<String> submittedAnswer = (List<String>) combinedMap.get("submittedAnswer");
			Collections.sort(submittedAnswer);

			String subans = "";
			for (String ans : submittedAnswer) {
				subans = subans + "," + ans;
			}

			subans = subans.substring(1);
			combinedMap.put("submittedAnswer", subans);
		}

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

	public static String evaluateUserAttemptAnswerMaster(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Integer performanceId = (Integer) request.getSession().getAttribute(CommonConstants.PERFORMANCE_ID);

		// Query for taking noOfQuestions, examId from UserAttemptMaster entity
		GenericValue userAttemptMasterGv = null;
		try {
			userAttemptMasterGv = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_MASTER)
					.where(CommonConstants.PERFORMANCE_ID, performanceId).queryOne();
		} catch (GenericEntityException e) {
			String errMsg = "Exception occured while fetching record from UserAttemptMaster entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		if (UtilValidate.isEmpty(userAttemptMasterGv)) {
			String errMsg = "Fetched record from UserAttemptMaster is null or empty ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		Integer noOfQuestions = userAttemptMasterGv.getInteger(CommonConstants.NO_OF_QUESTIONS);
		String examId = userAttemptMasterGv.getString(CommonConstants.EXAM_ID);

		GenericValue examMasterGv = null;
		try {

			examMasterGv = EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER)
					.where(CommonConstants.EXAM_ID, examId).cache().queryOne();
		} catch (GenericEntityException e) {

			String errMsg = "Exception occured while fetching record from ExamMaster : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		if (UtilValidate.isEmpty(examMasterGv)) {
			String errMsg = "Fetched record from ExamMaster is null or empty ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;

		}
		
		String examName =examMasterGv.getString(CommonConstants.EXAM_NAME);
		String enableNegativeMark = examMasterGv.getString(CommonConstants.ENABLE_NEGATIVE_MARK);
		
		//Taking record from userAttemptAnswerMaster entity based on performanceId
		List<GenericValue> userAttemptAnswerMasterList = null;
		try {

			userAttemptAnswerMasterList = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_ANSWER_MASTER)
					.where(CommonConstants.PERFORMANCE_ID, performanceId).cache().queryList();

		} catch (GenericEntityException e) {
			// If Exception occurred in the query, set the result as Error in request
			String errMsg = "Exception occured while fetching the record from UserAttemptAnswerMaster: "
					+ e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		// If the userAttemptAnswerMasterList is empty, set the result as Error in
		// request
		if (UtilValidate.isEmpty(userAttemptAnswerMasterList)) {
			String errMsg = "Fetched record from UserAttemptAnswerMaster is null or empty ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		Double totalScore = 0.0;
		Double userScore = 0.0;
		Integer totalCorrect = 0;
		Integer totalWrong = 0;
		String userPassed = "Y";

		Map<String, Integer> correctQuestionsTopicWise = new HashMap<String, Integer>();

		for (GenericValue userAttemptAnswerMasterRecord : userAttemptAnswerMasterList) {

			Long questionId = userAttemptAnswerMasterRecord.getLong(CommonConstants.QUESTION_ID);
			String submittedAnswer = (String) userAttemptAnswerMasterRecord.get(CommonConstants.SUBMITTED_ANSWER);

			GenericValue question = null;

			try {
				// Fetching question details from QuestionMaster entity
				question = EntityQuery.use(delegator).from(CommonConstants.QUESTION_MASTER)
						.where(CommonConstants.QUESTION_ID, questionId).cache().queryOne();

			} catch (GenericEntityException e) {
				// If Exception occurred in the query, set the result as Error in request
				String errMsg = "Exception occured while fetching the record from QuestionMaster entity: "
						+ e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			if (UtilValidate.isEmpty(question)) {
				String errMsg = "Fetched record from QuestionMaster entity is null or empty ";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

			String topicId = question.getString(CommonConstants.TOPIC_ID);
			String correctAnswer = question.getString(CommonConstants.ANSWER);
			Double answerValue = question.getDouble(CommonConstants.ANSWER_VALUE);
			Double negativeMarkValue = question.getDouble(CommonConstants.NEGATIVE_MARK_VALUE);
			totalScore += answerValue;
			if (UtilValidate.isNotEmpty(submittedAnswer)) {
				if (submittedAnswer.equalsIgnoreCase(correctAnswer)) {
					userScore += answerValue;
					totalCorrect++;
					if (correctQuestionsTopicWise.containsKey(topicId)) {
						correctQuestionsTopicWise.put(topicId, correctQuestionsTopicWise.get(topicId) + 1);

					} else {
						correctQuestionsTopicWise.put(topicId, 1);
					}

				} else {
					totalWrong++;
					if(enableNegativeMark.equals("Y")) {
						userScore -= negativeMarkValue;
					}
					if (correctQuestionsTopicWise.containsKey(topicId)) {
						correctQuestionsTopicWise.put(topicId, correctQuestionsTopicWise.get(topicId) + 0);

					} else {
						correctQuestionsTopicWise.put(topicId, 0);
					}
				}

			} else {

				if (correctQuestionsTopicWise.containsKey(topicId)) {
					correctQuestionsTopicWise.put(topicId, correctQuestionsTopicWise.get(topicId) + 0);

				} else {
					correctQuestionsTopicWise.put(topicId, 0);
				}

			}

		}

		List<Map<String, Object>> updatedUserAttemptTopicMasterList = new LinkedList<>();
		for (Entry<String, Integer> topicScore : correctQuestionsTopicWise.entrySet()) {

			String userPassedThisTopic = "N";
			String topicId = topicScore.getKey();
			double noOfCorrectQuestions = topicScore.getValue();
			GenericValue UserAttemptTopicMasterGv = null;
			try {
				UserAttemptTopicMasterGv = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_TOPIC_MASTER)
						.where(CommonConstants.PERFORMANCE_ID, performanceId, CommonConstants.TOPIC_ID, topicId)
						.queryOne();

			} catch (GenericEntityException e) {
				String errMsg = "Exception occured while fetching the record from UserAttemptTopicMaster entity: "
						+ e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			if (UtilValidate.isEmpty(UserAttemptTopicMasterGv)) {
				String errMsg = "Fetched record from UserAttemptTopicMaster is null or empty ";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			double topicPassPercentage = UserAttemptTopicMasterGv.getDouble(CommonConstants.TOPIC_PASS_PERCENTAGE);
			double totalQuestionsInThisTopic = UserAttemptTopicMasterGv
					.getInteger(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC);

			double actualUserTopicPercentage = (noOfCorrectQuestions / totalQuestionsInThisTopic) * 100;

			// checking user is passed or not in particular topic
			if (actualUserTopicPercentage >= topicPassPercentage) {
				userPassedThisTopic = "Y";
			}

			Map<String, Object> updateUserAttemptTopicMasterContext = UtilMisc.toMap(CommonConstants.PERFORMANCE_ID,
					performanceId, CommonConstants.TOPIC_ID, topicId, CommonConstants.USER_PASSED_THIS_TOPIC,
					userPassedThisTopic, CommonConstants.USER_TOPIC_PERCENTAGE, actualUserTopicPercentage,
					CommonConstants.CORRECT_QUESTIONS_IN_THIS_TOPIC, noOfCorrectQuestions, CommonConstants.USER_LOGIN,
					userLogin);

			// update the record in userAttemptTopicMaster entity
			Map<String, Object> updateUserAttemptTopicMasterResp = null;
			try {
				updateUserAttemptTopicMasterResp = dispatcher.runSync("updateUserAttemptTopicMaster",
						updateUserAttemptTopicMasterContext);
			} catch (GenericServiceException e) {

				String errMsg = "Exception occured while calling updateUserAttemptTopicMaster service : "
						+ e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

			if (ServiceUtil.isError(updateUserAttemptTopicMasterResp)) {
				
				String errMsg = "error occured while running updateUserAttemptTopicMaster service ";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;

			}

			if (updateUserAttemptTopicMasterResp.get(CommonConstants.USER_PASSED_THIS_TOPIC).equals("N")) {
				userPassed = "N";
			}
			
			GenericValue topicGV = null;
			try {
				topicGV=EntityQuery.use(delegator).from(CommonConstants.TOPIC_MASTER).where(CommonConstants.TOPIC_ID,topicId).queryOne();
			} catch (GenericEntityException e) {
				String errMsg = "Exception occured while fetching the record from TopicMaster entity: "
						+ e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			
			String topicName = topicGV.getString(CommonConstants.TOPIC_NAME);
			updateUserAttemptTopicMasterResp.put(CommonConstants.TOPIC_NAME,topicName );
			updatedUserAttemptTopicMasterList.add(updateUserAttemptTopicMasterResp);

		}

		// Calculating User percentage

		double score = userScore / totalScore * 100;
		score = Math.round(score);
		double passPercentage = examMasterGv.getDouble(CommonConstants.PASS_PERCENTAGE);

		if (score < passPercentage) {
			userPassed = "N";
		}
		// else userPassed will be Y
		Timestamp completedDate = new Timestamp(System.currentTimeMillis());
		Map<String, Object> updateUserAttemptContext = UtilMisc.toMap(CommonConstants.PERFORMANCE_ID, performanceId,
				CommonConstants.USER_PASSED, userPassed, CommonConstants.TOTAL_CORRECT, totalCorrect,
				CommonConstants.TOTAL_WRONG, totalWrong, CommonConstants.SCORE, score, CommonConstants.COMPLETED_DATE,
				completedDate, CommonConstants.USER_LOGIN, userLogin);

		Map<String, Object> updateUserAttemptMasterRep = null;
		try {
			// update userAttemptMaster entity
			updateUserAttemptMasterRep = dispatcher.runSync("updateUserAttemptMaster", updateUserAttemptContext);
		} catch (GenericServiceException e) {

			String errMsg = "Exception occured while calling updateUserAttemptMaster service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		if (ServiceUtil.isError(updateUserAttemptMasterRep)) {

			String errMsg = "error occured while running updateUserAttemptMaster service ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;

		}

		Map<String, Object> resultMap = UtilMisc.toMap(CommonConstants.EXAM_NAME,examName,
				"totalScore", totalScore, "totalWrongAnswersInExam", totalWrong,
				"totalCorrectQuestionsInExam", totalCorrect, "actualUserPercentage", score,
				CommonConstants.PASS_PERCENTAGE, passPercentage, CommonConstants.SCORE, score,
				CommonConstants.USER_PASSED, userPassed, CommonConstants.NO_OF_QUESTIONS, noOfQuestions,
				"noOfCorrectedQuestionsByTopicId", correctQuestionsTopicWise, "updatedUserAttemptTopicMasterList",
				updatedUserAttemptTopicMasterList);

		request.setAttribute("resultMap", resultMap);
		request.getSession().removeAttribute(CommonConstants.PERFORMANCE_ID);
		return CommonConstants.SUCCESS;

	}
	
	public static String findAllUserAttemptByPartyId(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		
		String partyId = userLogin.getString(CommonConstants.PARTY_ID);
		List<GenericValue> userAttemptMasterGVList =null;
		try {
			userAttemptMasterGVList = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_MASTER).where(CommonConstants.PARTY_ID,partyId).cache().queryList();
		} catch (GenericEntityException e) {
			String errMsg = "Exception occured while fetching the record from UserAttemptMaster entity: "
					+ e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		if(UtilValidate.isEmpty(userAttemptMasterGVList)) {
			String errMsg = "Fetched record from  UserAttemptMaster entity is null or empty";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
	
		
		
		List<Map<String,Object>> userAttemptMasterList = new ArrayList<>();
		for(GenericValue userAttemptMasterGV:userAttemptMasterGVList ) {
			String examId = userAttemptMasterGV.getString(CommonConstants.EXAM_ID);
			GenericValue examMasterGv =null; 
			try {
				examMasterGv=EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER).where(CommonConstants.EXAM_ID,examId).cache().queryOne();
			} catch (GenericEntityException e) {
				String errMsg = "Exception occured while fetching the record from ExamMaster entity: "
						+ e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			
			if(UtilValidate.isEmpty(examMasterGv)) {
				String errMsg = "Fetched record from  ExamMaster entity is null or empty";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			String examName = examMasterGv.getString(CommonConstants.EXAM_NAME);
			
			Map<String,Object> userAttemptMasterRecord = new HashMap<>();
			
			userAttemptMasterRecord.put(CommonConstants.EXAM_NAME, examName);
			userAttemptMasterRecord.put(CommonConstants.EXAM_ID,examId);
			userAttemptMasterRecord.put(CommonConstants.PERFORMANCE_ID,userAttemptMasterGV.getInteger(CommonConstants.PERFORMANCE_ID) );
			userAttemptMasterRecord.put(CommonConstants.ATTEMPT_NUMBER, userAttemptMasterGV.getLong(CommonConstants.ATTEMPT_NUMBER));
			userAttemptMasterRecord.put(CommonConstants.PARTY_ID,  userAttemptMasterGV.getString(CommonConstants.PARTY_ID));
			userAttemptMasterRecord.put(CommonConstants.SCORE,userAttemptMasterGV.getBigDecimal(CommonConstants.SCORE));
			userAttemptMasterRecord.put(CommonConstants.COMPLETED_DATE, userAttemptMasterGV.getTimestamp(CommonConstants.COMPLETED_DATE));
			userAttemptMasterRecord.put(CommonConstants.NO_OF_QUESTIONS,  userAttemptMasterGV.getInteger(CommonConstants.NO_OF_QUESTIONS));
			userAttemptMasterRecord.put(CommonConstants.TOTAL_CORRECT,  userAttemptMasterGV.getInteger(CommonConstants.TOTAL_CORRECT));
			userAttemptMasterRecord.put(CommonConstants.TOTAL_WRONG,  userAttemptMasterGV.getInteger(CommonConstants.TOTAL_WRONG));
			userAttemptMasterRecord.put(CommonConstants.USER_PASSED,  userAttemptMasterGV.getString(CommonConstants.USER_PASSED));
			
			
			
			userAttemptMasterList.add(userAttemptMasterRecord);
			
		}
		request.setAttribute("userAttemptMasterList",userAttemptMasterList );
		request.setAttribute(CommonConstants.RESULT,  CommonConstants.SUCCESS);
		return CommonConstants.SUCCESS;
	}
	
	public static String findUserAttemptTopicMasterByPerforamceId(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		
		Integer perforamceId = Integer.valueOf((String)combinedMap.get(CommonConstants.PERFORMANCE_ID));
		
		if(UtilValidate.isEmpty(perforamceId)) {
			String errMsg = "performanceId  is null or empty";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
			
		}
		
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		List<GenericValue> userAttemptTopicMasterGVList=null; 
		try {
			userAttemptTopicMasterGVList = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_TOPIC_MASTER).where(CommonConstants.PERFORMANCE_ID,perforamceId).queryList();
		} 
		catch (GenericEntityException e) {
			String errMsg = "Exception occured while fetching the record from UserAttemptTopicMaster entity: "
					+ e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
			
			
		}
		
		if(UtilValidate.isEmpty(userAttemptTopicMasterGVList)) {
			String errMsg = "Fetched record from  UserAttemptTopicMaster entity is null or empty";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
			
			
		}
		
		
		List<Map<String,Object>> userAttemptTopicMasterList = new ArrayList<>();
		for(GenericValue userAttemptTopicMasterGV :userAttemptTopicMasterGVList ) {
			
			String topicId = userAttemptTopicMasterGV.getString(CommonConstants.TOPIC_ID);
			
			Map<String,Object> userAttemptTopicMasterRecord = new HashMap<>();
			userAttemptTopicMasterRecord.put(CommonConstants.TOPIC_ID, topicId);
			userAttemptTopicMasterRecord.put(CommonConstants.PERFORMANCE_ID, userAttemptTopicMasterGV.get(CommonConstants.PERFORMANCE_ID));
			userAttemptTopicMasterRecord.put(CommonConstants.TOPIC_PASS_PERCENTAGE, userAttemptTopicMasterGV.get(CommonConstants.TOPIC_PASS_PERCENTAGE));
			userAttemptTopicMasterRecord.put(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC, userAttemptTopicMasterGV.get(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC));
			userAttemptTopicMasterRecord.put(CommonConstants.CORRECT_QUESTIONS_IN_THIS_TOPIC, userAttemptTopicMasterGV.get(CommonConstants.CORRECT_QUESTIONS_IN_THIS_TOPIC));
			userAttemptTopicMasterRecord.put(CommonConstants.USER_TOPIC_PERCENTAGE, userAttemptTopicMasterGV.get(CommonConstants.USER_TOPIC_PERCENTAGE));
			userAttemptTopicMasterRecord.put(CommonConstants.USER_PASSED_THIS_TOPIC, userAttemptTopicMasterGV.get(CommonConstants.USER_PASSED_THIS_TOPIC));
			
			GenericValue topicMasterGV = null;
			try {
				topicMasterGV=EntityQuery.use(delegator).from(CommonConstants.TOPIC_MASTER).where(CommonConstants.TOPIC_ID,topicId).queryOne();
			} catch (GenericEntityException e) {
				String errMsg = "Exception occured while fetching the record from TopicMaster entity: "
						+ e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
			
			if(UtilValidate.isEmpty(topicMasterGV)) {
				String errMsg = "Fetched record from  TopicMaster entity is null or empty";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
				
				
			}
			
			userAttemptTopicMasterRecord.put(CommonConstants.TOPIC_NAME, topicMasterGV.getString(CommonConstants.TOPIC_NAME));
			userAttemptTopicMasterList.add(userAttemptTopicMasterRecord);
			
		}
		request.setAttribute("userAttemptTopicMasterList", userAttemptTopicMasterList);
		request.setAttribute(CommonConstants.RESULT,  CommonConstants.SUCCESS);
		return  CommonConstants.SUCCESS;
		
	}
	
	

//	public static String evaluateUserAttemptAnswerMaster(HttpServletRequest request, HttpServletResponse response) {
//		
//		
//		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
//		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
//		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
//		Integer performanceId = (Integer) request.getSession().getAttribute(CommonConstants.PERFORMANCE_ID);
//		
//		
//		// Taking record from userAttemptAnswerMaster entity based on performanceId
//		List<GenericValue> userAttemptAnswerMasterList = null;
//		try {
//			
//			userAttemptAnswerMasterList = EntityQuery
//										.use(delegator)
//										.from(CommonConstants.USER_ATTEMPT_ANSWER_MASTER)
//										.where(CommonConstants.PERFORMANCE_ID, performanceId)
//										.cache()
//										.queryList();
//
//		} catch (GenericEntityException e) {
//			// If Exception occurred in the query, set the result as Error in request
//			String errMsg = "Exception occured while fetching the record from UserAttemptAnswerMaster: "
//					+ e.getMessage();
//			Debug.logError(e, errMsg, module);
//			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//			return CommonConstants.ERROR;
//		}
//		
//
//		// If the userAttemptAnswerMasterList is empty, set the result as Error in request
//		if (UtilValidate.isEmpty(userAttemptAnswerMasterList)) {
//			String errMsg = "Fetched record from UserAttemptAnswerMaster is null or empty ";
//			Debug.logError(errMsg, module);
//			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//			return CommonConstants.ERROR;
//		}
//
//		List<Map<String, Object>> evaluatedQuestionList = new LinkedList<>();
//		Map<String, Integer> noOfCorrectedQuestionsByTopicId = new HashMap<String, Integer>();
//		Map<String, Integer> noOfUnAnsweredQuestionsByTopicId = new HashMap<String, Integer>();
//		Map<String, Integer> totalEvaluatedQuestionsByTopicId = new HashMap<String,Integer>();
//		Double score = 0.0;
//		Double totalScore =0.0;
//		
//
//		// If the userAttemptAnswerMasterList is notEmpty
//		if (UtilValidate.isNotEmpty(userAttemptAnswerMasterList)) {
//			Integer noOfCorrectQuestions = 0;
//			Boolean isCorrect = false;
//			
//			// Iterate the userAttemptAnswerMasterList and taking the question wise submittedAnswer
//			for (GenericValue question : userAttemptAnswerMasterList) {
//				Long questionId = question.getLong(CommonConstants.QUESTION_ID);
//				String submittedAnswer = (String) question.get(CommonConstants.SUBMITTED_ANSWER);
//				
//				Map<String, Object> questionWithAnswer = new HashMap<>();
//				questionWithAnswer.put(CommonConstants.QUESTION_ID, questionId);
//				questionWithAnswer.put(CommonConstants.SUBMITTED_ANSWER, submittedAnswer);
//				GenericValue questionDetail = null;
//
//				try {
//					// Fetching question details from QuestionMaster entity
//					questionDetail = EntityQuery
//									.use(delegator)
//									.from(CommonConstants.QUESTION_MASTER)
//									.where(CommonConstants.QUESTION_ID, questionId)
//									.cache().queryOne();
//
//				} catch (GenericEntityException e) {
//					// If Exception occurred in the query, set the result as Error in request
//					String errMsg = "Exception occured while fetching the record from QuestionMaster entity: "
//							+ e.getMessage();
//					Debug.logError(e, errMsg, module);
//					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//					return CommonConstants.ERROR;
//
//				}
//
//				if (UtilValidate.isEmpty(questionDetail)) {
//					String errMsg = "Fetched record from QuestionMaster entity is null or empty ";
//					Debug.logError(errMsg, module);
//					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//					return CommonConstants.ERROR;
//				}
//
//				if (UtilValidate.isNotEmpty(questionDetail)) {
//
//					String topicId = questionDetail.getString(CommonConstants.TOPIC_ID);
//					String answer = questionDetail.getString(CommonConstants.ANSWER);
//					Double answerValue = questionDetail.getDouble(CommonConstants.ANSWER_VALUE);
//					
//					totalScore += answerValue;
//					questionWithAnswer.put(CommonConstants.TOPIC_ID, topicId);
//					questionWithAnswer.put(CommonConstants.ANSWER, answer);
//					
//					
//					// If submittedAnswer is empty, which is unAnswered
//					if (UtilValidate.isEmpty(submittedAnswer)) {
//						isCorrect = null;
//						questionWithAnswer.put("isCorrect", isCorrect);
//
//						if (totalEvaluatedQuestionsByTopicId.containsKey(topicId)) {
//							totalEvaluatedQuestionsByTopicId.replace(topicId,
//									totalEvaluatedQuestionsByTopicId.get(topicId) + 1);
//						} else {
//							totalEvaluatedQuestionsByTopicId.put(topicId, 1);
//						}
//						
//						if (noOfUnAnsweredQuestionsByTopicId.containsKey(topicId)) {
//							noOfUnAnsweredQuestionsByTopicId.replace(topicId,
//									noOfUnAnsweredQuestionsByTopicId.get(topicId) + 1);
//						} else {
//							noOfUnAnsweredQuestionsByTopicId.put(topicId, 1);
//						}
//
//					} else {
//						// if submittedAnswer is not empty, which is Answered
//						// If answer equals submittedAnswer
//						if (answer.equals(submittedAnswer)) {
//							isCorrect = true;
//							score += answerValue;
//							questionWithAnswer.put("isCorrect", isCorrect);
//
//							if (totalEvaluatedQuestionsByTopicId.containsKey(topicId)) {
//								totalEvaluatedQuestionsByTopicId.replace(topicId,
//										totalEvaluatedQuestionsByTopicId.get(topicId) + 1);
//							} else {
//								totalEvaluatedQuestionsByTopicId.put(topicId, 1);
//							}
//							
//							// Sum 1 to noOfCorrectedQuestions for this particular topicId key
//							if (noOfCorrectedQuestionsByTopicId.containsKey(topicId)) {
//								noOfCorrectedQuestionsByTopicId.replace(topicId,	
//										noOfCorrectedQuestionsByTopicId.get(topicId) + 1);
//							} else {
//								noOfCorrectedQuestionsByTopicId.put(topicId, 1);
//							}
//						}
//						// If answer not equals submittedAnswer
//						else {
//							isCorrect = false;
//							questionWithAnswer.put("isCorrect", isCorrect);
//							
//							if (totalEvaluatedQuestionsByTopicId.containsKey(topicId)) {
//								totalEvaluatedQuestionsByTopicId.replace(topicId,
//										totalEvaluatedQuestionsByTopicId.get(topicId) + 1);
//							} else {
//								totalEvaluatedQuestionsByTopicId.put(topicId, 1);
//							}
//						}
//					}
//				}
//				evaluatedQuestionList.add(questionWithAnswer);
//			}
//		}
//		
//		List<Map<String, Object>> updatedUserAttemptTopicMasterList = new LinkedList<>();
//		// Find totalQuestions in this topic
//				for (Map.Entry<String, Integer> entry : totalEvaluatedQuestionsByTopicId.entrySet()) {
//					String topicId = entry.getKey();
//					Integer noOfUnAnsweredQuestions = noOfUnAnsweredQuestionsByTopicId.get(topicId);
//					Integer noOfCorrectQuestions = noOfCorrectedQuestionsByTopicId.get(topicId);
//					GenericValue UserAttemptTopicMasterGv = null;
//					try {
//						UserAttemptTopicMasterGv = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_TOPIC_MASTER)
//								.where(CommonConstants.PERFORMANCE_ID, performanceId, CommonConstants.TOPIC_ID, topicId)
//								.queryOne();
//
//					} catch (GenericEntityException e) {
//						String errMsg = "Exception occured while fetching the record from UserAttemptTopicMaster entity: "+ e.getMessage();
//						Debug.logError(e, errMsg, module);
//						request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//						request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//						return CommonConstants.ERROR;
//						
//					}

//					String userPassedThisTopic = null;
//					double actualUserTopicPercentage = 0.0;
//					Map<String, Object> updateUserAttemptTopicMasterResp = null;
//					
//					if (UtilValidate.isNotEmpty(UserAttemptTopicMasterGv)) {
//						Double topicPassPercentage = UserAttemptTopicMasterGv.getDouble(CommonConstants.TOPIC_PASS_PERCENTAGE);
//						Integer totalQuestionsInThisTopic = UserAttemptTopicMasterGv
//								.getInteger(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC);
//						if(UtilValidate.isNotEmpty(noOfCorrectQuestions)) {
//						actualUserTopicPercentage = ((double) noOfCorrectQuestions / (double) totalQuestionsInThisTopic) * 100;
//						}
//						// checking user is passed or not in particular topic
//						if (actualUserTopicPercentage >= topicPassPercentage) {
//							userPassedThisTopic = "Y";
//						} else {
//							userPassedThisTopic = "N";
//						}
//
//					}
//
//					Map<String, Object> updateUserAttemptTopicMasterContext = UtilMisc.toMap(
//							CommonConstants.USER_PASSED_THIS_TOPIC, userPassedThisTopic, CommonConstants.USER_TOPIC_PERCENTAGE,
//							actualUserTopicPercentage, CommonConstants.CORRECT_QUESTIONS_IN_THIS_TOPIC, noOfCorrectQuestions,
//							CommonConstants.PERFORMANCE_ID, performanceId, CommonConstants.TOPIC_ID, topicId,
//							CommonConstants.USER_LOGIN, userLogin);
//
//					// update the record in userAttemptTopicMaster entity
//					try {
//						updateUserAttemptTopicMasterResp = dispatcher.runSync("updateUserAttemptTopicMaster",
//								updateUserAttemptTopicMasterContext);
//					} catch (GenericServiceException e) {
//
//						String errMsg = "Exception occured while calling updateUserAttemptTopicMaster service : "
//								+ e.getMessage();
//						Debug.logError(e, errMsg, module);
//						request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//						request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//						return CommonConstants.ERROR;
//					}
//					
//					if(UtilValidate.isNotEmpty(updateUserAttemptTopicMasterResp)){
//						updatedUserAttemptTopicMasterList.add(updateUserAttemptTopicMasterResp);
//					}
//
//				}
//		
//		
//		
//		
//		
//
//		// adding all topicwise correct questions for total correct Questions in exam
//		Integer totalCorrectQuestionsInExam = 0;
//		for (Map.Entry<String, Integer> entry : noOfCorrectedQuestionsByTopicId.entrySet()) {
//			totalCorrectQuestionsInExam += entry.getValue();
//		}
//
//		GenericValue userAttemptMasterGv = null;
//		// Query for taking noOfQuestions, examId from UserAttemptMaster entity
//		try {
//			userAttemptMasterGv = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_MASTER)
//					.where(CommonConstants.PERFORMANCE_ID, performanceId).queryOne();
//		} catch (GenericEntityException e) {
//			String errMsg = "Exception occured while fetching record from UserAttemptMaster entity : "
//					+ e.getMessage();
//			Debug.logError(e, errMsg, module);
//			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//			return CommonConstants.ERROR;
//		}
//
//		Integer noOfQuestions = 0;
//		String examId = null;
//
//		// If userAttemptMasterGv is not empty
//		if (UtilValidate.isNotEmpty(userAttemptMasterGv)) {
//			noOfQuestions = userAttemptMasterGv.getInteger(CommonConstants.NO_OF_QUESTIONS);
//			examId = userAttemptMasterGv.getString(CommonConstants.EXAM_ID);
//		}
//
//		Integer totalWrongAnswersInExam = noOfQuestions - totalCorrectQuestionsInExam;
//		//Calculating User percentage
//		//double actualUserPercentage = ((double) totalCorrectQuestionsInExam / (double) noOfQuestions) * 100;
//		double actualUserPercentage = ((double) score / (double) totalScore) * 100;
//		actualUserPercentage = Math.round(actualUserPercentage);
//		GenericValue examMasterGv = null;
//
//		try {
//			// Taking passpercentage from ExamMaster entity
//			examMasterGv = EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER)
//					.where(CommonConstants.EXAM_ID, examId).queryOne();
//		} catch (GenericEntityException e) {
//
//			String errMsg = "Exception occured while fetching record from ExamMaster : "
//					+ e.getMessage();
//			Debug.logError(e, errMsg, module);
//			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//			return CommonConstants.ERROR;
//		}
//
//		Double passPercentage = 0.0;
//		String userPassed = "N";
//
//		if (UtilValidate.isNotEmpty(examMasterGv)) {
//			passPercentage = examMasterGv.getDouble(CommonConstants.PASS_PERCENTAGE);
//		}
//
//		// checking user is passed or not
//		if (actualUserPercentage >= passPercentage) {
//			userPassed = "Y";
//		}
//
//		Map<String, Object> updateUserAttemptContext = UtilMisc.toMap(CommonConstants.PERFORMANCE_ID, performanceId,
//				CommonConstants.USER_PASSED, userPassed, CommonConstants.PASS_PERCENTAGE, passPercentage,
//				CommonConstants.TOTAL_CORRECT, totalCorrectQuestionsInExam, CommonConstants.TOTAL_WRONG,
//				totalWrongAnswersInExam, CommonConstants.SCORE, score, CommonConstants.USER_LOGIN, userLogin);
//		try {
//			// update userAttemptMaster entity
//			dispatcher.runSync("updateUserAttemptMaster", updateUserAttemptContext);
//		} catch (GenericServiceException e) {
//
//			String errMsg = "Exception occured while calling updateUserAttemptMaster service : "
//					+ e.getMessage();
//			Debug.logError(e, errMsg, module);
//			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
//			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
//			return CommonConstants.ERROR;
//		}
//		
//		Map<String, Object> resultMap =UtilMisc.toMap("totalScore", totalScore,
//									"totalWrongAnswersInExam",totalWrongAnswersInExam,
//									"totalCorrectQuestionsInExam", totalCorrectQuestionsInExam,
//									"actualUserPercentage", actualUserPercentage,
//									CommonConstants.PASS_PERCENTAGE, passPercentage,
//									CommonConstants.SCORE, score,
//									CommonConstants.USER_PASSED, userPassed,
//									CommonConstants.NO_OF_QUESTIONS, noOfQuestions,
//									"noOfUnAnsweredQuestionsByTopicId", noOfUnAnsweredQuestionsByTopicId,
//									"evaluatedQuestionList", evaluatedQuestionList,
//									"noOfCorrectedQuestionsByTopicId", noOfCorrectedQuestionsByTopicId,
//									"updatedUserAttemptTopicMasterList", updatedUserAttemptTopicMasterList
//						);
//		
//		request.setAttribute("resultMap", resultMap);
//		
//		return CommonConstants.SUCCESS;
//	}

}
