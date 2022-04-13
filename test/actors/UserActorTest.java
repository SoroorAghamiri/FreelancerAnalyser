package actors;

import akka.actor.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.typesafe.sslconfig.ssl.FakeChainedKeyStore;
import org.junit.*;
import akka.testkit.javadsl.TestKit;
import play.ApplicationLoader;
import play.Environment;
import play.api.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.libs.Json;
import play.test.Helpers;

import javax.inject.Inject;

/**
 * Testing user actor
 * @author Soroor
 */
public class UserActorTest {
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
     * creates a user actor, sends a message of type TimeMessage
     */
    @Test
    public void test() {
        final TestKit testProbe = new TestKit(system);
        ActorRef serviceActor = null;
        ActorRef timerActor = system.actorOf(TimerActor.getProps(testProbe.getRef()));
        ActorRef userActor = system.actorOf(UserActor.props(testProbe.getRef() , timerActor), "User-actor");

        userActor.tell(new UserActor.TimeMessage("test") , ActorRef.noSender());
        final ObjectNode testResult = Json.newObject();
        testResult.put("time", "test");
        ObjectNode timeMessage = testProbe.expectMsgClass(ObjectNode.class);
        Assert.assertEquals(testResult , timeMessage);
    }
}
