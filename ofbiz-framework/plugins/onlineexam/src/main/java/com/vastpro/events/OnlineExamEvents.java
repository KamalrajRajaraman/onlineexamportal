package com.vastpro.events;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.control.LoginWorker;

import com.vastpro.constants.CommonConstants;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.Loggable;
import com.vastpro.validator.LoginValidator;
import com.vastpro.validator.RegisterCheck;
import com.vastpro.validator.RegisterValidator;

//OnlineExamEvents contain common events related to Online Exam Application
public class OnlineExamEvents {

	public static final String module = OnlineExamEvents.class.getName();
	

	// used to login 
	public static String login(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		
		
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Locale locale = UtilHttp.getLocale(request);
		LoginValidator loginForm = HibernateHelper.populateBeanFromMap(combinedMap, LoginValidator.class);
		Set<ConstraintViolation<LoginValidator>> checkValidationErrors = HibernateHelper.checkValidationErrors(loginForm, Loggable.class);
		
		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, checkValidationErrors, request, locale,"MandatoryFieldErrMsgLoginForm", CommonConstants.RESOURCE_ERROR, false);
		request.setAttribute(CommonConstants.HAS_FORM_ERROR, hasFormErrors);
		
		if(hasFormErrors) {
			String errMsg = "Invalid Input" + module;
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;	
		}
		
		String partyId = null;
		String result = LoginWorker.login(request, response);
		
