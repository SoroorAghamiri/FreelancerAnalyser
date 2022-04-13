package actors;

import Helpers.FreelanceAPI;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

import java.util.List;
import java.util.concurrent.CompletionStage;

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

    @Test
    public void testSkillActor(){
        final TestKit testProbe = new TestKit(system);
        ActorRef serviceActor = system.actorOf(MockServiceActor.getProps());
        ActorRef skillActor = system.actorOf(SkillActor.getProps(serviceActor));

        CompletionStage<Object> response = FutureConverters.toJava(
                ask(skillActor , new ServiceActorProtocol.RequestMessage("test" , FreelanceAPI.SEARCH_TERM) , 1000));
        Assert.assertNotNull(response);
    }
}
