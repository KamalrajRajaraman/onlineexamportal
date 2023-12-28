package com.vastpro.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ofbiz.base.util.Debug;
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

public class OnlineExamEvents {

	public static final String module = OnlineExamEvents.class.getName();
	public static String resource_error = "OnlineexamUiLabels";

	// used to login 
	public static String login(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String partyId = null;
		String result = LoginWorker.login(request, response);

		if (CommonConstant.SUCCESS.equals(result)) {
			// check userLoginId in request
			String userLoginId = (String) request.getAttribute(CommonConstant.USERNAME);
			if (userLoginId == null) {
				userLoginId = request.getParameter(CommonConstant.USERNAME);
			}
			
			// Gets partyId from PartyAndUserLogin View Entity using userLoginId
			GenericValue partyIdGv = null;
			try {
				
				partyIdGv = EntityQuery.use(delegator).select(CommonConstant.PARTY_ID).from(CommonConstant.PARTY_AND_USER_LOGIN)
						.where(CommonConstant.USER_LOGIN_ID, userLoginId).queryOne();
			} 
			catch (GenericEntityException e) {
				Debug.logError(e, "Unable to retrieve partyId  from PartyAndUserLogin using userLoginId", module);			
				String errMsg = "Unable to retrieve partyId  from PartyAndUserLogin using userLoginId : " + e.getMessage();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				request.setAttribute("result", CommonConstant.ERROR);
				return CommonConstant.ERROR;
			}

			if (UtilValidate.isNotEmpty(partyIdGv)) {

				partyId = partyIdGv.getString(CommonConstant.PARTY_ID);

				EntityCondition roleTypeIdEqualsPersonRole = EntityCondition.makeCondition(CommonConstant.ROLE_TYPE_ID, "PERSON_ROLE");
				EntityCondition roleTypeIdEqualsAdmin = EntityCondition.makeCondition(CommonConstant.ROLE_TYPE_ID, "ADMIN");
				EntityCondition containsPartyId = EntityCondition.makeCondition(CommonConstant.PARTY_ID, partyId);
				EntityCondition containsRoleType = EntityCondition.makeCondition(roleTypeIdEqualsPersonRole, EntityOperator.OR, roleTypeIdEqualsAdmin);
				EntityCondition andCondition = EntityCondition.makeCondition(containsPartyId, EntityOperator.AND,
						containsRoleType);
				
				//RoleTypeId is retrieved from UserMaster ViewEntity
				GenericValue roleTypeGV = null;
				try {
					roleTypeGV = EntityQuery
								.use(delegator)
								.select(CommonConstant.ROLE_TYPE_ID)
								.from("UserMaster")
								.where(andCondition)
								.queryOne();
				} catch (GenericEntityException e) {
					Debug.logError(e, "Unable to retrieve RoleTypeId  from UserMaster ", module);	
					String errMsg = "Unable to retrieve RoleTypeId  from UserMaster: " + e.getMessage();
					request.setAttribute("_ERROR_MESSAGE_", errMsg);
					request.setAttribute("result", CommonConstant.ERROR);
					return CommonConstant.ERROR;
				}
				
				//If RoleTypeId is PersonRole ,partyId is stored in session
				if (UtilValidate.isNotEmpty(roleTypeGV)) {
					String partyRole = roleTypeGV.getString(CommonConstant.ROLE_TYPE_ID);
					request.setAttribute("partyRole", partyRole);
					if (partyRole.equals("PERSON_ROLE")) {
						HttpSession session = request.getSession();
						session.setAttribute(CommonConstant.PARTY_ID, partyId);
					}
				}
			}

		}
		return CommonConstant.SUCCESS;

	}

	
	//On user regiseration party,Person,UserLogin and PartyRole Records are created
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
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            request.setAttribute("result", CommonConstant.ERROR);
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
					request.setAttribute("result", "success");
					request.setAttribute("PartyRoleRecordMap", createRoleResult);
				}
			} catch (GenericServiceException e) {
				Debug.logError(e, "Failed to execute createPartyRoleRecord service", module);
				String errMsg = "Failed to execute createPartyRoleRecord service : " + e.getMessage();
	            request.setAttribute("_ERROR_MESSAGE_", errMsg);
	            request.setAttribute("result", CommonConstant.ERROR);
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
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            request.setAttribute("result", CommonConstant.ERROR);
            return CommonConstant.ERROR;
		}
		if (ServiceUtil.isSuccess(serviceResult)) {
			request.setAttribute("result", serviceResult.get(CommonConstant.RESPONSE_MESSAGE));
			request.setAttribute("userList", serviceResult.get("userList"));
		}

		return CommonConstant.SUCCESS;
	}

	
	
	public static String logout(HttpServletRequest request, HttpServletResponse response) {
		
		String result = LoginWorker.logout(request, response);
		if (!result.equals(CommonConstant.SUCCESS)) {
			String errMsg = "Unable to logout";
			request.setAttribute("ERROR_MESSAGE", errMsg);
			request.setAttribute("result", CommonConstant.ERROR);
			return CommonConstant.ERROR;
		}
		request.setAttribute("result", result);
		return CommonConstant.SUCCESS;
	}
}
