package actors;

import Helpers.FreelanceAPI;
import akka.actor.AbstractActor;
import akka.actor.Props;

import com.typesafe.config.Config;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import service.FreelancerAPIService;
import static akka.pattern.Patterns.pipe;

import java.util.concurrent.CompletionStage;

public class ServiceActor extends AbstractActor {

    private WSClient ws;
    private Config config;

    public ServiceActor(WSClient ws, Config config)
    {
        this.ws = ws;
        this.config = config;
    }
    public static Props getProps() {
        return Props.create(ServiceActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ServiceActorProtocol.RequestMessage.class,
                        requestMessage -> {
                            CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config)
                                    .getAPIResult(FreelanceAPI.BASE_URL.getUrl()
                                    + requestMessage.apiEndpoint.getUrl() + requestMessage.query);

                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());

                            pipe(result, getContext().dispatcher()).to(sender());
                        })
                .match(
                        ServiceActorProtocol.SingleProjectRequest.class,
                        request ->
                        {
                            CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config)
                                    .getAPIResult(FreelanceAPI.BASE_URL.getUrl()
                                    + FreelanceAPI.SEARCH_TERM.getUrl() + request.id);

                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());

                            pipe(result, getContext().dispatcher()).to(sender());
                        })
                .match(
                        ServiceActorProtocol.ReadabilityRequest.class,
                        request ->
                        {
                            CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config)
                                    .getAPIResult(FreelanceAPI.BASE_URL.getUrl() + request.freelanceApi.getUrl() + request.query);

                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());

                            pipe(result, getContext().dispatcher()).to(sender());
                        })
                .match(
                        ServiceActorProtocol.SingleReadabilityRequest.class,
                        request ->
                        {
                            CompletionStage<WSResponse> response = new FreelancerAPIService(ws, config)
                                    .getAPIResult(FreelanceAPI.BASE_URL.getUrl() + request.freelanceApi.getUrl() + request.id);

                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());

                            pipe(result, getContext().dispatcher()).to(sender());
                        })
                .build();
    }
}
