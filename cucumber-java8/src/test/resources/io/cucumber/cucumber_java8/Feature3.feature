Feature: Change the done status of a task in a category.
As a student, I mark a task as done on my course to do list, so I can track my accomplishments.

Scenario: Mark a task as done in my category #Normal Flow

  I should see the task added to course to do list.
    Given Service is running
        And the task exists and is not marked as done
            When I mark the task as done
            Then The task's doneStatus should be True 

Scenario: Mark a task that is already done as done in my category #Alternative Flow

    I should see no change
        Given Service is running
            And the task exists and is marked as done
                When I mark the task as done
                Then The task's doneStatus should be True


Scenario: Mark a task done status with a value that is not a boolean #Error Flow

    I should receive an error
        Given Service is running
            But the task does not exist
                When I mark the task that does not exist as done
                Then I should receive an error message from the post
     