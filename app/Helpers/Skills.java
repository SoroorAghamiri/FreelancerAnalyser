package Helpers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.Jobs_For_Project;
import model.Projects_For_A_Skill;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONArray$;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <code>
 *     Skills
 * </code>
 * gets the Json file from the controller, parses the file, returns each project as a string.
 * The result is displayed in skills view.
 * @author Soroor
 */

public class Skills {


	public Skills() {

	}

	/**
	 * Gets a json node from the home controller, converts it to a string and passes it to stringToProjectForASkill.
	 * Then using streams, a list of string is created that each of its elements, hold a concatenation of all the attributes in a
	 * <code> Projects For A Skill</code> object.
	 * @param receivedData Json node received from the controller
	 * @return list of string to display in the view
	 */
	public List<String> parseToSkills(JsonNode receivedData){
		String result = receivedData.toPrettyString();
		List<Projects_For_A_Skill> allproject= stringToProjectForASkill(result);
		List<String> foundprojectstring = new ArrayList<>();
		if(allproject != null){
			foundprojectstring = allproject.stream().map(p->p.getOwnerId().concat(p.getTitle().concat(p.getType().concat(p.getAllJobs())))).collect(Collectors.toList());
			return foundprojectstring;
		}else{
			foundprojectstring = Collections.singletonList("No Results Found.");
		}
		return foundprojectstring;
	}

	/**
	 * Gets the json string, parses it to a list of <code>Projects_For_A_Skill</code>,
	 * fetches the list of jobs related to each project and places them inside the list.
	 * @param received json string
	 * @return list of Projects_For_A_Skills
	 */
	public static List<Projects_For_A_Skill> stringToProjectForASkill(String received){
		try {
			ObjectMapper objectmapper = new ObjectMapper();
			objectmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ArrayNode projectarray = null;
			projectarray = (ArrayNode) new ObjectMapper().readTree(received).get("result").get("projects");
			List<Projects_For_A_Skill> allproject = new ArrayList<>();
			for(int i =0; i < projectarray.size();i++){
				String projectasstring = projectarray.get(i).toPrettyString();
				ArrayNode relatedjobs = (ArrayNode) new ObjectMapper().readTree(projectasstring).get("jobs");
				List<Jobs_For_Project> jobs = new ArrayList<>();
				for(int j = 0; j < relatedjobs.size();j++){
					Jobs_For_Project newjob = new Jobs_For_Project(relatedjobs.get(j).get("name").textValue());
					jobs.add(newjob);
				}
				Projects_For_A_Skill newproject = new Projects_For_A_Skill(projectarray.get(i).get("owner_id").intValue() ,
						projectarray.get(i).get("title").textValue(), projectarray.get(i).get("type").textValue(), jobs);
				allproject.add(newproject);
			}
			return allproject;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}



}
