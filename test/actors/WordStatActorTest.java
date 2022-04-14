package actors;
import Helpers.FreelanceAPI;
import Helpers.WordStat;
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

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Testing Word stats actor
 * @author Haitham Abdel-Salam
 */
public class WordStatActorTest {

    static ActorSystem system;
    static JsonNode allProjectsNode;
    static JsonNode singleProjectNode;
    /**
     * setting up actor system before running the test and setting up json nodes at the same time to be tested against
     */
    @BeforeClass
    public static void setup() throws Exception{
        system = ActorSystem.create();

        Path allProjectsResourceDirectory = Paths.get("test","resources","projects.json");
        allProjectsNode = readFromFile(allProjectsResourceDirectory);

        Path singleProjectResourceDirectory = Paths.get("test","resources","project.json");
        singleProjectNode = readFromFile(singleProjectResourceDirectory);
    }

    /**
     * reads json from json file and returns a json node
     */
    private static JsonNode readFromFile(Path resourceDirectory) throws Exception
    {
        List<String> lines = Files.readAllLines(resourceDirectory, Charset.defaultCharset());
        String jsonString = lines.stream().collect(Collectors.joining("\n"));

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }

    /**
     * stopping test kit from working after the tests are done
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Testing all project working after sending a msg to the word stats actor returning a json node
     */
    @Test
    public void testWordStatActorWithGLobalStats(){
        final TestKit testProbe = new TestKit(system);
        ActorRef wordStatActor = system.actorOf(WordStatsActor.getProps(testProbe.getRef()));

        CompletionStage<Object> response = FutureConverters.toJava(
                ask(wordStatActor , new ServiceActorProtocol.RequestMessage("test" , FreelanceAPI.WORD_STATS) , 1000));
        Assert.assertNotNull(response);

        final Props props = Props.create(WordStatsActor.class ,testProbe.getRef());
        final TestActorRef<WordStatsActor> ref = TestActorRef.create(system, props, "testB");
        final WordStatsActor actor = ref.underlyingActor();
        Assert.assertNotNull(WordStat.processAllProjectsStats(allProjectsNode));
    }

    /**
     * Testing a single project project working after sending a msg to the word stats actor returning a json node
     */
    @Test
    public void testWordStatActorWithSingleProjectStats() throws ExecutionException, InterruptedException {
        final TestKit testProbe = new TestKit(system);
        ActorRef wordStatActor = system.actorOf(WordStatsActor.getProps(testProbe.getRef()));

        CompletableFuture<Object> response = (CompletableFuture<Object>) FutureConverters.toJava(
                ask(wordStatActor ,
                        new ServiceActorProtocol.SingleProjectRequest("test" ,
                                FreelanceAPI.PROJECT_BY_ID) , 1000));

        Assert.assertNotNull(response);

        final Props props = Props.create(WordStatsActor.class ,testProbe.getRef());
        final TestActorRef<WordStatsActor> ref = TestActorRef.create(system, props, "testA");
        final WordStatsActor actor = ref.underlyingActor();
        Assert.assertNotNull(WordStat.processProjectStats(singleProjectNode));
    }

}
