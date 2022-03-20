package models;

import Helpers.Utils;
import Helpers.WordStat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.AllProjects;
import model.Project;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.api.Play;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


/**
 * Tests parsing and handling all projects model
 * @author Haitham Abdel-Salam
 */
public class AllProjectsWordStatsTest {

    public static JsonNode node;

    /**
     * Initializes the json node before each test to read mock json from file
     * @author Haitham Abdel-Salam
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        Path resourceDirectory = Paths.get("test","resources","projects.json");
        List<String> lines = Files.readAllLines(resourceDirectory, Charset.defaultCharset());
        String jsonString = lines.stream().collect(Collectors.joining("\n"));

        ObjectMapper mapper = new ObjectMapper();
        node = mapper.readTree(jsonString);
    }

    /**
     * Checks of processing allProject model is valid and not empty
     * @author Haitham Abdel-Salam
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void processAllProjectsStatsTestValid() throws JsonParseException, IOException {

        assertTrue("The returned map should not be empty", Collections.emptyList() !=
                WordStat.processAllProjectsStats(node));
    }

    /**
     * Checks of processing allproject model returns an empty map
     * @author Haitham Abdel-Salam
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void processAllProjectsStatsTestEmpty() throws JsonParseException, IOException {

        assertFalse("The returned map should not be empty", Collections.emptyList() ==
                WordStat.processAllProjectsStats(node));
    }

    /**
     * Tests proccessing words as streams
     * @author
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void processWordsTest() throws JsonParseException, IOException {

        List<String> words = new ArrayList<String>();
        words.add("John");
        words.add("John");
        words.add("John");
        words.add("Noel");
        words.add("Jack");
        words.add("Nelly");

        assertFalse("The returned map should not be empty", Collections.emptyList() ==
                WordStat.processWords(words));
    }

    /**
     * Tests converting json node to allProjects model
     * @author Haitham Abdel-Salam
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void convertJsonNodeToAllProjectsTest() throws JsonParseException, IOException {

        AllProjects  projects = Utils.convertNodeToAllProjects(node);

        assertTrue(projects != null);
    }

    /**
     * Testing getting projects
     * @author Haitham Abdel-Salam
     */
    @Test
    public void getProjectsTest() {

        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project());
        projects.add(new Project());

        AllProjects  allProjects = new AllProjects(projects);

        assertTrue(allProjects.getProjects() != null);
        assertTrue(allProjects.getProjects().size() == projects.size());
    }

    /**
     * Testing getting Titles
     * @author Haitham Abdel-Salam
     */
    @Test
    public void getTitlesTest() {

        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project());
        projects.add(new Project());

        AllProjects  allProjects = new AllProjects(projects);

        assertTrue(allProjects.getTitles() != null);
    }

    /**
     * Testing getting description
     * @author Haitham Abdel-Salam
     */
    @Test
    public void getDescriptionTest() {

        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project());
        projects.add(new Project());

        AllProjects  allProjects = new AllProjects(projects);

        assertTrue(allProjects.getDescriptions() != null);
    }
}
