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

public class Skills {
	/**
	 * This object holds the details that must be shown for a selected skill.
	 * @author Soroor
	 */

	public Skills() {
		
	}
	
	public static String received_skills;
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
			List<String> testprojects = new ArrayList<>();
			for(int i =0; i < allproject.size();i++){
				String newconcated = allproject.get(i).getOwnerId().concat(allproject.get(i).getTitle());
				newconcated = newconcated.concat(allproject.get(i).getType());
				newconcated = newconcated.concat(allproject.get(i).getAllJobs());
				testprojects.add(newconcated);
			}
//			List<String> foundprojectstring = allproject.stream().map(p->p.getOwnerId()+p.getTitle()+p.getType()+p.getAllJobs()).collect(Collectors.toList());
			return testprojects;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return  null;

	}



}