		//If logged in checking role type
		if (CommonConstants.SUCCESS.equals(result)) {
			
			//After logged in userLogin Generic Value available in session
			GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
			
			//Getting partyId available in userLogin GenericValue
			if(UtilValidate.isNotEmpty(userLogin)) {
				partyId = userLogin.getString(CommonConstants.PARTY_ID);
			}
			
			//if partyId is available retrieving roleTypeId List form UserMaster View Entity and partyRole is set in request Attribute
			if (UtilValidate.isNotEmpty(partyId)) { 
				
				//if partyId is available retrieving roleTypeId List form UserMaster View Entity 
				 List<GenericValue> roleTypeList = null;
				try {
					 roleTypeList = EntityQuery
								.use(delegator)
								.select(CommonConstants.ROLE_TYPE_ID)
								.from(CommonConstants.USER_MASTER)
								.where(CommonConstants.PARTY_ID,partyId)
								.queryList();
				}
				catch (GenericEntityException e) {
					String errMsg = "Unable to retrieve RoleTypeId  from UserMaster: " + e.getMessage();
					Debug.logError(e, errMsg, module);	
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
					
				}
				
				//Checking roleTypeId of Logged in Party is PERSON_ROLE or ADMIN
				String partyRoleTypeId =null;
				String personRole =null;
				String admin =null;				
				if (UtilValidate.isNotEmpty(roleTypeList)) {					
					for(GenericValue roleType:roleTypeList) {
						partyRoleTypeId = roleType.getString(CommonConstants.ROLE_TYPE_ID);
						if(CommonConstants.PERSON_ROLE.equals(partyRoleTypeId)) {
							personRole = partyRoleTypeId;
						}
						else if(CommonConstants.ADMIN.equals(partyRoleTypeId)) {
							admin = partyRoleTypeId;
						}	
					}
				}else {
					String errMsg = "retrieved RoleTypeId List  from UserMaster Entity is null or empty in " + module;
					request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
					return CommonConstants.ERROR;
				}
				
				//Storing partyRoleType 
				if(UtilValidate.isNotEmpty(personRole)&&UtilValidate.isNotEmpty(admin)) {
					request.setAttribute(CommonConstants.PARTY_ROLE_TYPE_ID, "both");
				}
				else if(UtilValidate.isNotEmpty(personRole)) {
					request.setAttribute(CommonConstants.PARTY_ROLE_TYPE_ID, personRole);
				}
				else if(UtilValidate.isNotEmpty(admin)) {
					request.setAttribute(CommonConstants.PARTY_ROLE_TYPE_ID, admin);
				}else {
					request.setAttribute(CommonConstants.PARTY_ROLE_TYPE_ID, "others");
				}
			}

		}
			return CommonConstants.SUCCESS;
		
	}

	
	//On user registering  party,Person,UserLogin and PartyRole Records are created
	public static String registerPersonAndUserLogin(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Locale locale = request.getLocale();
	
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		
		//populates the Bean class with values from the combined map
		RegisterValidator registerValidator = HibernateHelper.populateBeanFromMap(combinedMap, RegisterValidator.class);
		Set<ConstraintViolation<RegisterValidator>> validationErrors = HibernateHelper.checkValidationErrors(registerValidator, RegisterCheck.class);
		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, validationErrors, request, locale, "MandatoryFieldErrMsgLoginForm", CommonConstants.RESOURCE_ERROR, false);
		
		if(hasFormErrors) {
			String errMsg = "Errors in  createExam Form Validation";
			Debug.logError (errMsg, module);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			return CommonConstants.ERROR;
		}
		
		//createPersonAndUserLogin service is executed
		Map<String, Object> createPersonAndUserLoginResp = null;
		try {
			createPersonAndUserLoginResp = dispatcher.runSync(CommonConstants.CREATE_PERSON_AND_USER_LOGIN, combinedMap);
			Debug.logInfo("Successfully executed createPersonAndUserLogin service", module);
		} catch (GenericServiceException e) {			
			String errMsg = "Failed to execute createPersonAndUserLogin service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;
		}
		/*if error occurred while executing createPersonAndUserLogin service ,
		result and errMsg is set in request and error is returned*/
		if(ServiceUtil.isError(createPersonAndUserLoginResp)) {
			String errMsg = "Error occurred while executing createPersonAndUserLogin service ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;
			
		}
		
		/*if executing createPersonAndUserLogin service is success,
		 *then createPartyRole services is executed*/
		if (ServiceUtil.isSuccess(createPersonAndUserLoginResp)) {
			
			request.setAttribute(CommonConstants.PERSON_AND_USER_LOGIN_MAP, createPersonAndUserLoginResp);
			
			//partyId Of created user is available in createPersonAndUserLoginResp Map
			String partyId = (String) createPersonAndUserLoginResp.get(CommonConstants.PARTY_ID);
			Map<String, Object> createRoleCtx = new HashMap<>();
			createRoleCtx.put(CommonConstants.USER_LOGIN, userLogin);
			createRoleCtx.put(CommonConstants.PARTY_ID, partyId);
			//ROLE_TYPE_ID is constantly set as PERSON_ROLE
			createRoleCtx.put(CommonConstants.ROLE_TYPE_ID, CommonConstants.PERSON_ROLE);
			
			/*createPartyRole services is executed 
			 * NOTE:In PartyRole Entity,partyId and roleTypeID is composite key
			 * */
			Map<String, Object> createPartyRoleRecordResp = null;
			try {
				createPartyRoleRecordResp = dispatcher.runSync(CommonConstants.CREATE_PARTY_ROLE_RECORD, createRoleCtx);
				Debug.logInfo("Successfully executed createPartyRoleRecord service", module);
				
			} catch (GenericServiceException e) {
				
				//These get executed in case of exception while running the service
				String errMsg = "Failed to execute createPartyRoleRecord service : " + e.getMessage();
				Debug.logError(e, errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
	            return CommonConstants.ERROR;
			}
			
			
			if (ServiceUtil.isSuccess(createPartyRoleRecordResp)) {
				//If the service returns success, success message is added to the request
				Debug.logInfo("Role for the user is set successfully!", module);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
				request.setAttribute(CommonConstants.PARTY_ROLE_RECORD_MAP, createPartyRoleRecordResp);
			}
			else {
				String errMsg = "Error occured while executing createPartyRoleRecord service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
	            return CommonConstants.ERROR;
			}

		}

		return CommonConstants.SUCCESS;

	}

	public static String findAllUsers(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstants.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);

		Map<String, Object> findAllUserContext = new HashMap<>();
		findAllUserContext.put(CommonConstants.USER_LOGIN, userLogin);
		//findAllUser service is executed
		Map<String, Object> serviceResult = null;
		try {

			serviceResult = dispatcher.runSync(CommonConstants.FIND_ALL_USER, findAllUserContext);
			Debug.logInfo("Successfully execute findAllUser service", module);

		} catch (GenericServiceException e) {
			
			String errMsg = "Failed to execute findAllUser service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;
		}
		if (ServiceUtil.isSuccess(serviceResult)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.USER_LIST, serviceResult.get(CommonConstants.USER_LIST));
		}
		else {
			String errMsg = "Error occured while executing findAllUser service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
            return CommonConstants.ERROR;		
		}

		return CommonConstants.SUCCESS;
	}

	// Event for deleting a question from QuestionMaster entity based on questionId
	public static String deleteUser(HttpServletRequest request, HttpServletResponse response) {
		
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(CommonConstants.DELEGATOR);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		String partyId = (String) combinedMap.get(CommonConstants.PARTY_ID);
		
		
		if(UtilValidate.isEmpty(partyId)) {
			String errMsg = "partyId is empty ";
			Debug.logError( errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		GenericValue userLoginId =null;
		try {
			userLoginId = EntityQuery.use(delegator).select(CommonConstants.USER_LOGIN_ID).from(CommonConstants.USER_LOGIN).where(CommonConstants.PARTY_ID,partyId).queryOne();
		} catch (GenericEntityException e) {
			String errMsg = "Failed to retrieve userLoginId from  UserMaster View Entity" + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		
		if(UtilValidate.isNotEmpty(userLoginId)) {
			combinedMap.put(CommonConstants.USER_LOGIN_ID,userLoginId.getString(CommonConstants.USER_LOGIN_ID) );
			
		}
		
		
		Timestamp DisabledDateTime = new Timestamp(System.currentTimeMillis());
		combinedMap.put(CommonConstants.DISABLED_DATE_TIME, DisabledDateTime);
		
		Map<String, Object> deleteUserLoginResp = null;

		// calling deleteUserLogin service
		try {
			deleteUserLoginResp = dispatcher.runSync(CommonConstants.DELETE_USER_LOGIN, combinedMap);
			Debug.logInfo("Succesfully executed deleteUserLogin service", module);
		} catch (GenericServiceException e) {
			// If Exception occurred while execute the service, set result as Error in request
			
			String errMsg = "Failed to execute deleteUserLogin service : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		// If the deleteUserLogin service is success, set result as success in request
		if (ServiceUtil.isSuccess(deleteUserLoginResp)) {
			request.setAttribute(CommonConstants.RESULT, CommonConstants.SUCCESS);
			request.setAttribute(CommonConstants.RESULT_MAP, deleteUserLoginResp);
		} else {
			// If the deleteUserLogin service returns Error, set result as error in request
			String errMsg = "Error occured in deleteUserLogin service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
		}
		return CommonConstants.SUCCESS;
	}
	
	public static String logout(HttpServletRequest request, HttpServletResponse response) {
		
		String result = LoginWorker.logout(request, response);
		if (!result.equals(CommonConstants.SUCCESS)) {
			String errMsg = "Unable to logout";
			request.setAttribute(CommonConstants._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstants.RESULT, CommonConstants.ERROR);
			return CommonConstants.ERROR;
		}
		request.setAttribute(CommonConstants.RESULT, result);
		return CommonConstants.SUCCESS;
	}
}
