@VWO @Login
Feature: Login to VWO Application
  As a VWO user
  I want to login to the application
  So that I can access the dashboard

  Background:
    Given User is on VWO login page

  @ValidLogin @Smoke
  Scenario: Login with valid credentials
    When User enters username as "${VWO_USERNAME}" and password as "${VWO_PASSWORD}"
    Then User should be redirected to Dashboard

  @InvalidLogin @Regression
  Scenario Outline: Login with invalid credentials shows error
    When User enters username as "<username>" and password as "<password>"
    Then User should see error message "<errorMessage>"

    Examples:
      | username             | password  | errorMessage                                               |
      | invalid.user.1@example.test | invalid-value-1 | Your email, password, IP address or location did not match |
      | invalid.user.2@example.test | invalid-value-2 | Your email, password, IP address or location did not match |
      | invalid.user.3@example.test | invalid-value-3 | Your email, password, IP address or location did not match |
