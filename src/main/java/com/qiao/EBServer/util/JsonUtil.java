package com.qiao.EBServer.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiao.EBServer.domain.Domain;



public class JsonUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {

    }

    public static String serialize(Object o) throws JsonProcessingException{
    	return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }
    
    public static Object deSerialize(String json, Class c) throws IOException, JsonMappingException, IOException {
    	return objectMapper.readValue(json, c);
    }
    
    public static void main (String[] args) throws JsonProcessingException{
    	Domain d = new Domain();
    	d.getS4().add(new Domain());
    	
    	Map map = new HashMap<>();
    	map.put("1", d);
    	try {
			String s = serialize(map);
			System.out.println(s);
			Map dm = (Map) deSerialize(s, Map.class);
			System.out.println(dm.get(dm.keySet().iterator().next()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
