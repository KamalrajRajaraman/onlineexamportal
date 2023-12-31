package com.vastpro.services;

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

public class ExamMasterServices {
	public static final String module = ExamMasterServices.class.getName();

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
			e.printStackTrace();
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

	public static Map<String, Object> getExamDetails(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String examIdPK = (String) context.get(CommonConstant.EXAM_ID);
		List<Map<String, Object>> examList = new LinkedList<>();

		List<GenericValue> examMasterGenericValueList = null;

		try {
			examMasterGenericValueList = EntityQuery.use(delegator).from("ExamDetails")
					.where(CommonConstant.EXAM_ID, examIdPK).queryList();
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("Error in fetching record from ExamMaster entity ........" + module);
		}

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

				String topicId = examGenericValue.getString(CommonConstant.TOPIC_ID);
				String topicName = examGenericValue.getString(CommonConstant.TOPIC_NAME);

				String percentage = examGenericValue.getString(CommonConstant.PERCENTAGE);
				String topicPassPercentage = examGenericValue.getString(CommonConstant.TOPIC_PASS_PERCENTAGE);
				String questionPerExam = examGenericValue.getString(CommonConstant.QUESTION_PER_EXAM);

				Long questionId = examGenericValue.getLong(CommonConstant.QUESTION_ID);
				String questionDetail = examGenericValue.getString(CommonConstant.QUESTION_DETAIL);
				String optionA = examGenericValue.getString(CommonConstant.OPTION_A);
				String optionB = examGenericValue.getString(CommonConstant.OPTION_B);
				String optionC = examGenericValue.getString(CommonConstant.OPTION_C);
				String optionD = examGenericValue.getString(CommonConstant.OPTION_D);
				String optionE = examGenericValue.getString(CommonConstant.OPTION_E);
				String answer = examGenericValue.getString(CommonConstant.ANSWER);
				Long numAnswers = examGenericValue.getLong(CommonConstant.NUM_ANSWERS);
				String questionType = examGenericValue.getString(CommonConstant.QUESTION_TYPE);
				Integer difficultyLevel = examGenericValue.getInteger(CommonConstant.DIFFICULTY_LEVEL);
				BigDecimal answerValue = examGenericValue.getBigDecimal(CommonConstant.ANSWER_VALUE);

				Map<String, Object> exam = UtilMisc.toMap(CommonConstant.EXAM_ID, examId, CommonConstant.EXAM_NAME,
						examName, CommonConstant.DESCRIPTION, description, CommonConstant.CREATION_DATE, creationDate,
						CommonConstant.EXPIRATION_DATE, expirationDate, CommonConstant.NO_OF_QUESTIONS, noOfQuestions,
						CommonConstant.DURATION_MINUTES, durationMinutes, CommonConstant.PASS_PERCENTAGE,
						passPercentage, CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized,
						CommonConstant.ANSWER_MUST, answersMust, CommonConstant.ENABLE_NEGATIVE_MARK,
						enableNegativeMark, CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue,
						// Topic
						CommonConstant.TOPIC_ID, topicId, CommonConstant.TOPIC_NAME, topicName,
						// ETMM
						CommonConstant.PERCENTAGE, percentage, CommonConstant.TOPIC_PASS_PERCENTAGE,
						topicPassPercentage, CommonConstant.QUESTION_PER_EXAM, questionPerExam,
						// QM
						CommonConstant.QUESTION_ID, questionId, CommonConstant.QUESTION_DETAIL, questionDetail,
						CommonConstant.OPTION_A, optionA, CommonConstant.OPTION_B, optionB, CommonConstant.OPTION_C,
						optionC, CommonConstant.OPTION_D, optionD, CommonConstant.OPTION_E, optionE,
						CommonConstant.ANSWER, answer, CommonConstant.NUM_ANSWERS, numAnswers,
						CommonConstant.QUESTION_TYPE, questionType, CommonConstant.DIFFICULTY_LEVEL, difficultyLevel,
						CommonConstant.ANSWER_VALUE, answerValue);

				examList.add(exam);

			}

		}

		result.put("examList", examList);

		return result;
	}

	// Author:Benin
	public static Map<String, Object> findNoOfQuestionCountByExamID(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		String examId = (String) context.get(CommonConstant.EXAM_ID);
		try {

			GenericValue genericQuestionCount = EntityQuery.use(delegator).select(CommonConstant.NO_OF_QUESTIONS)
					.from(CommonConstant.EXAM_MASTER).where(CommonConstant.EXAM_ID, examId).queryOne();

			Long noOfQuestions = genericQuestionCount.getLong(CommonConstant.NO_OF_QUESTIONS);
			resultMap.put(CommonConstant.NO_OF_QUESTIONS, noOfQuestions);

		} catch (GenericEntityException e) {
			Debug.log("Error finding Number of Questions----" + e);
			resultMap = ServiceUtil.returnError("Error finding Number of Questions----" + module);
			return resultMap;

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
			String topicId =(String) topic.get(CommonConstant.TOPIC_ID);
			try {
				List<GenericValue> questions = EntityQuery.use(delegator)
						.select(CommonConstant.QUESTION_ID)
						.from(CommonConstant.QUESTION_MASTER)
						.where(CommonConstant.TOPIC_ID, topicId)
						.queryList();

				if (UtilValidate.isNotEmpty(questions)) {
					for (GenericValue question : questions) {
						String questionId = question.getString(CommonConstant.QUESTION_ID);
						questionIdList.add(questionId);
					}			
					topic.put("questionIdList", questionIdList);
				}
			} 
			catch (GenericEntityException e) {
				return ServiceUtil
						.returnError("Error in fetching record from Question Master entity ........" + module);
			}
		}
		resultMap.put("topicList", topicList);

		return resultMap;
	}

}
