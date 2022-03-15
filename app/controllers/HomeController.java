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
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(){
        return ok(views.html.index.render());
    }
    
    public CompletionStage<Result> getSearchTerm(String query) {
        return  new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + query)
        .thenApply(result -> ok(Readability.processReadability(result)));
    }
    
    public CompletableFuture<Result> readablity(String description) {
    	return CompletableFuture.completedFuture(ok("Preview Description: " + description + "\n" + Readability.processReadabilityForSingleProject(description)));
    }

    public CompletionStage<Result> getOwnerDetails(String owner_id){
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.OWNER_PROFILE.getUrl() + owner_id)
        .thenApply(result -> ok(result.asJson()));
    }

    public CompletionStage<Result> getSkillSearch(String skill_name) {
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + skill_name)
        .thenApply(result -> ok(result.asJson()));
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
