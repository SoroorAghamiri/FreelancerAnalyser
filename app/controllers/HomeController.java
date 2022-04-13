package controllers;

import Helpers.FreelanceAPI;
import Helpers.Readability;
import Helpers.Skills;
import Helpers.WordStat;
import actors.ServiceActor;
import actors.ServiceActorProtocol;
import actors.WordStatsActor;
import actors.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import play.core.NamedThreadFactory;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import play.mvc.Http.Request.*;
import scala.compat.java8.FutureConverters;
import service.FreelancerAPIService;
import play.libs.ws.*;

import java.util.List;
import java.util.Map;
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
 * @author Soroor Sadat Seyed Aghamiri
 * @author Asif Kazi Asif Tanim
 * @author Bariq Ishtiaq
 */
public class HomeController extends Controller{

    private final WSClient ws;
    private final Config config;
    final ActorRef serviceActor;
    final ActorRef skillActor;
    final ActorRef timerActor;
    @Inject private Materializer materializer;
    @Inject private ActorSystem actorSystem;

    final ActorRef wordStatsActor;
    final ActorRef readabilityActor;
    
    @Inject
    public HomeController(WSClient ws, Config config, ActorSystem system) {
        this.ws = ws;
        this.config = config;
        serviceActor = system.actorOf(Props.create(ServiceActor.class, ws, config));
        wordStatsActor = system.actorOf(Props.create(WordStatsActor.class, serviceActor));
        skillActor = system.actorOf(Props.create(SkillActor.class , serviceActor));
        timerActor = system.actorOf(TimerActor.getProps(serviceActor), "timeActor");
        readabilityActor = system.actorOf(Props.create(ReadabilityActor.class, serviceActor));
    }

    public WebSocket ws() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(out->UserActor.props(out, timerActor), actorSystem, materializer));
    }

    /**
	 * Action method calls the view template index and render the home page
	 * @author Kazi Asif Tanim
	 * @return returns a play.mvc.Result value, representing the HTTP response to
	 *         send to the client
	 */
    public Result index(Http.Request request){
        timerActor.tell(new TimerActor.NewSearch(null) , timerActor);
        return ok(views.html.index.render(request));
    }

    /**
     * Readability class to handale readability calculations
     * @author Kazi Asif Tanim
     * @param query of search query
     * @return returns a CompletionStage Result value of the fetch Freelancer.com API request
     */
    public CompletionStage<Result> getSearchTerm(String query) {
        // return  FutureConverters.toJava(ask(timerActor, new TimerActor.NewSearch(query) , 1000))
        // .thenApply(result -> ok(Readability.processReadability((JsonNode) result)));
    	 
        return FutureConverters.toJava(ask(readabilityActor,
                new ServiceActorProtocol.ReadabilityRequest(query, FreelanceAPI.SEARCH_TERM), 1000))
           .thenApply(response -> {
        	   try {
        		   ObjectMapper objectMapper = new ObjectMapper();
            	   JsonNode jsonNode = objectMapper.readTree(response.toString());
            	   return ok(jsonNode);
        	   }catch(Exception e) {
        		   return ok("Something went wrong when parsing json");
        	   }
               
           });
    }

    public void enableSearchTermActor(){
        FutureConverters.toJava(ask(timerActor, new TimerActor.NewSearch(query) , 1000));
    }

    /**
     * Readability class to handale one readability calculations
     * @author Kazi Asif Tanim
     * @param description of preview_description
     * @return returns a CompletionStage Result value of FKGL and FRI score
     */
    public CompletionStage<Result> readablity(String project_id) {
    	
    	return FutureConverters.toJava(ask(readabilityActor, 
    			new ServiceActorProtocol.SingleReadabilityRequest(project_id, FreelanceAPI.PROJECT_BY_ID), 1000))
           .thenApply(response -> {
        	   try {
        		   ObjectMapper objectMapper = new ObjectMapper();
            	   JsonNode jsonNode = objectMapper.readTree(response.toString());
            	   Map<String, String> result = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, String>>(){});
            	   return ok(views.html.readability.render(result));
        	   }catch(Exception e) {
        		   return ok("Something went wrong when parsing json");
        	   }
               
           });
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
        return FutureConverters.toJava(ask(
                skillActor , new ServiceActorProtocol.RequestMessage(skill_name,FreelanceAPI.SEARCH_TERM),1000))
                .thenApply(res->{
                    List<String> received = (List<String>) res;
                    return ok(views.html.skills.render(received, skill_name));
                });
    }

    /**
     * Action method calls the stat view and renders the stats page with a global result for latest 250 projects
     * @author Haitham Abdel-Salam
     * @param query Search term query
     * @return CompletionStage Result value of the latest 250 project with query term
     */
//    public CompletionStage<Result> getWordStats(String query)
//    {
//         CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config).
//                getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.WORD_STATS.getUrl() + query);
//
//         CompletionStage<Result> result = response.thenApply(res ->{
//             JsonNode node = res.asJson();
//             return ok(views.html.stats.render(WordStat.processAllProjectsStats(node),
//                     "Word stats for latest 250 projects for "+query+" term"));
//         });
//
//         return result;
//    }

    public CompletionStage<Result> getWordStats(String query)
    {
        return FutureConverters.toJava(ask(wordStatsActor,
                        new ServiceActorProtocol.RequestMessage(query, FreelanceAPI.WORD_STATS), 1000))
                   .thenApply(response -> {
                       Map<String, Integer> map = (Map<String, Integer>) response;
                       return ok(views.html.stats.render(map, "Word stats for latest 250 projects for "+query+" term"));
                   });
    }

    /**
     * Action method calls the stat view and renders the stats page with a single project stats result
     * @author Haitham Abdel-Salam
     * @param id project id
     * @return CompletionStage Result value of a single project
     */
    public CompletionStage<Result> getSingleProjectStats(String id)
    {
        return FutureConverters.toJava(ask(wordStatsActor,
                        new ServiceActorProtocol.SingleProjectRequest(id, FreelanceAPI.PROJECT_BY_ID), 1000))
                .thenApply(response -> {
                    Map<String, Integer> map = (Map<String, Integer>) response;
                    return ok(views.html.stats.render(map, "Single project "+id+" Stats"));
                });
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

//    public CompletionStage<Result> requestApi(String message) {
//        return FutureConverters.toJava(ask(serviceActor,
//                        new ServiceActorProtocol.RequestMessage(message, FreelanceAPI.SEARCH_TERM), 1000))
//                .thenApply(response -> ok(Readability.processReadability((JsonNode) response)));
//    }
}
