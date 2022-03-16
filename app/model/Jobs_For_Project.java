package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <code>
 *     Jobs For Project
 * </code>
 * holds the name of the skills related to each project.
 * @author Soroor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jobs_For_Project {
    public String name;

    public Jobs_For_Project(String name){
        this.name = name;
    }

    /**
     * Gets the name of this object's skill
     * @return skill
     */
    public String getName() {
        return name;
    }
}
