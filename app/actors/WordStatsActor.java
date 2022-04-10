package actors;

import Helpers.Utils;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import model.AllProjects;
import model.Project;
import play.libs.ws.WSClient;
import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordStatsActor extends AbstractActor {

    private  WSClient ws;
    private  Config config;
    private ActorRef serviceActor;

    public WordStatsActor(WSClient ws, Config config, ActorRef serviceActor)
    {
        this.ws = ws;
        this.config = config;
        this.serviceActor = serviceActor;
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ServiceActorProtocol.RequestMessage.class,
                        msg -> {
                            CompletableFuture<Object> fut =
                                    (CompletableFuture<Object>) ask(serviceActor, msg, 1000);

                            // the pipe pattern
                            pipe(fut, getContext().dispatcher()).to(getSender());
                        })
                .build();
    }


    /**
     * Processes projects returned in JSON format and produces the words statistics for all projects
     * @author Haitham Abdel-Salam
     * @param jsonNode json data to be processed
     * @return  map with Unique word as a key and frequency as a value.
     */
    public  Map<String, Integer> processAllProjectsStats(JsonNode jsonNode) {

        AllProjects projects = Utils.convertNodeToAllProjects(jsonNode);
        List<String> combinedStream = Stream.of(projects.getTitles(), projects.getDescriptions())
                .flatMap(Collection::stream).collect(toList());
        return processWords(combinedStream);
    }

    /**
     * Processes word statistics for a single project
     * @author Haitham Abdel-Salam
     * @param jsonNode json data to be processed
     * @return map with Unique word as a key and frequency as a value.
     */
    public  Map<String, Integer> processProjectStats(JsonNode jsonNode) {

        Project project = Utils.convertNodeToProject(jsonNode);
        List<String> combinedStream = Stream.of(project.getTitle(), project.getDescription()).collect(toList());
        return processWords(combinedStream);
    }

    /**
     * Processes a collection of strings and gets unique words and its frequencies
     * @author Haitham Abdel-Salam
     * @param combinedStream List of all strings to be processed
     * @return map with Unique word as a key and frequency as a value.
     */
    public  Map<String, Integer> processWords(List<String> combinedStream)
    {
        List<String> separatedWords = combinedStream.stream().map(w -> w
                        .replaceAll("[^A-Za-z0-9 ]", "").split(" "))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        HashMap<String, Integer> uniqueWordsFrequency = separatedWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(word -> word, Collectors.collectingAndThen(counting(), Long::intValue)))
                .entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(e->e.getKey(), v -> v.getValue(), (e1, e2) -> e1, LinkedHashMap::new));

        return uniqueWordsFrequency;
    }
}
