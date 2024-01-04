package com.vastpro.events;

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

import com.vastpro.constants.CommonConstant;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.Loggable;
import com.vastpro.validator.LoginValidator;

//OnlineExamEvents contain common events related to Online Exam Application
public class OnlineExamEvents {

	public static final String module = OnlineExamEvents.class.getName();
	

	// used to login 
	public static String login(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(CommonConstant.DELEGATOR);
		
		
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Locale locale = UtilHttp.getLocale(request);
		LoginValidator loginForm = HibernateHelper.populateBeanFromMap(combinedMap, LoginValidator.class);
		Set<ConstraintViolation<LoginValidator>> checkValidationErrors = HibernateHelper.checkValidationErrors(loginForm, Loggable.class);
		
		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, checkValidationErrors, request, locale,"MandatoryFieldErrMsgLoginForm", CommonConstant.RESOURCE_ERROR, false);
		request.setAttribute("hasFormErrors", hasFormErrors);
		
		if(hasFormErrors) {
			String errMsg = "Invalid Input" + module;
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;	
		}
		
		String partyId = null;
		String result = LoginWorker.login(request, response);
		
		//If logged in checking role type
		if (CommonConstant.SUCCESS.equals(result)) {
			
			//After logged in userLogin Generic Value available in session
			GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstant.USER_LOGIN);
			
			//Getting partyId available in userLogin GenericValue
			if(UtilValidate.isNotEmpty(userLogin)) {
				partyId = userLogin.getString(CommonConstant.PARTY_ID);
			}
			
