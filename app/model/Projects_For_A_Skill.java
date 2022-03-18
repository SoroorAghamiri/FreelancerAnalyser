package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * <code>
 *     Projects For A Skill
 * </code>
 * contains the details that must be shown for related projects to a skill.
 * @author Soroor
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Projects_For_A_Skill {
    public Integer owner_id;
    public String title;
    public String type;
    public List<Jobs_For_Project> jobs;

    public Projects_For_A_Skill(Integer ownerID, String title, String type, List<Jobs_For_Project> jobs){
        this.owner_id = ownerID;
        this.title = title;
        this.type = type;
        this.jobs = jobs;
    }

    /**
     * Gets the owner id of this object
     * @return owner id
     */
    public String getOwnerId(){
        return  "Owner_Id: "+this.owner_id.toString();
    }

    /**
     * Gets the title of this object
     * @return title
     */
    public String  getTitle(){
        return " Title: "+this.title;
    }

    /**
     * Gets the type of this object
     * @return type
     */
    public  String getType(){
        return  " Type: "+this.type;
    }

    /**
     * Concats all the job names related to this object
     * @return all the skills in one string
     */
    public String getAllJobs(){
        String concated = " Skills: ";
        for(int i = 0; i < this.jobs.size();i++){
            concated = concated.concat(this.jobs.get(i).getName().concat(" "));
        }
        return concated;
    }
}
