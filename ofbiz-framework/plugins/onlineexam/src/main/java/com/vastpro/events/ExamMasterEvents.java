package com.vastpro.events;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;
import com.vastpro.onlineexam.helper.OnlineExamHelper;
import com.vastpro.validator.ExamMasterCheck;
import com.vastpro.validator.ExamMasterValidator;
import com.vastpro.validator.HibernateHelper;

public class ExamMasterEvents {
	
	public static final String module = ExamMasterEvents.class.getName();
	
	public static String createExam(HttpServletRequest request, HttpServletResponse response) {	
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Map<String,Object> combinedMap=UtilHttp.getCombinedMap(request);	
		Locale locale= request.getLocale();
		
		//Hibernate Validation for creating exam
		ExamMasterValidator createExamForm =HibernateHelper.populateBeanFromMap(combinedMap, ExamMasterValidator.class);	
		Set<ConstraintViolation<ExamMasterValidator>> createExamvalidationErrors =HibernateHelper.checkValidationErrors(createExamForm, ExamMasterCheck.class);
		Boolean hasformErrors= HibernateHelper.validateFormSubmission(delegator, createExamvalidationErrors , request, locale, "InvalidErrMsg", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute(CommonConstants.HAS_FORM_ERROR, hasformErrors);
		if(hasformErrors){
			String errMsg= "Errors in  createExam Form Validation";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError(errMsg, module);
			return CommonConstants.ERROR;		
		}
		
		//If Has No Hiberate Validation Error createExam Service is called 
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		
		
		String examName = (String) combinedMap.get(CommonConstants.EXAM_NAME);
		String description = (String) combinedMap.get(CommonConstants.DESCRIPTION);
		String creationDate = (String) combinedMap.get(CommonConstants.CREATION_DATE);
		String expirationDate = (String) combinedMap.get(CommonConstants.EXPIRATION_DATE);
		String noOfQuestions = (String) combinedMap.get(CommonConstants.NO_OF_QUESTIONS);
		String durationMinutes = (String) combinedMap.get(CommonConstants.DURATION_MINUTES);
		String passPercentage = (String) combinedMap.get(CommonConstants.PASS_PERCENTAGE);
		String questionsRandomized = (String) combinedMap.get(CommonConstants.QUESTIONS_RANDOMIZED);
		String answersMust = (String) combinedMap.get(CommonConstants.ANSWER_MUST);
		String enableNegativeMark = (String) combinedMap.get(CommonConstants.ENABLE_NEGATIVE_MARK);
		String negativeMarkValue = (String) combinedMap.get(CommonConstants.NEGATIVE_MARK_VALUE);
	
		//creating map to pass required context to the service called 
		Map<String, Object> addExamContext = UtilMisc.toMap(
				CommonConstants.EXAM_NAME,examName,
				CommonConstants.DESCRIPTION, description,
				CommonConstants.CREATION_DATE, creationDate,
				CommonConstants.EXPIRATION_DATE, expirationDate,
				CommonConstants.NO_OF_QUESTIONS, noOfQuestions,
				CommonConstants.DURATION_MINUTES, durationMinutes,
				CommonConstants.PASS_PERCENTAGE, passPercentage,
				CommonConstants.QUESTIONS_RANDOMIZED, questionsRandomized,
				CommonConstants.ANSWER_MUST, answersMust,
				CommonConstants.ENABLE_NEGATIVE_MARK, enableNegativeMark,
				CommonConstants.NEGATIVE_MARK_VALUE,negativeMarkValue,
				CommonConstants.USER_LOGIN, userLogin);
		
		// creating ExamMaster Record by calling createExam service 
		Map<String, Object> createExamResp = null;
		try {
			createExamResp = dispatcher.runSync(CommonConstants.CREATE_EXAM, addExamContext);	
			Debug.logInfo("Successfully executed createExam Service", module);
		} catch (GenericServiceException e) {
			String errMsg = "Failed to execute createExam service : " + e.getMessage();
			 Debug.logError(e, errMsg, module);
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;
		}
		
		if (ServiceUtil.isSuccess(createExamResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);		
			//constructing exam as mapObject
			Map<String, Object> exam = new HashMap<>();			
			exam.put(CommonConstants.EXAM_ID,createExamResp.get(CommonConstants.EXAM_ID) );
			exam.put(CommonConstants.EXAM_NAME,createExamResp.get(CommonConstants.EXAM_NAME) );
			exam.put(CommonConstants.DESCRIPTION,createExamResp.get(CommonConstants.DESCRIPTION) );
			exam.put(CommonConstants.CREATION_DATE,createExamResp.get(CommonConstants.CREATION_DATE) );
			exam.put(CommonConstants.EXPIRATION_DATE,createExamResp.get(CommonConstants.EXPIRATION_DATE) );
			exam.put(CommonConstants.NO_OF_QUESTIONS,createExamResp.get(CommonConstants.NO_OF_QUESTIONS) );
			exam.put(CommonConstants.DURATION_MINUTES,createExamResp.get(CommonConstants.DURATION_MINUTES) );
			exam.put(CommonConstants.PASS_PERCENTAGE,createExamResp.get(CommonConstants.PASS_PERCENTAGE) );
			exam.put(CommonConstants.QUESTIONS_RANDOMIZED,createExamResp.get(CommonConstants.QUESTIONS_RANDOMIZED) );
			exam.put(CommonConstants.ANSWER_MUST,createExamResp.get(CommonConstants.ANSWER_MUST) );
			exam.put(CommonConstants.ENABLE_NEGATIVE_MARK,createExamResp.get(CommonConstants.ENABLE_NEGATIVE_MARK) );
			exam.put(CommonConstants.NEGATIVE_MARK_VALUE,createExamResp.get(CommonConstants.NEGATIVE_MARK_VALUE) );		
			request.setAttribute(CommonConstants.EXAM, exam);
			
		}
		else {
			 String errMsg = "Error while executing createExam service";
			 Debug.logError(errMsg, module);	
			 request.setAttribute(CommonConstants.RESULT_MAP,createExamResp);
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;		
		}
		return CommonConstants.SUCCESS;

	}
	
	//Method to retrieve all the exams from ExamMaster entity
	public static String findAllExams(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		//Creation of a map to send the context to the service 
		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstants.USER_LOGIN, userLogin);

		//Calling the service which in return provides the list of exams
		Map<String, Object> findAllExamsResp = null;
		try {
			findAllExamsResp = dispatcher.runSync(CommonConstants.FIND_ALL_EXAMS, findExamContext);	
			Debug.logInfo("Successfully executed findAllExams service", module);
		} catch (GenericServiceException e) {
			 Debug.logError(e, "Failed to execute findAllExams service", module);
			 String errMsg = "Failed to execute findAllExams service : " + e.getMessage();
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;
		}
		 
		String responseMessage = (String) findAllExamsResp.get(CommonConstants.RESPONSE_MESSAGE);
		if(responseMessage.equals(CommonConstants.RESPOND_EMPTY)) {
			String errMsg = (String) findAllExamsResp.get(CommonConstants.SUCCESS_MESSAGE);
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		//If the service returns success result and  examList is added to the request
		if (ServiceUtil.isSuccess(findAllExamsResp)) {	
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.EXAM_LIST, findAllExamsResp.get(CommonConstants.EXAM_LIST));
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = "Error while executing findAllExams service";
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,findAllExamsResp);
			return CommonConstants.ERROR;
		}
		return CommonConstants.SUCCESS;

	}

	//Method to find an exam by using examId from ExamMaster entity
	public static String findExamById(HttpServletRequest request, HttpServletResponse response) {	
		//Getting the examId  from the request
		String examId = request.getParameter(CommonConstants.EXAM_ID);
		
		//If examId is null or Empty ,error returned
		if(UtilValidate.isEmpty(examId)) {
			 String errMsg = "examId is Empty";
			 Debug.logError( errMsg, module);
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;		
		}
		
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		//Map is created to sent it as context to  the service called
		Map<String, Object> findExamByIdContext = new HashMap<>();
		findExamByIdContext.put(CommonConstants.USER_LOGIN, userLogin);
		findExamByIdContext.put(CommonConstants.EXAM_ID, examId);
		
		//Calling the findExamById  service which  return the exam detail for given the examId
		Map<String, Object> findExamByIdResp = OnlineExamHelper.findExamById(dispatcher, findExamByIdContext);

		//If the service returns success result and exam record is added to the request
		if (ServiceUtil.isSuccess(findExamByIdResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.EXAM, findExamByIdResp.get(CommonConstants.EXAM));
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = (String) findExamByIdResp.get(ModelService.ERROR_MESSAGE);
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg); 
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,findExamByIdResp);	
			return CommonConstants.ERROR;
		}
		
		return CommonConstants.SUCCESS;
	}

	//Method to delete an exam from ExamMaster entity
	/**
	 * Runs deleteExam Service ,which just update expirationDate.
	 * The ExamMaster Record is not deleted really instead it is hidden by filtering by date 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String deleteExam(HttpServletRequest request, HttpServletResponse response) {
		
		//Getting the examId  from the request	
		String examId = request.getParameter(CommonConstants.EXAM_ID);
		if(UtilValidate.isEmpty(examId)) {
			 String errMsg = "examId is Empty";
			 Debug.logError( errMsg, module);
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;		
		}
		
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Timestamp expirationDate = new Timestamp(System.currentTimeMillis());
		//Creation of a map to send the context to the service 
		Map<String, Object> deleteExamContext = new HashMap<>();
		deleteExamContext.put(CommonConstants.USER_LOGIN, userLogin);
		deleteExamContext.put(CommonConstants.EXAM_ID, examId);
		deleteExamContext.put(CommonConstants.EXPIRATION_DATE, expirationDate);
		
		Map<String, Object> deleteExamResp = null;
		
		//Calling the service which deletes the exam
		try {
			deleteExamResp = dispatcher.runSync(CommonConstants.DELETE_EXAM , deleteExamContext);
			Debug.logInfo("Successfully executed deleteExam service", module);
			
		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute deleteExam service", module);
			String errMsg = "Failed to execute deleteExam service : " + e.getMessage();
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;

		}
		//If the exam is deleted successfully, the response Map object  is set in request
		if (ServiceUtil.isSuccess(deleteExamResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.RESULT_MAP, deleteExamResp);
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = "Error while executing deleteExam service";
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg); 
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,deleteExamResp);
			return CommonConstants.ERROR;
		}

		return CommonConstants.SUCCESS;
	}
	
	public static String editExam(HttpServletRequest request, HttpServletResponse response) {
		
		LocalDispatcher dispatcher =(LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		
		Map<String, Object> editExamResp=null;
		
		if(UtilValidate.isEmpty(combinedMap.get(CommonConstants.EXAM_ID))) {
			 String errMsg = "examId is Empty";
			 Debug.logError( errMsg, module);
			 request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			 request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
             return CommonConstants.ERROR;
		}
		try {
			editExamResp = dispatcher.runSync(CommonConstants.EDIT_EXAM, combinedMap);
			Debug.logInfo("Successfully executed editExam service", module);
					
		} 
		catch (GenericServiceException e) {
			String errMsg = "Failed to execute editExam service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;
		}
		if (ServiceUtil.isSuccess(editExamResp)) {
			Debug.logInfo("Successfully executed editExam service", module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.RESULT_MAP, editExamResp);
		}
		else {
			//If the service returns error ,result and response Map Object is added to request Object
			String errMsg = "Error while executing editExam service";
			Debug.logError(errMsg, module);	
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg); 
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants.RESULT_MAP,editExamResp);
			return CommonConstants.ERROR;
		}
		
		return CommonConstants.SUCCESS;
		
	}
	
}








