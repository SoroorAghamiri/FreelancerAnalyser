package Helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.AllProjects;
import model.Project;
import scala.util.parsing.json.JSONObject;

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
    public static AllProjects convertNodeToAllProjects(JsonNode jsonNode)
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

    /**
     * Function to convert Project json node to Plain Old Java Object
     * @param jsonNode required JsonNode
     * @return returns POJO of new object created from JSON file
     */
    public static Project convertNodeToProject(JsonNode jsonNode)
    {
        Project projectPOJO = null;

        try {
            String jsonString = jsonNode.toPrettyString();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode node = objectMapper.readTree(jsonString);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String title = node.path("result").get("title").asText();
            String description = node.path("result").get("preview_description").asText();

            projectPOJO = new Project(title, description);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return projectPOJO;
    }
}
