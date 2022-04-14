package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;

import java.util.concurrent.TimeoutException;

import static akka.pattern.Patterns.ask;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class SupervisorActorTest {
    static ActorSystem system;
    scala.concurrent.duration.Duration timeout =
            scala.concurrent.duration.Duration.create(5, SECONDS);
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
    public void superVisorTest() throws InterruptedException, TimeoutException {
        Props superprops = Props.create(SupervisorActor.class);
        ActorRef supervisor = system.actorOf(superprops, "supervisor");
        ActorRef child =
                (ActorRef) Await.result(ask(supervisor, Props.create(ServiceActor.class), 5000), timeout);

        child.tell(new Exception(), ActorRef.noSender());
        assertEquals(0, Await.result(ask(child, "get", 5000), timeout));
    }
}