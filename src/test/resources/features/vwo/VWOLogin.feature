@VWO @Login
Feature: Login to VWO Application
  As a VWO user
  I want to login to the application
  So that I can access the dashboard

  Background:
    Given User is on VWO login page

  @ValidLogin @Smoke
  Scenario: Login with valid credentials
    When User enters username as "93npu2yyb0@esiix.com" and password as "Wingify@123"
    Then User should be redirected to Dashboard

  @InvalidLogin @Regression
  Scenario Outline: Login with invalid credentials shows error
    When User enters username as "<username>" and password as "<password>"
    Then User should see error message "<errorMessage>"

    Examples:
      | username             | password  | errorMessage                                               |
      | 93npu2yyb0@esiix.co  | admin12$$ | Your email, password, IP address or location did not match |
      | admin                | admin     | Your email, password, IP address or location did not match |
      | abc123               | xyz$$     | Your email, password, IP address or location did not match |
