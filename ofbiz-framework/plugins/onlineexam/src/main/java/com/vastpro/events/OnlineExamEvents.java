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

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		

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
			Debug.logError(e, "Failed to execute createPersonAndUserLogin service", module);
			String errMsg = "Failed to execute createPersonAndUserLogin service : " + e.getMessage();
			request.setAttribute(CommonConstant._ERROR_MESSAGE_, errMsg);
			request.setAttribute(CommonConstant.RESULT, CommonConstant.ERROR);
            return CommonConstant.ERROR;
		}

		if (ServiceUtil.isSuccess(serviceResultMap)) {
			request.setAttribute("PersonAndUserLoginMap", serviceResultMap);

			String partyId = (String) serviceResultMap.get("partyId");
			Map<String, Object> createRoleCtx = new HashMap<>();
			createRoleCtx.put("userLogin", userLogin);
			createRoleCtx.put("partyId", partyId);
			createRoleCtx.put("roleTypeId", "PERSON_ROLE");

			try {
				Map<String, Object> createRoleResult = dispatcher.runSync("createPartyRoleRecord", createRoleCtx);
				if (ServiceUtil.isSuccess(createRoleResult)) {
					request.setAttribute(CommonConstant.RESULT, CommonConstant.SUCCESS);
					request.setAttribute("PartyRoleRecordMap", createRoleResult);
				}
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute createPartyRoleRecord service", module);
				String errMsg = "Failed to execute createPartyRoleRecord service : " + e.getMessage();
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

		Map<String, Object> serviceResult = null;
		try {

			serviceResult = dispatcher.runSync("findAllUser", findAllUserContext);

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
