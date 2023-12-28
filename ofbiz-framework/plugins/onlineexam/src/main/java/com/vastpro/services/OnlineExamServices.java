
package com.vastpro.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class OnlineExamServices {
	
	public static final String module = OnlineExamServices.class.getName();


	public static Map<String, Object> findAllUsers(DispatchContext dctx,Map<String,? extends Object > context){
		

		Map<String, Object> returnSucces = ServiceUtil.returnSuccess();
		
		Delegator delegator = dctx.getDelegator();
		
		List<Map<String, Object>> userList = new LinkedList<>();
		List<GenericValue> GenericValueList =null;
		
		try {
			GenericValueList = EntityQuery.use(delegator).from("UserMaster").where("roleTypeId","PERSON_ROLE").queryList();
			
			
		} catch (GenericEntityException e) {
		
			e.printStackTrace();
		}
		if(UtilValidate.isNotEmpty(GenericValueList)) {
			
			for(GenericValue genericValue:GenericValueList) {
				String firstName = genericValue.getString("firstName");
				String lastName = genericValue.getString("lastName");
				String partyId = genericValue.getString("partyId");
				String roleTypeId = genericValue.getString("roleTypeId");			
				Map<String, Object> user = new HashMap<>();
				user.put("firstName", firstName);
				user.put("lastName", lastName);
				user.put("partyId", partyId);
				user.put("roleTypeId", roleTypeId);				
				userList.add(user);			
			}
			
			
			returnSucces.put("userList", userList);
			
		}
		
		
		return returnSucces;
		
	}

}
