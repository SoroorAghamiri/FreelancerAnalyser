package actors;

import Helpers.FreelanceAPI;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Testing skill actor
 * @author Soroor
 */
public class SkillActorTest {
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
     * creates a skill actor, sends a request message and calls OnRequest
     * @throws JsonProcessingException when json node is not valid
     */
    @Test
    public void testSkillActor() throws JsonProcessingException {
        final TestKit testProbe = new TestKit(system);
        ActorRef skillActor = system.actorOf(SkillActor.getProps(testProbe.getRef()));

        CompletionStage<Object> response = FutureConverters.toJava(
                ask(skillActor , new ServiceActorProtocol.RequestMessage("test" , FreelanceAPI.SEARCH_TERM) , 1000));
        Assert.assertNotNull(response);

        final Props props = Props.create(SkillActor.class ,testProbe.getRef());
        final TestActorRef<SkillActor> ref = TestActorRef.create(system, props, "testA");
        final SkillActor actor = ref.underlyingActor();
        String jsonString ="{\"result\":{\"projects\":[{\"owner_id\":4444,\"title\":\"Tom\",\"type\":\"Cruise\",\"jobs\":[{\"name\":\"PHP\",\"igonre\":true},{\"name\":\"JAVA\",\"igonre\":false}]},{\"owner_id\":5555,\"title\":\"Toma\",\"type\":\"Cruisea\",\"jobs\":[{\"name\":\"PHPa\",\"igonre\":true}]},{\"owner_id\":6666,\"title\":\"Tomf\",\"type\":\"Cruisef\",\"jobs\":[]}]}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        Assert.assertNotNull(actor.onRequest(actualObj));

    }
}
