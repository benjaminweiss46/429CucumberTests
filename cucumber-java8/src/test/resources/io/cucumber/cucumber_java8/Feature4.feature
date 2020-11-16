Feature: remove a task from a to do list

As a student, I remove an unnecessary task from my course to do list, so I can forget about it.

Scenario: remove task from a given category  #Normal Flow
	I should see the task removed from the course to do list
		Given Service is running
			And the task exists
			And The course category exists
			And the task is inside the category
				When I remove the task from the to do list in the category
				Then I should no longer see that task in the todo list that has a relationship to that category
                
Scenario: remove a task from a given project #Alternative Flow
	I should see the task removed from the course (project) task list
    	Given Service is running
    		  And the task exists
        	And the course project exists
        	And the task is inside the project
            	When I remove the task from the task list in the project
                Then I should no longer see the task with a relationship to that project


Scenario: remove task from the wrong category #Error Flow
	I should receive an error
		Given Service is running
			And the task exists
			And The course category exists
			And the task is inside the category
				When the task is removed from the wrong category
				Then I should receive an error message 
