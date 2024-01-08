package com.vastpro.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.CreateUserExamMappingCheck;
import com.vastpro.validator.FindUserExamMappingCheck;
import com.vastpro.validator.UserExamMappingValidator;

//Event class for UserExamMappingMaster operation
public class UserExamMappingEvents {
	public static final String module = UserExamMappingEvents.class.getName();

	// Method for create a record in UserExamMappingMaster entity
	public static String createUserExamMappingRecord(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Locale locale = request.getLocale();
		Map<String, Object> createUserExamMappingRecordResp = null;

		// Map to get and store the objects from request
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);

		// Validate the fields in the combinedMap for null values
		UserExamMappingValidator validateUserExamMapping = HibernateHelper.populateBeanFromMap(combinedMap,
				UserExamMappingValidator.class);
		Set<ConstraintViolation<UserExamMappingValidator>> validationErrors = HibernateHelper
				.checkValidationErrors(validateUserExamMapping, CreateUserExamMappingCheck.class);
		Boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, validationErrors, request, locale,
				"InvalidErrMsg", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute("hasFormErrors", hasFormErrors);

		if (hasFormErrors) {
			// Set error message in request in case of empty fields
			String errMsg = "Values are not assigned to every fields!";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			Debug.logError("Values are not assigned to every fields! ", module);
			return CommonConstants.ERROR;

		}

		try {
			// calling createUserExamMappingRecord service
			createUserExamMappingRecordResp = dispatcher.runSync("createUserExamMappingRecord", combinedMap);

		} catch (GenericServiceException e) {

			// If any exception occur in service, set error as a result in request object
			Debug.logError(e, "Failed to execute createUserExamMappingRecord service", module);
			String errMsg = "Failed to execute createUserExamMappingRecord service : " + e.getMessage();
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			return CommonConstants.ERROR;
		}

		// checking if the createUserExamMappingRecord service is success or not
		if (ServiceUtil.isSuccess(createUserExamMappingRecordResp)) {
			request.setAttribute("createUserExamMappingRecordMap", createUserExamMappingRecordResp);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			Debug.logInfo("CreateUserExamMappingRecord service has been executed successfully! ", module);

		} else {

			// If the createUserExamMappingRecord service returns Error, set result as error
			// in request
			String errMsg = "Error occured in createUserExamMappingRecord service";
			Debug.logError(errMsg, module);

			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError(errMsg, module);
			return CommonConstants.ERROR;
		}

