package org.vastpro.ofbizdemo.services;

import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;



public class ScheduleService {
	
	private static Integer count =0;
	
	public static final String module = ScheduleService.class.getName();
	
	public static Map<String, Object> callService(DispatchContext dctx,Map<String, ? extends Object> context){
		
		Map<String, Object> result =ServiceUtil.returnSuccess();
		
		System.out.println("ScheduleService called "+ ++count + "times" +context.get("message")+ module);
		Debug.logInfo("ScheduleService called "+ ++count + "times" +context.get("message"), module);
		
		return result;
		
	}

}
