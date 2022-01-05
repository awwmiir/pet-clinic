Feature: Find pet

  Background: a default pet type is set
    Given all pets are cat

  Scenario: find pet in pet service
    Given a pet with id = 2 exists
    And the owner of the pet is owner with id = 1
    When findPet with id = 2 is called
    Then expected pet is found correctly
