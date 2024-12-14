#Written by Carl-Johan
Feature: Issues from Carl
  As a user,
  I want to navigate through the webshop and verify its functionality
  so that I can ensure its usability.

  Scenario: Navigate to the Home page
    Given the user navigates to the shop homepage
    Then the page title should be "The Shop"

  Scenario: Navigate to the Shop page
    Given the user navigates to the shop homepage
    When the user clicks on the "Shop" link


  Scenario: Navigate to the checkout page
    Given the user navigates to the shop homepage
    When the user clicks on the "Checkout" button
    Then the page route should be "checkout"

  Scenario: Search for a specific product
    Given the user navigates to the shop homepage
    When the user searches for "Mens Casual Premium Slim Fit T-Shirts"
    Then the search results should display "Mens Casual Premium Slim Fit T-Shirts"
