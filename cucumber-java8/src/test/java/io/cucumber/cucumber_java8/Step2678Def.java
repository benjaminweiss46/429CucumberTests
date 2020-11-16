package io.cucumber.cucumber_java8;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import org.junit.Assert;

import apiDependencies.HttpMessageSender;
import apiDependencies.HttpResponseDetails;


public class Step2678Def {
		private static HashMap header = new HashMap<>();
		private static HttpMessageSender http = new HttpMessageSender("http://localhost:4567/gui");
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\runTodoManagerRestAPI-1.5.5.jar");
		private static Process p;
		
		//START OF FEATURE 2: Add a task to course to do list
		
	 	@Given("^Service is running$")
	    public void service_is_running() throws Throwable {
	 		System.out.println("check that localhost is running\n");
	        try {
		        final HttpResponseDetails response = //send get to server
		                http.send("", "GET");
		        System.out.println("Local host running");
	        }	
			catch (Exception e) { //not currently online
				System.out.println("Not connected to localhost");
				//Run jar file
				System.out.println("running the jar");
		        try {
					p = pb.start(); //run the jar
					System.out.println("System Running");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Assert.fail();
				}
			}
	    }
	    @And("^The course exists$")
	    public void the_course_exists() throws Throwable {
	    	final HttpResponseDetails response = //create a course ECSE429 :)
	                http.send("/projects", "POST", header,
	                        "{\"title\":\"ECSE429\",\"description\":\"Course ECSE429\",\"completed\":true}");

	        Assert.assertEquals(201, response.statusCode);
	        
	    }

