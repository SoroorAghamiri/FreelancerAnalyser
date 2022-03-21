package models;

import Helpers.Utils;
import Helpers.WordStat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Project;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * processes handling and parsing Project model
 * @author Haitham Abdel-Salam
 */
public class SingleProjectWordStatsTest {
    public static JsonNode node;

    /**
     * initializes json node before each test by read mock json from file
     * @author Haitham Abdel-Salam
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        Path resourceDirectory = Paths.get("test","resources","project.json");
        List<String> lines = Files.readAllLines(resourceDirectory, Charset.defaultCharset());
        String jsonString = lines.stream().collect(Collectors.joining("\n"));

        ObjectMapper mapper = new ObjectMapper();
        node = mapper.readTree(jsonString);
    }

    /**
     * Tests if processing projects map returns a map with data
     * @author
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void processSingleProjectsStatsTestValid() throws JsonParseException, IOException {

        assertTrue(Collections.emptyList() != WordStat.processProjectStats(node));
    }

    /**
     * Tests if processing projects map returns map with data
     * @author Haitham abdel-Salam
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void processSingleProjectsStatsTestEmpty() throws JsonParseException, IOException {

        assertFalse(Collections.emptyList() == WordStat.processProjectStats(node));
    }

    /**
     * Tests creating and usage of allprojects model
     * @author Haitham Abdel-Salam
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void processAllProjectsStatsTestHasValues() throws JsonParseException, IOException {

        Project project1 = new Project("test project 1", "Description of project 1");
        Project project2 = new Project("test project 2", "Description of project 2");

        assertEquals(project1.getTitle(),"test project 1");
        assertEquals(project2.getTitle(),"test project 2");
        assertEquals(project1.getDescription(),"Description of project 1");
        assertEquals(project2.getDescription() ,"Description of project 2");
    }

    /**
     * Tests conversion of Json node to project mocel
     * @author Haitham Abdel-Salam
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void convertsJsonNodeToProjectTest() throws JsonParseException, IOException {

        Project project = Utils.convertNodeToProject(node);
        assertTrue(project!=null);
    }
}
