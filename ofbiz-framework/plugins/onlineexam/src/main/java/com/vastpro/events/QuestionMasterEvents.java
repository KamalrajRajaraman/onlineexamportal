package com.vastpro.events;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.QuestionMasterCheck;
import com.vastpro.validator.QuestionMasterValidator;

//Class containing questionMaster entity related events
public class QuestionMasterEvents {

	public static final String module = QuestionMasterEvents.class.getName();
	public static String resource_error = "OnlineexamUiLabels";
	
	//Event for creating question in QuestionMaster Entity
	public static String createQuestion(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute(CommonConstant.DELEGATOR);
		
		Map<String, Object> combinedMap =  UtilHttp.getCombinedMap(request);
		
		Locale locale = UtilHttp.getLocale(request);
		QuestionMasterValidator questionForm = HibernateHelper.populateBeanFromMap(combinedMap, QuestionMasterValidator.class);
		Set<ConstraintViolation<QuestionMasterValidator>> checkValidationErrors = HibernateHelper.checkValidationErrors(questionForm, QuestionMasterCheck.class);
						
		
		boolean hasFormErrors = HibernateHelper.validateFormSubmission(
											delegator, 
											checkValidationErrors, 
											request,
											locale, 
											"MandatoryFieldErrMsgQuestionForm",
											resource_error, 
											false);
		
		if(hasFormErrors) {
			request.setAttribute("hasFormErrors", hasFormErrors);
			Debug.logError("The add question form has empty values ", module);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		
		request.setAttribute("hasFormErrors", hasFormErrors);
		
		
		//Retrieving AddQuestion Form data from combined Map which is sent as json object 
		String questionDetail = (String) combinedMap.get(CommonConstant.QUESTION_DETAIL);
		String optionA = (String) combinedMap.get(CommonConstant.OPTION_A);
		String optionB = (String) combinedMap.get(CommonConstant.OPTION_B);
		String optionC = (String) combinedMap.get(CommonConstant.OPTION_C);
		String optionD = (String) combinedMap.get(CommonConstant.OPTION_D);
		String optionE = (String) combinedMap.get(CommonConstant.OPTION_E);
		String answer = (String) combinedMap.get(CommonConstant.ANSWER);
		String numAnswers = (String) combinedMap.get(CommonConstant.NUM_ANSWERS);
		String questionType = (String) combinedMap.get(CommonConstant.QUESTION_TYPE);
		String difficultyLevel = (String) combinedMap.get(CommonConstant.DIFFICULTY_LEVEL);
		String answerValue = (String) combinedMap.get(CommonConstant.ANSWER_VALUE);
		String topicId = (String) combinedMap.get(CommonConstant.TOPIC_ID);
		String negativeMarkValue = (String) combinedMap.get(CommonConstant.NEGATIVE_MARK_VALUE);
		
		
		//creating map with question details and userlogin object
		Map<String, Object> addQuestionContext = new HashMap<>();
	//	addQuestionContext.put(CommonConstant.QUESTION_ID, questionId);
		addQuestionContext.put(CommonConstant.QUESTION_DETAIL, questionDetail);
		addQuestionContext.put(CommonConstant.OPTION_A, optionA);
		addQuestionContext.put(CommonConstant.OPTION_B, optionB);
		addQuestionContext.put(CommonConstant.OPTION_C, optionC);
		addQuestionContext.put(CommonConstant.OPTION_D, optionD);
		addQuestionContext.put(CommonConstant.OPTION_E, optionE);
		addQuestionContext.put(CommonConstant.ANSWER, answer);
		addQuestionContext.put(CommonConstant.NUM_ANSWERS, numAnswers);
		addQuestionContext.put(CommonConstant.QUESTION_TYPE, questionType);
		addQuestionContext.put(CommonConstant.DIFFICULTY_LEVEL, difficultyLevel);
		addQuestionContext.put(CommonConstant.ANSWER_VALUE, answerValue);
		addQuestionContext.put(CommonConstant.TOPIC_ID, topicId);
		addQuestionContext.put(CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue);
		addQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> serviceResultMap = null;
		
		//calling createQuestion service for creating question
		try {
			serviceResultMap = dispatcher.runSync("createQuestion", addQuestionContext);
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute(CommonConstant.RESULT, serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
				
				//created a new map called question and put all question details in that and set in request object
				Map<String,Object> question = new HashMap<>();
				question.put(CommonConstant.QUESTION_ID, serviceResultMap.get(CommonConstant.QUESTION_ID));
				question.put(CommonConstant.QUESTION_DETAIL, serviceResultMap.get(CommonConstant.QUESTION_DETAIL));
				question.put(CommonConstant.OPTION_A, serviceResultMap.get(CommonConstant.OPTION_A));
				question.put(CommonConstant.OPTION_B, serviceResultMap.get(CommonConstant.OPTION_B));
				question.put(CommonConstant.OPTION_C, serviceResultMap.get(CommonConstant.OPTION_C));
				question.put(CommonConstant.OPTION_D, serviceResultMap.get(CommonConstant.OPTION_D));
				question.put(CommonConstant.OPTION_E, serviceResultMap.get(CommonConstant.OPTION_E));
				question.put(CommonConstant.ANSWER, serviceResultMap.get(CommonConstant.ANSWER));
				question.put(CommonConstant.NUM_ANSWERS, serviceResultMap.get(CommonConstant.NUM_ANSWERS));
				question.put(CommonConstant.QUESTION_TYPE, serviceResultMap.get(CommonConstant.QUESTION_TYPE));
				question.put(CommonConstant.DIFFICULTY_LEVEL, serviceResultMap.get(CommonConstant.DIFFICULTY_LEVEL));
				question.put(CommonConstant.ANSWER_VALUE, serviceResultMap.get(CommonConstant.ANSWER_VALUE));
				question.put(CommonConstant.TOPIC_ID, serviceResultMap.get(CommonConstant.TOPIC_ID));
				question.put(CommonConstant.NEGATIVE_MARK_VALUE, serviceResultMap.get(CommonConstant.NEGATIVE_MARK_VALUE));
				
				request.setAttribute("question", question);
				
			}

			Debug.logInfo("=======Created QuestionMaster record in this event using service createQuestion=========",
					module);

		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute createQuestion service", module);
			String errMsg = "Failed to execute createQuestion service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}
	
	//Event for find All questions from QuestionMaster entity
	public static String findAllQuestions(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		
		//create a map with userLogin object
		Map<String, Object> findAllQuestionContext = new HashMap<>();
		findAllQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);
		
		Map<String, Object> serviceResult=null;
		
		//calling findAllQuestions 
		try {
			serviceResult=dispatcher.runSync("findAllQuestions", findAllQuestionContext);
		
		} catch (GenericServiceException e) {
			
			Debug.logError(e, "Failed to execute findAllQuestions service", module);
			String errMsg = "Failed to execute findAllQuestions service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		if(ServiceUtil.isSuccess(serviceResult)) {
			request.setAttribute("result", serviceResult.get(CommonConstant.RESPONSE_MESSAGE));
			request.setAttribute("questionList", serviceResult.get("questionList"));
		}
		
		return CommonConstant.SUCCESS;
	}
	
	
	//Event for deleting a question from QuestionMaster entity based on questionId
	public static String deleteQuestion(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String questionId = request.getParameter(CommonConstant.QUESTION_ID);
		
		//create a map with questionId, userlogin
		Map<String, Object> deleteQuestionContext = new HashMap<>();
		deleteQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);
		deleteQuestionContext.put(CommonConstant.QUESTION_ID,questionId);
		Map<String,Object> serviceResultMap=null;
		//calling deleteQuestion service for delete a question
		try {
			 serviceResultMap = dispatcher.runSync("deleteQuestion", deleteQuestionContext);
			 if (ServiceUtil.isSuccess(serviceResultMap)) {
					request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
					request.setAttribute("resultMap", serviceResultMap);
				}
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute deleteQuestion service", module);
			String errMsg = "Failed to execute deleteQuestion service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		
		
		return CommonConstant.SUCCESS;
	}

}