			//if partyId is available retrieving roleTypeId List form UserMaster View Entity and partyRole is set in request Attribute
			if (UtilValidate.isNotEmpty(partyId)) { 
				
				//if partyId is available retrieving roleTypeId List form UserMaster View Entity 
				 List<GenericValue> roleTypeList = null;
				try {
					 roleTypeList = EntityQuery
								.use(delegator)
								.select(CommonConstant.ROLE_TYPE_ID)
								.from("UserMaster")
								.where(CommonConstant.PARTY_ID,partyId)
								.queryList();
				}
				catch (GenericEntityException e) {
					
					Debug.logError(e, "Unable to retrieve RoleTypeId  from UserMaster ", module);	
					String errMsg = "Unable to retrieve RoleTypeId  from UserMaster: " + e.getMessage();
					request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
					return CommonConstant.ERROR;
					
				}
				
				//Checking roleTypeId of Logged in Party is PERSON_ROLE or ADMIN
				String partyRoleTypeId =null;
				String personRole =null;
				String admin =null;				
				if (UtilValidate.isNotEmpty(roleTypeList)) {					
					for(GenericValue roleType:roleTypeList) {
						partyRoleTypeId = roleType.getString(CommonConstant.ROLE_TYPE_ID);
						if(CommonConstant.PERSON_ROLE.equals(partyRoleTypeId)) {
							personRole = partyRoleTypeId;
						}
						else if(CommonConstant.ADMIN.equals(partyRoleTypeId)) {
							admin = partyRoleTypeId;
						}	
					}
				}else {
					String errMsg = "retrieved RoleTypeId List  from UserMaster Entity is null or empty in " + module;
					request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
					request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
					return CommonConstant.ERROR;
				}
				
				//Storing partyRoleType 
				if(UtilValidate.isNotEmpty(personRole)&&UtilValidate.isNotEmpty(admin)) {
					request.setAttribute(CommonConstant.PARTY_ROLE_TYPE_ID, "both");
				}
				else if(UtilValidate.isNotEmpty(personRole)) {
					request.setAttribute(CommonConstant.PARTY_ROLE_TYPE_ID, personRole);
				}
				else if(UtilValidate.isNotEmpty(admin)) {
					request.setAttribute(CommonConstant.PARTY_ROLE_TYPE_ID, admin);
				}else {
					request.setAttribute(CommonConstant.PARTY_ROLE_TYPE_ID, "others");
				}
			}

		}
			return CommonConstant.SUCCESS;
		
	}

	
	//On user registering  party,Person,UserLogin and PartyRole Records are created
	public static String registerPersonAndUserLogin(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(CommonConstant.USER_LOGIN);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(CommonConstant.DISPATCHER);

		String firstName = (String) request.getAttribute(CommonConstant.FIRST_NAME);
		String lastName = (String) request.getAttribute(CommonConstant.LAST_NAME);
		String userLoginId = (String) request.getAttribute(CommonConstant.USER_LOGIN_ID);
		String currentPassword = (String) request.getAttribute(CommonConstant.CURRENT_PASSWORD);
		String currentPasswordVerify = (String) request.getAttribute(CommonConstant.CURRENT_PASSWORD_VERIFY);

		Map<String, Object> registerCtx = new HashMap<>();
		registerCtx.put(CommonConstant.FIRST_NAME, firstName);
		registerCtx.put(CommonConstant.LAST_NAME, lastName);
		registerCtx.put(CommonConstant.USER_LOGIN_ID, userLoginId);
		registerCtx.put(CommonConstant.CURRENT_PASSWORD, currentPassword);
		registerCtx.put(CommonConstant.CURRENT_PASSWORD_VERIFY, currentPasswordVerify);
		registerCtx.put(CommonConstant.USER_LOGIN, userLogin);
		
		
		//createPersonAndUserLogin service is executed
		Map<String, Object> createPersonAndUserLoginResp = null;
		try {
			createPersonAndUserLoginResp = dispatcher.runSync("createPersonAndUserLogin", registerCtx);
			Debug.logInfo("Successfully execute createPersonAndUserLogin service", module);
		} catch (GenericServiceException e) {			
			Debug.logError(e, "Failed to execute createPersonAndUserLogin service", module);
			String errMsg = "Failed to execute createPersonAndUserLogin service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
            return CommonConstant.ERROR;
		}
		/*if error occurred while executing createPersonAndUserLogin service ,
		result and errMsg is set in request and error is returned*/
		if(ServiceUtil.isError(createPersonAndUserLoginResp)) {
			String errMsg = "Error occurred while executing createPersonAndUserLogin service ";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
            return CommonConstant.ERROR;
			
		}
		
		/*if executing createPersonAndUserLogin service is success,
		 *then createPartyRole services is executed*/
		if (ServiceUtil.isSuccess(createPersonAndUserLoginResp)) {
			
			request.setAttribute("PersonAndUserLoginMap", createPersonAndUserLoginResp);
			
			//partyId Of created user is available in createPersonAndUserLoginResp Map
			String partyId = (String) createPersonAndUserLoginResp.get(CommonConstant.PARTY_ID);
			Map<String, Object> createRoleCtx = new HashMap<>();
			createRoleCtx.put(CommonConstant.USER_LOGIN, userLogin);
			createRoleCtx.put(CommonConstant.PARTY_ID, partyId);
			//ROLE_TYPE_ID is constantly set as PERSON_ROLE
			createRoleCtx.put(CommonConstant.ROLE_TYPE_ID, CommonConstant.PERSON_ROLE);
			
			/*createPartyRole services is executed 
			 * NOTE:In PartyRole Entity,partyId and roleTypeID is composite key
			 * */
			Map<String, Object> createPartyRoleRecordResp = null;
			try {
				createPartyRoleRecordResp = dispatcher.runSync("createPartyRoleRecord", createRoleCtx);
				Debug.logInfo("Successfully execute createPartyRoleRecord service", module);
				
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute createPartyRoleRecord service", module);
				String errMsg = "Failed to execute createPartyRoleRecord service : " + e.getMessage();
				request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
	            return CommonConstant.ERROR;
			}
			
			
			if (ServiceUtil.isSuccess(createPartyRoleRecordResp)) {
				request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
				request.setAttribute("PartyRoleRecordMap", createPartyRoleRecordResp);
			}
			else {
				String errMsg = "Error occured while executing createPartyRoleRecord service";
				Debug.logError(errMsg, module);
				request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
				request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
	            return CommonConstant.ERROR;
			}

		}

		return CommonConstant.SUCCESS;

	}

	public static String findAllUsers(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		Map<String, Object> findAllUserContext = new HashMap<>();
		findAllUserContext.put(CommonConstant.USER_LOGIN, userLogin);
		//findAllUser service is executed
		Map<String, Object> serviceResult = null;
		try {

			serviceResult = dispatcher.runSync("findAllUser", findAllUserContext);
			Debug.logInfo("Successfully execute findAllUser service", module);

		} catch (GenericServiceException e) {
			Debug.logError(e, "Failed to execute findAllUser service", module);
			String errMsg = "Failed to execute findAllUser service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
            return CommonConstant.ERROR;
		}
		if (ServiceUtil.isSuccess(serviceResult)) {
			request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
			request.setAttribute("userList", serviceResult.get("userList"));
		}
		else {
			String errMsg = "Error occured while executing findAllUser service";
			Debug.logError(errMsg, module);
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
            return CommonConstant.ERROR;		
		}

		return CommonConstant.SUCCESS;
	}

	
	
	public static String logout(HttpServletRequest request, HttpServletResponse response) {
		
		String result = LoginWorker.logout(request, response);
		if (!result.equals(CommonConstant.SUCCESS)) {
			String errMsg = "Unable to logout";
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		request.setAttribute(CommonConstant.RESULT, result);
		return CommonConstant.SUCCESS;
	}
}
