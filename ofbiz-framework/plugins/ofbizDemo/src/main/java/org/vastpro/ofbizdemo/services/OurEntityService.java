package org.vastpro.ofbizdemo.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class OurEntityService {

	public static final String module = OurEntityService.class.getName();

	public static Map<String, Object> selectAll(DispatchContext dctx, Map<String, ? extends Object> context) {
		List<GenericValue> ofbizDemoList = null;
		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		// using Entity Engine

//		try {
//			ofbizDemoList = delegator.findList("OfbizDemo", null, null, null, null, false);
//		} catch (GenericEntityException e) {
//			Debug.logError(e, module);
//			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
//		}

		// Using EntityQuery API

//		try {
//			ofbizDemoList=EntityQuery.use(delegator).from("OfbizDemo").queryList();
//		} catch (GenericEntityException e) {
//			
//			Debug.logError(e, module);
//			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
//		}

		// using Entity Engine

		Set<String> fieldToSelect = UtilMisc.toSet("ofbizDemoId", "firstName");

		List<String> orderBy = UtilMisc.toList("ofbizDemoId");

		try {
			ofbizDemoList = delegator.findList("OfbizDemo", null, fieldToSelect, orderBy, null, false);
			Debug.logInfo("=======================Using Entity Engine" + ofbizDemoList + "============", module);
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
		}

		// Using Entity Query Api

		try {
			ofbizDemoList = EntityQuery.use(delegator).select("ofbizDemoId", "firstName").from("OfbizDemo")
					.orderBy("ofbizDemoId").queryList();
			Debug.logInfo("=======================Using Entity Query Api" + ofbizDemoList + "============", module);
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
		}

		// Condition record fetching
		// using Entity Engine
		EntityListIterator eli = null;
		GenericValue data=null;
		List<EntityExpr> expr = UtilMisc.toList(EntityCondition.makeCondition("ofbizDemoTypeId", "INTERNAL"),
				EntityCondition.makeCondition("firstName", "Kamalraj"));

		EntityCondition cond = EntityCondition.makeCondition(expr, EntityOperator.AND);

		orderBy = UtilMisc.toList("ofbizDemoId");

		try {
			eli = delegator.find("OfbizDemo", cond, null, null, orderBy, null);
			
			while ((data=eli.next())!=null) {
				Debug.logInfo(" Condition record fetching=======================Using Entity Engine" + data
						+ "============"+System.lineSeparator(), module);
			}

		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
		}

		// Using Entity QueryApi
		try {

			eli = EntityQuery.use(delegator).from("OfbizDemo")
					.where("ofbizDemoTypeId", "INTERNAL", "firstName", "Kamalraj").orderBy(orderBy).queryIterator();
			
			while ((data=eli.next())!=null) {
				Debug.logInfo(" Condition record fetching =======================Using Entity Api" + data
						+ "============"+System.lineSeparator(), module);

			}
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
		}

		return result;

	}
	
	
//	List<GenericValue> queryList = EntityQuery.use(delegator)
////			 .select(CommonConstant.QUESTION_ID)
////			 .from(CommonConstant.QUESTION_MASTER)
////			 .where(CommonConstant.TOPIC_ID,topic.get(CommonConstant.TOPIC_ID))
////			 .orderBy("-RANDOM()")
////			 .limit(maxRow).queryList();
//			  
//			  EntityFindOptions findOpts = new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, false);
//			  findOpts.setLimit(maxRow);
//			  EntityExpr condition = EntityCondition.makeCondition(CommonConstant.TOPIC_ID,topic.get(CommonConstant.TOPIC_ID));
//			  Set<String> fieldsToSelect = UtilMisc.toSet(CommonConstant.QUESTION_ID);
//			  List<String> order = UtilMisc.toList("-RANDOM()");
//			  List<GenericValue> findList = delegator.findList(CommonConstant.QUESTION_MASTER, condition, fieldsToSelect, order, findOpts, false);
			

//	Random rd = new Random();
//	List<GenericValue> selectedQuestions = new ArrayList<>();
//
//	for (int i = 0; i < questionsPerExam; i++) {
//		int rand = rd.nextInt(topicQuestions.size());
//		selectedQuestions.add(topicQuestions.get(rand));
//		topicQuestions.remove(rand);
//		
//	}
//	 attemptMasterCtx.put("topicIds", topicList);
//	 Map<String, Object> findQuestionsByTopicIdsResp=null;
//	 try {
//		 findQuestionsByTopicIdsResp = dispatcher.runSync("findQuestionsByTopicIds", attemptMasterCtx );
//	} catch (GenericServiceException e) {
//		String errMsg = "Unable to retrive QuestionIds from QuestionMaster entity: " + e.toString();
//		request.setAttribute("_ERROR_MESSAGE_", errMsg);
//		request.setAttribute("result", CommonConstant.ERROR);
//		return CommonConstant.ERROR;									
//	}
//	List randamQuestions = new LinkedList<>();
//	if(ServiceUtil.isSuccess(findQuestionsByTopicIdsResp)){
//		List<Map<String, Object>> topicWiseQuestions =(List<Map<String, Object>>) findQuestionsByTopicIdsResp.get("questionMap");
//		for(Map<String, Object> questions:topicWiseQuestions) {
//			
//			
//		}
//	
//		
//	}
	
	
//			List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("shoppingListTypeId", EntityOperator.EQUALS, "SLT_AUTO_REODR"),
//			 EntityCondition.makeCondition("isActive", EntityOperator.EQUALS, "Y"));
//			 EntityCondition cond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
//			 List<String> order = UtilMisc.toList("-lastOrderedDate");
//			 EntityListIterator eli = null;
//			 eli = delegator.find("ShoppingList", cond, null, null, order, null);
//			 
	
}
