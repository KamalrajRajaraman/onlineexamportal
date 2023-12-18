package com.vastpro.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class ExamTopicMappingMasterServices {
	
	public static final String module = ExamTopicMappingMasterServices.class.getName();
	
	public static Map<String,Object> findExamTopicMappingMasterRecords(DispatchContext dctx, Map<String, ? extends Object> context){
		
		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		String examIdPK=(String) context.get(CommonConstant.EXAM_ID);
		
		List<Map<String, Object>> examTopicMappingRecordList = new LinkedList<>();
		
		
		List<GenericValue> genericValueList = null;
		
		try {
			genericValueList =  EntityQuery.use(delegator).from("ExamTopicMappingViewEntity").where(CommonConstant.EXAM_ID,examIdPK).queryList();
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from ExamTopicMappingMaster entity ........" + module);
		}
		if (UtilValidate.isNotEmpty(genericValueList)) {
			
			for(GenericValue genericValue: genericValueList) {
				
				String examId = genericValue.getString(CommonConstant.EXAM_ID);
				String examName = genericValue.getString(CommonConstant.EXAM_NAME);
				String topicId = genericValue.getString(CommonConstant.TOPIC_ID);
				String topicName = genericValue.getString(CommonConstant.TOPIC_NAME);
				String percentage = genericValue.getString(CommonConstant.PERCENTAGE);
				String topicPassPercentage = genericValue.getString(CommonConstant.TOPIC_PASS_PERCENTAGE);
				String questionPerExam = genericValue.getString(CommonConstant.QUESTION_PER_EXAM);
				
				Map<String, Object> examTopicMappingRecord = new HashMap<>();
				examTopicMappingRecord.put(CommonConstant.EXAM_ID, examId);
				examTopicMappingRecord.put(CommonConstant.EXAM_NAME, examName);
				examTopicMappingRecord.put(CommonConstant.TOPIC_ID, topicId);
				examTopicMappingRecord.put(CommonConstant.TOPIC_NAME, topicName);
				examTopicMappingRecord.put(CommonConstant.PERCENTAGE, percentage);
				examTopicMappingRecord.put(CommonConstant.TOPIC_PASS_PERCENTAGE, topicPassPercentage);
				examTopicMappingRecord.put(CommonConstant.QUESTION_PER_EXAM, questionPerExam);
				
				examTopicMappingRecordList.add(examTopicMappingRecord);
						
			}
			result.put("examTopicMappingRecordList", examTopicMappingRecordList);
			
		}
		return result;
	}


}
