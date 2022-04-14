package actors;

import Helpers.Utils;
import Helpers.WordStat;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import model.AllProjects;
import model.Project;
import play.libs.ws.WSClient;
import scala.compat.java8.FutureConverters;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Word stat actor
 * @author Haitham Abdel-Salam
 */
public class WordStatsActor extends AbstractActor {

    /**
     * reference to service actor
     */
    private ActorRef serviceActor;

    /**
     * Word stat actor constructor
     * @author Haitham Abdel-Salam
     * @param serviceActor actor ref to service Actor
     */
    public WordStatsActor(ActorRef serviceActor)
    {
        this.serviceActor = serviceActor;
    }

    /**
     * Gets WordStats Actor Props
     * @author Haitham Abdel-Salam
     * @param serviceActor actor ref to service Actor
     */
    public static Props getProps(ActorRef serviceActor) {
        return Props.create(WordStatsActor.class , serviceActor);
    }


    /**
     * Handling reception of two messages from a regular request and a single project reques with Id
     * After to recives a msg it sends a msg to service actor and waits for respond then uses piping
     * returns hashmap of the processed single and all projects for the hashmap
     * @author Haitham Abdel-Salam
     * @return a receiver that would respond with onRequest
     */
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ServiceActorProtocol.RequestMessage.class,
                        msg -> {
                            CompletionStage<Object> fut =
                                    FutureConverters.toJava(ask(serviceActor, msg, 1000))
                                            .thenApply(result ->{
                                        return WordStat.processAllProjectsStats((JsonNode) result);
                                    });

                            pipe(fut, getContext().dispatcher()).to(sender());
                        })
                .match(
                        ServiceActorProtocol.SingleProjectRequest.class,
                        msg -> {
                            CompletionStage<Object> fut =
                                    FutureConverters.toJava(ask(serviceActor, msg, 1000))
                                            .thenApply(result ->{
                                                return WordStat.processProjectStats((JsonNode) result);
                                            });

                            pipe(fut, getContext().dispatcher()).to(sender());
                        })
                .build();
    }
}
