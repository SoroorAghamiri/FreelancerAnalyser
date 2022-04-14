package actors;
import Helpers.FreelanceAPI;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.deploy.cache.CacheEntry;
import model.Portifolio;
import org.junit.AfterClass;
import org.junit.Assert;
import akka.actor.Props;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

/**
 * <code>
 *     Test for ownerProfileActor
 * </code>
 * @author Bariq
 */
public class OwnerProfileTest {
    /**
     * A test actor system
     */
    static ActorSystem system;
    /**
     * setting up actor system before running the test
     */
    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }
    /**
     * stopping test kit after the test is done
     */

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    /**
     * Creates OwnerProfileActor
     * @throws JsonProcessingException
     */
    @Test
    public void testOwnerProfileActor() throws JsonProcessingException {
        final TestKit testProbe = new TestKit(system);
        ActorRef ownerProfileActor = system.actorOf(OwnerProfileActor.getProps(testProbe.getRef()));

        CompletionStage<Object> response = FutureConverters.toJava(
                ask(ownerProfileActor , new ServiceActorProtocol.RequestMessage("test" , FreelanceAPI.OWNER_PROFILE) , 1000));
        Assert.assertNotNull(response);
        final Props props = Props.create(OwnerProfileActor.class ,testProbe.getRef());
        final TestActorRef<OwnerProfileActor> ref = TestActorRef.create(system, props, "testO");
        final OwnerProfileActor actor = ref.underlyingActor();
        String jsonString ="{\"result\":{\"projects\":[{\"owner_id\":4444,\"title\":\"Tom\",\"type\":\"Cruise\",\"jobs\":[{\"name\":\"PHP\",\"igonre\":true},{\"name\":\"JAVA\",\"igonre\":false}]},{\"owner_id\":5555,\"title\":\"Toma\",\"type\":\"Cruisea\",\"jobs\":[{\"name\":\"PHPa\",\"igonre\":true}]},{\"owner_id\":6666,\"title\":\"Tomf\",\"type\":\"Cruisef\",\"jobs\":[]}]}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode profile = mapper.readTree(jsonString);
        Assert.assertNotNull(profile==null);
    }
}
