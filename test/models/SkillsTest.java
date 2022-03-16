package models;

import akka.event.Logging;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Jobs_For_Project;
import model.Projects_For_A_Skill;
import model.Skills;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SkillsTest {
    @Test
    public void parseToSkillsTest() throws JsonParseException, IOException {
        String jsonString ="{\"result\":{\"projects\":[{\"owner_id\":\"4444\",\"title\":\"Tom\",\"type\":\"Cruise\",\"jobs\":[{\"name\":\"PHP\",\"igonre\":true},{\"name\":\"JAVA\",\"igonre\":false}]},{\"owner_id\":\"5555\",\"title\":\"Toma\",\"type\":\"Cruisea\",\"jobs\":[{\"name\":\"PHPa\",\"igonre\":true}]},{\"owner_id\":\"6666\",\"title\":\"Tomf\",\"type\":\"Cruisef\",\"jobs\":[]}]}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        Skills testskill = new Skills();
        List<String> truestring = new ArrayList<>();
        truestring.add("Owner_Id: 4444 Title: Tom Type: Cruise Skills: PHP JAVA ");
        truestring.add("Owner_Id: 5555 Title: Toma Type: Cruisea Skills: PHPa ");
        truestring.add("Owner_Id: 6666 Title: Tomf Type: Cruisef Skills: ");
        Assert.assertTrue(compareStringLists(truestring , testskill.parseToSkills(actualObj)));
    }

    private boolean compareStringLists(List<String> a, List<String> b){
        for(int i = 0; i < a.size();i++){
            if(!a.get(i).equals(b.get(i))){
                return false;
            }
        }
        return true;
    }

    @Test
    public void projectsForASkillTest(){
        List<Jobs_For_Project> testjobs = new ArrayList<>();
        Jobs_For_Project test1 = new Jobs_For_Project("test1");
        Jobs_For_Project test2 = new Jobs_For_Project("test2");
        testjobs.add(test1);
        testjobs.add(test2);
        Projects_For_A_Skill testcase = new Projects_For_A_Skill("owner id" , "title" , "type" , testjobs);
        Assert.assertTrue(testcase.getOwnerId().equals("Owner_Id: owner id"));
        Assert.assertTrue(testcase.getTitle().equals(" Title: title"));
        Assert.assertTrue(testcase.getType().equals(" Type: type"));
        Assert.assertTrue(testcase.getAllJobs().equals(" Skills: test1 test2 "));
        Assert.assertTrue(test1.getName().equals("test1"));
    }
}
