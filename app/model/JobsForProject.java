package model;

/**
 * <code>
 *     Jobs For Project
 * </code>
 * holds the name of the skills related to each project.
 * @author Soroor
 */
public class JobsForProject {
    public String name;
    public JobsForProject(String name){
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
