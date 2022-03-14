package model;
<<<<<<< Updated upstream

import java.util.concurrent.CompletionStage.*;



public class Skills {
//	CompletionStage<Result> received_projects;
	public static String skill_name;
	public Skills(CompletionStage<Result> skills) {
//		this.received_projects = skills;
		Object obj = new JSONParser().parse(skills);
        
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
          
        // getting firstName and lastName
        this.skill_name = (String) jo.get("job_details");
//        String lastName = (String) jo.get("lastName");
	}
	
=======
//import org.json.simple.*;
import play.api.libs.json.*;

public class Skills {
	/**
	 * This object holds the details that must be shown for a selected skill.
	 * @author Soroor
	 */
//	public String owner_id;
//	public Date date;
	public String skill_name;
	public Skills(String skillName) {
		this.skill_name = skillName;
	}
	public Skills() {
		
	}
	
	public static String received_skills;
	public String parseToSkills(Json receivedData){
		this.received_skills = Json.stringify(receivedData);//JsonNode.get("job_details").textValue();
	}
>>>>>>> Stashed changes
}
