package com.mindtickle.qa.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtickle.qa.listener.TestListener;

public class JsonUtil {


	/**
	 * This Function is used to return the value from the api response depending on the map provided 
	 * @param apiResponse : Api response from which the values needs to be fetched
	 * @param valueToGetFromResponseMap : This map contains the value which we need to fetch from the response with Key as the element name from the response and value as its index
	 * @throws Exception 
	 */

	public static boolean returnValueFromResponse(String apiResponse,Map<String, Object> valueToGetFromResponseMap) throws Exception{

		List<JsonNode> nameNode = null;
		try {

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(apiResponse);

			for (String keys : valueToGetFromResponseMap.keySet()) {
				int index = 0;
				nameNode = rootNode.findParents(keys);
				if (nameNode.size() > 0){
					if (valueToGetFromResponseMap.get(keys) != null) {
						if (!valueToGetFromResponseMap.get(keys).equals("")){
							index = (int)valueToGetFromResponseMap.get(keys);
						}
					}
					JsonNode val = nameNode.get(index).get(keys);
					if(val.getClass().getSimpleName().equals("TextNode")) 
						valueToGetFromResponseMap.put(keys, (Object)val.textValue());
					else 
						valueToGetFromResponseMap.put(keys, (Object)val);
				}
			}

			return true;
		} catch (JsonProcessingException e) {
			TestListener.setException(e.getMessage(),e.getStackTrace());
			throw e;
			//			return false;
		} catch (IOException e) {
			TestListener.setException(e.getMessage(),e.getStackTrace());
			throw e;
			//			return false;
		}
	}

	/*	if (((HashMap) excelDataForPreReq[0][0]).get("preRequisiteSheet") != null) {
			if (((HashMap) excelDataForPreReq[0][0]).get("preRequisiteIdentifier") != null) {
				if (((HashMap) excelDataForPreReq[0][0]).get("preRequisiteIdentifier").toString()
						.equalsIgnoreCase("preReqRegisterUser_v1")) {
					Object[][] excelDataForPrePreReq = DataProviderUtil.provideDataMap(
							PropertiesUtil.getConstantProperty("TestData_API_Media"), ((HashMap) excelDataForPreReq[0][0]).get("preRequisiteSheet").toString(),
							"preReqRegisterUser_v1", true);
					RequestDTO prePreRequestDTO = new RequestDTO();
					prePreRequestDTO = RequestUtil.createRequestDTO(excelDataForPrePreReq, prePreRequestDTO);
					userEmail = PropertiesUtil.getConstantProperty("userEmail");
					password = PropertiesUtil.getConstantProperty("password");
					String prePreRequestBody = ((HashMap) excelDataForPreReq[0][0])
							.get("preRequisiteIdentifier").toString();
					InternalPIXIEHelper.deleteRegisteredUser(userEmail);
					testSpecificDataMap.put("email", userEmail);
					replacePreRequisiteRequestBody.put("email", testSpecificDataMap.get("email").toString());
					testSpecificDataMap.put("password", password);
					replacePreRequisiteRequestBody.put("password",
							testSpecificDataMap.get("password").toString());
					testSpecificDataMap.put("confirmPassword", password);
					replacePreRequisiteRequestBody.put("confirmPassword",
							testSpecificDataMap.get("confirmPassword").toString());
					testSpecificDataMap.put("contactNumber", PropertiesUtil.getConstantProperty("mobile"));
					replacePreRequisiteRequestBody.put("contactNumber",
							testSpecificDataMap.get("contactNumber").toString());
					String preRequestBody = JavaUtil.replacePreRequisite(replacePreRequisiteRequestBody,
							prePreRequestDTO.getRequestBody());
					if (preRequestBody != null)
						prePreRequestDTO.setRequestBody(preRequestBody);
					httpResponse = apihelper.createRequest(prePreRequestDTO, baseUrl, httpClient, httpContext,
							false);
					preResponseCode = httpResponse.getStatusLine().getStatusCode();
					if (preResponseCode == 200) {
						apiResponse = apihelper.getResponse(preRequestDTO, httpResponse);
						LoggerUtil.setlog(LogLevel.INFO, "Pre-PreRequisite Completed Successfully");
						valueToGetFromResponseMap.put("id", "");
						JsonUtil.returnValueFromResponse(apiResponse, valueToGetFromResponseMap);
					}
				}
			}
		}*/
}
