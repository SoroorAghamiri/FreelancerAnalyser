package controllers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import play.test.WithServer;
import play.test.*;
import static play.test.Helpers.*;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;
import static org.hamcrest.CoreMatchers.*;


public class HomeControllerTest extends WithApplication {

    @Inject
    Application application;

    /**
     * setting up testing application before each test
     * @author Haitham Abdel-Salam
     */
    @Before
    public void setup() {
        Module testModule =
                new AbstractModule() {
                    @Override
                    public void configure() {
                    }
                };

        GuiceApplicationBuilder builder =
                new GuiceApplicationLoader()
                        .builder(new ApplicationLoader.Context(Environment.simple()))
                        .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);

        Helpers.start(application);
    }


    /**
     * stops application after each test
     * @author Haitham Abdel-Salam
     */
    @After
    public void teardown() {
        Helpers.stop(application);
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test the search term from the url
     */
    @Test
    public void testGetSearchTerm(){
        Http.RequestBuilder request = Helpers.fakeRequest().method(GET).uri("/get-search-term/java");
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test method for getSearchTerm invalid response
     * @author Kazi Asif Tanim
     */
    @Test
    public void testGetSearchTermInvalid(){
        Http.RequestBuilder request = Helpers.fakeRequest().method(GET).uri("/get-search-term/");
        Result result = route(application, request);
        assertEquals(NOT_FOUND, result.status());
    }

    /**
     * Test method for readability in HomeController
     * @author Kazi Asif Tanim
     */
    @Test
    public void testReadablity() throws UnsupportedEncodingException {
        Http.RequestBuilder request = Helpers.fakeRequest().method(GET).uri("/readablity/" + URLEncoder.encode("I am a representative of an open source first person shooter game. We are looking for a programmer t", StandardCharsets.UTF_8.toString()));
        Result result = Helpers.route(application, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test method for readability invalid response
     * @author Kazi Asif Tanim
     */
    @Test
    public void testReadablityInvalid() throws UnsupportedEncodingException {
        Http.RequestBuilder request = Helpers.fakeRequest().method(GET).uri("/readablity/" + URLEncoder.encode("", StandardCharsets.UTF_8.toString()));
        Result result = Helpers.route(application, request);
        assertEquals(NOT_FOUND, result.status());
    }

    /**
     * Tests get skill search from a get request
     * @author soroor
     */
    @Test
    public void testGetSkillSearch(){
    	Call action = routes.HomeController.getSkillSearch("Java");
        Http.RequestBuilder request = Helpers.fakeRequest(action);
        Result response = Helpers.route(application, request);
        assertEquals(response.status(), OK);
    }

    /**
     * @author Haitham Abdel-Salam
     * Junit test for getWordStats
     * Test method for {@link controllers.HomeController#getWordStats(String)}.
     */
    @Test
    public void testGetWordsStats() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/stats/unity");
        Result result = route(application,request);
        assertEquals(OK, result.status());
    }

    /**
     * @author Haitham Abdel-Salam
     * Junit test for getWordStats
     * Test method for {@link controllers.HomeController#getSingleProjectStats(String)} (Integer)}.
     */
    @Test
    public void testGetSingleProjectStats() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/stats/single/3324623");
        Result result = route(application,request);
        assertEquals(OK, result.status());
    }

    /**
     * Covering equivalent classes for getSingleProjectStats. Invalid non integer values mix of characters and integers
     * Test method for {@link controllers.HomeController#getSingleProjectStats(String)} (Integer)}.
     * @author Haitham Abdel-Salam
     */
    @Test
    public void testGetSingleProjectStatsInvalidMix() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/stats/single/xyxx123");
        Result result = route(application,request);
        assertEquals(NOT_FOUND, result.status());
    }

    /**
     * Covering equivalent classes for getSingleProjectStats. Invalid non integer only characters
     * Test method for {@link controllers.HomeController#getSingleProjectStats(String)} (Integer)}.
     * @author Haitham Abdel-Salam
     */
    @Test
    public void testGetSingleProjectStatsInvalidCharacters() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/stats/single/abcdefg");
        Result result = route(application,request);
        assertEquals(NOT_FOUND, result.status());
    }

    /**
     * Covering equivalent classes for getSingleProjectStats. Invalid no id in URL
     * Test method for {@link HomeController#getSingleProjectStatsNotFound()}.
     * @author Haitham Abdel-Salam
     */
    @Test
    public void testGetSingleProjectStatsInvalidNoURL() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/stats/single/");
        Result result = route(application,request);
        assertEquals(NOT_FOUND, result.status());
    }
}
