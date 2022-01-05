Feature: Find Owner

  Scenario:
    Given an owner with id = 1 is created
    And owner exists in the repository
    When findOwner with Id = 1 is called
    Then expected owner is found correctly
