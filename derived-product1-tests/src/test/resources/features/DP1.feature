@DP1HomePage
Feature: Validate slides DP1 Home Page

  @Smoke
  Scenario Outline: Check number of slides and duration of each slide on DP1 Home Page
    Given User is on DP1 Home Page
    When Retrieve slide count, title and duration
    Then Validate slide titles and duration with testdata
