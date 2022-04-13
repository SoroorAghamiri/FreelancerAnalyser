package actors;

import Helpers.FreelanceAPI;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.ws.WSClient;

public class ServiceActorProtocol {

    public static class RequestMessage
    {
        public final String query;
        public final FreelanceAPI apiEndpoint;

        public RequestMessage(String query, FreelanceAPI apiEndpoint) {
            this.query = query;
            this.apiEndpoint = apiEndpoint;

        }
    }

    public static class SingleProjectRequest
    {
        public final String id;
        public final FreelanceAPI apiEndpoint;

        public SingleProjectRequest(String id, FreelanceAPI apiEndpoint) {
            this.id = id;
            this.apiEndpoint = apiEndpoint;

        }
    }

    public static class JsonMessage
    {
        public final JsonNode jsonNode;

        public JsonMessage(JsonNode jsonNode) {
            this.jsonNode = jsonNode;

        }
    }
    
    /**
     * Readability request actor class 
     * @author Kazi Asif Tanim
     * @params string query
     * @param FreelanceAPI freelanceApi
     */
    public static class ReadabilityRequest{
    	public final String query;
    	public final FreelanceAPI freelanceApi;
    	
    	public ReadabilityRequest(String query, FreelanceAPI freelanceApi) {
    		this.query = query;
    		this.freelanceApi = freelanceApi;
    	}
    }
    
    /**
     * Single Readability request actor class 
     * @author Kazi Asif Tanim
     * @params string id
     * @param FreelanceAPI freelanceApi
     */
    public static class SingleReadabilityRequest{
    	public final String id;
    	public final FreelanceAPI freelanceApi;
    	
    	public SingleReadabilityRequest(String id, FreelanceAPI freelanceApi) {
    		this.id = id;
    		this.freelanceApi = freelanceApi;
    	}
    }
}
