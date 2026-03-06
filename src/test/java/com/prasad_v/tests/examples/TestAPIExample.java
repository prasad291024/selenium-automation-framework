package com.prasad_v.tests.examples;

import com.prasad_v.utils.APIUtil;
import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Testing")
@Feature("REST API Validation")
public class TestAPIExample {

    @Test
    @Story("Get User API")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET request returns user data successfully")
    public void testGetUser() {
        LoggerUtil.info("Starting API test - Get User");
        
        Response response = APIUtil.get("https://reqres.in", "/api/users/2");
        
        LoggerUtil.info("Response Status Code: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(200);
        
        String firstName = response.jsonPath().getString("data.first_name");
        LoggerUtil.info("User First Name: " + firstName);
        assertThat(firstName).isEqualTo("Janet");
        
        LoggerUtil.info("API test completed successfully");
    }

    @Test
    @Story("Create User API")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify POST request creates user successfully")
    public void testCreateUser() {
        LoggerUtil.info("Starting API test - Create User");
        
        String requestBody = "{ \"name\": \"John\", \"job\": \"QA Engineer\" }";
        Response response = APIUtil.post("https://reqres.in", "/api/users", requestBody);
        
        LoggerUtil.info("Response Status Code: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(201);
        
        String name = response.jsonPath().getString("name");
        assertThat(name).isEqualTo("John");
        
        LoggerUtil.info("User created successfully");
    }

    @Test
    @Story("Update User API")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PUT request updates user successfully")
    public void testUpdateUser() {
        LoggerUtil.info("Starting API test - Update User");
        
        String requestBody = "{ \"name\": \"John Updated\", \"job\": \"Senior QA\" }";
        Response response = APIUtil.put("https://reqres.in", "/api/users/2", requestBody);
        
        LoggerUtil.info("Response Status Code: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(200);
        
        String job = response.jsonPath().getString("job");
        assertThat(job).isEqualTo("Senior QA");
        
        LoggerUtil.info("User updated successfully");
    }

    @Test
    @Story("Delete User API")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify DELETE request removes user successfully")
    public void testDeleteUser() {
        LoggerUtil.info("Starting API test - Delete User");
        
        Response response = APIUtil.delete("https://reqres.in", "/api/users/2");
        
        LoggerUtil.info("Response Status Code: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(204);
        
        LoggerUtil.info("User deleted successfully");
    }
}
