#Written by Fahima

Feature: Fahimas issues
  #webshop-agil-testautomatiserare.netlify.app


#1
  Scenario: the users should see all products
    Given the user navigates to the shop homepage
    When user clicks on the "Shop" button
    And user clicks the "All" button
    Then all products should be visible

#2
  Scenario: Add and remove a product from the cart
    Given the user navigates to the shop homepage
    When user clicks on the "Shop" button
    And the user adds a product to the basket
    And the user clicks on the "Checkout" button
    Then the user removes the product from the shopping cart

#3
  Scenario: User searches for a product with no search results
    Given the user navigates to the shop homepage
    When user clicks on the "Shop" button
    Then the user types "Men's Cotton Jacket" into the search bar and presses the "Enter" key

#4
  Scenario: User attempts checkout with empty required fields
    Given the user navigates to the shop homepage
    And the user clicks on the "Checkout" button
    And the user clicks on the Continue to Checkout button
    Then the user should see the error message under each empty required fields

#5
  Scenario: Validate required fields during checkout
    Given the user navigates to the shop homepage
    When user clicks on the "Shop" button
    And the user clicks on the "Checkout" button
    And the user fills in all required fields
    And the user clicks on the Continue to Checkout button
    # Then the user should not see the error message under any of the required fields ?