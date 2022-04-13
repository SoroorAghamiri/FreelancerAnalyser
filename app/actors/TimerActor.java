package actors;

import Helpers.FreelanceAPI;
import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.Duration;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import static akka.pattern.Patterns.ask;

/**
 * Keeps the track of time and sends a refresh message after specific time spans.
 * @author Soroor Aghamiri
 */
public class TimerActor extends AbstractActorWithTimers {
    /**
     * List of all registered users
     */
    private Set<ActorRef> allUsers;

    /**
     * a reference to the service actor
     */
    private ActorRef serviceActor;
    /**
     * Last keyword searched.
     */
    private String latestSearch = null;

    /**
     * the message to send every x seconds to the websocket to display a new project on view
     */
    private static final class PushProject{}

    /**
     * the message to send when a new user actor wants to register
     */
    static public  class RegisterMessage{}

    /**
     * Message that contains the keyword for a new search request.
     */
    static public class NewSearch{
        public String keyword;
        public NewSearch(String keyword){
            this.keyword = keyword;
        }
    }

    public static Props getProps(ActorRef serviceActor) {
        return Props.create(TimerActor.class , serviceActor);
    }
    public TimerActor(ActorRef serviceActor){
        allUsers = new HashSet<>();
        this.serviceActor = serviceActor;
        latestSearch = null;
    }

    /**
     * Starts the timer
     */
    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new PushProject(), Duration.create(15, TimeUnit.SECONDS));
    }

    /**
     * Handles the actions towards different messages
     * @return receive builder
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(RegisterMessage.class, msg->allUsers.add(sender()))
                .match(NewSearch.class , msg->{
                    latestSearch = msg.keyword;
                    notifyUsers();
                })
                .match(PushProject.class , msg->notifyUsers()).build();
    }

    /**
     * If there is a new keyword, fetches the new projects
     */
    public void notifyUsers(){
        if(latestSearch != null){
            fetchResult(latestSearch);
        }

    }

    /**
     * Sends a request to service actor to receive the projects related to keyword
     * @param keyword new searched term
     * @return result of search to the websocket
     */
    private CompletionStage<Object> fetchResult(String keyword) {
        return FutureConverters.toJava(
                    ask(serviceActor ,
                            new ServiceActorProtocol.RequestMessage(keyword , FreelanceAPI.SEARCH_TERM) ,
                            5000)).thenApply(res->{
            JsonNode ress = Json.toJson(res);
            UserActor.TimeMessage tMsg = new UserActor.TimeMessage(ress.toPrettyString());
            allUsers.forEach(ar -> ar.tell(tMsg, self()));
            return res;
        });

    }

}
