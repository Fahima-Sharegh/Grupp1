Feature: Web Shop Tests

  Scenario: Verify the title of the web shop
    Given I open the web shop
    Then the title should be "Webbutiken"

  Scenario: Verify the "All Products" button text
    Given I open the web shop
    Then the "All products" button should be visible
