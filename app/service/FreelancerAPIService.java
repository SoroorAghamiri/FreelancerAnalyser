package service;

import play.mvc.*;
import java.util.concurrent.CompletionStage;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import com.typesafe.config.Config;

public class FreelancerAPIService extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private final Config config;

    @Inject
    public FreelancerAPIService(WSClient ws, Config config) {
        this.ws = ws;
        this.config = config;
    }

    public CompletionStage<WSResponse> getAPIResult(String url){
        return ws
        .url(url)
        .addHeader("freelancer-oauth-v1", config.getString("FREELANCER_API_SECRECT"))
        .get();
    }
}
