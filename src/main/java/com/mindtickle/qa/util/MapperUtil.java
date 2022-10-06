/*
Copyright (C)  Noon Academy, Inc - All Rights Reserved
Unauthorized copying of this file, via any medium is strictly prohibited
Proprietary and confidential
*/


package com.mindtickle.qa.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class MapperUtil {
 public MapperUtil() {
 }

 private static final ModelMapper modelMapper = new ModelMapper();
 private final ObjectMapper objectMapper = new ObjectMapper();
 private static ObjectMapper snakeCaseEnabledObjectMapper;


 public static ModelMapper getModelMapper() {
     modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
     return modelMapper;
 }

 public ObjectMapper getPlainObjectMapper() {
     return objectMapper;
 }

 public static ObjectMapper getSnakeCaseEnabledObjectMapper() {
	 snakeCaseEnabledObjectMapper = (new ObjectMapper()).setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
     return snakeCaseEnabledObjectMapper;
 }

 public static Map convertStringToMap(String value) {
     try {
         return getSnakeCaseEnabledObjectMapper().readValue(value, Map.class);
     } catch (IOException var2) {
         var2.printStackTrace();
         return null;
     }
 }

 public static <T> T convertStringToObject(String value, TypeReference typeReference) {
     try {
         return getSnakeCaseEnabledObjectMapper().readValue(value, typeReference);
     } catch (IOException var3) {
         var3.printStackTrace();
         return null;
     }
 }

 public static <T> T convertStringToObject(String value, Class<T> responseClass) {
     try {
         return getSnakeCaseEnabledObjectMapper().readValue(value, responseClass);
     } catch (IOException var3) {
         var3.printStackTrace();
         return null;
     }
 }

 public static <T> T convertStringToObject(String value, JavaType javaType) {
     try {
         return getSnakeCaseEnabledObjectMapper().readValue(value, javaType);
     } catch (IOException var3) {
         var3.printStackTrace();
         return null;
     }
 }

 public static JavaType constructCollectionJavaType(Class targetObject) {
     return getSnakeCaseEnabledObjectMapper().getTypeFactory().constructCollectionType(List.class, targetObject);
 }

 public static String convertObjectToString(Object value) {
	 System.out.println("header value is --"+ value);
     try {

         return getSnakeCaseEnabledObjectMapper().writeValueAsString(value);
     } catch (JsonProcessingException var2) {
         var2.printStackTrace();
         return null;
     }
 }
}

