Feature: Account module Functionality
  As a user, I want to access and manage my account page, so I can perform various actions.

  Background: User should be logged in
    Given The user logs in with valid cridentials

  Scenario: verify if the account bar is clickable and visible
    When the user clicks on the account bar
    Then the user should be able to click on the account bar
    And the account bar should be visible

    Scenario: verify the user can update or edit his/her profile
      When the user clicks on the account bar
      And the user clicks on the edit button
      Then Edit button should be clickable
      When the user updates his profile with the new informations
      And the user clisks on the save button
      Then the save button should be clickable
      And the user should be able the receive "Your account information has been updated"




