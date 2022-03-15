package Helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.AllProjects;
import model.Project;

import java.util.List;
import java.util.function.Function;

/**
 * Utility class with helper functions
 * @author Haitham Abdel-Salam
 */
public class Utils {
    /**
     * Converts jsonNode to Plain Old Java Object and returns all projects with title and description fields
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
