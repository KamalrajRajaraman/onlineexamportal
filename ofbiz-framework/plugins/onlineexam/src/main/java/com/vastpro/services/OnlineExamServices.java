
package com.vastpro.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstants;

public class OnlineExamServices {
	
	public static final String module = OnlineExamServices.class.getName();

	/**
	 * This method is used to retrieve all the users from UserMaster view entity
	 * @param DispatchContext 
	 * @param Map<String, ? extends Object>
	 * @return Map<String,Object>	
	 */
	public static Map<String, Object> findAllUsers(DispatchContext dctx,Map<String,? extends Object > context){
		

		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		
		Delegator delegator = dctx.getDelegator();
		
		List<Map<String, Object>> userList = new LinkedList<>();
		List<GenericValue> GenericValueList =null;
		
		try {
			//Retrieve all users based on roleTypeId
			GenericValueList = EntityQuery.use(delegator).from(CommonConstants.USER_MASTER).where(CommonConstants.ROLE_TYPE_ID , "PERSON_ROLE").queryList();
				
		} catch (GenericEntityException e) {
			//If Exception occurred return error map
			String errMsg = "Exception in fetching record from UserMaster view entity : " + e.getMessage();
			Debug.logError(e, errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		if(UtilValidate.isEmpty(GenericValueList)) {
			//If Retrieved GenericValueList is empty or null
			String errMsg = "Retrieved user List from UserMaster view entity is empty or null ";
			Debug.logError( errMsg, module);
			return ServiceUtil.returnError(errMsg + module);
		}
		
		if(UtilValidate.isNotEmpty(GenericValueList)) {
			//If GenericValueList is not empty, iterate the list.
			for(GenericValue genericValue:GenericValueList) {
				String firstName = genericValue.getString("firstName");
				String lastName = genericValue.getString("lastName");
				String partyId = genericValue.getString("partyId");
				String roleTypeId = genericValue.getString("roleTypeId");
				
			//Construct a map with required values	
				Map<String, Object> user = new HashMap<>();
				user.put("firstName", firstName);
				user.put("lastName", lastName);
				user.put("partyId", partyId);
				user.put("roleTypeId", roleTypeId);				
				userList.add(user);			
			}	
			resultMap.put("userList", userList);
			
		}
		
		
		return resultMap;
		
	}

}
