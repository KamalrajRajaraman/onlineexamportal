package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

//Topic related events 
public class TopicMasterEvents {
	public static final String module = TopicMasterEvents.class.getName();
	
	//Event for creating new topic in TopicMaster entity
	public static String createTopic(HttpServletRequest request, HttpServletResponse response) {
		

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		// Retrieving Topic Form Data from React which is sent as Json Object
		String topicId = (String) request.getAttribute(CommonConstant.TOPIC_ID);
		String topicName = (String) request.getAttribute(CommonConstant.TOPIC_NAME);

		//create a map with topic details and userlogin object 
		Map<String, Object> addTopicContext = new HashMap<>();
		addTopicContext.put(CommonConstant.TOPIC_ID, topicId);
		addTopicContext.put(CommonConstant.TOPIC_NAME, topicName);
		addTopicContext.put(CommonConstant.USER_LOGIN, userLogin);

		//calling createTopic service 
		try {

			Map<String, Object> serviceResult = dispatcher.runSync("createTopic", addTopicContext);
			if (ServiceUtil.isSuccess(serviceResult)) {
				Map<String,Object> topic = UtilMisc.toMap(
						CommonConstant.TOPIC_ID,serviceResult.get(CommonConstant.TOPIC_ID),
						CommonConstant.TOPIC_NAME,serviceResult.get(CommonConstant.TOPIC_NAME));
				request.setAttribute("topic", topic);
				request.setAttribute("result", serviceResult.get(CommonConstant.RESPONSE_MESSAGE));
			}
			Debug.logInfo("=======Created TopicMaster record in this event using service createTopic=========", module);
		}

		catch (GenericServiceException e) {
			//If exception occured error setted as result in request object 
			Debug.logError(e, "Failed to execute createTopic service", module);
			String errMsg = "Failed to execute createTopic service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}
	
	//Event for find all topics from TopicMaster entity
	public static String findAllTopics(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		
		//Create a map with userlogin object
		Map<String, Object> findAllTopicContext = new HashMap<>();
		findAllTopicContext.put(CommonConstant.USER_LOGIN, userLogin);
		
		Map<String, Object> topicList=null;
		try {
			//calling service by name findAllTopics 
			topicList=dispatcher.runSync("findAllTopics", findAllTopicContext);
			
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute findAllTopics service", module);
			String errMsg = "Failed to execute findAllTopics service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		
		//checking the service is success or not
		if(ServiceUtil.isSuccess(topicList)) {
			request.setAttribute(CommonConstant.RESPONSE_MESSAGE, topicList.get(CommonConstant.RESPONSE_MESSAGE));
			request.setAttribute("topicList", topicList.get("topicList"));
		}
		
		return CommonConstant.SUCCESS;
	}
	
	//Event for find a particular topic based on topic id
	public static String findTopicById(HttpServletRequest request, HttpServletResponse response) {
		

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String topicId = (String) request.getAttribute(CommonConstant.TOPIC_ID);

		Map<String, Object> findTopicContext = new HashMap<>();
		findTopicContext.put(CommonConstant.USER_LOGIN, userLogin);
		findTopicContext.put(CommonConstant.TOPIC_ID, topicId);
		
		Map<String, Object> serviceResultMap = null;
		//calling findTopicById service 
		try {
			serviceResultMap = dispatcher.runSync("findTopicById", findTopicContext);
			
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			
			Debug.logError(e, "Failed to execute findTopicById service", module);
			String errMsg = "Failed to execute findTopicById service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		
		//checking if the findTopicById service is success or not
		if (ServiceUtil.isSuccess(serviceResultMap)) {
			request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
			Map<String,Object> topic = UtilMisc.toMap(
					CommonConstant.TOPIC_ID,serviceResultMap.get(CommonConstant.TOPIC_ID),
					CommonConstant.TOPIC_NAME,serviceResultMap.get(CommonConstant.TOPIC_NAME));
			request.setAttribute("topic", topic);
			
		}
		return CommonConstant.SUCCESS;

	}
	
	//Event for deleting a topic based on topic id
	public static String deleteTopic(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String topicId = request.getParameter(CommonConstant.TOPIC_ID);
		
		//creating map with inclusion of topicId and userLogin object 
		Map<String, Object> deleteTopicContext = new HashMap<>();
		deleteTopicContext.put(CommonConstant.USER_LOGIN, userLogin);
		deleteTopicContext.put(CommonConstant.TOPIC_ID,topicId);
		Map<String,Object> serviceResultMap=null;
		 try {
			 //calling deleteTopic service
			 serviceResultMap = dispatcher.runSync("deleteTopic", deleteTopicContext);
			 if (ServiceUtil.isSuccess(serviceResultMap)) {
					request.setAttribute("result", serviceResultMap.get(CommonConstant.RESPONSE_MESSAGE));
					request.setAttribute("resultMap", serviceResultMap);
				}
		} catch (GenericServiceException e) {
			//If Exception occured, error setted as result in request attribute 
			Debug.logError(e, "Failed to execute deleteTopic service", module);
			String errMsg = "Failed to execute deleteTopic service : " + e.getMessage();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		
		
		return CommonConstant.SUCCESS;
	}

}