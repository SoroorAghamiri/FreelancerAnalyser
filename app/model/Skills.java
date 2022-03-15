package model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONArray$;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		JsonNode result = receivedData.get("result");
		String allprojects = result.toPrettyString();

			ArrayNode projectarray = null;
			try {
				projectarray = (ArrayNode) new ObjectMapper().readTree(allprojects).get("projects");
				List<Projects_For_A_Skill> foundprojects = new ObjectMapper().readValue(projectarray.toString(), new TypeReference<List<Projects_For_A_Skill>>() {
				});
				List<String> foundprojectstring = foundprojects.stream().map(p->p.owner_id+" "+p.title+" "+p.type+" "+p.related_skills).collect(Collectors.toList());
				return foundprojectstring;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}


		return  null;
	}


}
