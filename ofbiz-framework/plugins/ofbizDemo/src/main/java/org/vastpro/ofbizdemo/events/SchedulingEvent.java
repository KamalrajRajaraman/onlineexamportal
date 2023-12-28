package org.vastpro.ofbizdemo.events;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.calendar.RecurrenceRule;

public class SchedulingEvent {

	public static final String module = SchedulingEvent.class.getName();

	public static String callEvent(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		Map<String, Object> context = UtilMisc.toMap("message", "This is a test.","userLogin",userLogin);

		try {

			long startTime = (new Date()).getTime();
			int frequency = RecurrenceRule.SECONDLY;
			int interval = 5;
			int count = 10;
			dispatcher.schedule("ScheduleService", context, startTime, frequency, interval, count);

		} catch (GenericServiceException e) {
			String errMsg = "Unable to invoke service :" + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			return "error";
		}

		return "success";

	}

}
