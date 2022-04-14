package actors;

import Helpers.FreelanceAPI;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WSClient;
import com.typesafe.config.Config;
import java.security.Provider;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static org.mockito.Mockito.mock;

/**
 * tests service actor
 * @author Soroor
 */
public class ServiceActorTest {
    /**
     * A test actor system
     */
    static ActorSystem system;

    private WSClient ws = mock(WSClient.class);

    private Config config = mock(Config.class);

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
     * sends request message and single project request message to service actor
     */
    @Test
    public void testServiceActor(){
        final TestKit testProbe = new TestKit(system);
        ActorRef victim = null;
        ActorRef serviceActor = system.actorOf(Props.create(ServiceActor.class , ws, config));

        serviceActor.tell(new ServiceActorProtocol.RequestMessage("test" , FreelanceAPI.SEARCH_TERM)
                , testProbe.getRef());
        serviceActor.tell(new ServiceActorProtocol.SingleProjectRequest("1000" , FreelanceAPI.SEARCH_TERM)
         , ActorRef.noSender());
    }
}
