package models;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Jobs_For_Project;
import model.Projects_For_A_Skill;
import Helpers.Skills;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the parser in the <code>Skills</code> and the <code>Projects_For_A_Skill</code> and <code>Jobs_For_Project</code>
 * objects and their methods.
 * @author Soroor
 */
public class SkillsTest {
    /**
     * Creates a test jason file, stores it in a list of strings, sends the list of string and the parseToSkill method
     * to compareStringLists method. The result must be asserted to true.
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void parseToSkillsTest() throws JsonParseException, IOException {
        String jsonString ="{\"result\":{\"projects\":[{\"owner_id\":4444,\"title\":\"Tom\",\"type\":\"Cruise\",\"jobs\":[{\"name\":\"PHP\",\"igonre\":true},{\"name\":\"JAVA\",\"igonre\":false}]},{\"owner_id\":5555,\"title\":\"Toma\",\"type\":\"Cruisea\",\"jobs\":[{\"name\":\"PHPa\",\"igonre\":true}]},{\"owner_id\":6666,\"title\":\"Tomf\",\"type\":\"Cruisef\",\"jobs\":[]}]}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        Skills testskill = new Skills();
        List<String> truestring = new ArrayList<>();
        truestring.add("Owner_Id: 4444 Title: Tom Type: Cruise Skills: PHP JAVA ");
        truestring.add("Owner_Id: 5555 Title: Toma Type: Cruisea Skills: PHPa ");
        truestring.add("Owner_Id: 6666 Title: Tomf Type: Cruisef Skills: ");
        Assert.assertTrue(compareStringLists(truestring , testskill.parseToSkills(actualObj)));
    }

     /**
     * Compares two lists of strings.
     * @param a list of strings
     * @param b list of strings
     * @return true if both lists are the same, false if not
     */
    private boolean compareStringLists(List<String> a, List<String> b){
        for(int i = 0; i < a.size();i++){
            if(!a.get(i).equals(b.get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a list of <code>Jobs_For_Project</code> object, and a <code>Project_For_A_Skill</code> object.
     * Tests methods for both objects.
     */

    @Test
    public void projectsForASkillTest(){
        List<Jobs_For_Project> testjobs = new ArrayList<>();
        Jobs_For_Project test1 = new Jobs_For_Project("test1");
        Jobs_For_Project test2 = new Jobs_For_Project("test2");
        testjobs.add(test1);
        testjobs.add(test2);
        Projects_For_A_Skill testcase = new Projects_For_A_Skill(1234 , "title" , "type" , testjobs);
        Assert.assertTrue(testcase.getOwnerId().equals("Owner_Id: 1234"));
        Assert.assertTrue(testcase.getTitle().equals(" Title: title"));
        Assert.assertTrue(testcase.getType().equals(" Type: type"));
        Assert.assertTrue(testcase.getAllJobs().equals(" Skills: test1 test2 "));
        Assert.assertTrue(test1.getName().equals("test1"));
    }
}
