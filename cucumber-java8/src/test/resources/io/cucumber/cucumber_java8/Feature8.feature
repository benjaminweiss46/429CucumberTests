Feature: Find all incomplete HIGH priority tasks. 

As a student,I query all incomplete HIGH priority tasks from all my classes,to identity my short-termgoals.

Background: 
    Given Service is running
  
  Scenario: Find all incomplete tasks in the HIGH priority category #Normal Flow
  I should see all incomplete tasks related to the HIGH priority category
    Given The HIGH priority category exists
      And Todos that are associated to the HIGH priority category and have a doneStatus of false exist
     When I send a request to see the incomplete todos of the HIGH priority category
     Then I should see all incomplete tasks of the HIGH priority category
  
  Scenario: Find all incomplete tasks for the HIGH priority category but none exist #Alternate Flow
  I should see a successful request but get no todos
    Given The HIGH priority category exists
      But Todos that are associated to the HIGH priority category and have a doneStatus of false do not exist
      When I send a request to see the incomplete todos of the HIGH priority category
     Then I should see no todos
  
  Scenario: Find all incomplete todos for the HIGH priority category but the category does not exist #Error Flow
  I will receive the incomplete todos of another category
     Given the HIGH priority category does not exist
     But Another category does exist
     When I try to see the unfinished todos in the HIGH priority category
     Then I will recieve the incomplete todos of another category