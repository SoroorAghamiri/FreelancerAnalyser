package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllProjects {

    private List<Project> projects;


    public AllProjects(List<Project> projects)
    {
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<String> getTitles()
    {
        return projects.stream().map(t -> t.title).collect(Collectors.toList());

    }

    public List<String> getDescriptions()
    {
        return projects.stream().map(d -> d.description).collect(Collectors.toList());
    }
}
