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
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.TopicMasterCheck;
import com.vastpro.validator.TopicMasterValidator;

//Topic related events 
public class TopicMasterEvents {
	public static final String module = TopicMasterEvents.class.getName();
	

	// Event for creating new topic in TopicMaster entity
	public static String createTopic(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);

		// Retrieving Topic Form Data from React which is sent as Json Object

		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Locale locale = UtilHttp.getLocale(request);
		TopicMasterValidator topicForm = HibernateHelper.populateBeanFromMap(combinedMap, TopicMasterValidator.class);
		Set<ConstraintViolation<TopicMasterValidator>> checkValidationErrors = HibernateHelper
				.checkValidationErrors(topicForm, TopicMasterCheck.class);
		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, checkValidationErrors, request,
				locale, "MandatoryFieldErrMsgTopicForm", CommonConstants.RESOURCE_ERROR, false);
		
		request.setAttribute("hasFormErrors", hasFormErrors);
		
		
		String topicName = (String) combinedMap.get(CommonConstants.TOPIC_NAME);
		// create a map with topic details and userlogin object
		Map<String, Object> addTopicContext = new HashMap<>();
		// addTopicContext.put(CommonConstant.TOPIC_ID, topicId);
		addTopicContext.put(CommonConstants.TOPIC_NAME, topicName);
		addTopicContext.put(CommonConstants.USER_LOGIN, userLogin);

		if (hasFormErrors == true) {
			request.setAttribute("hasFormErrors", hasFormErrors);
			return CommonConstants.ERROR;
		} 
		else {
			request.setAttribute("hasFormErrors", hasFormErrors);
			try {
				// calling createTopic service
				Map<String, Object> serviceResult = dispatcher.runSync("createTopic", addTopicContext);
				if (ServiceUtil.isSuccess(serviceResult)) {
					Map<String, Object> topic = UtilMisc.toMap(CommonConstants.TOPIC_ID,
							serviceResult.get(CommonConstants.TOPIC_ID), CommonConstants.TOPIC_NAME,
							serviceResult.get(CommonConstants.TOPIC_NAME));
					request.setAttribute("topic", topic);
					request.setAttribute(CommonConstants.RESULT, serviceResult.get(CommonConstants.RESPONSE_MESSAGE));
				}
				Debug.logInfo("=======Created TopicMaster record in this event using service createTopic=========",
						module);
			}

			catch (GenericServiceException e) {
				// If exception occured error setted as result in request object
				Debug.logError(e, "Failed to execute createTopic service", module);
				String errMsg = "Failed to execute createTopic service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

		}

		return CommonConstants.SUCCESS;
	}

	// Event for find all topics from TopicMaster entity
	public static String findAllTopics(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		// Create a map with userlogin object
		Map<String, Object> findAllTopicContext = new HashMap<>();
		findAllTopicContext.put(CommonConstants.USER_LOGIN, userLogin);

		Map<String, Object> topicList = null;
		try {
			// calling service by name findAllTopics
			topicList = dispatcher.runSync("findAllTopics", findAllTopicContext);

		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute findAllTopics service", module);
			String errMsg = "Failed to execute findAllTopics service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		// checking the service is success or not
		if (ServiceUtil.isSuccess(topicList)) {
			request.setAttribute(CommonConstants.RESPONSE_MESSAGE, topicList.get(CommonConstants.RESPONSE_MESSAGE));
			request.setAttribute("topicList", topicList.get("topicList"));
		}

		return CommonConstants.SUCCESS;
	}

	// Event for find a particular topic based on topic id
	public static String findTopicById(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		String topicId = (String) request.getAttribute(CommonConstants.TOPIC_ID);

		Map<String, Object> findTopicContext = new HashMap<>();
		findTopicContext.put(CommonConstants.USER_LOGIN, userLogin);
		findTopicContext.put(CommonConstants.TOPIC_ID, topicId);

		Map<String, Object> serviceResultMap = null;
		// calling findTopicById service
		try {
			serviceResultMap = dispatcher.runSync("findTopicById", findTopicContext);

			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {

			Debug.logError(e, "Failed to execute findTopicById service", module);
			String errMsg = "Failed to execute findTopicById service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		// checking if the findTopicById service is success or not
		if (ServiceUtil.isSuccess(serviceResultMap)) {
			request.setAttribute(CommonConstants.RESULT, serviceResultMap.get(CommonConstants.RESPONSE_MESSAGE));
			Map<String, Object> topic = UtilMisc.toMap(CommonConstants.TOPIC_ID,
					serviceResultMap.get(CommonConstants.TOPIC_ID), CommonConstants.TOPIC_NAME,
					serviceResultMap.get(CommonConstants.TOPIC_NAME));
			request.setAttribute("topic", topic);

		}
		return CommonConstants.SUCCESS;

	}

	// Event for deleting a topic based on topic id
	public static String deleteTopic(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		String topicId = request.getParameter(CommonConstants.TOPIC_ID);

		// creating map with inclusion of topicId and userLogin object
		Map<String, Object> deleteTopicContext = new HashMap<>();
		deleteTopicContext.put(CommonConstants.USER_LOGIN, userLogin);
		deleteTopicContext.put(CommonConstants.TOPIC_ID, topicId);
		Map<String, Object> serviceResultMap = null;
		try {
			// calling deleteTopic service
			serviceResultMap = dispatcher.runSync("deleteTopic", deleteTopicContext);
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute(CommonConstants.RESULT, serviceResultMap.get(CommonConstants.RESPONSE_MESSAGE));
				request.setAttribute("resultMap", serviceResultMap);
			}
		} catch (GenericServiceException e) {
			// If Exception occured, error setted as result in request attribute
			Debug.logError(e, "Failed to execute deleteTopic service", module);
			String errMsg = "Failed to execute deleteTopic service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		return CommonConstants.SUCCESS;
	}

}
