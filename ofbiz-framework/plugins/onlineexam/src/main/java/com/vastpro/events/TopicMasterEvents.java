package com.vastpro.events;

import java.sql.Timestamp;
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
import org.apache.ofbiz.base.util.UtilValidate;
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

		request.setAttribute(CommonConstants.HAS_FORM_ERROR, hasFormErrors);

		String topicName = (String) combinedMap.get(CommonConstants.TOPIC_NAME);
		
		// create a map with topic details and user login object
		Map<String, Object> addTopicContext = new HashMap<>();
		
		addTopicContext.put(CommonConstants.TOPIC_NAME, topicName);
		addTopicContext.put(CommonConstants.USER_LOGIN, userLogin);
		Map<String, Object> createUpdateTopicResp = null;
		
		if (hasFormErrors) {
			//If hasFormErrors is true, set result as Error in request
			String errMsg = "Error founded while executing hibernate validation in create topic form ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		} 
		
		if(combinedMap.containsKey(CommonConstants.TOPIC_ID)) {
			
			try {
				createUpdateTopicResp = dispatcher.runSync(CommonConstants.UPDATE_TOPIC, combinedMap);
				
				} catch (GenericServiceException e) {
				
					Debug.logError(e, "Failed to execute updateTopic service", module);
					String errMsg = "Failed to execute updateTopic service : " + e.getMessage();
					request.setAttribute(CommonConstants._ERROR_MESSAGE_,  errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
				}		
			}
		else {
		
			try {
				// calling createTopic service
				createUpdateTopicResp = dispatcher.runSync(CommonConstants.CREATE_TOPIC, addTopicContext);
				
				Debug.logInfo("=======createTopic method ran successfully=========",module);
			}

			catch (GenericServiceException e) {
				// If exception occurred error set as result in request object
				Debug.logError(e, "Failed to execute createTopic service", module);
				String errMsg = "Failed to execute createTopic service : " + e.getMessage();
				request.setAttribute(CommonConstants._ERROR_MESSAGE_,  errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}
		}
			
			//If createTopic service is success
			if (ServiceUtil.isSuccess(createUpdateTopicResp)) {
				
				Map<String, Object> topic = UtilMisc.toMap(
						CommonConstants.TOPIC_ID, createUpdateTopicResp.get(CommonConstants.TOPIC_ID), 
						CommonConstants.TOPIC_NAME, createUpdateTopicResp.get(CommonConstants.TOPIC_NAME));
				request.setAttribute(CommonConstants.TOPIC, topic);
				request.setAttribute(CommonConstants.RESULT, createUpdateTopicResp.get(CommonConstants.RESPONSE_MESSAGE));
			}
			else {	
				//If the service returns Error, set result as Error in request
				String errMsg = "Error occurred while running createTopic service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				return CommonConstants.ERROR;
			}

		return CommonConstants.SUCCESS;
	}

	// Event for find all topics from TopicMaster entity
	public static String findAllTopics(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		// Create a map with user login object
		Map<String, Object> findAllTopicContext = new HashMap<>();
		findAllTopicContext.put(CommonConstants.USER_LOGIN, userLogin);

		Map<String, Object> topicList = null;
		try {
			// calling service by name findAllTopics
			topicList = dispatcher.runSync(CommonConstants.FIND_ALL_TOPICS, findAllTopicContext);

		} catch (GenericServiceException e) {
			// If exception occurred, error set as result in request object
			Debug.logError(e, "Failed to execute findAllTopics service", module);
			String errMsg = "Failed to execute findAllTopics service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		// checking the service is success or not
		if (ServiceUtil.isSuccess(topicList)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.TOPIC_LIST, topicList.get(CommonConstants.TOPIC_LIST));
		}
		else {
			//If the service returns Error, set result as Error in request
			String errMsg = "Error occurred while running findAllTopics service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
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
		
		if(UtilValidate.isEmpty(topicId)) {
			//If topicId is empty, set result as error in request
			String errMsg = "topicId is empty";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		Map<String, Object> findTopicByIdResp = null;
		// calling findTopicById service
		try {
			findTopicByIdResp = dispatcher.runSync(CommonConstants.FIND_TOPIC_BY_ID, findTopicContext);

			Debug.logInfo("========findTopicById method executed successfully========", module);
		} catch (GenericServiceException e) {
			// If exception occurred, error set as result in request object
			Debug.logError(e, "Failed to execute findTopicById service", module);
			String errMsg = "Failed to execute findTopicById service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		// checking if the findTopicById service is success or not
		if (ServiceUtil.isSuccess(findTopicByIdResp)) {
			request.setAttribute(CommonConstants.RESULT, findTopicByIdResp.get(CommonConstants.RESPONSE_MESSAGE));
			Map<String, Object> topic = UtilMisc.toMap(CommonConstants.TOPIC_ID,
					findTopicByIdResp.get(CommonConstants.TOPIC_ID), CommonConstants.TOPIC_NAME,
					findTopicByIdResp.get(CommonConstants.TOPIC_NAME));
			request.setAttribute(CommonConstants.TOPIC, topic);

		}
		else {
			//If the service returns Error, set result as Error in request
			String errMsg = "Error occurred while running findTopicById service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		return CommonConstants.SUCCESS;

	}

	// Event for deleting a topic based on topic id
	public static String deleteTopic(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		String topicId = request.getParameter(CommonConstants.TOPIC_ID);
		Timestamp expirationDate = new Timestamp(System.currentTimeMillis());
		
		if(UtilValidate.isEmpty(topicId)) {
			//If the topicId is empty, set the result as error in request
			String errMsg = "topicId is empty";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		// creating map with inclusion of topicId and userLogin object
		Map<String, Object> deleteTopicContext = new HashMap<>();
		deleteTopicContext.put(CommonConstants.USER_LOGIN, userLogin);
		deleteTopicContext.put(CommonConstants.TOPIC_ID, topicId);
		deleteTopicContext.put(CommonConstants.EXPIRATION_DATE, expirationDate);
		Map<String, Object> deleteTopicResp = null;
		try {
			// calling deleteTopic service
			deleteTopicResp = dispatcher.runSync(CommonConstants.DELETE_TOPIC, deleteTopicContext);
			
		} catch (GenericServiceException e) {
			// If Exception occurred, error set as result in request attribute
			
			String errMsg = "Failed to execute deleteTopic service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		if (ServiceUtil.isSuccess(deleteTopicResp)) {
			request.setAttribute(CommonConstants.RESULT, deleteTopicResp.get(CommonConstants.RESPONSE_MESSAGE));
			request.setAttribute("resultMap", deleteTopicResp);
		}
		else {
			//If the service returns Error, set result as Error in request
			String errMsg = "Error occurred while running deleteTopic service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}

		return CommonConstants.SUCCESS;
	}

}
