@DP2HomePage
Feature: On DP2 Home Page, check for duplicates in footer links.

  @Smoke
  Scenario: Write all footer links into a csv file and report if any duplicate hyperlinks are present.
    Given User is on DP2 Home Page
    When User scrolls to the end of the Home Page
    And Collects all the footer links
    And Writes them into a csv file
    Then Validate the Urls and report if there are any duplicates
