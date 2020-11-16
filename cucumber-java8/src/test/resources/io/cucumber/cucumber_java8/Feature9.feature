Feature: Change the category of a task

As a student, I want to adjust the priority of a task, to help better manage my time.

Scenario: Move a task from the LOW priority (category) to HIGH category #Normal Flow

  I should see the task added to course to do list
    Given Service is running
    	And The LOW category exists
    	And the HIGH category exists
        And the task exists
        And the task is in the LOW category
     		When I add it to the HIGH category then remove it from the LOW category
     		Then I should see the task in the HIGH category

Scenario: Move a task from the LOW priority (category) and add it to the Low priority (category) #Alternative Flow

  I should see no change
    Given Service is running
    	And The LOW category exists
        And the task exists
        And the task is in the LOW category
     		When I add it to the LOW category
     		Then I should still see the task inside the LOW category


Scenario: Move a task from the LOW priority (category) and add it to the non-existent EMERGENCY priority (category) #Error Flow

  I should see no change
 	 Given Service is running
 		 And The LOW category exists
         And the task exists
	 	 And the task is in the LOW category
         But the EMERGENCY category does not exist
 			 When I add the task to the EMERGENCY priority 
 			 Then I should see an error message





