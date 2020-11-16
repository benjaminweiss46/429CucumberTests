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
@CucumberOptions(
        glue = {"io,cucumber.cucumber_java8.features"},
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class Feature1Step {
    private Thingifier todoManager;
    private static HashMap header = new HashMap<>();
    private String taskID;
    private String lowID;
    private String highID;
    private HttpApiResponse errorResponse;

    private static HttpMessageSender http = new HttpMessageSender("http://localhost:4567/gui");
    ProcessBuilder pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\wrunTodoManagerRestAPI-1.5.5.jar");
    public static Process p;

    @Given("^The Service is running$")
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
                p = pb.start(); //run the jar
                System.out.println("System Running");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                throw new Exception("Error has occurred starting the host.");
            }
        }

        todoManager = TodoManagerModel.definedAsThingifier();
    }

    @When("^I add the task to the LOW category$")
    public void iAddTheTaskToTheLOWCategory() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/"+taskID+"/categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"guid\":\""+lowID+"\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @When("^I add the task to the High category$")
    public void iAddTheTaskToTheHighCategory() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/"+taskID+"/categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"guid\":\""+highID+"\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @When("^I add the task to the IMPORTANT category$")
    public void iAddTheTaskToTheIMPORTANTCategory() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/"+taskID+"/categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"guid\":\"IMPORTANT\"}");

        errorResponse = new ThingifierHttpApi(todoManager).post(request);
    }

    @Then("^I should see the task in the to do list with a relationship to LOW$")
    public void iShouldSeeTheTaskInTheToDoListWithARelationshipToLOW() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/"+taskID+"/categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("LOW"), true);
    }

    @Then("^I should see the task in the to do list with a relationship to HIGH$")
    public void iShouldSeeTheTaskInTheToDoListWithARelationshipToHIGH() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos/"+taskID+"/categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("HIGH"), true);
    }

    @Then("^I should see and error message explaining the category does not exist$")
    public void iShouldSeeAndErrorMessageExplainingTheCategoryDoesNotExist() throws Throwable {
        System.out.println(errorResponse.getStatusCode());
        System.out.println(errorResponse.getBody());

        Assert.assertEquals(errorResponse.getStatusCode(), 404);
    }

    @And("^the LOW category exists$")
    public void theLOWCategoryExists() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"LOW\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);

        JSONObject obj = new JSONObject(response.getBody());
        lowID = obj.getString("guid");
    }

    @And("^The task exists$")
    public void theTaskExists() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("todos");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"Todo to categorize\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);

        JSONObject obj = new JSONObject(response.getBody());
        taskID = obj.getString("guid");
    }

    @And("^The HIGH category exists$")
    public void theHIGHCategoryExists() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"HIGH\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);

        JSONObject obj = new JSONObject(response.getBody());
        highID = obj.getString("guid");
    }

    @But("^the IMPORTANT category does not exist$")
    public void theIMPORTANTCategoryDoesNotExist() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("IMPORTANT"), false);
    }

//    @After
//    public void cleanAfterScenario() {
//        try {
//            p.destroyForcibly();
//            final HttpResponseDetails response = //shutdown system
//                    http.send("/shutdown", "GET", header, "");
//        }
//        catch (Exception e1) {
//            System.out.println("successful shutdown");
//        }
//    }
}