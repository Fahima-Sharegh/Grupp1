# Written by Anders
Feature: Issues from Anders
  As a user,
  I want to navigate through the webshop and verify its functionality
  so that I can ensure its usability.

  Scenario: Check that all links on the page work correctly
    Given I am on the page "https://webshop-agil-testautomatiserare.netlify.app/"
    Then I should not get an HTTP response error for any of the links