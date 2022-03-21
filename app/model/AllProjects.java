package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Model class defines the structure of all project
 * @author Haitham Abdel-Salam
 */
public class AllProjects {

    private List<Project> projects;


    /**
     * Constructs all projects List instance
     * @param projects all projects instance
     */
    public AllProjects(List<Project> projects)
    {
        this.projects = projects;
    }

    /**
     * @return List of all projects
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * @return List of all titles in all the projects
     */
    public List<String> getTitles()
    {
        return projects.stream().map(t -> t.title).collect(Collectors.toList());

    }

    /**
     * @return List of all descriptions in all the projects
     */
    public List<String> getDescriptions()
    {
        return projects.stream().map(d -> d.description).collect(Collectors.toList());
    }
}
