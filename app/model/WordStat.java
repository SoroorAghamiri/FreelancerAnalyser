package model;

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

    public static Map<String, Integer> processStats(JsonNode jsonNode) {

        AllProjects projects = convertNodeToPOJO(jsonNode);
        List<String> combinedStream = Stream.of(projects.getTitles(), projects.getDescriptions())
                .flatMap(Collection::stream).collect(toList());

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

    /**
     * Converts jsonNode to Plain Old Java Object
     * @param jsonNode JsonNode objects returned from the result
     * @return List of Serialized projects
     */
    public static AllProjects convertNodeToPOJO(JsonNode jsonNode)
    {
        AllProjects allProjects = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = jsonNode.toPrettyString();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(jsonString).get("result").get("projects");
            List<Project> projects = objectMapper.readValue(arrayNode.toString(), new TypeReference<List<Project>>() {});
            allProjects = new AllProjects(projects);

        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }

        return allProjects;
    }

}
