package actors;

import akka.actor.AbstractActor;

import akka.actor.ActorRef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.JobsForProject;
import model.ProjectsForASkill;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static akka.pattern.Patterns.pipe;

/**
 * Skill actor requests the data for a skill from Service actor and parses it
 * @author Soroor
 */
public class SkillActor extends AbstractActor {
    /**
     * reference to service actor
     */
    private ActorRef serviceActor;


    public SkillActor(ActorRef serviceActor){
        this.serviceActor = serviceActor;
    }

    public static Props getProps(ActorRef serviceActor) {
        return Props.create(SkillActor.class , serviceActor);
    }


    /**
     * Gets the request for the projects related to a selected keyword.
     * must notify users when a new project is received
     * also must register the new user -> should this be handled in the main page or in all the pages?
     * @return a receiver that would respond with onRequest
     */

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ServiceActorProtocol.RequestMessage.class, res->{
                            CompletionStage<Object> received = FutureConverters.toJava(ask(serviceActor, res, 1000))
                                            .thenApply(result ->{
                                                return onRequest((JsonNode) result);
                                            });

                            pipe(received, getContext().dispatcher()).to(sender());
                        })
                .build();
    }

    /**
     * Get the json, get the wanted fields and return the result
     * @param received the result of api request
     * @return a list of strings containing the parsed projects
     */
    private List<String> onRequest(JsonNode received){
        String selected = received.toPrettyString();
        List<ProjectsForASkill> allproject= stringToProjectForASkill(selected);
        List<String> foundprojectstring = new ArrayList<>();
        if(allproject != null){
            foundprojectstring = allproject.stream().map(p->p.getOwnerId().concat(p.getTitle().concat(p.getType().concat(p.getAllJobs())))).collect(Collectors.toList());
            return foundprojectstring;
        }else{
            foundprojectstring = Collections.singletonList("No Results Found.");
        }
        return foundprojectstring;
    }

    /**
     * Gets the json string, parses it to a list of <code>ProjectsForASkill</code>,
     * fetches the list of jobs related to each project and places them inside the list.
     * @param received json string
     * @return list of ProjectsForASkills
     */
    public static List<ProjectsForASkill> stringToProjectForASkill(String received){
        try {
            ObjectMapper objectmapper = new ObjectMapper();
            objectmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ArrayNode projectarray = null;
            projectarray = (ArrayNode) new ObjectMapper().readTree(received).get("result").get("projects");
            List<ProjectsForASkill> allproject = new ArrayList<>();
            for(int i =0; i < projectarray.size();i++){
                String projectasstring = projectarray.get(i).toPrettyString();
                ArrayNode relatedjobs = (ArrayNode) new ObjectMapper().readTree(projectasstring).get("jobs");
                List<JobsForProject> jobs = new ArrayList<>();
                for(int j = 0; j < relatedjobs.size();j++){
                    JobsForProject newjob = new JobsForProject(relatedjobs.get(j).get("name").textValue());
                    jobs.add(newjob);
                }
                ProjectsForASkill newproject = new ProjectsForASkill(projectarray.get(i).get("owner_id").intValue() ,
                        projectarray.get(i).get("title").textValue(), projectarray.get(i).get("type").textValue(), jobs);
                allproject.add(newproject);
            }
            return allproject;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
