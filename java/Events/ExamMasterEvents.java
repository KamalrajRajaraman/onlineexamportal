package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class ExamMasterEvents {
	public static final String module = ExamMasterEvents.class.getName();

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
				request.setAttribute("result", serviceResultMap);
			}

			Debug.logInfo("=======Creating ExamMAster record in event using service createExam=========", module);
		} catch (GenericServiceException e) {

			String errMsg = "Unable to create new records in ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;

	}

	public static String findAllExams(HttpServletRequest request, HttpServletResponse response) {
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

	public static String deleteExam(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String examId = request.getParameter(CommonConstant.EXAM_ID);

		Map<String, Object> deleteExamContext = new HashMap<>();
		deleteExamContext.put(CommonConstant.USER_LOGIN, userLogin);
		deleteExamContext.put(CommonConstant.EXAM_ID, examId);
		Map<String, Object> serviceResultMap = null;
		try {
			serviceResultMap = dispatcher.runSync("deleteExam", deleteExamContext);
			if (ServiceUtil.isSuccess(serviceResultMap)) {
				request.setAttribute("result", serviceResultMap);
			}
		} catch (GenericServiceException e) {
			String errMsg = "Unable to delete  record  from ExamMaster entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}

		return CommonConstant.SUCCESS;
	}

	

}