	    @When("^I add a task to the course to do list$")
	    public void i_add_a_task_to_the_course_to_do_list() throws Throwable { //Add a todo to our created course
			final HttpResponseDetails response =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");

	        Assert.assertEquals(201, response.statusCode);
	        String[] id = (response.body).split("\"");
			final HttpResponseDetails response2 =
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response.statusCode);
	    }


	    @Then("^I should see the task in the to do list with a relationship to the course$") //look for the task you added
	    public void i_should_see_the_task_in_the_to_do_list_with_a_relationship_to_the_course() throws Throwable {
			final HttpResponseDetails response =
	                http.send("/todos/3/tasksof", "GET", header, "");
			
	        Assert.assertEquals(200, response.statusCode);
	        
	        String[] output = (response.body).split("title");
	        if (!output[0].contains("2")) {
	        	Assert.fail();
	        }
	    }
	    @Given("^course does not exist$")
	    public void course_does_not_exist() throws Throwable { //ensure course ddoesn't exist
			final HttpResponseDetails response =
		                http.send("/projects/3", "GET", header, "");
			if (response.statusCode != 404) { //if it does remove it
				System.out.println("Course with id 3 exists must delete for this test");
				final HttpResponseDetails response2 =
		                http.send("/projects/3", "DELETE", header, "");
			}
	    	System.out.println("Course with id 3 does not currently exist->Good");
	    }

	    @When("^I try to add a task to the course to do list$")
	    public void i_try_to_add_a_task_to_the_course_to_do_list() throws Throwable { //try to add a task to course todo list
			final HttpResponseDetails response =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"Error Test\",\"description\":\"\",\"doneStatus\":false}");

	        Assert.assertEquals(201, response.statusCode);
	        String[] id = (response.body).split("\"");
			final HttpResponseDetails response2 = //error since that course does not exist
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"4\"}");
			Assert.assertEquals(404, response2.statusCode);
	    }
	    
	    @Then("^task will not be related to the selected course$")
	    public void task_will_not_be_related_to_the_selected_course() throws Throwable {
			final HttpResponseDetails response = //todo is not related to any projects
	                http.send("/todos/3/tasksof", "GET", header, "");
			if (!response.body.contains("[]")) {
				Assert.fail();
			}
	    }
	    @Given("^Two courses exists$")
	    public void two_courses_exists() throws Throwable {
	    	final HttpResponseDetails response = //create two courses for testing
	                http.send("/projects", "POST", header,
	                        "{\"title\":\"ECSE429\",\"description\":\"Course ECSE429\"}");
	    	
	    	Assert.assertEquals(201, response.statusCode);

	    	final HttpResponseDetails response2 =
	                http.send("/projects", "POST", header,
	                        "{\"title\":\"ECSE420\",\"description\":\"Course ECSE420\"}");

	        Assert.assertEquals(201, response2.statusCode);
	    }
	    
	    @When("^I add a task that is related to one course to the to do list of another$")
	    public void i_add_a_task_that_is_related_to_one_course_to_the_to_do_list_of_another() throws Throwable {
	    	final HttpResponseDetails response = //create task
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");

	        Assert.assertEquals(201, response.statusCode);
	        
	        String[] id = (response.body).split("\"");
	        
			final HttpResponseDetails response2 = //add to one course
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response2.statusCode);
			
			final HttpResponseDetails response3 = //then add to another
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"3\"}");
			Assert.assertEquals(201, response3.statusCode);
	    }
	    
	    @Then("^I should see the task in the to do list with a relationship to both courses$")
	    public void i_should_see_the_task_in_the_to_do_list_with_a_relationship_to_both_courses() throws Throwable {
			final HttpResponseDetails response = //becomes related to both courses
	                http.send("/todos/3/tasksof", "GET", header, "");
			
	        Assert.assertEquals(200, response.statusCode);
	        
	        if (!(response.body.contains("ECSE429")) && (response.body.contains("ECSE420"))) {
	        	Assert.fail();
	        }


	    }
	    //END OF FEATURE 2: Add a task to course to do list
	    
	    //START OF FEATURE 6: Find all incomplete tasks related to a class
	    
	    @And("^To dos that are tasksof the course and doneStatus=False exist$")
	    public void to_dos_that_are_tasksof_the_category_and_donestatusfalse_exist() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");
	    	String[] id = (response.body).split("\"");
	    	Assert.assertEquals(201, response.statusCode);
	    	final HttpResponseDetails response1 =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo2\",\"description\":\"Created by testing\",\"doneStatus\":true}");
	    	String[] id1 = (response1.body).split("\"");
	        Assert.assertEquals(201, response1.statusCode);
			final HttpResponseDetails response2 =
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response2.statusCode);
			final HttpResponseDetails response3 =
	                http.send("/todos/"+id1[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response3.statusCode);
			//Added two todos to ECSE 429 one complete and one incomplete, to fulfill condition
	    }

	    @But("^To dos that are tasksof the course and doneStatus=False dont exist$")
	    public void to_dos_that_are_tasksof_the_course_and_donestatusfalse_dont_exist() throws Throwable {
	    	// just added the course and haven't added any todos in this scenarios so this is already achieved by cleaning 
	    	//up after each scenario but will check. If any new todo's exist other than 2 initial they could be related to course
			final HttpResponseDetails response =
	                http.send("projects/2/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
			if (!response.body.contains("[]")) {
				Assert.fail();
			}
	    }

	    @But("^Another course does$")
	    public void another_course_does() throws Throwable {
			final HttpResponseDetails response = //the course we are trying to get doesn't exist but another course does exist
	                http.send("projects/1", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
	    }
	    @When("^I send a request to see unfinished tasks$") //When I try to see tasks of course 2 will give me tasks of course 1 since 2 doesnt exist
	    public void i_send_a_request_to_see_unfinished_tasks() throws Throwable {
			final HttpResponseDetails response =
	                http.send("projects/2/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
			
	    }

	    @When("^I try to see the unfinished tasks$") 
	    public void i_try_to_see_the_unfinished_tasks() throws Throwable {
			final HttpResponseDetails response = //getting all unfinished tasks for project 3
	                http.send("projects/3/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
	    }

	    @Then("^I should see all incomplete tasks of the course$")
	    public void i_should_see_all_incomplete_tasks_of_the_category() throws Throwable {
			final HttpResponseDetails response = //looking if incomplete tasks of project 2 are returned
	                http.send("projects/2/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
			if (!response.body.contains("mytodo")) {
				Assert.fail();
			}
	    }

	    
	    @Then("^I should see no tasks$")
	    public void i_should_see_no_tasks() throws Throwable {
			final HttpResponseDetails response = //looking at incomplete tasks of project 2 but none are returned
	                http.send("projects/2/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
			if (!response.body.contains("[]")) { //empty list
				Assert.fail();
			}
	    }

	    @Then("^I will recieve the incomplete tasks for another course$") 
	    public void i_will_recieve_the_incomplete_tasks_for_another_course() throws Throwable { //When I try to see tasks of course 2 will give me tasks of course 1 since 2 doesnt exist
			final HttpResponseDetails response =
	                http.send("projects/3/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response.statusCode);
			final HttpResponseDetails response2 =
	                http.send("projects/1/tasks?doneStatus=false", "GET", header, "");
			Assert.assertEquals(200, response2.statusCode);
			if (!response.body.equals(response2.body)) { //proof that its giving me the tasks for 1 in actuality 
				Assert.fail();
			}
	    }

	    
	    
	    //END OF FEATURE 6: Find all incomplete tasks related to a class
	    
	    //START OF FEATURE 7 :Remove to do list from a class
	    
	    @And("^To dos that are tasksof the course exist$") //Add a two tasks one done and one not down to a course
	    public void to_dos_that_are_tasksof_the_course_exist() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");
	    	String[] id = (response.body).split("\"");
	    	Assert.assertEquals(201, response.statusCode);
	    	final HttpResponseDetails response1 =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo2\",\"description\":\"Created by testing\",\"doneStatus\":true}");
	    	String[] id1 = (response1.body).split("\"");
	        Assert.assertEquals(201, response1.statusCode);
			final HttpResponseDetails response2 =
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response2.statusCode);
			final HttpResponseDetails response3 =
	                http.send("/todos/"+id1[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response3.statusCode);
	    }

	    @And("^The course completed tag is true$") //Check if the courses tag is completed
	    public void the_course_completed_tag_is_true() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/projects/2", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	String[] id = (response.body).split(",");
	    	if (!id[2].contains("true")) {
	    		Assert.fail();
	    	}
	    }	
	    

	    @When("^I remove the to dos of the class$") //remove the todos of the class we made for testing
	    public void i_remove_the_to_dos_of_the_class() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/projects/2/tasks", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	String[] id = (response.body).split("\"");
	    	final HttpResponseDetails response2 =
	                http.send("/projects/2/tasks/"+id[5], "DELETE", header, "");
	    	Assert.assertEquals(200, response2.statusCode);
	    	final HttpResponseDetails response3 =
	                http.send("/projects/2/tasks/"+id[27], "DELETE", header, "");
	    	Assert.assertEquals(200, response3.statusCode);
	    	
	    }

	    @Then("^I should no longer see those tasks in the to do list with a relationship to the course$") //no more todos related to the course all removed
	    public void i_should_no_longer_see_those_tasks_in_the_to_do_list_with_a_relationship_to_the_course() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/projects/2/tasks", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	if (!response.body.contains("[]")) {
	    		Assert.fail();
	    	}
	    }
	    

	    @But("^The to dos that are tasksof the course are 0$") 
	    public void the_to_dos_that_are_tasksof_the_category_are_0() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/projects/2/tasks", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	if (!response.body.contains("[]")) { //makesure the course has no todos related to it
	    		Assert.fail();
	    	}
	    }
	    
	    @When("^I try to remove the to dos of the class$")
	    public void i_try_to_remove_the_to_dos_of_the_class() throws Throwable {
	    	final HttpResponseDetails response = //can't remove tasks from course since none exist
	                http.send("/projects/2/tasks/1", "DELETE", header, "");
	    	Assert.assertEquals(404, response.statusCode);
	    }

	    @Then("^I get an error message$")
	    public void i_get_an_error_message() throws Throwable {
	    	final HttpResponseDetails response = //returns an error message explaining that the course does not exist
	                http.send("/projects/2/tasks/1", "DELETE", header, "");
	    	if (!response.body.contains("errorMessages")) {
	    		Assert.fail();
	    	}
	    }
	    
	    @Given("^Multiple completed classes$")
	    public void multiple_completed_classes() throws Throwable {
	    	final HttpResponseDetails response = //create two classes with completed tag true
	                http.send("/projects", "POST", header,
	                        "{\"title\":\"ECSE429\",\"description\":\"Course ECSE429\",\"completed\":true}");
	    	
	    	Assert.assertEquals(201, response.statusCode);

	    	final HttpResponseDetails response2 =
	                http.send("/projects", "POST", header,
	                        "{\"title\":\"ECSE420\",\"description\":\"Course ECSE420\",\"completed\":true}");

	        Assert.assertEquals(201, response2.statusCode);
	    }
	    
	    @And("^Each completed class has todo/s$")
	    public void each_completed_class_has_todos() throws Throwable {
	    	final HttpResponseDetails response = //add a todo to each one of our completed classes
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");
	    	String[] id = (response.body).split("\"");
	    	Assert.assertEquals(201, response.statusCode);
	    	final HttpResponseDetails response1 =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo2\",\"description\":\"Created by testing\",\"doneStatus\":true}");
	    	String[] id1 = (response1.body).split("\"");
	        Assert.assertEquals(201, response1.statusCode);
			final HttpResponseDetails response2 =
	                http.send("/todos/"+id[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"2\"}");
			Assert.assertEquals(201, response2.statusCode);
			final HttpResponseDetails response3 =
	                http.send("/todos/"+id1[3]+"/tasksof", "POST", header,
	                        "{\"id\":\"3\"}");
			Assert.assertEquals(201, response3.statusCode);
	    }
	    
	    @When("^I remove the to dos of any class that is completed$")
	    public void i_remove_the_to_dos_of_any_class_that_is_completed() throws Throwable {
	    	//comments to explain how I would do for any context
	    	final HttpResponseDetails response1 =
	                http.send("/projects?completed=true", "GET", header, ""); //get projects that are completed
	    	Assert.assertEquals(200, response1.statusCode);
	    	
	    	final HttpResponseDetails response =
	                http.send("/projects/2/tasks", "GET", header, ""); //get tasks related to the project, then delete them
	    	Assert.assertEquals(200, response.statusCode);
	    	final HttpResponseDetails response2 =
	                http.send("/projects/3/tasks", "GET", header, ""); //get tasks related to the project, then delete them
	    	Assert.assertEquals(200, response2.statusCode);

	    	final HttpResponseDetails response3 =
	                http.send("/projects/2/tasks/3", "DELETE", header, "");
	    	Assert.assertEquals(200, response3.statusCode);
	    	final HttpResponseDetails response4 =
	                http.send("/projects/3/tasks/4", "DELETE", header, "");
	    	Assert.assertEquals(200, response4.statusCode);
	    }

	    @Then("^I should no longer see todos related to any class that is completed$")
	    public void i_should_no_longer_see_todos_related_to_any_class_that_is_completed() throws Throwable {
	    	final HttpResponseDetails response =
	                http.send("/projects/2/tasks", "GET", header, ""); //get tasks related to the project, then delete them
	    	Assert.assertEquals(200, response.statusCode);
	    	final HttpResponseDetails response2 =
	                http.send("/projects/3/tasks", "GET", header, ""); //get tasks related to the project, then delete them
	    	Assert.assertEquals(200, response2.statusCode);
	    	if ((!response.body.contains("[]")) || (!response2.body.contains("[]"))) {
	    		Assert.fail();
	    	}
	    }

	    //END OF FEATURE 7 :Remove to do list from a class. 
	    //START OF FEATURE 8: Find all incomplete HIGH priority tasks

	    @Given("^The HIGH priority category exists$")
	    public void the_high_priority_category_exists() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories", "POST", header,
	                        "{\"title\":\"HIGH\",\"description\":\"PRIORITY HIGH\"}");
	    	Assert.assertEquals(201, response.statusCode);
	    }

	    @Given("^the HIGH priority category does not exist$")
	    public void the_high_priority_category_does_not_exist() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3", "GET", header, "");
	    	Assert.assertEquals(404, response.statusCode);
	    }
	    @And("^Todos that are associated to the HIGH priority category and have a doneStatus of false exist$")
	    public void todos_that_are_associated_to_the_high_priority_category_and_have_a_donestatus_of_false_exist() throws Throwable {
	    	final HttpResponseDetails response = //add a todo to each one of our completed classes
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");
	    	String[] id = (response.body).split("\"");
	    	Assert.assertEquals(201, response.statusCode);
	    	final HttpResponseDetails response1 =
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo2\",\"description\":\"Created by testing\",\"doneStatus\":true}");
	    	String[] id1 = (response1.body).split("\"");
	        Assert.assertEquals(201, response1.statusCode);
			final HttpResponseDetails response2 =
	                http.send("/categories/3/todos", "POST", header,
	                        "{\"id\":\""+id[3]+"\"}");
			Assert.assertEquals(201, response2.statusCode);
			final HttpResponseDetails response3 =
	                http.send("/categories/3/todos", "POST", header,
	                		"{\"id\":\""+id1[3]+"\"}");
			Assert.assertEquals(201, response3.statusCode);
	    }

	    @But("^Todos that are associated to the HIGH priority category and have a doneStatus of false do not exist$")
	    public void todos_that_are_associated_to_the_high_priority_category_and_have_a_donestatus_of_false_do_not_exist() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3/todos", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	if (!response.body.contains("[]")) {
	    		Assert.fail();
	    	}
	    }

	    @But("^Another category does exist$")
	    public void another_category() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	if (response.body.contains("[]")) {
	    		Assert.fail();
	    	}
	    	final HttpResponseDetails response2 = //add a todo to another category just to see
	                http.send("/todos", "POST", header,
	                        "{\"title\":\"mytodo\",\"description\":\"Created by testing\",\"doneStatus\":false}");
	    	String[] id = (response2.body).split("\"");
	    	Assert.assertEquals(201, response2.statusCode);
			final HttpResponseDetails response3 =
	                http.send("/categories/2/todos", "POST", header,
	                        "{\"id\":\""+id[3]+"\"}");
			Assert.assertEquals(201, response3.statusCode);
	    	
	    }
	    @When("^I send a request to see the incomplete todos of the HIGH priority category$")
	    public void i_send_a_request_to_see_the_incomplete_todos_of_the_high_priority_category() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3/todos?doneStatus=false", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    }

	    @When("^I try to see the unfinished todos in the HIGH priority category$")
	    public void i_try_to_see_the_unfinished_todos_in_the_high_priority_category() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3/todos?doneStatus=false", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    }

	    @Then("^I should see all incomplete tasks of the HIGH priority category$")
	    public void i_should_see_all_incomplete_tasks_of_the_high_priority_category() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3/todos?doneStatus=false", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	if (response.body.contains("[]")) {
	    		Assert.fail();
	    	}
	    }

	    @Then("^I should see no todos$")
	    public void i_should_see_no_todos() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3/todos?doneStatus=false", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	if (!response.body.contains("[]")) {
	    		Assert.fail();
	    	}
	    }

	    @Then("^I will recieve the incomplete todos of another category$")
	    public void i_will_recieve_the_incomplete_todos_of_another_category() throws Throwable {
	    	final HttpResponseDetails response = 
	                http.send("/categories/3/todos?doneStatus=false", "GET", header, "");
	    	Assert.assertEquals(200, response.statusCode);
	    	final HttpResponseDetails response2 = 
	                http.send("/categories/3/todos?doneStatus=false", "GET", header, "");
	    	Assert.assertEquals(200, response2.statusCode);
	    	if (!response2.body.equals(response.body)) {
	    		Assert.fail();
	    	}
	    }

	    //END OF FEATURE 8: Find all incomplete HIGH priority tasks
	    @After
	    public void cleanUpAfterScenario(){
	    	try {
	    		final HttpResponseDetails response = //shutdown system
	                http.send("/shutdown", "GET", header, "");
	    	}
	    	catch (Exception e1) {
	    		System.out.println("successful shutdown");
	    	}
	    	
	        try {
				p = pb.start(); //run it again
				System.out.println("restarted");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }



}
