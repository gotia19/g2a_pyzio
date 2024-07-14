Feature: Product in a cart

  Scenario Outline: Search for a product, save its price, add it to the cart, and verify the price in the cart
    Given I am on the homepage
    When I search for the product "<productName>"
    Then I save the price of the product
    When I add the product to the cart
    Then I verify the product price in the cart

    Examples:
      | productName                                                     |
      | Corel Painter Essentials 7 (PC) - Corel Key - GLOBAL            |
      | McAfee AntiVirus PC 1 Device 1 Year McAfee Key GLOBAL           |
      | Avast Driver Updater (PC) 1 Device, 1 Year - Avast Key - GLOBAL |