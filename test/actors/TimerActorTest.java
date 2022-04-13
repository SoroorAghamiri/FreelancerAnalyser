package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.typesafe.config.Config;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.mvc.Action;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;

public class TimerActorTest {
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
     * sends messages to timer actor
     */
    @Test
    public void timerActorTest(){
        final TestKit testProbe = new TestKit(system);
        ActorRef timerActor = system.actorOf(TimerActor.getProps(testProbe.getRef()));

        timerActor.tell(new TimerActor.RegisterMessage() , testProbe.getRef());
        timerActor.tell(new TimerActor.NewSearch("test") , ActorRef.noSender());
        ServiceActorProtocol.RequestMessage expected = testProbe.expectMsgClass(ServiceActorProtocol.RequestMessage.class);
        Assert.assertNotNull(expected);


        ActorRef serviceActor = system.actorOf(MockServiceActor.getProps());
        ActorRef secondTimeActor = system.actorOf(TimerActor.getProps(serviceActor));
        secondTimeActor.tell(new TimerActor.NewSearch("test2") , testProbe.getRef());
        Assert.assertNull(testProbe.expectMsgClass(UserActor.TimeMessage.class));
    }
}
