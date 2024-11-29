Feature: The Shop Application

Scenario: Verify the page title
    Given the user navigates to the shop homepage
    Then the page title should be "Webbutiken"

Scenario: Verify the 'All products' button text
    Given the user navigates to the shop homepage
    Then the 'All products' button text should be "All products"
