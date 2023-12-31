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
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.control.LoginWorker;

import com.vastpro.constants.CommonConstant;
import com.vastpro.validator.HibernateHelper;
import com.vastpro.validator.Loggable;
import com.vastpro.validator.Validator;

import bsh.util.Util;

public class OnlineExamEvents {

	public static final String module = OnlineExamEvents.class.getName();
	public static String resource_error = "OnlineexamUiLabels";

	// used to login ofbiz
	public static String login(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String partyId = null;
		String result = LoginWorker.login(request, response);

		if (result == CommonConstant.SUCCESS) {
			// check userLoginId in request
			String userLoginId = (String) request.getAttribute("USERNAME");
			if (userLoginId == null) {
				userLoginId = request.getParameter("USERNAME");
			}
			GenericValue partyIdGv = null;
			try {
				// Gets partyId from PartyAndUserLogin View Entity using userLoginId
				partyIdGv = EntityQuery.use(delegator).select("partyId").from("PartyAndUserLogin")
						.where("userLoginId", userLoginId).queryOne();
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}

			if (UtilValidate.isNotEmpty(partyIdGv)) {

				partyId = partyIdGv.getString(CommonConstant.PARTY_ID);

				EntityCondition conditionA = EntityCondition.makeCondition(CommonConstant.ROLE_TYPE_ID, "PERSON_ROLE");
				EntityCondition conditionB = EntityCondition.makeCondition(CommonConstant.ROLE_TYPE_ID, "ADMIN");
				EntityCondition conditionC = EntityCondition.makeCondition(CommonConstant.PARTY_ID, partyId);
				EntityCondition orCondition = EntityCondition.makeCondition(conditionA, EntityOperator.OR, conditionB);
				EntityCondition andCondition = EntityCondition.makeCondition(conditionC, EntityOperator.AND,
						orCondition);

				GenericValue roleTypeGV = null;
				try {
					roleTypeGV = EntityQuery.use(delegator).select(CommonConstant.ROLE_TYPE_ID).from("UserMaster")
							.where(andCondition).queryOne();
				} catch (GenericEntityException e) {

					e.printStackTrace();
				}

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

	public static String checkUserRole(HttpServletRequest request) {

		Delegator delegator = (Delegator) request.getAttribute("delegator");

		// check userLoginId in request
		String userLoginId = (String) request.getAttribute("USERNAME");
		if (userLoginId == null) {
			userLoginId = request.getParameter("USERNAME");
		}

		try {
			// Gets partyId from PartyAndUserLogin View Entity using userLoginId
			GenericValue genericValue = EntityQuery.use(delegator).select("partyId").from("PartyAndUserLogin")
					.where("userLoginId", userLoginId).queryOne();

			EntityCondition conditionA = EntityCondition.makeCondition(CommonConstant.ROLE_TYPE_ID, "PERSON_ROLE");
			EntityCondition conditionB = EntityCondition.makeCondition(CommonConstant.ROLE_TYPE_ID, "ADMIN");
			EntityCondition conditionC = EntityCondition.makeCondition(CommonConstant.PARTY_ID,
					genericValue.getString(CommonConstant.PARTY_ID));

			EntityCondition orCondition = EntityCondition.makeCondition(conditionA, EntityOperator.OR, conditionB);

			EntityCondition andCondition = EntityCondition.makeCondition(conditionC, EntityOperator.AND, orCondition);

			GenericValue queryOne = EntityQuery.use(delegator).select(CommonConstant.ROLE_TYPE_ID).from("UserMaster")
					.where(andCondition).queryOne();

			if (queryOne != null) {
				String partyRole = queryOne.getString(CommonConstant.ROLE_TYPE_ID);
				return partyRole;
			}

		} catch (GenericEntityException e) {

			e.printStackTrace();
		}

		return "noPartyRole";

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
				String errMsg = "Unable to create records in PartyRole entity: " + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				request.setAttribute("result1", "error");
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
			String errMsg = "Unable to retrive records from UserMaster view entity: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			request.setAttribute("result", "error");
			return CommonConstant.ERROR;
		}
		if (ServiceUtil.isSuccess(serviceResult)) {
			request.setAttribute("result", serviceResult.get(CommonConstant.RESPONSE_MESSAGE));
			request.setAttribute("userList", serviceResult.get("userList"));
		}

		return CommonConstant.SUCCESS;
	}

	public static String updateUserAttemptAnswerMaster(HttpServletRequest request, HttpServletResponse response) {

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		List<Map<String, Object>> questionList = (List<Map<String, Object>>) request.getAttribute("submittedAnswers");

		for (Map<String, Object> question : questionList) {
			String sequenceNumber = (String) question.get(CommonConstant.SEQUENCE_NUM);
			String performanceId = (String) question.get(CommonConstant.PERFORMANCE_ID);
			String questionId = (String) question.get(CommonConstant.QUESTION_ID);
			String submittedAnswer = (String) question.get(CommonConstant.SUBMITTED_ANSWER);

			try {
				Map<String, Object> serviceResultMap = dispatcher.runSync("updateUserAttemptAnswerMaster",
						UtilMisc.toMap(CommonConstant.PERFORMANCE_ID, performanceId, CommonConstant.QUESTION_ID,
								questionId, CommonConstant.SEQUENCE_NUM, sequenceNumber,
								CommonConstant.SUBMITTED_ANSWER, submittedAnswer, CommonConstant.USER_LOGIN,
								userLogin));

				if (!ServiceUtil.isSuccess(serviceResultMap)) {

					return CommonConstant.ERROR;
				}

			} catch (GenericServiceException e) {
				Debug.logError("cannot update record in user attempt answer master entity", e.toString());
				return CommonConstant.ERROR;
			}
		}
		return CommonConstant.SUCCESS;
	}
}
