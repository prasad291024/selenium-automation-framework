package com.prasad_v.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class APIUtil {

    private static RequestSpecification getRequestSpec(String baseUri) {
        return RestAssured.given()
                .baseUri(baseUri)
                .header("Content-Type", "application/json");
    }

    public static Response get(String baseUri, String endpoint) {
        LoggerUtil.info("GET Request: " + baseUri + endpoint);
        Response response = getRequestSpec(baseUri).get(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response post(String baseUri, String endpoint, Object body) {
        LoggerUtil.info("POST Request: " + baseUri + endpoint);
        Response response = getRequestSpec(baseUri).body(body).post(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response put(String baseUri, String endpoint, Object body) {
        LoggerUtil.info("PUT Request: " + baseUri + endpoint);
        Response response = getRequestSpec(baseUri).body(body).put(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response delete(String baseUri, String endpoint) {
        LoggerUtil.info("DELETE Request: " + baseUri + endpoint);
        Response response = getRequestSpec(baseUri).delete(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response getWithHeaders(String baseUri, String endpoint, Map<String, String> headers) {
        RequestSpecification spec = getRequestSpec(baseUri);
        headers.forEach(spec::header);
        return spec.get(endpoint);
    }
}
