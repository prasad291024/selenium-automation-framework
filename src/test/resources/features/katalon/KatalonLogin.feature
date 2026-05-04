@Katalon @Login
Feature: Login to Katalon Demo Application
  As a Katalon demo user
  I want to login to the application
  So that I can access the appointment page

  Background:
    Given User is on Katalon home page

  @ValidLogin @Smoke
  Scenario: Login with valid credentials reaches appointment page
    When User logs in to Katalon with username "John Doe" and password "ThisIsNotAPassword"
    Then User should see the Make Appointment header

  @InvalidLogin @Regression
  Scenario Outline: Login with invalid credentials shows error
    When User logs in to Katalon with username "<username>" and password "<password>"
    Then User should see Katalon error message "<errorMessage>"

    Examples:
      | username      | password       | errorMessage                                                          |
      | invalid_user  | invalid_pass   | Login failed! Please ensure the username and password are valid.      |
      | wronguser     | wrongpass      | Login failed! Please ensure the username and password are valid.      |
