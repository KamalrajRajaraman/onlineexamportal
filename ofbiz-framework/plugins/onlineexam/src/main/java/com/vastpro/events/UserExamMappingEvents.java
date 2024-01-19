package com.vastpro.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;
import com.vastpro.validator.CreateUserExamMappingCheck;
import com.vastpro.validator.FindUserExamMappingCheck;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.UserExamMappingValidator;

//Event class for UserExamMappingMaster operation
public class UserExamMappingEvents {
	public static final String module = UserExamMappingEvents.class.getName();

	// Method for create a record in UserExamMappingMaster entity
	public static String createUserExamMappingRecord(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Locale locale = request.getLocale();
		Map<String, Object> createOrUpdateUserExamMappingRecordResp = null;
		GenericValue findPartyIdExamIdResp= null;

		// Map to get and store the objects from request
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);

		// Validate the fields in the combinedMap for null values
		UserExamMappingValidator validateUserExamMapping = HibernateHelper.populateBeanFromMap(combinedMap,
				UserExamMappingValidator.class);
		Set<ConstraintViolation<UserExamMappingValidator>> validationErrors = HibernateHelper
				.checkValidationErrors(validateUserExamMapping, CreateUserExamMappingCheck.class);
		Boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, validationErrors, request, locale,
				"InvalidErrMsg", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute("hasFormErrors", hasFormErrors);

		if (hasFormErrors) {
			// Set error message in request in case of empty fields
			String errMsg = "Values are not assigned to every fields!";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError("Values are not assigned to every fields! ", module);
			return CommonConstants.ERROR;

		}
		String partyId = (String) combinedMap.get(CommonConstants.PARTY_ID);
		String examId= (String) combinedMap.get(CommonConstants.EXAM_ID);
		try {
			findPartyIdExamIdResp = EntityQuery.use(delegator).from(CommonConstants.USER_EXAM_MAPPING_MASTER).where(CommonConstants.PARTY_ID,partyId,CommonConstants.EXAM_ID,examId).queryOne();
		} catch (GenericEntityException e1) {
			String errMsg = "Error finding Party Id and Exam Id";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError(errMsg, module);
			return CommonConstants.ERROR;
		}
		

		if (UtilValidate.isEmpty(findPartyIdExamIdResp)) {
			try {
				// calling createUserExamMappingRecord service
				createOrUpdateUserExamMappingRecordResp = dispatcher.runSync("createUserExamMappingRecord", combinedMap);

			} catch (GenericServiceException e) {

				// If any exception occur in service, set error as a result in request object
				Debug.logError(e, "Failed to execute createUserExamMappingRecord service", module);
				String errMsg = "Failed to execute createUserExamMappingRecord service : " + e.getMessage();
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				return CommonConstants.ERROR;
			}
		}
		
		else {
			try {
				// calling createUserExamMappingRecord service
				createOrUpdateUserExamMappingRecordResp = dispatcher.runSync("updateUserExamMappingRecord", combinedMap);

			} catch (GenericServiceException e) {

				// If any exception occur in service, set error as a result in request object
				String errMsg = "Failed to execute updateUserExamMappingRecord service : " + e.getMessage();
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				Debug.logError(e, errMsg, module);
				return CommonConstants.ERROR;
			}
		}
		
		// checking if the createUserExamMappingRecord service is success or not
		if (ServiceUtil.isSuccess(createOrUpdateUserExamMappingRecordResp)) {
			request.setAttribute("createUserExamMappingRecordMap", createOrUpdateUserExamMappingRecordResp);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			Debug.logInfo("CreateUserExamMappingRecord service has been executed successfully! ", module);

		} else {

			// If the createUserExamMappingRecord service returns Error, set result as error
			// in request
			String errMsg = "Error occured in createUserExamMappingRecord service";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError(errMsg, module);
			return CommonConstants.ERROR;
		}

		return CommonConstants.SUCCESS;
	}

	// Event for showing exams for the particular user based on partyId
	public static String findAllExamForPartyId(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Locale locale = request.getLocale();

		// checking partyId comes as a parameter
		String partyId = request.getParameter(CommonConstants.PARTY_ID);

		// checking partyId comes as a attribute
		if (UtilValidate.isEmpty(partyId)) {
			partyId = (String) request.getAttribute(CommonConstants.PARTY_ID);
		}

		// If the partyId didn't came as both parameter and attribute then take it from
		// the userLogin
		if (UtilValidate.isEmpty(partyId)) {
			partyId = userLogin.getString(CommonConstants.PARTY_ID);
		}

		// create a map with partyId, userLogin object
		Map<String, Object> findExamContext = new HashMap<>();
		findExamContext.put(CommonConstants.USER_LOGIN, userLogin);
		findExamContext.put(CommonConstants.PARTY_ID, partyId);

		UserExamMappingValidator userExamMappingValidator = HibernateHelper.populateBeanFromMap(findExamContext,
				UserExamMappingValidator.class);
		Set<ConstraintViolation<UserExamMappingValidator>> validationErrors = HibernateHelper
				.checkValidationErrors(userExamMappingValidator, FindUserExamMappingCheck.class);
		Boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, validationErrors, request, locale,
				"InvalidErrMsg", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute("hasFormErrors", hasFormErrors);

		if (hasFormErrors) {
			// Set error message in request in case of empty fields
			String errMsg = "Values are not assigned to every fields! ";
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			Debug.logError(errMsg, module);
			return CommonConstants.ERROR;
		}

		// calling showExamsForPartyId service for showing exams
		Map<String, Object> findAllExamForPartyIdResp = null;
		try {
			findAllExamForPartyIdResp = dispatcher.runSync("findAllExamForPartyId", findExamContext);
			Debug.logInfo("showExamsForPartyId service has been executed successfully!", module);

		} catch (GenericServiceException e) {
			// If any exception occur in service, set error as a result in request object
			String errMsg = "Failed to execute findAllExamForPartyId service";
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, e.getMessage());
			return CommonConstants.ERROR;
		}

		if (ServiceUtil.isSuccess(findAllExamForPartyIdResp)) {
			request.setAttribute("examList", findAllExamForPartyIdResp.get("examList"));
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, "showExamsForPartyId service executed successfully");
		} else {
			request.setAttribute("createUserExamMappingRecordMap", null);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, "Error while executing showExamsForPartyId service");
			return CommonConstants.ERROR;
		}
		return CommonConstants.SUCCESS;

	}

	


}