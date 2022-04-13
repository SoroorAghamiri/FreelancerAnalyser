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

public class OwnerProfileActorTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void test() {
        final TestKit testProbe = new TestKit(system);
        ActorRef serviceActor = system.actorOf(MockServiceActor.getProps());
        ActorRef ownerProfileActor = system.actorOf(OwnerProfileActor.getProps(serviceActor));

        CompletionStage<Object> response = FutureConverters.toJava(
                ask(ownerProfileActor , new ServiceActorProtocol.RequestMessage("test" , FreelanceAPI.OWNER_PROFILE) , 1000));
        Assert.assertNotNull(response);
    }
}

