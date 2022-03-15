package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Projects_For_A_Skill {
    public String owner_id;
    public String title;
    public String type;
    public List<Jobs_For_Project> jobs;

    public Projects_For_A_Skill(String ownerID, String title, String type, List<Jobs_For_Project> jobs){
        this.owner_id = ownerID;
        this.title = title;
        this.type = type;
        this.jobs = jobs;
    }

    public String getOwnerId(){
        return  "Owner_Id: "+this.owner_id;
    }
    public String  getTitle(){
        return "Title: "+this.title;
    }
    public  String getType(){
        return  "Type: "+this.type;
    }
    public String getAllJobs(){
        String concated = "Skills: ";
        for(int i = 0; i < this.jobs.size();i++){
            concated = concated.concat(this.jobs.get(i).getName());
        }
        return concated;
    }
}
