package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Model class defines the structure of a single project
 * @author Haitham Abdel-Salam
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    public String title;
    public String description;

    public Project(){}

    /**
     * Constructs a project with title and description
     * @param title project title
     * @param description project description
     */
    public Project(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    /**
     * @return project title
     */
    public String getTitle() {return title;}

    /**
     * @return project description
     */
    public String getDescription()
    {
        return description;
    }
}
