package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class QuestionMasterEvents {

	public static final String module = QuestionMasterEvents.class.getName();
	public static String createQuestion(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		//Retrieving AddQuestion Form data from react which is sent as json object 
		String questionId = (String) request.getAttribute(CommonConstant.QUESTION_ID);
		String questionDetail = (String) request.getAttribute(CommonConstant.QUESTION_DETAIL);
		String optionA = (String) request.getAttribute(CommonConstant.OPTION_A);
		String optionB = (String) request.getAttribute(CommonConstant.OPTION_B);
		String optionC = (String) request.getAttribute(CommonConstant.OPTION_C);
		String optionD = (String) request.getAttribute(CommonConstant.OPTION_D);
		String optionE = (String) request.getAttribute(CommonConstant.OPTION_E);
		String answer = (String) request.getAttribute(CommonConstant.ANSWER);
		String numAnswers = (String) request.getAttribute(CommonConstant.NUM_ANSWERS);
		String questionType = (String) request.getAttribute(CommonConstant.QUESTION_TYPE);
		String difficultyLevel = (String) request.getAttribute(CommonConstant.DIFFICULTY_LEVEL);
		String answerValue = (String) request.getAttribute(CommonConstant.ANSWER_VALUE);
		String topicId = (String) request.getAttribute(CommonConstant.TOPIC_ID);
		String negativeMarkValue = (String) request.getAttribute(CommonConstant.NEGATIVE_MARK_VALUE);

		Map<String, Object> addQuestionContext = new HashMap<>();
		addQuestionContext.put(CommonConstant.QUESTION_ID, questionId);
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
		try {
			serviceResultMap = dispatcher.runSync("createQuestion", addQuestionContext);
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
				
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
			
			String errMsg = "Unable to create new records in QuestionMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}
	
	public static String findAllQuestions(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		
		Map<String, Object> findAllQuestionContext = new HashMap<>();
		findAllQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);
		
		Map<String, Object> serviceResult=null;
		try {
			
			serviceResult=dispatcher.runSync("findAllQuestions", findAllQuestionContext);
			
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrive records from QuestionMaster entity: " + e.toString();
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
	
	
	
	public static String deleteQuestion(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String questionId = request.getParameter(CommonConstant.QUESTION_ID);
		
		Map<String, Object> deleteQuestionContext = new HashMap<>();
		deleteQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);
		deleteQuestionContext.put(CommonConstant.QUESTION_ID,questionId);
		Map<String,Object> serviceResultMap=null;
		 try {
			 serviceResultMap = dispatcher.runSync("deleteQuestion", deleteQuestionContext);
			 if (ServiceUtil.isSuccess(serviceResultMap)) {
					request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
					request.setAttribute("resultMap", serviceResultMap);
				}
		} catch (GenericServiceException e) {
			String errMsg = "Unable to delete  record  from QuestionMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		
		
		return CommonConstant.SUCCESS;
	}

}
