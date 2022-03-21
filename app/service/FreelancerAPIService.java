package service;

import play.mvc.*;
import java.util.concurrent.CompletionStage;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import com.typesafe.config.Config;

/**
 * FreelancerAPIService class to handale all API call
 * @author Kazi Asif Tanim
 */
public class FreelancerAPIService extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private final Config config;


    /**
     * This is a parametarized constractor to pass dependancies
     * @param ws WSClient to pass dependencies from controller
     * @param config Config object to pass dependencies from controller
     * @author Kazi Asif Tanim
     */
    @Inject
    public FreelancerAPIService(WSClient ws, Config config) {
        this.ws = ws;
        this.config = config;
    }

    /**
     * This method call's Freelancer.com API
     * @param url string URL of the API
     * @return a CompletionStage WSResponse response to controller
     * @author Kazi Asif Tanim
     */
    public CompletionStage<WSResponse> getAPIResult(String url){
        return ws
        .url(url)
        .addHeader("freelancer-oauth-v1", config.getString("FREELANCER_API_SECRECT"))
        .get();
    }
}
