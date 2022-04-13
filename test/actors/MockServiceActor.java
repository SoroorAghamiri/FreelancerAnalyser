package actors;

import Helpers.FreelanceAPI;
import akka.actor.AbstractActor;
import akka.actor.Props;
import com.typesafe.config.Config;
import controllers.routes;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import service.FreelancerAPIService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.pipe;

public class MockServiceActor extends AbstractActor{
    @Inject
    Application application;
    public MockServiceActor()
    {
    }
    public static Props getProps() {
        return Props.create(MockServiceActor.class);
    }
    @Before
    public void setup() {
        Helpers.start(application);
    }
    @After
    public void teardown() {
        Helpers.stop(application);
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ServiceActorProtocol.RequestMessage.class,
                        requestMessage -> {
                            Call action = routes.HomeController.getSearchTerm("java");
                            Http.RequestBuilder request = Helpers.fakeRequest(action);
                            CompletionStage<Object> response = (CompletionStage<Object>) Helpers.route(application, request);

                            pipe(response, getContext().dispatcher()).to(sender());
                        })

                .build();
    }
}
//.match(
//                        ServiceActorProtocol.SingleProjectRequest.class,
//                        request ->
//                        {
//                            CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config)
//                                    .getAPIResult(FreelanceAPI.BASE_URL.getUrl()
//                                            + FreelanceAPI.SEARCH_TERM.getUrl() + request.id);
//
//                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());
//
//                            pipe(result, getContext().dispatcher()).to(sender());
//                        }
//                )