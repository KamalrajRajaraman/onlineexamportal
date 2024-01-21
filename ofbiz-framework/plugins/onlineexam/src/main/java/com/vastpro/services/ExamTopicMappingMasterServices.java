package com.vastpro.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class ExamTopicMappingMasterServices {
	
	public static final String module = ExamTopicMappingMasterServices.class.getName();
	
	/**
	 * This method is used to retrieve all the topics assigned to particular exam
	 *  from the ExamTopicMappingViewEntity  
	 * @param DispatchContext 
	 * @param Map<String, ? extends Object>
	 * @return Map<String,Object>
	 */
	public static Map<String,Object> findExamTopicMappingMasterRecords(DispatchContext dctx, Map<String, ? extends Object> context){
		
		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		String examIdPK=(String) context.get(CommonConstants.EXAM_ID);
		
		List<Map<String, Object>> examTopicMappingRecordList = new LinkedList<>();
		
		List<GenericValue> genericValueList = null;
		
		try {
			//Query for fetching record from ExamTopicMappingViewEntity based on examId
			genericValueList =  EntityQuery.use(delegator)
								.from(CommonConstants.EXAM_TOPIC_MAPPING_VIEW_ENTITY)
								.where(CommonConstants.EXAM_ID,examIdPK)
								.cache()
								.queryList();
		} 
		catch (GenericEntityException e) {
			//Exception occured while fetching record from entity
			String errMsg = "Error in fetching record from ExamTopicMappingViewEntity entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		if(UtilValidate.isEmpty(genericValueList)) {
			//If the genericValueList is empty return error map
			String errMsg = "Retreived record list from ExamTopicMappingViewEntity is null or empty";
			Debug.logWarning(errMsg, module);
			return ServiceUtil.returnMessage(CommonConstants.RESPOND_EMPTY,errMsg + module);
		}
		
		if (UtilValidate.isNotEmpty(genericValueList)) {
			//If the genericValueList is not empty, iterate the list
			for(GenericValue genericValue: genericValueList) {
				
				//extracting fields from genericValue object
				String examId = genericValue.getString(CommonConstants.EXAM_ID);
				String examName = genericValue.getString(CommonConstants.EXAM_NAME);
				String topicId = genericValue.getString(CommonConstants.TOPIC_ID);
				String topicName = genericValue.getString(CommonConstants.TOPIC_NAME);
				String percentage = genericValue.getString(CommonConstants.PERCENTAGE);
				String topicPassPercentage = genericValue.getString(CommonConstants.TOPIC_PASS_PERCENTAGE);
				String questionPerExam = genericValue.getString(CommonConstants.QUESTION_PER_EXAM);
				
				//Construct a map with required values
				Map<String, Object> examTopicMappingRecord = new HashMap<>();
				examTopicMappingRecord.put(CommonConstants.EXAM_ID, examId);
				examTopicMappingRecord.put(CommonConstants.EXAM_NAME, examName);
				examTopicMappingRecord.put(CommonConstants.TOPIC_ID, topicId);
				examTopicMappingRecord.put(CommonConstants.TOPIC_NAME, topicName);
				examTopicMappingRecord.put(CommonConstants.PERCENTAGE, percentage);
				examTopicMappingRecord.put(CommonConstants.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
				examTopicMappingRecord.put(CommonConstants.QUESTION_PER_EXAM, questionPerExam);
				
				examTopicMappingRecordList.add(examTopicMappingRecord);
						
			}
			result.put(CommonConstants.EXAM_TOPIC_MAPPING_RECORD_LIST, examTopicMappingRecordList);
			
		}
		return result;
	}
	public static Map<String, Object> findNoOfQuestionsFromExamMaster(DispatchContext dctx, Map<String, ? extends Object> context){
		
		Delegator delegator= dctx.getDelegator();
		String examId= (String) context.get(CommonConstants.EXAM_ID);
		String topicId= (String) context.get(CommonConstants.TOPIC_ID);
		
		GenericValue findPercentageBeforeUpdateGV= null;
		List<GenericValue> topicPercentageListGV= null;
		List<BigDecimal> topicPercentageList=new LinkedList<>();
		Map<String, Object> serviceResultMap=ServiceUtil.returnSuccess();
		
		try {
			findPercentageBeforeUpdateGV = EntityQuery.use(delegator).select(CommonConstants.PERCENTAGE).
								from(CommonConstants.EXAM_TOPIC_MAPPING_MASTER).
								where(CommonConstants.EXAM_ID, examId, CommonConstants.TOPIC_ID, topicId)
								.queryOne();
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
		}
		serviceResultMap.put(CommonConstants.PERCENTAGE_TO_BE_UPDATED, findPercentageBeforeUpdateGV.getBigDecimal(CommonConstants.PERCENTAGE));
		
		try {
			topicPercentageListGV= EntityQuery.use(delegator).select(CommonConstants.PERCENTAGE).from(CommonConstants.EXAM_TOPIC_MAPPING_MASTER).queryList();
			
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
		}
		for(GenericValue topicPercentage: topicPercentageListGV) {
			topicPercentageList.add(topicPercentage.getBigDecimal(CommonConstants.PERCENTAGE));
		}
		serviceResultMap.put(CommonConstants.PERCENTAGE_LIST, topicPercentageList);
		return serviceResultMap;
	}


}
