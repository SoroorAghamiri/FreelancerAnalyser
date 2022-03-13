package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class WordStat {

    public Integer uniqueWords;

    public WordStat()
    {

    }

    static
    {

    }

    public static void processStats(JsonNode jsonNode) {

        List<Project> projects = convertToPojoList(jsonNode);

    }

    /**
     *
     * @param jsonNode JsonNode objects returned from the result
     * @return List of Serialized projects
     */
    public static List<Project> convertToPojoList(JsonNode jsonNode)
    {
        List<Project> projects = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = jsonNode.toPrettyString();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(jsonString).get("result").get("projects");
            projects = objectMapper.readValue(arrayNode.toString(), new TypeReference<List<Project>>() {});

            System.out.println(arrayNode.size());
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }

        return projects;
    }

}
