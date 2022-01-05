Feature: Find Pet

  Scenario: new pet is added to owners pet list
    Given an owner with id = 1 is created
    And owner exists in the repository
    When owner requests a new pet
    Then a new pet is created and returned
    And a new pet is added to owner pet list
