package controllers;

import play.mvc.*;
import service.FreelancerAPIService;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import model.*;
import com.typesafe.config.Config;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller{

    private final WSClient ws;
    private final Config config;

    @Inject
    public HomeController(WSClient ws,Config config) {
        this.ws = ws;
        this.config = config;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(){
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> getSearchTerm(String query) {
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + query)
        .thenApply(result -> ok(result.asJson()));
    }

    public CompletionStage<Result> getOwnerDetails(String owner_id){
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.OWNER_PROFILE.getUrl() + owner_id + "?profile_description=true")
        .thenApply(result -> ok(result.asJson()));
    }

    public CompletionStage<Result> getSkillSearch(String skill_name) {
        return new FreelancerAPIService(ws, config).getAPIResult(FreelanceAPI.BASE_URL.getUrl() + FreelanceAPI.SEARCH_TERM.getUrl() + skill_name)
        .thenApply(result -> ok(result.asJson()));
    }

}
