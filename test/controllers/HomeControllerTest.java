package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

/**
 *
 *
 * @author
 */
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
     * test for the getskillsearch view to check the data on the view
     *
     */
    @Test
    public void testGetSkillSearch(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/get-skill-search/java");
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


}
