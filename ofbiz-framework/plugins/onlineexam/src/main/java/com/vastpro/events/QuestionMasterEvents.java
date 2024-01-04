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

	// Event for creating question in QuestionMaster Entity
	public static String createQuestion(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstant.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstant.DELEGATOR);

		// Create a combined map from request
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Locale locale = UtilHttp.getLocale(request);

		//Hibernate validation for create question input values
		QuestionMasterValidator questionForm = HibernateHelper.populateBeanFromMap(combinedMap,
				QuestionMasterValidator.class);
		Set<ConstraintViolation<QuestionMasterValidator>> checkValidationErrors = HibernateHelper
				.checkValidationErrors(questionForm, QuestionMasterCheck.class);

		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, checkValidationErrors, request,
				locale, "MandatoryFieldErrMsgQuestionForm", CommonConstant.RESOURCE_ERROR, false);
		request.setAttribute("hasFormErrors", hasFormErrors);

		// Checking form has errors or not
		if (hasFormErrors) {
			
			String errMsg = "Error founded while executing hibernate validation in create question form ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			
			Debug.logError("The add question form has empty values ", module);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		Map<String, Object> createQuestionResp = null;

		// calling createQuestion service for creating question
		try {
			createQuestionResp = dispatcher.runSync("createQuestion", combinedMap);
			Debug.logInfo("Succesfully executed createQuestion service", module);

		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute createQuestion service", module);
			String errMsg = "Failed to execute createQuestion service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		// If the service is success, set the result as success in request
		if (ServiceUtil.isSuccess(createQuestionResp)) {
			request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
			request.setAttribute("question", createQuestionResp);
		} else {
			// If the service return Error, set the result as error in request
			String errMsg = "Error occured while running createQuestion service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}

	// Event for find All questions from QuestionMaster entity
	public static String findAllQuestions(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstant.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstant.DISPATCHER);

		// create a map with userLogin object
		Map<String, Object> findAllQuestionContext = new HashMap<>();
		findAllQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> findAllQuestionsResp = null;

		// calling findAllQuestions
		try {
			findAllQuestionsResp = dispatcher.runSync("findAllQuestions", findAllQuestionContext);
			Debug.logInfo("Succesfully executed findAllQuestions service", module);
		} catch (GenericServiceException e) {
			// If Exception occurred while execute the service
			String errMsg = "Failed to execute findAllQuestions service " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		// If the service is success, set the result as success in request
		if (ServiceUtil.isSuccess(findAllQuestionsResp)) {
			request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
			request.setAttribute("questionList", findAllQuestionsResp.get("questionList"));
		} else {
			// If the service returns error, set the result as error in request
			String errMsg = "Error occured while running findAllQuestions service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}

	// Event for deleting a question from QuestionMaster entity based on questionId
	public static String deleteQuestion(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstant.DISPATCHER);

		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Map<String, Object> deleteQuestionResp = null;

		// calling deleteQuestion service
		try {
			deleteQuestionResp = dispatcher.runSync("deleteQuestion", combinedMap);
			Debug.logInfo("Succesfully executed deleteQuestion service", module);
		} catch (GenericServiceException e) {
			// If Exception occured while execute the service, set result as Error in request
			Debug.logError(e, "Failed to execute deleteQuestion service", module);
			String errMsg = "Failed to execute deleteQuestion service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		// If the deleteQuestion service is success, set result as success in request
		if (ServiceUtil.isSuccess(deleteQuestionResp)) {
			request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
			request.setAttribute("resultMap", deleteQuestionResp);
		} else {
			// If the deleteQuestion service returns Error, set result as error in request
			String errMsg = "Error occured in deleteQuestion service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
		}
		return CommonConstant.SUCCESS;
	}

}
