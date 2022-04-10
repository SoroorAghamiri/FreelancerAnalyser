package actors;

import Helpers.FreelanceAPI;
import Helpers.WordStat;
import akka.actor.AbstractActor;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import actors.ServiceActorProtocol.*;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import service.FreelancerAPIService;
import static akka.pattern.Patterns.pipe;

import java.util.concurrent.CompletionStage;

public class ServiceActor extends AbstractActor {


    public static Props getProps() {
        return Props.create(ServiceActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ServiceActorProtocol.RequestMessage.class,
                        requestMessage -> {
                            CompletionStage<WSResponse> response = new FreelancerAPIService(requestMessage.ws, requestMessage.config)
                                    .getAPIResult(FreelanceAPI.BASE_URL.getUrl()
                                    + FreelanceAPI.SEARCH_TERM.getUrl() + requestMessage.query);

                            CompletionStage<Object> result = response.thenApply(res-> res.asJson());

                            pipe(result, getContext().dispatcher()).to(sender());
                        })
                .build();
    }
}
