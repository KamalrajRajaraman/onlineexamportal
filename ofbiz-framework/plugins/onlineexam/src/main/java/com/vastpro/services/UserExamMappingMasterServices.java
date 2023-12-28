package com.vastpro.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.constants.CommonConstant;

public class UserExamMappingMasterServices {
	public static final String module = UserExamMappingMasterServices.class.getName();
	
	public static Map<String, Object> showExamsForPartyId(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> result = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String partyIdPK = (String) context.get(CommonConstant.PARTY_ID);
		List<GenericValue> examGenericValue = null;
		List<Map<String, Object>> mapValuesList= new LinkedList<Map<String,Object>>();
		try {
			
			
			examGenericValue = EntityQuery.use(delegator).from("UserExamMappingViewEntity")
					.where(CommonConstant.PARTY_ID, partyIdPK).queryList();

			if (examGenericValue != null) {

				for (GenericValue value : examGenericValue) {
					String examId = value.getString(CommonConstant.EXAM_ID);
					String examName = value.getString(CommonConstant.EXAM_NAME);

					Map<String, Object> exam = UtilMisc.toMap(CommonConstant.EXAM_ID, examId,
							CommonConstant.EXAM_NAME, examName);
					mapValuesList.add(exam); 
				}
				
				result.put("examList", mapValuesList);
				
			}

		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("Error in fetching record from UserExamMappingViewEntity entity ........" + module);
		}
		return result;

	}

}
