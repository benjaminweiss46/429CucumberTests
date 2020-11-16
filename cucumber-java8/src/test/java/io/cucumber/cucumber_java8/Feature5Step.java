package io.cucumber.cucumber_java8;

import apiDependencies.HttpMessageSender;
import apiDependencies.HttpResponseDetails;
import io.cucumber.java.en.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Assert;
import org.junit.runner.RunWith;
import uk.co.compendiumdev.casestudy.todomanager.TodoManagerModel;
import uk.co.compendiumdev.casestudy.todomanager.http_api.HeadersSupport;
import uk.co.compendiumdev.thingifier.Thingifier;
import uk.co.compendiumdev.thingifier.api.http.HttpApiRequest;
import uk.co.compendiumdev.thingifier.api.http.HttpApiResponse;
import uk.co.compendiumdev.thingifier.api.http.ThingifierHttpApi;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(snippets = CucumberOptions.SnippetType.CAMELCASE)
public class Feature5Step {
    private Thingifier todoManager;
    private HttpApiResponse responseError;

    private static HttpMessageSender http = new HttpMessageSender("http://localhost:4567/gui");
    ProcessBuilder pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\runTodoManagerRestAPI-1.5.5.jar");

    @Given("^the service is running$")
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

    @When("^I add the course to categories$")
    public void iAddTheCourseToCategories() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"ClassBeingAdded\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @When("^I add the course to projects$")
    public void iAddTheCourseToProjects() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("projects");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"ClassBeingAdded\"}");

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).post(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Then("^I should see the course in the categories$")
    public void iShouldSeeTheCourseInTheCategories() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("ClassBeingAdded"), true);
    }

    @Then("^I should see the course in the projects$")
    public void iShouldSeeTheCourseInTheProjects() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("projects");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("ClassBeingAdded"), true);
    }

    @Then("^I should see an error message explaining the course is missing a title$")
    public void iShouldSeeAnErrorMessageExplainingTheCourseIsMissingATitle() throws Throwable {
        System.out.println(responseError.getStatusCode());
        System.out.println(responseError.getBody());

        Assert.assertEquals(responseError.getStatusCode(), 400);
    }

    @And("^the given course category does not exist$")
    public void theGivenCourseCategoryDoesNotExist() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("ClassBeingAdded"), false);
    }

    @And("^the given course project does not exist$")
    public void theGivenCourseProjectDoesNotExist() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("projects");
        request.getHeaders().putAll(HeadersSupport.acceptJson());

        final HttpApiResponse response = new ThingifierHttpApi(todoManager).get(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Assert.assertEquals(response.getBody().contains("ClassBeingAdded"), false);
    }

    @When("^I add the course to categories but the title field is missing$")
    public void iAddTheCourseToCategoriesButTheTitleFieldIsMissing() throws Throwable {
        HttpApiRequest request = new HttpApiRequest("categories");
        request.getHeaders().putAll(HeadersSupport.acceptJson());
        request.getHeaders().putAll(HeadersSupport.containsJson());

        request.setBody("{\"title\":\"\"}");

        responseError = new ThingifierHttpApi(todoManager).post(request);
    }
}