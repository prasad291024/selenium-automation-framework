@OrangeHRM @Login
Feature: Login to OrangeHRM Application
  As an OrangeHRM user
  I want to login to the application
  So that I can access the PIM dashboard

  Background:
    Given User is on OrangeHRM login page

  @ValidLogin @Smoke
  Scenario: Login with valid credentials
    When User logs in with username "${OHR_USERNAME}" and password "${OHR_PASSWORD}"
    Then User should see the PIM menu header

  @InvalidLogin @Regression
  Scenario Outline: Login with invalid credentials shows error
    When User logs in with username "<username>" and password "<password>"
    Then User should see OrangeHRM error message "<errorMessage>"

    Examples:
      | username     | password     | errorMessage              |
      | wronguser    | wrongpass    | Invalid credentials       |
      | admin        | wrongpass    | Invalid credentials       |
