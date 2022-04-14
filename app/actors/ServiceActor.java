package actors;

import Helpers.FreelanceAPI;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.typesafe.config.Config;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import service.FreelancerAPIService;
import static akka.pattern.Patterns.pipe;

import java.util.concurrent.CompletionStage;

/**
 * Service Actor class that handles Api calls
 * @author Haitham Abdel-Salam
 */
public class ServiceActor extends AbstractActor {

    /**
     * WSClient
     */
    private WSClient ws;
    /**
     * Config
     */
    private Config config;
    int state = 0;

    /**
     * Service Actor Constructor
     * @param ws WSClient
     * @param config Config
     */
    public ServiceActor(WSClient ws, Config config)
    {
        this.ws = ws;
        this.config = config;
    }

    /**
     * Creating a new service actor
     * @return
     */
    public ServiceActor(){
        super();
    }

    public static Props getProps() {
        return Props.create(ServiceActor.class);
    }

    /**
     * Receives a Request msg with the query and API endpoint and if the message if matched it returns a JSON
     * as a result of a completion Stage
     * Receives a message from a single project request with endpoint and project ID
     * @return Receive
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Exception.class,
                        exception -> {
                            throw exception;
                        })
                .match(Integer.class, i -> state = i)
                .matchEquals("get", s -> getSender().tell(state, getSelf()))
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
                                            + request.apiEndpoint.getUrl() + request.id);

                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());

                            pipe(result, getContext().dispatcher()).to(sender());
                        }
                )
                .build();
    }
}
