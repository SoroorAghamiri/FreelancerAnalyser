package model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
	 * The <code>
	 *     parseToSkills
	 * </code>
	 * method parses the json file and stores the result in a list of <code> Projects For A Skill</code> obejct.
	 * Then using streams, a list of string is created that each of its elements, hold a concatenation of all the attributes in a
	 * <code> Projects For A Skill</code> object.
	 * @param receivedData
	 * @return
	 */
	public List<String> parseToSkills(JsonNode receivedData){
		String result = receivedData.toPrettyString();
		try {
			ObjectMapper objectmapper = new ObjectMapper();
			objectmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ArrayNode projectarray = null;
			projectarray = (ArrayNode) new ObjectMapper().readTree(result).get("result").get("projects");
			List<Projects_For_A_Skill> allproject = new ArrayList<>();
			for(int i =0; i < projectarray.size();i++){
				String projectasstring = projectarray.get(i).toPrettyString();
				ArrayNode relatedjobs = (ArrayNode) new ObjectMapper().readTree(projectasstring).get("jobs");
				List<Jobs_For_Project> jobs = new ArrayList<>();
				for(int j = 0; j < relatedjobs.size();j++){
					Jobs_For_Project newjob = new Jobs_For_Project(relatedjobs.get(j).get("name").textValue());
					jobs.add(newjob);
				}
				Projects_For_A_Skill newproject = new Projects_For_A_Skill(projectarray.get(i).get("owner_id").textValue() ,
						projectarray.get(i).get("title").textValue(), projectarray.get(i).get("type").textValue(), jobs);
				allproject.add(newproject);
			}

			List<String> foundprojectstring = allproject.stream().map(p->p.getOwnerId().concat(p.getTitle().concat(p.getType().concat(p.getAllJobs())))).collect(Collectors.toList());
			return foundprojectstring;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return  null;

	}



}
