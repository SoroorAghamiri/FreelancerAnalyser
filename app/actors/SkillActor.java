package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.JobsForProject;
import model.ProjectsForASkill;
import play.libs.Json;
import play.mvc.Result;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.*;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

public class SkillActor extends AbstractActor {


    /**
     * The message requesting the projects related to the searched keyword.
     */
    public static final class RequestSkillProject{
        String receivedJson;
        public RequestSkillProject(JsonNode selected){
            this.receivedJson = selected.toPrettyString();
        }
    }

    /**
     * The message containing the result received and manipulated.
     * This message must be returned as a response to the call to onRequest, and must be displayed on the skills page
     */
    public static final class ReturnedProjects{
        List<String> allProjects;
        public ReturnedProjects(List<String> all){
            this.allProjects = all;
        }
    }

    static public Props getProps(){
        return Props.create(SkillActor.class , SkillActor::new);
    }



    /**
     * Gets the request for the projects related to a selected keyword.
     * must notify users when a new project is received
     * also must register the new user -> should this be handled in the main page or in all the pages?
     * @return a receiver that would respond with onRequest
     */

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ServiceActorProtocol.JsonMessage.class , res->sender()
                        .tell(new ReturnedProjects(onRequest(res.jsonNode)) , self()))
                .build();
    }

    /**
     * Get the json, get the wanted fields and return the result
     * TODO: we also need a method to ensure there are no duplicates
     * TODO: return parameter is not correct. you need to send a returnedproject message
     * @param received
     * @return
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
