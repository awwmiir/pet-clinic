Feature:
  Background: a default pet type is set
    Given all pets are cat

  Scenario: find pet in pet service
    Given a pet with id = 2 exists
    And the owner of the pet is owner with id = 1
    When owner requests to save pet in his pet list
    Then the pet is saved in owners list correctly
