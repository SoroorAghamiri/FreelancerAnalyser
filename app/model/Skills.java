package model;

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
	
}
