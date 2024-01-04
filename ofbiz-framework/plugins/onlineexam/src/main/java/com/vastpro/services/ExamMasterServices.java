package com.vastpro.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

import com.vastpro.constants.CommonConstants;

public class ExamMasterServices {
	public static final String module = ExamMasterServices.class.getName();

	public static Map<String, Object> findAllExams(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		List<Map<String, Object>> examList = new LinkedList<>();

		List<GenericValue> examMasterGenericValueList = null;

		try {
			
			examMasterGenericValueList = EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER).queryList();

			if (UtilValidate.isNotEmpty(examMasterGenericValueList)) {

				for (GenericValue examGenericValue : examMasterGenericValueList) {

					String examId = examGenericValue.getString(CommonConstants.EXAM_ID);
					String examName = examGenericValue.getString(CommonConstants.EXAM_NAME);
					String description = examGenericValue.getString(CommonConstants.DESCRIPTION);
					Timestamp creationDate = examGenericValue.getTimestamp(CommonConstants.CREATION_DATE);
					Timestamp expirationDate = examGenericValue.getTimestamp(CommonConstants.EXPIRATION_DATE);
					Long noOfQuestions = examGenericValue.getLong(CommonConstants.NO_OF_QUESTIONS);
					Long durationMinutes = examGenericValue.getLong(CommonConstants.DURATION_MINUTES);
					BigDecimal passPercentage = examGenericValue.getBigDecimal(CommonConstants.PASS_PERCENTAGE);
					String questionsRandomized = examGenericValue.getString(CommonConstants.QUESTIONS_RANDOMIZED);
					String answersMust = examGenericValue.getString(CommonConstants.ANSWER_MUST);
					String enableNegativeMark = examGenericValue.getString(CommonConstants.ENABLE_NEGATIVE_MARK);
					BigDecimal negativeMarkValue = examGenericValue.getBigDecimal(CommonConstants.NEGATIVE_MARK_VALUE);

					Map<String, Object> exam = UtilMisc.toMap(CommonConstants.EXAM_ID, examId, CommonConstants.EXAM_NAME,
							examName, CommonConstants.DESCRIPTION, description, CommonConstants.CREATION_DATE,
							creationDate, CommonConstants.EXPIRATION_DATE, expirationDate,
							CommonConstants.NO_OF_QUESTIONS, noOfQuestions, CommonConstants.DURATION_MINUTES,
							durationMinutes, CommonConstants.PASS_PERCENTAGE, passPercentage,
							CommonConstants.QUESTIONS_RANDOMIZED, questionsRandomized, CommonConstants.ANSWER_MUST,
							answersMust, CommonConstants.ENABLE_NEGATIVE_MARK, enableNegativeMark,
							CommonConstants.NEGATIVE_MARK_VALUE, negativeMarkValue);

					examList.add(exam);
				}
			}
			result.put("examList", examList);
		} catch (GenericEntityException e) {
			Debug.logError(e,"Error in fetching All record from ExamMaster entity" ,module);
			return ServiceUtil.returnError("Error in fetching All  record from ExamMaster entity :" + module);
		}
		return result;

	}

	public static Map<String, Object> findExamById(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String examIdPK = (String) context.get(CommonConstants.EXAM_ID);
		GenericValue examGenericValue = null;

		try {
			
			examGenericValue = EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER)
					.where(CommonConstants.EXAM_ID, examIdPK).queryOne();

			if (UtilValidate.isNotEmpty(examGenericValue)) {
				String examId = examGenericValue.getString(CommonConstants.EXAM_ID);
				String examName = examGenericValue.getString(CommonConstants.EXAM_NAME);
				String description = examGenericValue.getString(CommonConstants.DESCRIPTION);
				Timestamp creationDate = examGenericValue.getTimestamp(CommonConstants.CREATION_DATE);
				Timestamp expirationDate = examGenericValue.getTimestamp(CommonConstants.EXPIRATION_DATE);
				Long noOfQuestions = examGenericValue.getLong(CommonConstants.NO_OF_QUESTIONS);
				Long durationMinutes = examGenericValue.getLong(CommonConstants.DURATION_MINUTES);
				BigDecimal passPercentage = examGenericValue.getBigDecimal(CommonConstants.PASS_PERCENTAGE);
				String questionsRandomized = examGenericValue.getString(CommonConstants.QUESTIONS_RANDOMIZED);
				String answersMust = examGenericValue.getString(CommonConstants.ANSWER_MUST);
				String enableNegativeMark = examGenericValue.getString(CommonConstants.ENABLE_NEGATIVE_MARK);
				BigDecimal negativeMarkValue = examGenericValue.getBigDecimal(CommonConstants.NEGATIVE_MARK_VALUE);

				Map<String, Object> exam = UtilMisc.toMap(CommonConstants.EXAM_ID, examId, CommonConstants.EXAM_NAME,
						examName, CommonConstants.DESCRIPTION, description, CommonConstants.CREATION_DATE, creationDate,
						CommonConstants.EXPIRATION_DATE, expirationDate, CommonConstants.NO_OF_QUESTIONS, noOfQuestions,
						CommonConstants.DURATION_MINUTES, durationMinutes, CommonConstants.PASS_PERCENTAGE,
						passPercentage, CommonConstants.QUESTIONS_RANDOMIZED, questionsRandomized,
						CommonConstants.ANSWER_MUST, answersMust, CommonConstants.ENABLE_NEGATIVE_MARK,
						enableNegativeMark, CommonConstants.NEGATIVE_MARK_VALUE, negativeMarkValue);
				result.put("exam", exam);

			}

		} catch (GenericEntityException e) {
			Debug.logError(e,"Error in fetching exam record by examId from ExamMaster entity" ,module);
			return ServiceUtil.returnError("Error in fetching exam record by examId from ExamMaster entity " + module);
		}
		return result;

	}

	

	
	public static Map<String, Object> findNoOfQuestionCountByExamID(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		String examId = (String) context.get(CommonConstants.EXAM_ID);
		try {

			GenericValue genericQuestionCount = EntityQuery.use(delegator).select(CommonConstants.NO_OF_QUESTIONS)
					.from(CommonConstants.EXAM_MASTER).where(CommonConstants.EXAM_ID, examId).queryOne();

			Long noOfQuestions = genericQuestionCount.getLong(CommonConstants.NO_OF_QUESTIONS);
			resultMap.put(CommonConstants.NO_OF_QUESTIONS, noOfQuestions);

		} catch (GenericEntityException e) {
			Debug.logError(e,"Error in fetching NoOfQuestion By ExamID from ExamMaster entity" ,module);
			return ServiceUtil.returnError("Error finding Number of Questions" + module);
		}

		return resultMap;

	}

	public static Map<String, Object> findQuestionsByTopicIds(DispatchContext dctx,
			Map<String, ? extends Object> context) {
		
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		
		List<Map<String,Object>> topicList = (List<Map<String,Object>>) context.get("topicList");		
		
			
		
		for (Map<String,Object> topic : topicList) {
			List<String> questionIdList = new LinkedList<>();
			String topicId =(String) topic.get(CommonConstants.TOPIC_ID);
			try {
				List<GenericValue> questions = EntityQuery.use(delegator)
						.select(CommonConstants.QUESTION_ID)
						.from(CommonConstants.QUESTION_MASTER)
						.where(CommonConstants.TOPIC_ID, topicId)
						.queryList();

				if (UtilValidate.isNotEmpty(questions)) {
					for (GenericValue question : questions) {
						String questionId = question.getString(CommonConstants.QUESTION_ID);
						questionIdList.add(questionId);
					}			
					topic.put("questionIdList", questionIdList);
				}
			} 
			catch (GenericEntityException e) {
				Debug.logError(e,"Error in fetching record from Question Master entity" ,module);
				return ServiceUtil
						.returnError("Error in fetching record from Question Master entity" + module);
			}
		}
		resultMap.put("topicList", topicList);

		return resultMap;
	}

}
