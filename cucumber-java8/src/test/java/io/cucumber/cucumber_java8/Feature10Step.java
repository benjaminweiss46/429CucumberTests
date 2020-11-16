package io.cucumber.cucumber_java8;

import apiDependencies.HttpMessageSender;
import apiDependencies.HttpResponseDetails;
import io.cucumber.java.en.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.runner.RunWith;
import uk.co.compendiumdev.casestudy.todomanager.TodoManagerModel;
import uk.co.compendiumdev.casestudy.todomanager.http_api.HeadersSupport;
import uk.co.compendiumdev.thingifier.Thingifier;
import uk.co.compendiumdev.thingifier.api.http.HttpApiRequest;
import uk.co.compendiumdev.thingifier.api.http.HttpApiResponse;
import uk.co.compendiumdev.thingifier.api.http.ThingifierHttpApi;

import java.io.IOException;
import java.util.HashMap;

@RunWith(Cucumber.class)
@CucumberOptions(snippets = CucumberOptions.SnippetType.CAMELCASE)
public class Feature10Step {
    private Thingifier todoManager;
    private String taskID;
    private static HashMap header = new HashMap<>();

    private static HttpMessageSender http = new HttpMessageSender("http://localhost:4567/gui");
    ProcessBuilder pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\runTodoManagerRestAPI-1.5.5.jar");

    @Given("^The service is running$")
    public void theServiceIsRunning() throws Throwable {
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
                Feature1Step.p = pb.start(); //run the jar
                System.out.println("System Running");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                throw new Exception("Error has occurred starting the host.");
            }
        }

        todoManager = TodoManagerModel.definedAsThingifier();
    }

    @When("^I add a description to the task$")
    public void iAddADescriptionToTheTask() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/" + taskID);
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"description\":\"New description added!\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Then("^I should see the task description change$")
    public void iShouldSeeTheTaskDescriptionChange() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/" + taskID);
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("New description added!"), true);
    }

    @Then("^I should see the description appear for that task$")
    public void iShouldSeeTheDescriptionAppearForThatTask() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/" + taskID);
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("New description added!"), true);
    }

    @Then("^I should see an error message explaining that the task does not exist$")
    public void iShouldSeeAnErrorMessageExplainingThatTheTaskDoesNotExist() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/" + taskID);
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @And("^task exists$")
    public void taskExists() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"Todo to describe\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);

        JSONObject obj = new JSONObject(response.getBody());
        taskID = obj.getString("guid");
    }

    @And("^description for task exists$")
    public void descriptionForTaskExists() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/" + taskID);
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"description\":\"Task with description\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        JSONObject obj = new JSONObject(response.getBody());
        Assert.assertEquals(obj.isNull("description"), false);
    }

    @But("^description is not present$")
    public void descriptionIsNotPresent() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/" + taskID);
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        JSONObject obj = new JSONObject(response.getBody());

        Assert.assertEquals(obj.isNull("description"), true);
    }

    @But("^task does not exist$")
    public void taskDoesNotExist() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("Todo to describe"), false);
    }
}