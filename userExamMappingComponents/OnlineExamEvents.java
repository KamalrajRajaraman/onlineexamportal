package com.vastpro.onlineexam.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.control.LoginWorker;

import com.vastpro.constants.CommonConstant;

public class OnlineExamEvents {
	public static final String module = OnlineExamEvents.class.getName();

	/**
	 * create Exam method is used to insert new record into ExamMaster Entity
	 * through createExam Service def
	 */
	public static String createExam(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> serviceResultMap = null;
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		// Getting Add Exam Form data which is sent as Json Object from React
		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		String examName = (String) request.getAttribute(CommonConstant.EXAM_NAME);
		String description = (String) request.getAttribute(CommonConstant.DESCRIPTION);
		String creationDate = (String) request.getAttribute(CommonConstant.CREATION_DATE);
		String expirationDate = (String) request.getAttribute(CommonConstant.EXPIRATION_DATE);
		String noOfQuestions = (String) request.getAttribute(CommonConstant.NO_OF_QUESTIONS);
		String durationMinutes = (String) request.getAttribute(CommonConstant.DURATION_MINUTES);
		String passPercentage = (String) request.getAttribute(CommonConstant.PASS_PERCENTAGE);
		String questionsRandomized = (String) request.getAttribute(CommonConstant.QUESTIONS_RANDOMIZED);
		String answersMust = (String) request.getAttribute(CommonConstant.ANSWER_MUST);
		String enableNegativeMark = (String) request.getAttribute(CommonConstant.ENABLE_NEGATIVE_MARK);
		String negativeMarkValue = (String) request.getAttribute(CommonConstant.NEGATIVE_MARK_VALUE);

		Map<String, Object> addExamContext = UtilMisc.toMap(CommonConstant.EXAM_ID, examId, CommonConstant.EXAM_NAME,
				examName, CommonConstant.DESCRIPTION, description, CommonConstant.CREATION_DATE, creationDate,
				CommonConstant.EXPIRATION_DATE, expirationDate, CommonConstant.NO_OF_QUESTIONS, noOfQuestions,
				CommonConstant.DURATION_MINUTES, durationMinutes, CommonConstant.PASS_PERCENTAGE, passPercentage,
				CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized, CommonConstant.ANSWER_MUST, answersMust,
				CommonConstant.ENABLE_NEGATIVE_MARK, enableNegativeMark, CommonConstant.NEGATIVE_MARK_VALUE,
				negativeMarkValue, CommonConstant.USER_LOGIN, userLogin);
		try {
			// createExam service is called
			serviceResultMap = dispatcher.runSync("createExam", addExamContext);
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute("examList", serviceResultMap);
			}

			Debug.logInfo("=======Creating ExamMAster record in event using service createExam=========", module);
		} catch (GenericServiceException e) {

			String errMsg = "Unable to create new records in ExamMaster entity: " + e.toString();
			request.setAttribute(CommonConstant.ERROR, errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;

	}

	public static String deleteExam(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String examId = (String) request.getAttribute("examId");
		GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");
		Map<String, Object> resultMap = UtilMisc.toMap(CommonConstant.EXAM_ID, examId, CommonConstant.USER_LOGIN,
				userLogin);

		try {
			Map<String, Object> deleteExamMap = dispatcher.runSync("deleteExam", resultMap);
			request.setAttribute("deleteList", deleteExamMap);
			if (ServiceUtil.isSuccess(deleteExamMap)) {
				request.setAttribute("deleteList", deleteExamMap);
			}
			Debug.logInfo("=======deleteing ExamMAster record in event using service deleteExam=========", module);

		} catch (GenericServiceException e) {
			String errorMsg = "Unable to delete record from Exam Master";
			request.setAttribute(CommonConstant.ERROR, errorMsg);
			request.setAttribute("result", "error");
			e.printStackTrace();
		}

		return CommonConstant.SUCCESS;
	}

	public static String findAllExam(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> examList = null;

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);

		try {
			examList = dispatcher.runSync("findAllExams", findExamContext);
			if (ServiceUtil.isSuccess(examList)) {
				request.setAttribute("examList", examList.get("examList"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrieve  records  from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

	}

	public static String findExamById(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> examResult = null;

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String examId = request.getParameter(CommonConstant.EXAM_ID);

		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstant.EXAM_ID, examId);

		try {
			examResult = dispatcher.runSync("findExamById", findExamContext);
			if (ServiceUtil.isSuccess(examResult)) {
				request.setAttribute("exam", examResult.get("exam"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrieve  records  from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

	}

	public static String createQuestion(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		// Retrieving AddQuestion Form data from react which is sent as json object
		String questionId = (String) request.getAttribute(CommonConstant.QUESTION_ID);
		String questionDetail = (String) request.getAttribute(CommonConstant.QUESTION_DETAIL);
		String optionA = (String) request.getAttribute(CommonConstant.OPTION_A);
		String optionB = (String) request.getAttribute(CommonConstant.OPTION_B);
		String optionC = (String) request.getAttribute(CommonConstant.OPTION_C);
		String optionD = (String) request.getAttribute(CommonConstant.OPTION_D);
		String optionE = (String) request.getAttribute(CommonConstant.OPTION_E);
		String answer = (String) request.getAttribute(CommonConstant.ANSWER);
		String numAnswers = (String) request.getAttribute(CommonConstant.NUM_ANSWERS);
		String questionType = (String) request.getAttribute(CommonConstant.QUESTION_TYPE);
		String difficultyLevel = (String) request.getAttribute(CommonConstant.DIFFICULTY_LEVEL);
		String answerValue = (String) request.getAttribute(CommonConstant.ANSWER_VALUE);
		String topicId = (String) request.getAttribute(CommonConstant.TOPIC_ID);
		String negativeMarkValue = (String) request.getAttribute(CommonConstant.NEGATIVE_MARK_VALUE);

		Map<String, Object> addQuestionContext = new HashMap<>();
		addQuestionContext.put(CommonConstant.QUESTION_ID, questionId);
		addQuestionContext.put(CommonConstant.QUESTION_DETAIL, questionDetail);
		addQuestionContext.put(CommonConstant.OPTION_A, optionA);
		addQuestionContext.put(CommonConstant.OPTION_B, optionB);
		addQuestionContext.put(CommonConstant.OPTION_C, optionC);
		addQuestionContext.put(CommonConstant.OPTION_D, optionD);
		addQuestionContext.put(CommonConstant.OPTION_E, optionE);
		addQuestionContext.put(CommonConstant.ANSWER, answer);
		addQuestionContext.put(CommonConstant.NUM_ANSWERS, numAnswers);
		addQuestionContext.put(CommonConstant.QUESTION_TYPE, questionType);
		addQuestionContext.put(CommonConstant.DIFFICULTY_LEVEL, difficultyLevel);
		addQuestionContext.put(CommonConstant.ANSWER_VALUE, answerValue);
		addQuestionContext.put(CommonConstant.TOPIC_ID, topicId);
		addQuestionContext.put(CommonConstant.NEGATIVE_MARK_VALUE, negativeMarkValue);
		addQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> resultMap = null;
		try {
			resultMap = dispatcher.runSync("createQuestion", addQuestionContext);
			if (ServiceUtil.isSuccess(resultMap)) {
				request.setAttribute("result", resultMap);
			}

			Debug.logInfo("=======Created QuestionMaster record in this event using service createQuestion=========",
					module);

		} catch (GenericServiceException e) {

			String errMsg = "Unable to create new records in QuestionMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}

	public static String deleteQuestion(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Integer questionId = (Integer) request.getAttribute("questionId");
		GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");
		Map<String, Object> resultMap = UtilMisc.toMap(CommonConstant.QUESTION_ID, questionId,
				CommonConstant.USER_LOGIN, userLogin);

		try {
			Map<String, Object> deleteQuestionMap = dispatcher.runSync("deleteQuestion", resultMap);
			request.setAttribute("deleteList", deleteQuestionMap);
			if (ServiceUtil.isSuccess(deleteQuestionMap)) {
				request.setAttribute("deleteList", deleteQuestionMap);
			}
			Debug.logInfo("=======deleting QuestionMaster record in event using service QuetionMaster=========",
					module);

		} catch (GenericServiceException e) {
			String errorMsg = "Unable to delete record from Question Master";
			request.setAttribute(CommonConstant.ERROR, errorMsg);
			request.setAttribute("result", "error");
			e.printStackTrace();
		}

		return CommonConstant.SUCCESS;
	}

	public static String findAllQuestions(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		Map<String, Object> findAllQuestionContext = new HashMap<>();
		findAllQuestionContext.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> questionList = null;
		try {

			questionList = dispatcher.runSync("findAllQuestions", findAllQuestionContext);

		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrive records from QuestionMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		if (ServiceUtil.isSuccess(questionList)) {

			request.setAttribute("questionList", questionList);
		}

		return CommonConstant.SUCCESS;
	}

	public static String doLogin(HttpServletRequest request, HttpServletResponse response) {

		String result = LoginWorker.login(request, response);
		return result;
	}

//Topics

	public static String createTopic(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		// Retrieving Topic Form Data from React which is sent as Json Object
		String topicId = (String) request.getAttribute(CommonConstant.TOPIC_ID);
		String topicName = (String) request.getAttribute(CommonConstant.TOPIC_NAME);

		Map<String, Object> addTopicContext = new HashMap<>();
		addTopicContext.put(CommonConstant.TOPIC_ID, topicId);
		addTopicContext.put(CommonConstant.TOPIC_NAME, topicName);
		addTopicContext.put(CommonConstant.USER_LOGIN, userLogin);

		try {
			Map<String, Object> serviceResult = dispatcher.runSync("createTopic", addTopicContext);
			if (ServiceUtil.isSuccess(serviceResult)) {
				request.setAttribute("result", serviceResult);
			}
			Debug.logInfo("=======Created TopicMaster record in this event using service createTopic=========", module);
		}

		catch (GenericServiceException e) {
			String errMsg = "Unable to create new records in TopicMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}

	public static String deleteTopic(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String topicId = (String) request.getAttribute("topicId");
		GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");
		Map<String, Object> resultMap = UtilMisc.toMap(CommonConstant.TOPIC_ID, topicId, CommonConstant.USER_LOGIN,
				userLogin);

		try {
			Map<String, Object> deleteTopicMap = dispatcher.runSync("deleteTopic", resultMap);
			request.setAttribute("deleteList", deleteTopicMap);
			if (ServiceUtil.isSuccess(deleteTopicMap)) {
				request.setAttribute("deleteList", deleteTopicMap);
			}
			Debug.logInfo("=======deleting TopicMaster record in event using service TopicMaster=========", module);

		} catch (GenericServiceException e) {
			String errorMsg = "Unable to delete record from Topic Master";
			request.setAttribute(CommonConstant.ERROR, errorMsg);
			request.setAttribute("result", "error");
			e.printStackTrace();
		}

		return CommonConstant.SUCCESS;
	}

	public static String findAllTopics(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("findAllTopics event called");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		Map<String, Object> findAllTopicContext = new HashMap<>();
		findAllTopicContext.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> topicList = null;
		try {

			topicList = dispatcher.runSync("findAllTopics", findAllTopicContext);

		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrive records from TopicMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		System.out.println("topicLists::::::" + topicList);
		request.setAttribute("topicList", topicList.get("topicList"));
		System.out.println("Request:::::" + request);

		if (ServiceUtil.isSuccess(topicList)) {

			// request.setAttribute("topicList", topicList.get("topicList"));
			System.out.println("Request:::::" + request);
		}

		return CommonConstant.SUCCESS;
	}

//Mapping Events

	public static String addExamToUser(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");
		Map<String, Object> serviceResultMap = null;
		
		String partyId = (String) request.getAttribute(CommonConstant.PARTY_ID);
		String examId = (String) request.getAttribute(CommonConstant.EXAM_ID);
		String allowedAttempts = (String) request.getAttribute(CommonConstant.ALLOWED_ATTEMPTS);
		String noOfAttempts = (String) request.getAttribute(CommonConstant.NO_OF_ATTEMPTS);
		String lastPerformanceDate = (String) request.getAttribute(CommonConstant.LAST_PERFORMANCE_DATE);
		String timeoutDays = (String) request.getAttribute(CommonConstant.TIMEOUT_DAYS);
		String passwordChangesAuto = (String) request.getAttribute(CommonConstant.PASSWORD_CHANGES_AUTO);
		String canSplitExams = (String) request.getAttribute(CommonConstant.CAN_SPLIT_EXAMS);
		String canSeeDetailedResults = (String) request.getAttribute(CommonConstant.CAN_SEE_DETAILED_RESULTS);
		String maxSplitAttempts = (String) request.getAttribute(CommonConstant.MAX_SPLIT_ATTEMPTS);

		Map<String, Object> formListMap = UtilMisc.toMap(CommonConstant.PARTY_ID, partyId ,CommonConstant.EXAM_ID, examId,
				CommonConstant.ALLOWED_ATTEMPTS, allowedAttempts, CommonConstant.NO_OF_ATTEMPTS, noOfAttempts,
				CommonConstant.LAST_PERFORMANCE_DATE, lastPerformanceDate, CommonConstant.TIMEOUT_DAYS, timeoutDays,
				CommonConstant.PASSWORD_CHANGES_AUTO, passwordChangesAuto, CommonConstant.CAN_SPLIT_EXAMS,
				canSplitExams, CommonConstant.CAN_SEE_DETAILED_RESULTS, canSeeDetailedResults,
				CommonConstant.MAX_SPLIT_ATTEMPTS, maxSplitAttempts, CommonConstant.USER_LOGIN, userLogin);

		try {
			serviceResultMap = dispatcher.runSync("addExamToUser", formListMap);
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute("examList", serviceResultMap);
			}

			Debug.logInfo("=======Creating UserExamMappingMaster record in event using service addExamToUser=========",
					module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to create new records in ExamMaster entity: " + e.toString();
			request.setAttribute(CommonConstant.ERROR, errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;
	}

//Register Person and userlogin
	public static String registerPersonAndUserLogin(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		String firstName = (String) request.getAttribute("firstName");
		String lastName = (String) request.getAttribute("lastName");
		String userLoginId = (String) request.getAttribute("userLoginId");
		String currentPassword = (String) request.getAttribute("currentPassword");
		String currentPasswordVerify = (String) request.getAttribute("currentPasswordVerify");

		Map<String, Object> registerCtx = new HashMap<>();
		registerCtx.put("firstName", firstName);
		registerCtx.put("lastName", lastName);
		registerCtx.put("userLoginId", userLoginId);
		registerCtx.put("currentPassword", currentPassword);
		registerCtx.put("currentPasswordVerify", currentPasswordVerify);
		registerCtx.put("userLogin", userLogin);
		Map<String, Object> serviceResultMap = null;

		try {
			serviceResultMap = dispatcher.runSync("createPersonAndUserLogin", registerCtx);

		} catch (GenericServiceException e) {

			String errMsg = "Unable to create records in Party and User Login entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}

		if (ServiceUtil.isSuccess(serviceResultMap)) {
			request.setAttribute("result", serviceResultMap);

			String partyId = (String) serviceResultMap.get("partyId");
			Map<String, Object> createRoleCtx = new HashMap<>();
			createRoleCtx.put("userLogin", userLogin);
			createRoleCtx.put("partyId", partyId);
			createRoleCtx.put("roleTypeId", "PERSON_ROLE");

			try {
				Map<String, Object> createRoleResult = dispatcher.runSync("createPartyRoleRecord", createRoleCtx);
				if (ServiceUtil.isSuccess(createRoleResult)) {
					request.setAttribute("result1", createRoleResult);
				}
			} catch (GenericServiceException e) {
				String errMsg = "Unable to create records in PartyRole entity: " + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				request.setAttribute("result1", "error");
				return CommonConstant.ERROR;
			}

		}

		return CommonConstant.SUCCESS;

	}

//ShowAll Users
	
	public static String findAllUsers(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("findAllUsers event called");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		Map<String, Object> findAllUserContext = new HashMap<>();
		findAllUserContext.put(CommonConstant.USER_LOGIN, userLogin);

		Map<String, Object> userList = null;
		try {

			userList = dispatcher.runSync("findAllUser", findAllUserContext);

		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrive records from TopicMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		System.out.println("userLists::::::" + userList);
		request.setAttribute("userList", userList.get("userList"));
		System.out.println("Request:::::" + request);

		if (ServiceUtil.isSuccess(userList)) {

			// request.setAttribute("topicList", topicList.get("topicList"));
			System.out.println("Request:::::" + request);
		}

		return CommonConstant.SUCCESS;
	}
	
	
	public static String showExamsForPartyId(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> examResult = null;

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String partyId = (String) request.getAttribute(CommonConstant.PARTY_ID);

		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstant.PARTY_ID, partyId);

		try {
			examResult = dispatcher.runSync("showExamsForPartyId", findExamContext);
			if (ServiceUtil.isSuccess(examResult)) {
				request.setAttribute("examList", examResult.get("examList"));
			}
			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
		} catch (GenericServiceException e) {
			String errMsg = "Unable to retrieve  records  from UserExamMappingMasterview  entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		return CommonConstant.SUCCESS;

		
				
	}
	
	
	
	
	
	
	
	
	
	

	// public static String createExam(HttpServletRequest request,
	// HttpServletResponse response) {
//
//		System.out.println("=========================React Callled ==================");
//		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
//		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//		String examId =(String) request.getAttribute(CommonConstant.EXAM_ID);
//		String examName =(String) request.getAttribute(CommonConstant.EXAM_NAME);
//		String description =(String) request.getAttribute(CommonConstant.DESCRIPTION);
//		String creationDate =(String) request.getAttribute(CommonConstant.CREATION_DATE);
//		String expirationDate =(String) request.getAttribute(CommonConstant.EXPIRATION_DATE);
//		String noOfQuestions =(String) request.getAttribute(CommonConstant.NO_OF_QUESTIONS);
//		String durationMinutes =(String) request.getAttribute(CommonConstant.DURATION_MINUTES);
//		String passPercentage =(String) request.getAttribute(CommonConstant.PASS_PERCENTAGE);
//		String questionsRandomized =(String) request.getAttribute(CommonConstant.QUESTIONS_RANDOMIZED);
//		String answersMust =(String) request.getAttribute(CommonConstant.ANSWER_MUST);
//		String enableNegativeMark =(String) request.getAttribute(CommonConstant.ENABLE_NEGATIVE_MARK);
//		String negativeMarkValue =(String) request.getAttribute(CommonConstant.NEGATIVE_MARK_VALUE);
//		Map<String, Object> context = UtilMisc.toMap(
//				CommonConstant.EXAM_ID, examId, 
//				CommonConstant.EXAM_NAME, examName,
//				CommonConstant.DESCRIPTION, description,
//				CommonConstant.CREATION_DATE, creationDate,
//				CommonConstant.EXPIRATION_DATE, expirationDate,
//				CommonConstant.NO_OF_QUESTIONS, noOfQuestions,
//				CommonConstant.DURATION_MINUTES, durationMinutes,
//				CommonConstant.PASS_PERCENTAGE, passPercentage,
//				CommonConstant.QUESTIONS_RANDOMIZED, questionsRandomized,
//				CommonConstant.ANSWER_MUST, answersMust,
//				CommonConstant.ENABLE_NEGATIVE_MARK, enableNegativeMark,
//				CommonConstant.NEGATIVE_MARK_VALUE,negativeMarkValue, 
//				CommonConstant.USER_LOGIN, userLogin);
//		System.out.println("create exam event called");
//		try {
//			Debug.logInfo("=======Creating ExamMAster record in event using service createExam=========", module);
//			dispatcher.runSync("createExam", context);
//		} catch (GenericServiceException e) {
//			String errMsg = "Unable to create new records in ExamMaster entity: " + e.toString();
//			request.setAttribute("ERROR_MESSAGE", errMsg);
//			return CommonConstant.ERROR;
//		}
//
//		return CommonConstant.SUCCESS;
//
//	}
//	

//	
//	
//	public static String findAllExam(HttpServletRequest request, HttpServletResponse response) {
//		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
//		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//		Map<String, Object> findExamMap = new HashMap<>();
//		findExamMap.put(CommonConstant.USER_LOGIN, userLogin);
//		Map<String, Object> resultMap=null;
//		try {
//			resultMap=dispatcher.runSync("findExams", findExamMap);
//			
//			System.out.println("Event ResultMap : "+resultMap);
//			if(ServiceUtil.isSuccess(resultMap)) {
//				request.setAttribute("result", resultMap);
//			}
//			Debug.logInfo("=======Retriving  ExamMAster record in this event using service findExams=========", module);
//		} catch (GenericServiceException e) {
//			String errMsg = "Unable to retrieve  records  from ExamMaster entity: " + e.toString();
//			request.setAttribute("ERROR_MESSAGE", errMsg);
//			return CommonConstant.ERROR;
//		}
//		return CommonConstant.SUCCESS;
//		
//	}
}