package com.vastpro.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityComparisonOperator;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class TopicMasterServices {
	public static final String module = TopicMasterServices.class.getName();
	
	/**
	 * This method is used to retrieve all topics from TopicMaster entity
	 * @param DispatchContext
	 * @param Map<String, Object>
	 * @return
	 * 		Map<String, Object>
	 */
	public static Map<String, Object> findAllTopics(DispatchContext dctx,Map<String,? extends Object > context){
		
		Map<String, Object> serviceResultMap = ServiceUtil.returnSuccess();
		List<Map<String, Object>> topicList = new LinkedList<>();
		
		Delegator delegator = dctx.getDelegator();
		List<GenericValue> topicGenericValueList =null;
		
		Timestamp currentTime= new Timestamp(System.currentTimeMillis());
		
		EntityExpr makeCondition1 = EntityCondition.makeCondition(CommonConstants.EXPIRATION_DATE, EntityComparisonOperator.EQUALS,null);
		EntityExpr makeCondition2 = EntityCondition.makeCondition(CommonConstants.EXPIRATION_DATE, EntityComparisonOperator.GREATER_THAN, currentTime);
		EntityExpr makeCondition = EntityCondition.makeCondition(makeCondition1, EntityComparisonOperator.OR, makeCondition2); 
		try {
			topicGenericValueList = EntityQuery.use(delegator).from(CommonConstants.TOPIC_MASTER)
					.where(makeCondition).cache().queryList();
		} 
		catch (GenericEntityException e) {
			//Exception occurred while Execute the query
			String errMsg = "Exception occured while fetching record from TopicMasterEntity"+ e.getMessage();
			Debug.logError(e,errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		
		//If the Retrieved topics are empty
		if(UtilValidate.isEmpty(topicGenericValueList)) {
			//If the genericValueList is empty return error map
			String errMsg = "Retreived record list from TopicMasterEntity is null or empty";
			Debug.logWarning(errMsg, module);
			return ServiceUtil.returnMessage(CommonConstants.RESPOND_EMPTY,errMsg + module);
		}
		
		
		
		
		//If the Retrieved topics are not empty 
		if(UtilValidate.isNotEmpty(topicGenericValueList)) {
			for(GenericValue topicGenericValue:topicGenericValueList) {
				Map<String, Object> topic = new HashMap<>();
				String topicId= topicGenericValue.getString(CommonConstants.TOPIC_ID);
				topic.put(CommonConstants.TOPIC_ID, topicId);
				topic.put(CommonConstants.TOPIC_NAME, topicGenericValue.getString(CommonConstants.TOPIC_NAME));
				topicList.add(topic);
			}
		}
		
		serviceResultMap.put(CommonConstants.TOPIC_LIST, topicList);
		return serviceResultMap;		
	}
}
