package com.vastpro.events;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.control.LoginWorker;

import com.vastpro.constants.CommonConstant;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.Loggable;
import com.vastpro.validator.Validator;

public class OnlineExamEvents {
	
	public static final String module = OnlineExamEvents.class.getName();
	public static String resource_error = "OnlineexamUiLabels";
	
	// used to login ofbiz
	public static String login(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		Locale locale = UtilHttp.getLocale(request);
		
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		String username = (String) combinedMap.get("USERNAME");
		String password = (String) combinedMap.get("PASSWORD");
	
		Map<String, Object> userLoginMap = UtilMisc.toMap("username", username, "password", password);
	
 		Validator loginForm = HibernateHelper.populateBeanFromMap(userLoginMap, Validator.class);
 		
 
		Set<ConstraintViolation<Validator>> errors = HibernateHelper.checkValidationErrors(loginForm,
				Loggable.class);
		
		boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale, "MandatoryFieldErrMsgLoginForm",
				resource_error, false);
		request.setAttribute("hasFormErrors", hasFormErrors);
		
		if(!hasFormErrors) {
			return CommonConstant.ERROR;
		}
		
		String result = LoginWorker.login(request, response);
		System.out.println(result);
		return CommonConstant.SUCCESS;

	}
	
	public static String registerPersonAndUserLogin(HttpServletRequest request, HttpServletResponse response) {
		

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		
		
		String firstName = (String) request.getAttribute("firstName");
		String lastName = (String) request.getAttribute("lastName");
		String userLoginId = (String) request.getAttribute("userLoginId");
		String currentPassword = (String) request.getAttribute("currentPassword");
		String currentPasswordVerify = (String) request.getAttribute("currentPasswordVerify");
		
		Map<String,Object> registerCtx = new HashMap<>();
		registerCtx.put("firstName", firstName);
		registerCtx.put("lastName", lastName);
		registerCtx.put("userLoginId", userLoginId);
		registerCtx.put("currentPassword", currentPassword);
		registerCtx.put("currentPasswordVerify", currentPasswordVerify);
		registerCtx.put("userLogin", userLogin);
		Map<String, Object> serviceResultMap=null;
		
		try {
		serviceResultMap = dispatcher.runSync("createPersonAndUserLogin", registerCtx);
			
		} catch (GenericServiceException e) {
			
			String errMsg = "Unable to create records in Party and User Login entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		
		if(ServiceUtil.isSuccess(serviceResultMap)) {
			request.setAttribute("result", serviceResultMap);
			
			String partyId = (String) serviceResultMap.get("partyId");
			Map<String,Object> createRoleCtx = new HashMap<>();
			createRoleCtx.put("userLogin", userLogin);
			createRoleCtx.put("partyId", partyId);
			createRoleCtx.put("roleTypeId", "PERSON_ROLE");
			
			
			try {
				Map<String, Object> createRoleResult = dispatcher.runSync("createPartyRoleRecord", createRoleCtx);
				if(ServiceUtil.isSuccess(createRoleResult)) {
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
}