		return CommonConstants.SUCCESS;
	}

	// Event for showing exams for the particular user based on partyId
	public static String showExamsForPartyId(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Locale locale = request.getLocale();

		// checking partyId comes as a parameter
		String partyId = request.getParameter(CommonConstants.PARTY_ID);

		// checking partyId comes as a attribute
		if (UtilValidate.isEmpty(partyId)) {
			partyId = (String) request.getAttribute(CommonConstants.PARTY_ID);
		}

		// If the partyId didn't came as both parameter and attribute then take it from
		// the userLogin
		if (UtilValidate.isEmpty(partyId)) {
			partyId = userLogin.getString(CommonConstants.PARTY_ID);
		}

		// create a map with partyId, userLogin object
		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstants.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstants.PARTY_ID, partyId);

		UserExamMappingValidator userExamMappingValidator = HibernateHelper.populateBeanFromMap(findExamContext,
				UserExamMappingValidator.class);
		Set<ConstraintViolation<UserExamMappingValidator>> validationErrors = HibernateHelper
				.checkValidationErrors(userExamMappingValidator, FindUserExamMappingCheck.class);
		Boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, validationErrors, request, locale,
				"InvalidErrMsg", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute("hasFormErrors", hasFormErrors);

		if (hasFormErrors) {
			// Set error message in request in case of empty fields
			String errMsg = "Values are not assigned to every fields! ";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError(errMsg, module);
			return CommonConstants.ERROR;
		}

		// calling showExamsForPartyId service for showing exams
		Map<String, Object> showExamsForPartyIdResp = null;
		try {
			showExamsForPartyIdResp = dispatcher.runSync("showExamsForPartyId", findExamContext);
			Debug.logInfo("showExamsForPartyId service has been executed successfully!", module);

		} catch (GenericServiceException e) {
			// If any exception occur in service, set error as a result in request object
			String errMsg = "Failed to execute showExamsForPartyId service";
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, "Failed to execute showExamsForPartyId service");
			return CommonConstants.ERROR;
		}
		if (ServiceUtil.isSuccess(showExamsForPartyIdResp)) {
			request.setAttribute("examList", showExamsForPartyIdResp.get("examList"));
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, "showExamsForPartyId service executed successfully");
		} else {
			request.setAttribute("createUserExamMappingRecordMap", null);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, "Error while executing showExamsForPartyId service");
			return CommonConstants.ERROR;
		}
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
	
	
	public static String evaluateUserAttemptAnswerMaster(HttpServletRequest request, HttpServletResponse response) {
		
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Integer performanceId =  (Integer) request.getSession().getAttribute("performanceId");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		
		List<GenericValue>	userAttemptAnswerMasterList= null;
		
		try {
			//Taking record from userAttemptAnswerMaster entity based on performanceId
			userAttemptAnswerMasterList = EntityQuery.use(delegator).
									from(CommonConstants.USER_ATTEMPT_ANSWER_MASTER).
									where(CommonConstants.PERFORMANCE_ID , performanceId).
									queryList();

		} 
		catch (GenericEntityException e) {
			//If Exception occurred in the query, set the result as Error in request
			String errMsg = "Exception occured while fetching the record from UserAttemptAnswerMaster: "+e.getMessage();
			Debug.logError(e, errMsg , module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		List<Map<String, Object>> evaluatedQuestionList = new LinkedList<>();
		Map<String, Integer> noOfCorrectedQuestionsByTopicId = new HashMap<String, Integer>();
		Map<String, Integer> noOfUnAnsweredQuestionsByTopicId = new HashMap<String, Integer>();
		Double score = 0.0;
	
		//If the userAttemptAnswerMasterList is empty, set the result as Error in request
		if(UtilValidate.isEmpty(userAttemptAnswerMasterList)) {
			String errMsg = "Fetched record from UserAttemptAnswerMaster is null or empty ";
			Debug.logError( errMsg , module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		//If the userAttemptAnswerMasterList is notEmpty 
		if(UtilValidate.isNotEmpty(userAttemptAnswerMasterList)) {
		Integer noOfCorrectQuestions=0;
		Boolean isCorrect = false;
	
		//Iterate the userAttemptAnswerMasterList and taking the question wise submittedAnswer 
		for(GenericValue question : userAttemptAnswerMasterList) {
			Long questionId =  question.getLong(CommonConstants.QUESTION_ID);
			String submittedAnswer = (String) question.get(CommonConstants.SUBMITTED_ANSWER);
			Map<String, Object> questionWithAnswer = new HashMap<>();
			questionWithAnswer.put(CommonConstants.QUESTION_ID, questionId);
			questionWithAnswer.put(CommonConstants.SUBMITTED_ANSWER, submittedAnswer);
			GenericValue questionDetail=null;
			
			try {
				//Fetching question details from QuestionMaster entity
				 questionDetail = EntityQuery.use(delegator).from(CommonConstants.QUESTION_MASTER).where(CommonConstants.QUESTION_ID , questionId).queryOne();
	
			} 
			catch (GenericEntityException e) {
				//If Exception occurred in the query, set the result as Error in request
				String errMsg = "Exception occured while fetching the record from QuestionMaster entity: "+e.getMessage();
				Debug.logError(e, errMsg , module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
				
			}
			
		if(UtilValidate.isEmpty(questionDetail)) {
			String errMsg = "Fetched record from QuestionMaster entity is null or empty ";
			Debug.logError( errMsg , module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
			
			
		if(UtilValidate.isNotEmpty(questionDetail)) {
			
			String topicId = questionDetail.getString(CommonConstants.TOPIC_ID);
			String answer = questionDetail.getString(CommonConstants.ANSWER);
			Double answerValue = questionDetail.getDouble(CommonConstants.ANSWER_VALUE);
			
				questionWithAnswer.put(CommonConstants.TOPIC_ID, topicId);
				questionWithAnswer.put(CommonConstants.ANSWER, answer);
				
				//If submittedAnswer is empty, which is unAnswered
			if(UtilValidate.isEmpty(submittedAnswer))	{
				isCorrect = null;
				questionWithAnswer.put("isCorrect", isCorrect);
				
				if(noOfUnAnsweredQuestionsByTopicId.containsKey(topicId)) {
					noOfUnAnsweredQuestionsByTopicId.replace(topicId, noOfCorrectedQuestionsByTopicId.get(topicId)+1); 	
				}
				else {
					noOfUnAnsweredQuestionsByTopicId.put(topicId, 1);
				}
				
				
			}
			else {
				//If answer equals submittedAnswer 
			if(answer.equals(submittedAnswer)) {
				isCorrect = true;
				score += answerValue;
				questionWithAnswer.put("isCorrect", isCorrect);
				
				//Sum 1 to noOfCorrectedQuestions for this particular topicId key
				if(noOfCorrectedQuestionsByTopicId.containsKey(topicId)) {
					noOfCorrectedQuestionsByTopicId.replace(topicId, noOfCorrectedQuestionsByTopicId.get(topicId)+1); 	
				}
				else {
					noOfCorrectedQuestionsByTopicId.put(topicId, 1);
				}
			}
			//If answer not equals submittedAnswer
			else {
				isCorrect = false;
				questionWithAnswer.put("isCorrect", isCorrect);
			}
		}
			}
		evaluatedQuestionList.add(questionWithAnswer);	
		}
	}
	
	//Find totalQuestions in this topic
	for (Map.Entry<String,Integer> entry : noOfCorrectedQuestionsByTopicId.entrySet())  {
        String topicId = entry.getKey();
        Integer noOfUnAnsweredQuestions = noOfUnAnsweredQuestionsByTopicId.get(topicId);
        Integer noOfCorrectQuestions = entry.getValue();
        GenericValue UserAttemptTopicMasterGv = null;
        try {
    		 UserAttemptTopicMasterGv =EntityQuery.use(delegator)
    						.from(CommonConstants.USER_ATTEMPT_TOPIC_MASTER)
    				.where(CommonConstants.PERFORMANCE_ID, performanceId,CommonConstants.TOPIC_ID, topicId)
    				.queryOne();
    		 
    	} 
        catch (GenericEntityException e) {
    		
    		e.printStackTrace();
    	}
        String userPassedThisTopic = null;
        double actualUserTopicPercentage = 0.0;
        Map<String, Object> updateUserAttemptTopicMasterResp = null;
        if(UtilValidate.isNotEmpty(UserAttemptTopicMasterGv)) {
        	Double userTopicPercentage = UserAttemptTopicMasterGv.getDouble(CommonConstants.TOPIC_PASS_PERCENTAGE);
        	Integer totalQuestionsInThisTopic = UserAttemptTopicMasterGv.getInteger(CommonConstants.TOTAL_QUESTIONS_IN_THIS_TOPIC);
        	 actualUserTopicPercentage =  ((double)noOfCorrectQuestions / (double)totalQuestionsInThisTopic)*100;
        
        	 //checking user is passed or not in particular topic
        	 if(actualUserTopicPercentage >= userTopicPercentage) {
        		 userPassedThisTopic = "Y";
        	}
        	else {
        		 userPassedThisTopic = "N";
        	}
        	
        }
        
        Map<String, Object> updateUserAttemptTopicMasterContext =
        		UtilMisc.toMap(CommonConstants.USER_PASSED_THIS_TOPIC, userPassedThisTopic,
        						CommonConstants.USER_TOPIC_PERCENTAGE, actualUserTopicPercentage,
        						CommonConstants.CORRECT_QUESTIONS_IN_THIS_TOPIC, noOfCorrectQuestions,
        						CommonConstants.PERFORMANCE_ID, performanceId,
        						CommonConstants.TOPIC_ID, topicId
        						,CommonConstants.USER_LOGIN, userLogin);
        
        //update the record in userAttemptTopicMaster entity
        try {
        updateUserAttemptTopicMasterResp	= 
        		 dispatcher.runSync("updateUserAttemptTopicMaster" , updateUserAttemptTopicMasterContext);
		} catch (GenericServiceException e) {
			
			e.printStackTrace();
		}
        
        
	
	}
	
	//adding all topicwise correct questions for total correct Questions in exam
	Integer totalCorrectQuestionsInExam = 0;
	for (Map.Entry<String,Integer> entry : noOfCorrectedQuestionsByTopicId.entrySet())  {
         totalCorrectQuestionsInExam += entry.getValue();
	}
	
	GenericValue userAttemptMasterGv = null;
	//Query for taking noOfQuestions, examId from UserAttemptMaster entity
	try {
		 userAttemptMasterGv = EntityQuery.use(delegator).from(CommonConstants.USER_ATTEMPT_MASTER).where(CommonConstants.PERFORMANCE_ID, performanceId).queryOne();
	} catch (GenericEntityException e) {
		e.printStackTrace();
	}
	
	Integer noOfQuestions=0;
	String examId = null;
	
	//If userAttemptMasterGv is not empty
	if(UtilValidate.isNotEmpty(userAttemptMasterGv)) {
		 noOfQuestions = userAttemptMasterGv.getInteger(CommonConstants.NO_OF_QUESTIONS);
		 examId = userAttemptMasterGv.getString(CommonConstants.EXAM_ID);
	}
	
	Integer totalWrongAnswersInExam = noOfQuestions - totalCorrectQuestionsInExam ; 
	
	double actualPassPercentage = ((double)totalCorrectQuestionsInExam /(double) noOfQuestions)*100;
	GenericValue examMasterGv = null;
	
	try {
		//Taking passpercentage from ExamMaster entity
		 examMasterGv = EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER).where(CommonConstants.EXAM_ID, examId).queryOne();
	} catch (GenericEntityException e) {
		
		e.printStackTrace();
	}
	
	Double passPercentage = 0.0;
	String userPassed = "N";
	
	if(UtilValidate.isNotEmpty(examMasterGv)) {
		 passPercentage = examMasterGv.getDouble(CommonConstants.PASS_PERCENTAGE);
	}
	
	//checking user is passed or not
	if(actualPassPercentage >= passPercentage) {
		userPassed = "Y";
	}
	
	Map<String, Object> updateUserAttemptContext = UtilMisc.toMap(
												CommonConstants.PERFORMANCE_ID, performanceId,
												CommonConstants.USER_PASSED, userPassed,
												CommonConstants.PASS_PERCENTAGE, passPercentage,
												CommonConstants.TOTAL_CORRECT, totalCorrectQuestionsInExam,
												CommonConstants.TOTAL_WRONG, totalWrongAnswersInExam,
												CommonConstants.SCORE, score,
												CommonConstants.USER_LOGIN, userLogin
			);
	try {
		//update userAttemptMaster entity
		dispatcher.runSync("updateUserAttemptMaster", updateUserAttemptContext);
	} catch (GenericServiceException e) {
		
		e.printStackTrace();
	}
		request.setAttribute("totalWrongAnswersInExam", totalWrongAnswersInExam);
		request.setAttribute("totalCorrectQuestionsInExam", totalCorrectQuestionsInExam);
		request.setAttribute(CommonConstants.PASS_PERCENTAGE, actualPassPercentage);
		request.setAttribute(CommonConstants.SCORE, score);
		request.setAttribute("noOfUnAnsweredQuestionsByTopicId", noOfUnAnsweredQuestionsByTopicId);
		request.setAttribute("evaluatedQuestionList", evaluatedQuestionList);
		request.setAttribute("noOfQuestionsByTopicId", noOfCorrectedQuestionsByTopicId);
		return CommonConstants.SUCCESS;
	}

}