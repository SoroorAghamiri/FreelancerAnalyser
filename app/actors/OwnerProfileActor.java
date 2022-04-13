package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.Portifolio;
import model.Profile;
import scala.jdk.javaapi.FutureConverters;
import play.libs.ws.WSClient;
import Helpers.Utils;
import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.concurrent.CompletableFuture;

/**
 * <code>
 *     Actor for Employer Profile
 * </code>
 */

public class OwnerProfileActor extends AbstractActor {
    private ActorRef serviceActor;

    public OwnerProfileActor(ActorRef serviceActor) {
        this.serviceActor = serviceActor;
    }

    public static Props getProps(ActorRef serviceActor) {
        return Props.create(OwnerProfileActor.class , serviceActor);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ServiceActorProtocol.RequestMessage.class,
                        msg -> {
                            CompletableFuture<Object> fut =
                                    (CompletableFuture<Object>) ask(serviceActor, msg, 1000);
                            pipe(fut, getContext().dispatcher()).to(getSender());
                        })
                .build();
    }
    /**
     * Generates output as Json
     * @author Bariq
     * @param profile
     * @return jsonNode
     */
    public JsonNode generateJson(Profile profile) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(profile);
        return jsonNode;
    }
    /**
     * Processes details returned in JSON format and produces the Employer profile for all owner_id's
     * @author Bariq
     * @param jsonNode json data to be processed
     * @return  profile
     */
    public Profile CreateProfile(JsonNode jsonNode, String userId) {

        JsonNode user = jsonNode.get("result").get("users").get(userId);
        ArrayNode portfolios = (ArrayNode) jsonNode.get("result").get("portfolios").get(userId);
        List<Portifolio> portifolioList = new ArrayList<>();
        if (portfolios != null) {
            for (JsonNode portfolio : portfolios) {
                portifolioList.add(new Portifolio(portfolio.get("title").toPrettyString(), portfolio.get("description").toPrettyString()));
            }
        }
        String name = user.get("username").toPrettyString();

        ArrayNode jobs = (ArrayNode) user.get("jobs");
        int jobcount = jobs.size();
        String city = user.get("location").get("city").toPrettyString();
        String country = user.get("location").get("country").get("name").toPrettyString();
        long date = user.get("registration_date").asLong();
        String role = user.get("role").toPrettyString();
        String paymentMethod = user.get("primary_currency").get("code").toPrettyString() + user.get("primary_currency").get("sign").toPrettyString();
        boolean verified = user.get("status").get("email_verified").asBoolean();
        int recommendedby = user.get("recommendations").asInt();
        Profile profile = new Profile(name, city, country, role, verified, paymentMethod, jobcount, recommendedby, date, portifolioList);


        return profile;
    }


}




