package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test the search term from the url
     */
    @Test
    public void testGetSearchTerm(){
        Http.RequestBuilder request =  new Http.RequestBuilder()
                .method(GET)
                .uri("/get-search-term/java");
        Result result = route(app,request);
        assertEquals(OK, result.status());
    }

    /**
     * test methods for the owner details if it gives result ok
     */
    @Test
    public void testGetOwnerDetails(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/owner-details/27343515");
        Result result = route(app,request);
        assertEquals(OK, result.status());
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
        Result result = route(app,request);
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
        Result result = route(app,request);
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
        Result result = route(app,request);
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
        Result result = route(app,request);
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
        Result result = route(app,request);
        assertEquals(NOT_FOUND, result.status());
    }
}
