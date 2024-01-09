package org.vastpro.ofbizdemo.events;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.net.ntp.TimeStamp;


public class DemoJsonEvents {
	
	public static final String module = DemoJsonEvents.class.getName();
	
	public static String callJsonEvent (HttpServletRequest request,HttpServletResponse response) {
		
//		Map<String, Object>  context = UtilMisc.toMap("firstNamr","Kamal","lastName","R");
		
		
		Map<String, Object>  resultContext  = new HashedMap<>();
		resultContext.put("firstName", "Kamal");
		resultContext.put("lastName", "R");
		request.setAttribute("resultContext", resultContext);
		
 		return "success";
		
	}
	

	
	
}
