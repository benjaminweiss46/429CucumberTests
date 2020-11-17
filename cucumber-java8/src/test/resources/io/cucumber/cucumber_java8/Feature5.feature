Feature: Create list for class
As a student, I create a to do list for a new class I am taking, so I can manage course work.

	Scenario: Create a to do list for a new course #Normal Flow
    I should see that a new category has been created for the course.
    	Given The Service is running
        And the given course category does not exist
        And the given course project does not exist
        	When I add the course to categories
            Then I should see the course in the categories
            
    Scenario: Create a to do list for a new course #Alternate Flow
    I should see that a new project has been created for the course.
    	Given The Service is running
        And the given course category does not exist
        And the given course project does not exist
        	When I add the course to projects
            Then I should see the course in the projects

	Scenario: Create a to do list for a course without a title #Error Flow
    I should receive an error
    	Given The Service is running
        And the given course category does not exist
        And the given course project does not exist
        	When I add the course to categories but the title field is missing
            Then I should see an error message explaining the course is missing a title