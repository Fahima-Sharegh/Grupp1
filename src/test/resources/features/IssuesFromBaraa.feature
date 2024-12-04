Feature: Checkout Process
  As a user
  I want to complete the checkout process
  So that I can purchase items from the shop

  Scenario: Add a product to the cart
    Given I am on the homepage
    When I click the "Shop" link
    And I click the "Add to cart"
    Then the product should be added to the cart

  Scenario: Navigate to the checkout page
    Given I am on the homepage
    When I click on the "Checkout" button
    Then I should be redirected to the checkout page

  Scenario: Fill out billing details
    Given I am on the checkout page
    When I fill in the "First name" field with "Baraa"
    And I fill in the "Last name" field with "Abdullatif"
    And I fill in the "Email" field with "baraa.abdullatif@iths.se"
    And I fill in the "Address" field with "Lab la bla"
    And I fill in the "Country" field with "Sweden"
    And I fill in the "City" field with "Gothenburg"
    And I fill in the "Zip" field with "44444"
    Then the billing details should be filled in correctly

  Scenario: Remove an item from the basket
    Given I am on the checkout page
    When I remove an item from the basket
    Then the basket should be empty

  Scenario: Complete the checkout process
    Given I have filled out my billing details
    When I select "credit" as the "Payment method"
    And I fill in the "Name on card" field with "Baraa Abdullatif"
    And I fill in the "Credit card number" field with "1111111111111111"
    And I fill in the "Expiration" field with "12/01"
    And I fill in the "CVV" field with "111"
    And I click the "Continue to checkout" button
    Then the checkout process should be completed successfully
