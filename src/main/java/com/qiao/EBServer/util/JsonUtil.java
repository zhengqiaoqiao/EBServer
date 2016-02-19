package com.qiao.EBServer.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {

    }

    public static String serialize(Object o) throws JsonProcessingException{
    	return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }
    
    @SuppressWarnings("unchecked")
	public static Object deSerialize(String json, Class c) throws IOException, JsonMappingException, IOException {
    	return objectMapper.readValue(json, c);
    }
}
