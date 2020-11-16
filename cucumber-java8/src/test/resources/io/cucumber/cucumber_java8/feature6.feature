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
Feature: Find all incomplete tasks related to a class. 

As a student,I query the incomplete tasks for a class I am taking,to help manage my time.
  Background: 
    Given Service is running
  
  Scenario: Find all incomplete tasks for a given course #Normal Flow
  I should see all incomplete tasks related to a class
    Given The course exists
      And To dos that are tasksof the course and doneStatus=False exist
     When I send a request to see unfinished tasks
     Then I should see all incomplete tasks of the course
  
  Scenario: Find all incomplete tasks for a given course but none exist #Alternate Flow
  I should see a successful request but get no output
    Given The course exists
      But To dos that are tasksof the course and doneStatus=False dont exist 
     When I send a request to see unfinished tasks
     Then I should see no tasks
  
  Scenario: Find all incomplete tasks for a given course but course doesnt exist #Error Flow
  I will recieve the incomplete tasks for another course
     Given course does not exist
     But Another course does
     When I try to see the unfinished tasks
     Then I will recieve the incomplete tasks for another course

