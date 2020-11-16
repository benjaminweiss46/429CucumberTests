Feature: Task priority

As a student, I categorize tasks as HIGH, MEDIUM or LOW priority, so I can better manage my time.

	Scenario: Add task to LOW priority category #Normal Flow
    I should see the task has been placed in the LOW category.
    	Given The Service is running
        And the LOW category exists
        And The task exists
        	When I add the task to the LOW category
            Then I should see the task in the to do list with a relationship to LOW
            
	Scenario: Add a task to HIGH priority category #Alternate Flow
    I should see the task has been placed in the HIGH category.
    	Given The Service is running
        And The HIGH category exists
        And The task exists
        	When I add the task to the High category
            Then I should see the task in the to do list with a relationship to HIGH
    
    Scenario: Add a task to IMPORTANT priority category #Error Flow
    I should receive an error
    	Given The Service is running
        And The task exists
       	But the IMPORTANT category does not exist
        	When I add the task to the IMPORTANT category
            Then I should see and error message explaining the category does not exist