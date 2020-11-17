Feature: Changing task description
As a student, I want to change a task description, to better represent the work to do.

	Scenario: Change description of a task. #Normal Flow
    I should see the description of the task change.
   		Given The Service is running
        And task exists
        And description for task exists
        	When I add a description to the task
            Then I should see the task description change
    
    Scenario: Add description of a task. #Alternate Flow
    I should see the description added to the task.
    	Given The Service is running
        And task exists
        But description is not present
        	When I add a description to the task
            Then I should see the description appear for that task
    
    Scenario: Add description of an inexistant task #Error Flow
    I should see an error message.
    	Given The Service is running
        But task does not exist
        	When I add a description to the task
            Then I should see an error message explaining that the task does not exist