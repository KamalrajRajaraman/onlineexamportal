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
/**
 * 
 * @author Benin JM
 *
 */
public class ExamMasterServices {
	public static final String module = ExamMasterServices.class.getName();
	/**
	 * This method is used to retrieve all the exams from the Exam Master entity 
	 * @param DispatchContext 
	 * @param Map<String, ? extends Object>
	 * @return	Map<String, ? extends Object>
	 */
	public static Map<String, Object> findAllExams(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		List<Map<String, Object>> examList = new LinkedList<>();

		List<GenericValue> examMasterGenericValueList = null;

		try {
			//Query to fetch all the exams from ExamMaster entity
			examMasterGenericValueList = EntityQuery.use(delegator)
					.from(CommonConstants.EXAM_MASTER).queryList();

		} catch (GenericEntityException e) {
			
			//In case of exception, the following codes get executed
			String errMsg = "Error in fetching All record from ExamMaster entity"+e.getMessage();
			Debug.logError(e,errMsg ,module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		if(UtilValidate.isEmpty(examMasterGenericValueList)) {
			//Retrieved exam list from ExamMaster entity is null or empty
			String errMsg = "Error in fetching All record from ExamMaster entity";
			Debug.logError(errMsg ,module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		
		//Checks if the list of exams returned by the query is not empty
		if (UtilValidate.isNotEmpty(examMasterGenericValueList)) {
			
			for (GenericValue examGenericValue : examMasterGenericValueList) {
				//Extract all fields from generic value object
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
				
				//Maps the exam details  
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
		
		//Adds the exam list to the result map
		result.put("examList", examList);
		return result;

	}
	/**
	 * This method accepts an exam Id and provides the details of the exam
	 * @param DispatchContext
	 * @param Map<String, ? extends Object>
	 * @return Map<String, ? extends Object>
	 */
	public static Map<String, Object> findExamById(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String examIdPK = (String) context.get(CommonConstants.EXAM_ID);
		GenericValue examGenericValue = null;

		try {
			
			//Query to fetch an exam from ExamMaster entity for the provided examId
			examGenericValue = EntityQuery.use(delegator).from(CommonConstants.EXAM_MASTER)
					.where(CommonConstants.EXAM_ID, examIdPK).queryOne();

			}

		 catch (GenericEntityException e) {
			
			//In case of exception, the following codes get executed
			String errMsg = "Error in fetching exam record by examId from ExamMaster entity"+e.getMessage();
			Debug.logError(e,errMsg ,module);
			return ServiceUtil.returnError(errMsg + module);
		}

		if(UtilValidate.isEmpty(examGenericValue)) {
			 	String errMsg = "Retrieved exam list from ExamMaster entity is null or empty";
				Debug.logError(errMsg ,module);
				return ServiceUtil.returnError(errMsg + module);
		}
		
		//Checks if the entity has the exam for the provided examId
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

			//Maps the exam details
			Map<String, Object> exam = UtilMisc.toMap(CommonConstants.EXAM_ID, examId, CommonConstants.EXAM_NAME,
					examName, CommonConstants.DESCRIPTION, description, CommonConstants.CREATION_DATE, creationDate,
					CommonConstants.EXPIRATION_DATE, expirationDate, CommonConstants.NO_OF_QUESTIONS, noOfQuestions,
					CommonConstants.DURATION_MINUTES, durationMinutes, CommonConstants.PASS_PERCENTAGE,
					passPercentage, CommonConstants.QUESTIONS_RANDOMIZED, questionsRandomized,
					CommonConstants.ANSWER_MUST, answersMust, CommonConstants.ENABLE_NEGATIVE_MARK,
					enableNegativeMark, CommonConstants.NEGATIVE_MARK_VALUE, negativeMarkValue);
			
			//Adds the exam to the result map
			result.put("exam", exam);
		}
		return result;
	}

	/**
	 * This method accepts and exam Id and provides the number of questions assigned to that exam	
	 * @param DispatchContext
	 * @param Map<String, Object>
	 * @return
	 * 		Map<String, Object> 
	 */
	public static Map<String, Object> findNoOfQuestionCountByExamId(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();

		String examId = (String) context.get(CommonConstants.EXAM_ID);
		GenericValue genericQuestionCount= null;
		
		try {
			//Query to find the number of questions for the exam id provided
			genericQuestionCount = EntityQuery.use(delegator).select(CommonConstants.NO_OF_QUESTIONS)
					.from(CommonConstants.EXAM_MASTER).where(CommonConstants.EXAM_ID, examId).queryOne();

		} catch (GenericEntityException e) {
			String errMsg = "Exception in fetching NoOfQuestion By ExamID from ExamMaster entity";
			Debug.logError(e,errMsg ,module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		//Checks if number of questions in available for the exam 
		if(UtilValidate.isNotEmpty(genericQuestionCount)) {
			Long noOfQuestions  = genericQuestionCount.getLong(CommonConstants.NO_OF_QUESTIONS);
			resultMap.put(CommonConstants.NO_OF_QUESTIONS, noOfQuestions);
		}
		else {
			String errMsg = "while fetching Number of questions from ExamMaster entity  is empty";
			Debug.logError(errMsg, module);
			return ServiceUtil.returnError("while fetching Number of questions from ExamMaster entity  is empty"+module);
			
		}
			
		//Adds the exam list to the result map
		
		return resultMap;

	}
	
	/**
	 * This method accepts a list of topic ID and provides the question IDs 
	 * assigned to each of them 
	 * @param DispatchContext
	 * @param Map<String, Object>
	 * @return
	 * 		Map<String, Object>
	 */
	public static Map<String, Object> findQuestionsByTopicIds(DispatchContext dctx,
			Map<String, ? extends Object> context) {
		
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		
		List<Map<String,Object>> topicList = (List<Map<String,Object>>) context.get("topicList");		
		List<GenericValue> questionIdListGV=null;			
		
		for (Map<String,Object> topic : topicList) {
			List<String> questionIdList = new LinkedList<>();
			String topicId =(String) topic.get(CommonConstants.TOPIC_ID);
			try {
				
				//Query to fetch list of question IDs assigned to each
				questionIdListGV = EntityQuery.use(delegator)
						.select(CommonConstants.QUESTION_ID)
						.from(CommonConstants.QUESTION_MASTER)
						.where(CommonConstants.TOPIC_ID, topicId)
						.queryList();

			} 
			catch (GenericEntityException e) {
				
				//In case of exception, the following codes get executed
				String errMsg = "Error in fetching record from Question Master entity";
				Debug.logError(e, errMsg ,module);
				return ServiceUtil.returnError(errMsg + module);
			}
			
			if(UtilValidate.isEmpty(questionIdListGV)) {
				//If Retrieved questionId list is empty or null
				String errMsg = "Error in fetching record from Question Master entity";
				Debug.logError(errMsg ,module);
				return ServiceUtil.returnError(errMsg + module);
			}
			
			//Checks if the returned list is empty
			if (UtilValidate.isNotEmpty(questionIdListGV)) {
				
				//The question IDs are added to a list
				for (GenericValue question : questionIdListGV) {
					String questionId = question.getString(CommonConstants.QUESTION_ID);
					questionIdList.add(questionId);
				}			
			}
			
			//The questionIdList is returned to its mapped topic
			topic.put("questionIdList", questionIdList);
		}
		
		//The list of topics is returned to the event
		resultMap.put("topicList", topicList);

		return resultMap;
	}

}
