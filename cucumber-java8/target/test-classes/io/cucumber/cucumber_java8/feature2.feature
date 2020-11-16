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
Feature: Add a task to course to do list
As a student,I add a task to a course to do list,so I can remember it.
  Background: 
    Given Service is running
  
  Scenario: Add a task to a courses todo list #Normal Flow
  I should see the task added to course to do list
  	 Given The course exists
     When I add a task to the course to do list
     Then I should see the task in the to do list with a relationship to the course
     
     
  Scenario: Add a task thats already on one courses todo list to another #Alternate Flow
  I should see the task added to course to do list as well as the course it previously belonged to
  	Given Two courses exists
    When I add a task that is related to one course to the to do list of another
    Then I should see the task in the to do list with a relationship to both courses
  
  
  
  Scenario: Add a task to a nonexistant courses todo list #Error Flow
  I should receive an error
    Given course does not exist
    When I try to add a task to the course to do list
    Then task will not be related to the selected course
  