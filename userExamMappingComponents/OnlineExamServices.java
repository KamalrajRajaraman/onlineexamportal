package com.vastpro.onlineexam.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;
import com.vastpro.ofbizdemo.services.OfbizDemoServices;

public class OnlineExamServices {
	public static final String module = OfbizDemoServices.class.getName();

	public static Map<String, Object> findAllExams(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		List<Map<String, Object>> examList = new LinkedList<>();

		List<GenericValue> examMasterGenericValueList = null;

		try {
			// examsList =
			// EntityQuery.use(delegator).from(CommonConstant.EXAM_MASTER).cache(true).queryList();
			examMasterGenericValueList = EntityQuery.use(delegator).from(CommonConstant.EXAM_MASTER).queryList();

			if (UtilValidate.isNotEmpty(examMasterGenericValueList)) {

				for (GenericValue examGenericValue : examMasterGenericValueList) {

					String examId = examGenericValue.getString(CommonConstant.EXAM_ID);
					String examName = examGenericValue.getString(CommonConstant.EXAM_NAME);
					String description = examGenericValue.getString(CommonConstant.DESCRIPTION);
					Timestamp creationDate = examGenericValue.getTimestamp(CommonConstant.CREATION_DATE);
					Timestamp expirationDate = examGenericValue.getTimestamp(CommonConstant.EXPIRATION_DATE);
					Long noOfQuestions = examGenericValue.getLong(CommonConstant.NO_OF_QUESTIONS);
					Long durationMinutes = examGenericValue.getLong(CommonConstant.DURATION_MINUTES);
					BigDecimal passPercentage = examGenericValue.getBigDecimal(CommonConstant.PASS_PERCENTAGE);
					String questionsRandomized = examGenericValue.getString(CommonConstant.QUESTIONS_RANDOMIZED);
					String answersMust = examGenericValue.getString(CommonConstant.ANSWER_MUST);
					String enableNegativeMark = examGenericValue.getString(CommonConstant.ENABLE_NEGATIVE_MARK);
					BigDecimal negativeMarkValue = examGenericValue.getBigDecimal(CommonConstant.NEGATIVE_MARK_VALUE);

					Map<String, Object> exam = UtilMisc.toMap(CommonConstant.EXAM_ID, examId, CommonConstant.EXAM_NAME,
							examName, CommonConstant.DESCRIPTION, description, CommonConstant.CREATION_DATE,
							creationDate, CommonConstant.EXPIRATION_DATE, expirationDate,
							CommonConstant.NO_OF_QUESTIONS, noOfQuestions, CommonConstant.DURATION_MINUTES,
							durationMinutes, CommonConstant.PASS_PERCENTAGE, passPercentage,
							CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized, CommonConstant.ANSWER_MUST,
							answersMust, CommonConstant.ENABLE_NEGATIVE_MARK, enableNegativeMark,
							CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue);

					examList.add(exam);
				}
			}
			result.put("examList", examList);
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from ExamMaster entity ........" + module);
		}
		return result;

	}

	public static Map<String, Object> findExamById(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String examIdPK = (String) context.get(CommonConstant.EXAM_ID);
		GenericValue examGenericValue = null;

		try {
			// examsList =
			// EntityQuery.use(delegator).from(CommonConstant.EXAM_MASTER).cache(true).queryList();
			examGenericValue = EntityQuery.use(delegator).from(CommonConstant.EXAM_MASTER)
					.where(CommonConstant.EXAM_ID, examIdPK).queryOne();

			if (UtilValidate.isNotEmpty(examGenericValue)) {
				String examId = examGenericValue.getString(CommonConstant.EXAM_ID);
				String examName = examGenericValue.getString(CommonConstant.EXAM_NAME);
				String description = examGenericValue.getString(CommonConstant.DESCRIPTION);
				Timestamp creationDate = examGenericValue.getTimestamp(CommonConstant.CREATION_DATE);
				Timestamp expirationDate = examGenericValue.getTimestamp(CommonConstant.EXPIRATION_DATE);
				Long noOfQuestions = examGenericValue.getLong(CommonConstant.NO_OF_QUESTIONS);
				Long durationMinutes = examGenericValue.getLong(CommonConstant.DURATION_MINUTES);
				BigDecimal passPercentage = examGenericValue.getBigDecimal(CommonConstant.PASS_PERCENTAGE);
				String questionsRandomized = examGenericValue.getString(CommonConstant.QUESTIONS_RANDOMIZED);
				String answersMust = examGenericValue.getString(CommonConstant.ANSWER_MUST);
				String enableNegativeMark = examGenericValue.getString(CommonConstant.ENABLE_NEGATIVE_MARK);
				BigDecimal negativeMarkValue = examGenericValue.getBigDecimal(CommonConstant.NEGATIVE_MARK_VALUE);

				Map<String, Object> exam = UtilMisc.toMap(CommonConstant.EXAM_ID, examId, CommonConstant.EXAM_NAME,
						examName, CommonConstant.DESCRIPTION, description, CommonConstant.CREATION_DATE, creationDate,
						CommonConstant.EXPIRATION_DATE, expirationDate, CommonConstant.NO_OF_QUESTIONS, noOfQuestions,
						CommonConstant.DURATION_MINUTES, durationMinutes, CommonConstant.PASS_PERCENTAGE,
						passPercentage, CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized,
						CommonConstant.ANSWER_MUST, answersMust, CommonConstant.ENABLE_NEGATIVE_MARK,
						enableNegativeMark, CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue);
				result.put("exam", exam);

			}

		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from ExamMaster entity ........" + module);
		}
		return result;

	}

