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


public class TimerActor extends AbstractActorWithTimers {
    /**
     * List of all registered users
     */
    private Set<ActorRef> allUsers;

    /**
     * a reference to the service actor
     */
    private ActorRef serviceActor;
    private String latestSearch = null;

    /**
     * the message to send every x seconds to the websocket to display a new project on view
     */
    private static final class PushProject{}

    /**
     * the message to send when a new user actor wants to register
     */
    static public  class RegisterMessage{}

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

    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new PushProject(), Duration.create(5, TimeUnit.SECONDS));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(RegisterMessage.class, msg->allUsers.add(sender()))
                .match(NewSearch.class , msg->{
                    latestSearch = msg.keyword;
                    notifyUsers();
                })
                .match(PushProject.class , msg->notifyUsers()).build();
    }

    public void notifyUsers(){
        if(latestSearch != null){
            fetchResult(latestSearch);
        }

    }

    private CompletionStage<Object> fetchResult(String keyword) {
        return FutureConverters.toJava(
                    ask(serviceActor ,
                            new ServiceActorProtocol.RequestMessage(keyword , FreelanceAPI.SINGLE_RESULT) ,
                            5000)).thenApply(res->{
            JsonNode ress = Json.toJson(res);
            UserActor.TimeMessage tMsg = new UserActor.TimeMessage(ress.toPrettyString());
            allUsers.forEach(ar -> ar.tell(tMsg, self()));
            return res;
        });
    }

}
