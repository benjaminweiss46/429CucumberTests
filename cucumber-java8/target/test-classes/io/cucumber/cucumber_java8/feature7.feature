#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
Feature: Remove to do list from a class.

As a student,I remove a to do list for a class which I am no longer taking,to declutter my schedule
  Background: 
    Given Service is running
  
  Scenario: Remove all to dos related to a class #Normal Flow
  I should no longer see to dos that are tasksof the class
    Given The course exists
      And To dos that are tasksof the course exist
      And The course completed tag is true
     When I remove the to dos of the class
     Then I should no longer see those tasks in the to do list with a relationship to the course
     
  Scenario: Remove all to dos related to any class tagged as completed #Alternate flow
  I remove todos for any class that is completed in the system
    Given Multiple completed classes
      And Each completed class has todo/s
     When I remove the to dos of any class that is completed
     Then I should no longer see todos related to any class that is completed
  
  Scenario: Remove all to dos related to a class category but none already exist #Error flow
  I get an error message, could not find any instances..
     Given The course exists
      But The to dos that are tasksof the course are 0
      And The course completed tag is true
     When I try to remove the to dos of the class
     Then I get an error message
     
     