	// Topic services

	public static Map<String, Object> findAllTopics(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();
		List<Map<String, Object>> topicList = new LinkedList<>();

		Delegator delegator = dctx.getDelegator();
		List<GenericValue> topicGenericValueList = null;
		try {
			topicGenericValueList = EntityQuery.use(delegator).from(CommonConstant.TOPIC_MASTER).queryList();
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from TopicMaster entity ........" + module);
		}
		for (GenericValue topicGenericValue : topicGenericValueList) {
			Map<String, Object> topic = new HashMap<>();
			String topicId = topicGenericValue.getString(CommonConstant.TOPIC_ID);
			topic.put(CommonConstant.TOPIC_ID, topicId);
			topic.put(CommonConstant.TOPIC_NAME, topicGenericValue.getString(CommonConstant.TOPIC_NAME));
			topicList.add(topic);
		}
		returnSucces.put("topicList", topicList);
		return returnSucces;

	}

	// Question services
	public static Map<String, Object> findAllQuestions(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();
		List<Map<String, Object>> questionList = new LinkedList<>();

		Delegator delegator = dctx.getDelegator();
		List<GenericValue> questionGenericValueList = null;
		try {
			questionGenericValueList = EntityQuery.use(delegator).from(CommonConstant.QUESTION_MASTER).queryList();
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from QuestionMaster entity ........" + module);
		}
		for (GenericValue questionGenericValue : questionGenericValueList) {

			Long questionId = questionGenericValue.getLong(CommonConstant.QUESTION_ID);
			String questionDetail = questionGenericValue.getString(CommonConstant.QUESTION_DETAIL);
			String optionA = questionGenericValue.getString(CommonConstant.OPTION_A);
			String optionB = questionGenericValue.getString(CommonConstant.OPTION_B);
			String optionC = questionGenericValue.getString(CommonConstant.OPTION_C);
			String optionD = questionGenericValue.getString(CommonConstant.OPTION_D);
			String optionE = questionGenericValue.getString(CommonConstant.OPTION_E);
			String answer = questionGenericValue.getString(CommonConstant.ANSWER);
			Long numAnswers = questionGenericValue.getLong(CommonConstant.NUM_ANSWERS);
			String questionType = questionGenericValue.getString(CommonConstant.QUESTION_TYPE);
			Integer difficultyLevel = questionGenericValue.getInteger(CommonConstant.DIFFICULTY_LEVEL);
			BigDecimal answerValue = questionGenericValue.getBigDecimal(CommonConstant.ANSWER_VALUE);
			String topicId = questionGenericValue.getString(CommonConstant.TOPIC_ID);
			BigDecimal negativeMarkValue = questionGenericValue.getBigDecimal(CommonConstant.NEGATIVE_MARK_VALUE);

			Map<String, Object> question = new HashMap<>();
			question.put(CommonConstant.QUESTION_ID, questionId);
			question.put(CommonConstant.QUESTION_DETAIL, questionDetail);
			question.put(CommonConstant.OPTION_A, optionA);
			question.put(CommonConstant.OPTION_B, optionB);
			question.put(CommonConstant.OPTION_C, optionC);
			question.put(CommonConstant.OPTION_D, optionD);
			question.put(CommonConstant.OPTION_E, optionE);
			question.put(CommonConstant.ANSWER, answer);
			question.put(CommonConstant.NUM_ANSWERS, numAnswers);
			question.put(CommonConstant.QUESTION_TYPE, questionType);
			question.put(CommonConstant.DIFFICULTY_LEVEL, difficultyLevel);
			question.put(CommonConstant.ANSWER_VALUE, answerValue);
			question.put(CommonConstant.TOPIC_ID, topicId);
			question.put(CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue);

			questionList.add(question);
		}
		returnSucces.put("questionList", questionList);
		return returnSucces;

	}

