package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    public String title;
    public String description;

    public Project()
    {

    }

    public Project(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public String getTitle()
    {
        return title;

    }

    public String getDescription()
    {
        return description;
    }
}
