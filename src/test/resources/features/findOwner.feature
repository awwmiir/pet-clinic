Feature: Find Owner
  Scenario:
    Given an owner with id = 1 is created
    And owners first name = amir and lastname = alizad
    And owner lives in city = tehran and at address = tehran-rey
    And owners phone = +123456789
    And owner exists in the repository
    When findOwner with Id = 1 is called
    Then expected owner is found correctly
