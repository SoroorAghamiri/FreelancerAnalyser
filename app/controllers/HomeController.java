package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.*;
import service.FreelancerAPIService;
import play.libs.ws.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.inject.Inject;
import model.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import java.lang.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller{

    private final WSClient ws;
    private final Config config;

    @Inject
    public HomeController(WSClient ws,Config config) {
        this.ws = ws;
        this.config = config;
    }

    /**
	 * Action method calls the view template index and render the home page
	 * @author Kazi Asif Tanim
	 * @return returns a play.mvc.Result value, representing the HTTP response to
	 *         send to the client
	 */
    public Result index(){
        return ok(views.html.index.render());
    }
    
    /**
     * Readability class to handale readability calculations
     * @author Kazi Asif Tanim
     * @param String of search query
     * @return returns a CompletionStage<Result> value of the fetch Freelancer.com API request
     */
    public CompletionStage<Result> getSearchTerm(String query) {
        return  new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + query)
        .thenApply(result -> ok(Readability.processReadability(result)));
    }
    
    /**
     * Readability class to handale one readability calculations
     * @author Kazi Asif Tanim
     * @param String of preview_description
     * @return returns a CompletionStage<Result> value of FKGL & FRI score
     */
    public CompletableFuture<Result> readablity(String description) {
    	return CompletableFuture.completedFuture(ok("Preview Description: " + description + "\n" + Readability.processReadabilityForSingleProject(description)));
    }

    public CompletionStage<Result> getOwnerDetails(String owner_id){
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.OWNER_PROFILE.getUrl() + owner_id)
        .thenApply(result -> ok(result.asJson()));
    }

    /**
     * Gets the 10 latest projects related to a skill
     * @param skill_name the skill selected in the main page
     * @return skill view, displaying 10 latest projects
     */

    public CompletionStage<Result> getSkillSearch(String skill_name) {
        CompletionStage<Result> result = new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + skill_name)
                .thenApply(success ->{
                    JsonNode received = success.asJson();
                    return ok(views.html.skills.render(new Skills().parseToSkills(received)));
                });

        return result;
    }


    public CompletionStage<Result> getWordStats(String query)
    {
         CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config).
                getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.WORD_STATS.getUrl() + query);

         CompletionStage<Result> result = response.thenApply(res ->{
             JsonNode node = res.asJson();
             return ok(views.html.stats.render(WordStat.processAllProjectsStats(node),
                     "Word stats for latest 250 projects for "+query+" term"));
         });

         return result;
    }

    public CompletionStage<Result> getSingleProjectStats(Integer id)
    {
        CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config).
                getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.PROJECT_BY_ID.getUrl() + id);

        CompletionStage<Result> result = response.thenApply(res ->{
            JsonNode node = res.asJson();
            return ok(views.html.stats.render(WordStat.processProjectStats(node),
                    "Single project "+id.toString()+" Stats"));
        });

        return result;
    }
}
