package model;

import Helpers.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

public class WordStat {

    public static Integer uniqueWordsCount;
    public static ArrayList<Integer> uniqueWords;

    public WordStat()
    {

    }

    static
    {
        uniqueWords = new ArrayList<>();
    }

    public static Map<String, Integer> processAllProjectsStats(JsonNode jsonNode) {

        AllProjects projects = Utils.convertNodeToAllProjects(jsonNode);
        List<String> combinedStream = Stream.of(projects.getTitles(), projects.getDescriptions())
                .flatMap(Collection::stream).collect(toList());
        return processWords(combinedStream);
    }

    public static Map<String, Integer> processProjectStats(JsonNode jsonNode) {

        Project project = Utils.convertNodeToProject(jsonNode);
        List<String> combinedStream = Stream.of(project.getTitle(), project.getDescription()).collect(toList());
        return processWords(combinedStream);
    }

    private static  Map<String, Integer> processWords(List<String> combinedStream)
    {
        List<String> separatedWords = combinedStream.stream().map(w -> w
                        .replaceAll("[^A-Za-z0-9 ]", "").split(" "))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        HashMap<String, Integer>  uniqueWordsFrequency = separatedWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(word -> word, Collectors.collectingAndThen(counting(), Long::intValue)))
                .entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(e->e.getKey(), v -> v.getValue(), (e1, e2) -> e1, LinkedHashMap::new));

        return uniqueWordsFrequency;
    }
}
