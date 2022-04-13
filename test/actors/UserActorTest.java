package actors;

import akka.actor.*;
import org.junit.*;
import akka.testkit.javadsl.TestKit;

public class UserActorTest {
    static ActorSystem system;
    ActorRef ref;

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
        ActorRef victim = null;
        ActorRef serviceActor = system.actorOf(ServiceActor.getProps());
        ActorRef timerActor = system.actorOf(TimerActor.getProps(serviceActor), "timeActor");
        ActorRef userActor = system.actorOf(UserActor.props(victim , timerActor), "User-actor");

        userActor.tell(new UserActor.SearchedTerm("test") , ActorRef.noSender());
        UserActor.SearchedTerm searchedTerm = testProbe.expectMsgClass(UserActor.SearchedTerm.class);
        Assert.assertEquals("test" , searchedTerm.keyword);
    }
}
