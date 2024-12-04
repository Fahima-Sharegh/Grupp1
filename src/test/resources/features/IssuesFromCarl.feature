Feature: Issues from Carl
  As a user,
  I want to navigate through the webshop and verify its functionality
  so that I can ensure its usability.

  Scenario: Navigate to the Home page
    Given the user navigates to the shop homepage
    Then the page title should be "Webbutiken"

  Scenario: Navigate to the checkout page
    Given the user navigates to the shop homepage
    When the user clicks on the "Checkout"
    Then the page route should be "checkout"

  Scenario: Search for a specific product
    Given the user navigates to the shop homepage
    When the user searches for "Mens Casual Premium Slim Fit T-Shirts"
    Then the search results should display "Mens Casual Premium Slim Fit T-Shirts"

  Scenario: Fill out billing details
    Given the user navigates to the checkout page
    When the user fills out the billing form with valid details
    Then the billing form should be filled correctly

  Scenario: Complete the checkout process
    Given the user navigates to the checkout page
    And the billing form is filled out with valid details
    When the user clicks on the "Complete Checkout"
    Then the page title should be "Order Confirmation"
