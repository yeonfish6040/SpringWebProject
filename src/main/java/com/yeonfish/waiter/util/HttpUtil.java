package com.yeonfish.waiter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yeonfish.waiter.DevController.logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


public class HttpUtil {

    public static String callApi(String requestUrl, JSONObject params) throws ParseException, JsonProcessingException, JsonMappingException {
        final logger log = new logger();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestMessage = new HttpEntity<>(params.toString(), httpHeaders);
        String response = restTemplate.postForObject(requestUrl, requestMessage, String.class);

        return response;
    }
}
