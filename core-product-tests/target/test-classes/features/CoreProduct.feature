@CoreProduct
Feature: Validate Core Product Home Page and News and Features Page

  @Smoke @Regression
  Scenario: Retrieve details of all Men's Jackets and store them in a file
    Given User is on Core Product Home Page
    When Navigate to Men's Page
    And Find all Jackets
    And For each Jacket, retrieve Price, Title and Top Seller Message (if any)
    And Store the Jacket details in a file

  @Smoke
  Scenario: On News & Features page, identify the count of videos older than 3d
    Given User is on Core Product Home Page
    When Navigate to News and Features Page
    And Identify and count the number of Video feeds
    And Find the count of Videos which are older than 3d
    Then Validate that list of Video feeds is not empty
