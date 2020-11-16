package io.cucumber.cucumber_java8; 

import org.junit.Assert;


import io.cucumber.java.After;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import apiDependencies.HttpMessageSender;
import apiDependencies.HttpResponseDetails;
public class Step349Def {
	
	private static HashMap headers = new HashMap<>();
	private static HttpMessageSender http = new HttpMessageSender("http://localhost:4567/gui");
	ProcessBuilder pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\runTodoManagerRestAPI-1.5.5.jar");
	private static Process p;


	//FEATURE 3 START
/*
	@Given("^Service is running$")
	public void service_is_running() throws Throwable {

		try {

			final HttpResponseDetails responseGet = http.send("/todos", "GET", headers, "");
			Assert.assertEquals(200, responseGet.statusCode);
			System.out.println("Localhost still running");
		}catch(Exception e) {
			System.out.println("Not connected to localhost");
			//Assert.fail();

		}
	}*/
	

	@And("^the task exists and is not marked as done$")
	public void the_task_exists_and_is_not_marked_as_done() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/todos", "POST", headers, "{\"title\":\"TestingTodo\",\"description\":\"Made for Testing\",\"doneStatus\":false}");
		//We know ID will be 3
		assertEquals(201, responsePost.statusCode);

	}

	@And("^the task exists and is marked as done$")
	public void the_task_exists_and_is_marked_as_done() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/todos", "POST", headers, 
						"{\"title\":\"TestingTodo\",\"description\":\"Made for Testing\",\"doneStatus\":true}");
		//We know ID will be 3
		assertEquals(201, responsePost.statusCode);

	}

	@When("^I mark the task as done$")
	public void i_mark_the_task_as_done() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/todos/3", "POST", headers, 
						"{\"title\":\"TestingTodo\",\"description\":\"Made for Testing\",\"doneStatus\":true}");
		assertEquals(200, responsePost.statusCode);

	}

	@Then("^The task's doneStatus should be True$")
	public void the_tasks_donestatus_should_be_true() throws Throwable {
		// This will return all responses with a done status of true
		final HttpResponseDetails responseGet = http.send("/todos?doneStatus=true", "GET", headers, "");
		assertEquals(200, responseGet.statusCode);
		assertTrue(responseGet.body.contains("TestingTodo"));
	}

	@But("^the task does not exist$")
	public void the_task_does_not_exist() throws Throwable {
		final HttpResponseDetails responseGet =
				http.send("/todos/4", "GET", headers, "");
		// if it exists
		if (responseGet.statusCode != 404) { 
			final HttpResponseDetails responseDel =
					http.send("/todos/4", "DELETE", headers, "");
		}
	}

	@When("^I mark the task that does not exist as done$")
	public void i_mark_the_task_that_does_not_exist_as_done() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/todos/3", "POST", headers, 
						"{\"title\":\"nonexistentTodo\",\"description\":\"This doesnt exist\",\"doneStatus\":true}");
		assertEquals(404, responsePost.statusCode);

	}

	@Then("^I should receive an error message from the post$")
	public void i_should_receive_an_error_message_from_the_post() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/todos/3", "POST", headers, 
						"{\"title\":\"nonexistentTodo\",\"description\":\"This doesnt exist\",\"doneStatus\":true}");
		assertEquals(404, responsePost.statusCode);
		assertTrue(responsePost.body.contains("errorMessages"));

	}

	// FEATURE 3 END


	//FEATURE 4 START

	@And("^The course category exists$")
	public void the_course_category_exists() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/categories/2", "POST", headers,
						"{\"title\":\"CATEGORY429\",\"description\":\"Category429\"}");
		assertEquals(200, responsePost.statusCode);
	}

	@And("^the task exists$")
	public void the_task_exists() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/todos", "POST", headers, 
						"{\"title\":\"TestingTodo\",\"description\":\"Made for Testing\",\"doneStatus\":false}");
		//We know ID will be 3
		String[] id = responsePost.body.split("\"");
		assertEquals(201, responsePost.statusCode);

	}

	@And("^the task is inside the category$")
	public void the_task_is_inside_the_category() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/categories/2/todos", "POST", headers,
						"{\"id\":\"3\"}");
		assertEquals(201, responsePost.statusCode);

	}


	@When("^I remove the task from the to do list in the category$")
	public void i_remove_the_task_from_the_to_do_list_in_the_category() throws Throwable {
		final HttpResponseDetails responseDel = http.send("/categories/2/todos/3", "DELETE", headers, "");
		assertEquals(200, responseDel.statusCode);

	}
	@Then("^I should no longer see that task in the todo list that has a relationship to that category$")
	public void i_should_no_longer_see_that_task_in_the_todo_list_that_has_a_relationship_to_that_category() throws Throwable {
		final HttpResponseDetails responseGet = http.send("/categories/2/todos", "GET", headers, "");
		assertFalse(responseGet.body.contains("TestingTodo"));
	}

	@And("^the course project exists$")
	public void the_course_project_exists() throws Throwable {
		final HttpResponseDetails responsePost = //create two classes with completed tag true
				http.send("/projects", "POST", headers,
						"{\"title\":\"Project429\",\"description\":\"Project429\",\"completed\":false}"); 
		assertEquals(201, responsePost.statusCode);
	}

	@And("^the task is inside the project$")
	public void the_task_is_inside_the_project() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/projects/2/tasks", "POST", headers,  "{\"id\":\"3\"}");
		assertEquals(201, responsePost.statusCode);


	}

	@When("^I remove the task from the task list in the project$")
	public void i_remove_the_task_from_the_task_list_in_the_project() throws Throwable {
		final HttpResponseDetails responseDel = http.send("/projects/2/tasks/3", "DELETE", headers, "");
		assertEquals(200, responseDel.statusCode);

	}


	@Then("^I should no longer see the task with a relationship to that project$")
	public void i_should_no_longer_see_the_task_with_a_relationship_to_that_project() throws Throwable {
		final HttpResponseDetails responseGet = http.send("/projects/2/tasks", "GET", headers, "");
		assertEquals(200, responseGet.statusCode);
		assertFalse(responseGet.body.contains("TestingTodo"));
	}



	@When("^the task is removed from the wrong category$")
	public void i_remove_the_task_from_the_to_do_list_in_the_category_that_does_not_exist() throws Throwable {
		final HttpResponseDetails responseDel =
				http.send("/categories/20/todos/3", "DELETE", headers, ""); 
		assertEquals(400, responseDel.statusCode);
	}


	@Then("^I should receive an error message$")
	public void i_should_receive_an_error_message() throws Throwable {
		final HttpResponseDetails responseDel =
				http.send("/categories/20/todos/3", "DELETE", headers, "");
		assertEquals(400, responseDel.statusCode);
		assertTrue(responseDel.body.contains("errorMessages"));

	}


	//FEATURE 4 DONE HERE


	//FEATURE 9 START

	@And("^The LOW category exists$")
	public void the_low_category_exists() throws Throwable {
		// update instead of create to know IDs of cateogry
		final HttpResponseDetails responsePost =
				http.send("/categories/1", "POST", headers,
						"{\"title\":\"LOW\",\"description\":\"This is for low importance tasks\"}");
		assertEquals(200, responsePost.statusCode);
		
	}

	@And("^the HIGH category exists$")
	public void the_high_category_exists() throws Throwable {
		// update instead of create to know IDs of cateogry
		final HttpResponseDetails responsePost =
				http.send("/categories/2", "POST", headers,
						"{\"title\":\"HIGH\",\"description\":\"This is for tasks of HIGH imoprtance\"}");
		assertEquals(200, responsePost.statusCode);
	}

	//task exists

	@And("^the task is in the LOW category$")
	public void the_task_is_in_the_low_category() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/categories/1/todos", "POST",headers, "{\"id\":\"3\"}");
		assertEquals(201, responsePost.statusCode);
		
	}

	@When("^I add it to the HIGH category then remove it from the LOW category$")
	public void i_add_it_to_the_high_category_then_remove_it_from_the_low_category() throws Throwable {
			final HttpResponseDetails responsePost =
					http.send("/categories/2/todos", "POST",headers, "{\"id\":\"3\"}");
			assertEquals(201, responsePost.statusCode);
			
			final HttpResponseDetails responseDelete =
					http.send("/categories/1/todos/3", "DELETE", headers, "");
			assertEquals(200, responseDelete.statusCode);

	}

	@Then("^I should see the task in the HIGH category$")
	public void i_should_see_the_task_in_the_high_category() throws Throwable {
		final HttpResponseDetails responseGet = http.send("/categories/2/todos", "GET", headers, "");
		assertEquals(200, responseGet.statusCode);
		assertTrue(responseGet.body.contains("TestingTodo"));
		
	}


	@When("^I add it to the LOW category$")
	public void i_add_it_to_the_low_category() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/categories/2/todos", "POST",headers, "{\"id\":\"3\"}");
		assertEquals(201, responsePost.statusCode);
	}

	@Then("^I should still see the task inside the LOW category$")
	public void i_should_still_see_the_task_inside_the_low_category() throws Throwable {
		final HttpResponseDetails responseGet = http.send("/categories/1/todos", "GET", headers, "");
		assertEquals(200, responseGet.statusCode);
		assertTrue(responseGet.body.contains("TestingTodo"));
	}


	@But("^the EMERGENCY category does not exist$")
	public void the_emergency_category_does_not_exist() throws Throwable {
		final HttpResponseDetails responseGet = http.send("/categories/3", "GET", headers, "");
		if (responseGet.statusCode != 404) { //if it does remove it
			final HttpResponseDetails responseDel =
					http.send("/categories/3", "DELETE", headers, "");
		}
		
	}


	@When("^I add the task to the EMERGENCY priority$")
	public void i_add_the_task_to_the_emergency_priority() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/categories/3/todos", "POST",headers, "{\"id\":\"3\"}");
		assertEquals(404, responsePost.statusCode);

	}

	@Then("^I should see an error message$")
	public void i_should_see_an_error_message() throws Throwable {
		final HttpResponseDetails responsePost =
				http.send("/categories/3/todos", "POST",headers, "{\"id\":\"3\"}");
		assertEquals(404, responsePost.statusCode);
		assertTrue(responsePost.body.contains("errorMessages"));

	}
	
	// FEATURE 9 END



















}