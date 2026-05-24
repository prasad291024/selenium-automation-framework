@OrangeHRM @Login
Feature: Login to OrangeHRM Application
  As an OrangeHRM user
  I want to login to the application
  So that I can access the PIM dashboard

  Background:
    Given User is on OrangeHRM login page

  @ValidLogin @Smoke
  Scenario: Login with valid credentials
    Then User should see the PIM menu header

  @InvalidLogin @Regression
  Scenario Outline: Login with invalid credentials shows error
    When User logs in with username "<username>" and password "<password>"
    Then User should see OrangeHRM error message "<errorMessage>"

    Examples:
      | username     | password     | errorMessage              |
      | invalid-user-1 | invalid-value-1 | Invalid credentials       |
      | invalid-user-2 | invalid-value-2 | Invalid credentials       |
