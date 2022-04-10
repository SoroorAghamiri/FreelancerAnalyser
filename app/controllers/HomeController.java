package controllers;

import Helpers.FreelanceAPI;
import Helpers.Readability;
import Helpers.Skills;
import Helpers.WordStat;
import actors.ServiceActor;
import actors.ServiceActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import service.FreelancerAPIService;
import play.libs.ws.*;

import java.security.PublicKey;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.typesafe.config.Config;
import java.lang.*;

import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 * @author Haitham Abdel-Sala
 * @author Soroor Aghimiri
 * @author Asif Kazi Asif Tanim
 * @author Bariq Ishtiaq
 */
public class HomeController extends Controller{

    private final WSClient ws;
    private final Config config;
    final ActorRef serviceActor;

    @Inject
    public HomeController(WSClient ws,Config config, ActorSystem system) {
        this.ws = ws;
        this.config = config;
        serviceActor = system.actorOf(ServiceActor.getProps());
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
     * @param query of search query
     * @return returns a CompletionStage Result value of the fetch Freelancer.com API request
     */
    public CompletionStage<Result> getSearchTerm(String query) {
        return  new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() +
                        FreelanceAPI.SEARCH_TERM.getUrl() + query)
        .thenApply(result -> ok(Readability.processReadability(result.asJson())));
    }

    /**
     * Readability class to handale one readability calculations
     * @author Kazi Asif Tanim
     * @param description of preview_description
     * @return returns a CompletionStage Result value of FKGL and FRI score
     */
    public CompletableFuture<Result> readablity(String description) {
    	return CompletableFuture.completedFuture(ok("Preview Description: " +
                description + "\n" + Readability.processReadabilityForSingleProject(description)));
    }

    /**
     * @author Bariq
     * this return the owner details by the owner_id
     * @param owner_id owner id
     * @return CompletionStage with result
     */
    public CompletionStage<Result> getOwnerDetails(String owner_id){
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() +
                        FreelanceAPI.OWNER_PROFILE.getUrl() + owner_id)
        .thenApply(result -> ok(result.asJson()));
    }

    /**
     * Gets the 10 latest projects related to a skill
     * @author Soroor
     * @param skill_name the skill selected in the main page
     * @return skill view, displaying 10 latest projects
     */
    public CompletionStage<Result> getSkillSearch(String skill_name) {
        CompletionStage<Result> result = new FreelancerAPIService(ws, config)
                .getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + skill_name)
                .thenApply(success ->{
                    JsonNode received = success.asJson();
                    return ok(views.html.skills.render(new Skills().parseToSkills(received) , skill_name));
                });

        return result;
    }

    /**
     * Action method calls the stat view and renders the stats page with a global result for latest 250 projects
     * @author Haitham Abdel-Salam
     * @param query Search term query
     * @return CompletionStage Result value of the latest 250 project with query term
     */
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

    /**
     * Action method calls the stat view and renders the stats page with a single project stats result
     * @author Haitham Abdel-Salam
     * @param id project id
     * @return CompletionStage Result value of a single project
     */
    public CompletionStage<Result> getSingleProjectStats(String id)
    {
        CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config).
                getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.PROJECT_BY_ID.getUrl() + id);

        CompletionStage<Result> result = response.thenApply(res ->{
            if(res.getStatus() == 404)
                return notFound("project not found!");
            JsonNode node = res.asJson();

            return ok(views.html.stats.render(WordStat.processProjectStats(node),
                    "Single project "+id+" Stats"));
        });

        return result;
    }

    /**
     * @author Haitham Abdel-Salam
     * @return not found when status is 404
     */
    public Result getSingleProjectStatsNotFound()
    {
        return notFound("project not found! id is missing...");
    }

    /**
     * render ownerView Page and load the data return from the api
     * @param owner_id owner id
     * @author Bariq
     * @return result
     */
    public Result getOwnerView(String owner_id){
        return ok(views.html.ownerProfile.render());
    }

    public CompletionStage<Result> requestApi(String message) {
        return FutureConverters.toJava(ask(serviceActor,
                        new ServiceActorProtocol.RequestMessage(message, ws, config), 1000))
                .thenApply(response -> ok(Readability.processReadability((JsonNode) response)));
    }
}
