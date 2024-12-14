Feature: Issues from Anders
  As a user,
  I want to navigate through the webshop and verify its functionality
  so that I can ensure its usability.

  Scenario: Check that all links on the page work correctly
    Given I am on the page "https://webshop-agil-testautomatiserare.netlify.app/"
    Then I should not get an HTTP response error for any of the links

  Scenario: Verify product quantity counter and cart consistency when adding different products
    Given I navigate to the checkout page
    And the cart is empty
    When I navigate to the product page
    And I add up to 8 products to the cart
    Then the cart item counter on the product page should show "8" items
    When I navigate to the checkout page
    Then the cart on the checkout page should have "8" items
    When I navigate to the product page
    And I add up to 15 products to the cart
    Then the cart item counter on the product page should show "15" items
    When I navigate to the checkout page
    Then the cart on the checkout page should have "15" items