	// FindAll Users

	public static Map<String, Object> findAllUsers(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();

		Delegator delegator = dctx.getDelegator();

		List<Map<String, Object>> userList = new LinkedList<>();
		List<GenericValue> GenericValueList = null;

		try {
			GenericValueList = EntityQuery.use(delegator).from("UserMaster").where("roleTypeId", "PERSON_ROLE")
					.queryList();

		} catch (GenericEntityException e) {

			e.printStackTrace();
		}
		if (UtilValidate.isNotEmpty(GenericValueList)) {

			for (GenericValue genericValue : GenericValueList) {
				String firstName = genericValue.getString("firstName");
				String lastName = genericValue.getString("lastName");
				String partyId = genericValue.getString("partyId");
				String roleTypeId = genericValue.getString("roleTypeId");
				String userLoginId = genericValue.getString("userLoginId");

				Map<String, Object> user = new HashMap<>();
				user.put("firstName", firstName);
				user.put("lastName", lastName);
				user.put("partyId", partyId);
				user.put("roleTypeId", roleTypeId);
				user.put("userLoginId", userLoginId);

				userList.add(user);
			}

			returnSucces.put("userList", userList);

		}
		return returnSucces;
	}

	public static Map<String, Object> showExamsForPartyId(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String partyIdPK = (String) context.get(CommonConstant.PARTY_ID);
		List<GenericValue> examGenericValue = null;
		List<Map<String, Object>> mapValuesList= new LinkedList<Map<String,Object>>();
		try {
			
			// EntityQuery.use(delegator).from(CommonConstant.EXAM_MASTER).cache(true).queryList();
			examGenericValue = EntityQuery.use(delegator).from("UserExamMappingViewEntity")
					.where(CommonConstant.PARTY_ID, partyIdPK).queryList();

			if (examGenericValue != null) {

				for (GenericValue value : examGenericValue) {
					String examId = value.getString(CommonConstant.EXAM_ID);
					String examName = value.getString(CommonConstant.EXAM_NAME);

					Map<String, Object> exam = UtilMisc.toMap(CommonConstant.EXAM_ID, examId,
							CommonConstant.EXAM_NAME, examName);
					mapValuesList.add(exam); 
				}
				
				result.put("examList", mapValuesList);
				
			}

		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from UserExamMappingViewEntity entity ........" + module);
		}
		return result;

	}

}

//	 public static Map<String, Object> findExams(DispatchContext dctx, Map<String, ? extends Object> context){
//		Delegator delegator= dctx.getDelegator();
//		 Map<String, Object> result = ServiceUtil.returnSuccess();
//		 Map<String, Object> examDetailMap = new HashMap<>();
//		 
//		 List<GenericValue> examsList = null;
//		 
//		 try {
//			 examsList=EntityQuery.use(delegator).from("ExamMaster").cache(true).queryList();
//			 
//			 if(UtilValidate.isNotEmpty(examsList)) {
//				 System.out.println("examList : "+examsList);
//				
//				 examsList.forEach((value)->{
//					 String examId=value.getString("examId");
//					 String examName= value.getString("examName");
//					 String description= value.getString("description");
//					 String creationDate= value.getString("creationDate");
//					 String expirationDate=value.getString("expirationDate");
//					 String noOfQuestions=value.getString("noOfQuestions");
//					 String durationMinutes=value.getString("durationMinutes");
//					 String passPercentage= value.getString("passPercentage");
//					 String questionsRandomized= value.getString("questionsRandomized");
//					 String answersMust = value.getString("answersMust");
//					 String enableNegativeMark = value.getString("enableNegativeMark");
//					 String negativeMarkValue = value.getString("negativeMarkValue");
//					 
//					 Map<String, String> valueMap = UtilMisc.toMap(
//							 "examId", examId, "examName", examName, "description",description, "creationDate", creationDate,
//							 "expirationDate",expirationDate, "noOfQuestions", noOfQuestions, "durationMinutes", durationMinutes,
//							 "passPercentage", passPercentage, "questionsRandomized", questionsRandomized, "answersMust", answersMust, 
//							 "enableNegativeMark", enableNegativeMark, "negativeMarkValue",negativeMarkValue
//							 );
//		
//					 examDetailMap.put(examId, valueMap);
//				 });
//			 }
//			 result.put("examList", examDetailMap);
//			 
//			 
//			 
//			 
//			 //	String examId=examsList.get(0);
//			// result.put("examsList",examsList);
//		 }
//		 catch(GenericEntityException e) {
//			 return ServiceUtil.returnError("Error in fetching record from ExamMaster entity ........" + module);
//		 }
//		 
//		 System.out.println("Result Map: "+result);
//		 
//		 
//		 return result;
//	 }
