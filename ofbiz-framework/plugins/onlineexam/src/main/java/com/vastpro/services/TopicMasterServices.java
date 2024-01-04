package com.vastpro.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class TopicMasterServices {
	public static final String module = TopicMasterServices.class.getName();
	
	public static Map<String, Object> findAllTopics(DispatchContext dctx,Map<String,? extends Object > context){
		
		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();
		List<Map<String, Object>> topicList = new LinkedList<>();
		
		Delegator delegator = dctx.getDelegator();
		List<GenericValue> topicGenericValueList =null;
		try {
			topicGenericValueList = EntityQuery.use(delegator).from(CommonConstants.TOPIC_MASTER).queryList();
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from TopicMaster entity ........" + module);
		}
		for(GenericValue topicGenericValue:topicGenericValueList) {
			Map<String, Object> topic = new HashMap<>();
			String topicId= topicGenericValue.getString(CommonConstants.TOPIC_ID);
			topic.put(CommonConstants.TOPIC_ID, topicId);
			topic.put(CommonConstants.TOPIC_NAME, topicGenericValue.getString(CommonConstants.TOPIC_NAME));
			topicList.add( topic);
		}
		returnSucces.put("topicList", topicList);
		return returnSucces;		
	}
}
