package actors;

import Helpers.FreelanceAPI;
import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.HomeController;
import play.Logger;
import play.libs.Json;
import scala.compat.java8.FutureConverters;

import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

/**
 * This actor is responsible for handling the websocket.
 * Each time a new user joins, a new user actor is created and registers itself with the timer actor.
 * @author Soroor
 */
public class UserActor extends AbstractActor {
    /**
     * A reference to the websocket
     */
    private final ActorRef ws;
    /**
     * Reference to the time actor
     */
    private final ActorRef timeActor;

    /**
     * The message that contains the search result, sent from timer actor
     */
    static public class TimeMessage {
        public final String searchResult;
        public TimeMessage(String searchResult) {
            this.searchResult = searchResult;
        }
    }

    public UserActor(final ActorRef wsOut , ActorRef timeActor){
        ws = wsOut;
        this.timeActor = timeActor;
//        history = new HashSet<>();
    }
    public static Props props(final ActorRef wsout , ActorRef timeActor) {
        return Props.create(UserActor.class, wsout ,  timeActor);
    }

    /**
     * registers itself when initiated
     */
    @Override
    public void preStart() {
        timeActor.tell(new TimerActor.RegisterMessage(), self());
    }

    /**
     * Handles messages
     * @return receive builder
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TimeMessage.class , msg->{sendTime(msg);}).build();
    }

    /**
     * gets the time, encapsulate it in json, sends it to the websocket
     * @param msg time received from timeractor
     */
    private void sendTime(TimeMessage msg) {
        final ObjectNode response = Json.newObject();
        response.put("time", msg.searchResult);
        ws.tell(response, self());

    }
}
