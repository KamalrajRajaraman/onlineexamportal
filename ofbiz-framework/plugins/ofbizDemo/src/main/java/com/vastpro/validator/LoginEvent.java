//package com.vastpro.validator;
////package com.vastpro.constants;
////
////import java.util.Locale;
////import java.util.Map;
////import java.util.Set;
////
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
////import javax.validation.ConstraintViolation;
////
////import org.apache.ofbiz.base.util.Debug;
////import org.apache.ofbiz.base.util.UtilHttp;
////import org.apache.ofbiz.base.util.UtilMisc;
////import org.apache.ofbiz.entity.Delegator;
////import org.apache.ofbiz.webapp.control.LoginWorker;
////
////import com.vastpro.validator.HibernateHelper;
////
////
////public class LoginEvent {
////
////	public static final String module = LoginEvent.class.getName();
////	public static String resource_error = "OnlineexamUiLabels";
////
////	public static String doLogin(HttpServletRequest request, HttpServletResponse response) {
////		
////		
////		Delegator delegator = (Delegator) request.getAttribute("delegator");
////		Locale locale = UtilHttp.getLocale(request);
////		
////		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
////		String username = (String) combinedMap.get("USERNAME");
////		String password = (String) combinedMap.get("PASSWORD");
////	
////		Map<String, Object> userLoginMap = UtilMisc.toMap("username", username, "password", password);
////		//validate the input by define validator
//// 		HibernateValidator loginForm = HibernateHelper.populateBeanFromMap(userLoginMap, HibernateValidator.class);
//// 		
//// 		//sereparates error
////		Set<ConstraintViolation<HibernateValidator>> errors = HibernateHelper.checkValidationErrors(loginForm,
////				LoginFormCheck.class);
////		
////		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale, "MandatoryFieldErrMsgLoginForm",
////				resource_error, false);
////		request.setAttribute("hasFormErrors", hasFormErrors);
////		
////		String result = LoginWorker.login(request, response);
////		Debug.logInfo("=======================LOGIN EVENT END SUCCESSFULLY======", module);
////
////		return result;
////
////	}
////}
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.ofbiz.base.util.UtilMisc;
//import org.apache.ofbiz.base.util.UtilValidate;
//import org.apache.ofbiz.entity.Delegator;
//import org.apache.ofbiz.entity.GenericEntityException;
//import org.apache.ofbiz.entity.GenericValue;
//import org.apache.ofbiz.entity.util.EntityQuery;
//import org.apache.ofbiz.service.DispatchContext;
//import org.apache.ofbiz.service.ServiceUtil;
//
//import com.vastpro.constants.CommonConstant;
//
//public static Map<String, Object> getExamDetails(DispatchContext dctx, Map<String, ? extends Object> context) {
//
//	Map<String, Object> result = ServiceUtil.returnSuccess();
//	Delegator delegator = dctx.getDelegator();
//	String examIdPK = (String) context.get(CommonConstant.EXAM_ID);
//	List<Map<String, Object>> examList = new LinkedList<>();
//
//	List<GenericValue> examMasterGenericValueList = null;
//
//	try {
//		examMasterGenericValueList = EntityQuery.use(delegator).from("ExamDetails")
//				.where(CommonConstant.EXAM_ID, examIdPK).queryList();
//	} catch (GenericEntityException e) {
//		e.printStackTrace();
//		return ServiceUtil.returnError("Error in fetching record from ExamMaster entity ........" + module);
//	}
//
//	if (UtilValidate.isNotEmpty(examMasterGenericValueList)) {
//
//		for (GenericValue examGenericValue : examMasterGenericValueList) {
//
//			String examId = examGenericValue.getString(CommonConstant.EXAM_ID);
//			String examName = examGenericValue.getString(CommonConstant.EXAM_NAME);
//			String description = examGenericValue.getString(CommonConstant.DESCRIPTION);
//			Timestamp creationDate = examGenericValue.getTimestamp(CommonConstant.CREATION_DATE);
//			Timestamp expirationDate = examGenericValue.getTimestamp(CommonConstant.EXPIRATION_DATE);
//			Long noOfQuestions = examGenericValue.getLong(CommonConstant.NO_OF_QUESTIONS);
//			Long durationMinutes = examGenericValue.getLong(CommonConstant.DURATION_MINUTES);
//			BigDecimal passPercentage = examGenericValue.getBigDecimal(CommonConstant.PASS_PERCENTAGE);
//			String questionsRandomized = examGenericValue.getString(CommonConstant.QUESTIONS_RANDOMIZED);
//			String answersMust = examGenericValue.getString(CommonConstant.ANSWER_MUST);
//			String enableNegativeMark = examGenericValue.getString(CommonConstant.ENABLE_NEGATIVE_MARK);
//			BigDecimal negativeMarkValue = examGenericValue.getBigDecimal(CommonConstant.NEGATIVE_MARK_VALUE);
//
//			String topicId = examGenericValue.getString(CommonConstant.TOPIC_ID);
//			String topicName = examGenericValue.getString(CommonConstant.TOPIC_NAME);
//
//			String percentage = examGenericValue.getString(CommonConstant.PERCENTAGE);
//			String topicPassPercentage = examGenericValue.getString(CommonConstant.TOPIC_PASS_PERCENTAGE);
//			String questionPerExam = examGenericValue.getString(CommonConstant.QUESTION_PER_EXAM);
//
//			Long questionId = examGenericValue.getLong(CommonConstant.QUESTION_ID);
//			String questionDetail = examGenericValue.getString(CommonConstant.QUESTION_DETAIL);
//			String optionA = examGenericValue.getString(CommonConstant.OPTION_A);
//			String optionB = examGenericValue.getString(CommonConstant.OPTION_B);
//			String optionC = examGenericValue.getString(CommonConstant.OPTION_C);
//			String optionD = examGenericValue.getString(CommonConstant.OPTION_D);
//			String optionE = examGenericValue.getString(CommonConstant.OPTION_E);
//			String answer = examGenericValue.getString(CommonConstant.ANSWER);
//			Long numAnswers = examGenericValue.getLong(CommonConstant.NUM_ANSWERS);
//			String questionType = examGenericValue.getString(CommonConstant.QUESTION_TYPE);
//			Integer difficultyLevel = examGenericValue.getInteger(CommonConstant.DIFFICULTY_LEVEL);
//			BigDecimal answerValue = examGenericValue.getBigDecimal(CommonConstant.ANSWER_VALUE);
//
//			Map<String, Object> exam = UtilMisc.toMap(CommonConstant.EXAM_ID, examId, CommonConstant.EXAM_NAME,
//					examName, CommonConstant.DESCRIPTION, description, CommonConstant.CREATION_DATE, creationDate,
//					CommonConstant.EXPIRATION_DATE, expirationDate, CommonConstant.NO_OF_QUESTIONS, noOfQuestions,
//					CommonConstant.DURATION_MINUTES, durationMinutes, CommonConstant.PASS_PERCENTAGE,
//					passPercentage, CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized,
//					CommonConstant.ANSWER_MUST, answersMust, CommonConstant.ENABLE_NEGATIVE_MARK,
//					enableNegativeMark, CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue,
//					// Topic
//					CommonConstant.TOPIC_ID, topicId, CommonConstant.TOPIC_NAME, topicName,
//					// ETMM
//					CommonConstant.PERCENTAGE, percentage, CommonConstant.TOPIC_PASS_PERCENTAGE,
//					topicPassPercentage, CommonConstant.QUESTION_PER_EXAM, questionPerExam,
//					// QM
//					CommonConstant.QUESTION_ID, questionId, CommonConstant.QUESTION_DETAIL, questionDetail,
//					CommonConstant.OPTION_A, optionA, CommonConstant.OPTION_B, optionB, CommonConstant.OPTION_C,
//					optionC, CommonConstant.OPTION_D, optionD, CommonConstant.OPTION_E, optionE,
//					CommonConstant.ANSWER, answer, CommonConstant.NUM_ANSWERS, numAnswers,
//					CommonConstant.QUESTION_TYPE, questionType, CommonConstant.DIFFICULTY_LEVEL, difficultyLevel,
//					CommonConstant.ANSWER_VALUE, answerValue);
//
//			examList.add(exam);
//
//		}
//
//	}
//
//	result.put("examList", examList);
//
//	return result;
//}