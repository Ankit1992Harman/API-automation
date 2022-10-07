package com.mindtickle.qa.api.test.user.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtickle.qa.api.dto.RequestDTO;
import com.mindtickle.qa.api.test.requestDTO.petRequest;
import com.mindtickle.qa.apibase.APITestBase;
import com.mindtickle.qa.apihelper.ApiHelper;
import com.mindtickle.qa.listener.TestListener;
import com.mindtickle.qa.util.DataProviderUtil;
import com.mindtickle.qa.util.DataProviderUtil.mapKeys;
import com.mindtickle.qa.util.JavaUtil;
import com.mindtickle.qa.util.LoggerUtil;
import com.mindtickle.qa.util.LoggerUtil.LogLevel;
import com.mindtickle.qa.util.PropertiesUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class UserTestClass extends APITestBase {

  static String baseUrl = null;
  static petRequest petRequestResponse;
  static HashMap<Integer, String> createdUserMap;
  static HashMap<Integer, String> updatedUserMap;
  ApiHelper apihelper = null;
  HttpResponse httpResponse = null;
  RequestDTO requestDTO = null;
  String apiResponse = null;
  int responseCode = 0;
  int failureCount = 0;
  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeClass
  public void initializeClass() throws NumberFormatException, Exception {
    LoggerUtil
        .setlog(LogLevel.ONLYLOGS, "Initializing class variables for Services");
    apihelper = new ApiHelper();
    petRequestResponse = new petRequest();
    createdUserMap = new HashMap<>();
    updatedUserMap = new HashMap<>();
    baseUrl = PropertiesUtil.getEnvConfigProperty("baseUserAPI");
  }

  @BeforeMethod
  public void beforeMethod()
      throws SQLException, ClientProtocolException, URISyntaxException, IOException {
    failureCount = 0;
  }

  @Test(dataProvider = "createUser", priority = 1, enabled = true)
  public void testPostAPI(String testName, HashMap<String, Object> requestDtoMap)
      throws IOException {
    System.out.println(requestDtoMap);
    requestDTO = (RequestDTO) requestDtoMap.get(mapKeys.reqDTO.toString());
    System.out.println(requestDTO.getRequestBody().toString());
    ObjectMapper om = new ObjectMapper();
    List<petRequest> myObjects = Arrays
        .asList(om.readValue(requestDTO.getRequestBody(), petRequest[].class));
    String userNameCreated = java.util.UUID.randomUUID().toString();
    for (petRequest pr : myObjects) {
      pr.setUsername(userNameCreated);
      createdUserMap.put(pr.getId(), userNameCreated);
    }
    String requestString = objectMapper.writeValueAsString(myObjects);
    System.out.println(requestString);
    requestDTO.setRequestBody(requestString);
    httpClient = HttpClients.createDefault();
    httpResponse = apihelper.createRequest(requestDTO, baseUrl, httpClient, httpContext, false);
    responseCode = httpResponse.getStatusLine().getStatusCode();
    Assert.assertEquals(responseCode, 200);


  }


  @Test(dataProvider = "updateUser", priority = 2, enabled = true)
  public void testPutAPI(String testName, HashMap<String, Object> requestDtoMap)
      throws IOException {
    HashMap<Object, Object> testSpecificDataMap = new HashMap<Object, Object>();
    requestDTO = (RequestDTO) requestDtoMap.get(mapKeys.reqDTO.toString());
    petRequest petRequestDto = objectMapper
        .readValue(requestDTO.getRequestBody(), petRequest.class);
    String userNameToUpdate = createdUserMap.get(petRequestDto.getId());

    String origPathBody = requestDTO.getPathParamJson();
    LinkedHashMap<String, String> replacePathBody = new LinkedHashMap<String, String>();
    replacePathBody.put("username", userNameToUpdate);
    String pathBody = JavaUtil.replacePreRequisite(replacePathBody, origPathBody);
		if (pathBody != null) {
			requestDTO.setPathParamJson(pathBody);
		}
    petRequest petRequestDTO = objectMapper
        .readValue(requestDTO.getRequestBody(), petRequest.class);
    String updatedUserName = java.util.UUID.randomUUID().toString();
    updatedUserMap.put(petRequestDto.getId(), updatedUserName);
    petRequestDTO.setUsername(updatedUserName);
    String requestString = objectMapper.writeValueAsString(petRequestDTO);
    requestDTO.setRequestBody(requestString);
    httpClient = HttpClients.createDefault();
    httpResponse = apihelper.createRequest(requestDTO, baseUrl, httpClient, httpContext, false);
    responseCode = httpResponse.getStatusLine().getStatusCode();
    Assert.assertEquals(responseCode, 200);


  }


  @Test(dataProvider = "getUser", priority = 3, enabled = true)
  public void getUser(String testName, HashMap<String, Object> requestDtoMap)
      throws IOException {
    HashMap<Object, Object> testSpecificDataMap = new HashMap<Object, Object>();
    requestDTO = (RequestDTO) requestDtoMap.get(mapKeys.reqDTO.toString());
    String origPathBody = requestDTO.getPathParamJson();
    LinkedHashMap<String, String> replacePathBody = new LinkedHashMap<String, String>();
    petRequest petRequestDto = objectMapper
        .readValue(requestDTO.getExpectedResponseJson(), petRequest.class);
    String userNameToGet = updatedUserMap.get(petRequestDto.getId());
    replacePathBody.put("username", userNameToGet);
    String pathBody = JavaUtil.replacePreRequisite(replacePathBody, origPathBody);
		if (pathBody != null) {
			requestDTO.setPathParamJson(pathBody);
		}

    httpClient = HttpClients.createDefault();
    httpResponse = apihelper.createRequest(requestDTO, baseUrl, httpClient, httpContext, false);
    responseCode = httpResponse.getStatusLine().getStatusCode();
    Assert.assertEquals(responseCode, 200);
    apiResponse = apihelper.getResponse(requestDTO, httpResponse);
    petRequestDto.setUsername(userNameToGet);
    String responseExpected = objectMapper.writeValueAsString(petRequestDto);
    requestDTO.setExpectedResponseJson(responseExpected);
		if (StringUtils.isNotBlank(requestDTO.getExpectedResponseJson())) {
			apihelper.validateRequest(requestDTO, apiResponse);
		}
  }

  @DataProvider(name = "createUser", parallel = false)
  public Object[][] createUser() throws Exception {
    Object[][] sheetArray = DataProviderUtil.provideDataMap(
        PropertiesUtil.getConstantProperty("TestData_API_User"), "getUsers_v1",
        "createUser", true);
    return DataProviderUtil.sheetMapToDPMap(sheetArray);
  }

  @DataProvider(name = "updateUser", parallel = false)
  public Object[][] updateUser() throws Exception {
    Object[][] sheetArray = DataProviderUtil.provideDataMap(
        PropertiesUtil.getConstantProperty("TestData_API_User"), "getUsers_v1",
        "updateUser", true);
    return DataProviderUtil.sheetMapToDPMap(sheetArray);
  }

  @DataProvider(name = "getUser", parallel = false)
  public Object[][] getUser() throws Exception {
    Object[][] sheetArray = DataProviderUtil.provideDataMap(
        PropertiesUtil.getConstantProperty("TestData_API_User"), "getUsers_v1",
        "getUser", true);
    return DataProviderUtil.sheetMapToDPMap(sheetArray);
  }

}
