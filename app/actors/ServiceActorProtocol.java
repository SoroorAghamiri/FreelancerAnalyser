package actors;

import Helpers.FreelanceAPI;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.ws.WSClient;

/**
 * Class that acts as a wrapper to all messages sent by service actor
 * @author Haitham Abdel-Salam
 */
public class ServiceActorProtocol {

    /**
     * Class that represent a message sent by service actor that contains the query and specific endpoint
     * for general requests
     * @author Haitham Abdel-Salam
     */
    public static class RequestMessage
    {
        /**
         * Query or keyword
         */
        public final String query;

        /**
         * Specific endpoint
         */
        public final FreelanceAPI apiEndpoint;

        /**
         * Message class constructor
         * @param query keywrod
         * @param apiEndpoint freelancer api endpoint
         */
        public RequestMessage(String query, FreelanceAPI apiEndpoint) {
            this.query = query;
            this.apiEndpoint = apiEndpoint;

        }
    }

    /**
     * Class that represent a message sent by service actor that contains the endpoint and a specific id
     * @author Haitham Abdel-Salam
     */
    public static class SingleProjectRequest
    {
        /**
         * project id
         */
        public final String id;

        /**
         * end point for the api
         */
        public final FreelanceAPI apiEndpoint;

        /**
         * Constructor of Single Project request
         * @param id
         * @param apiEndpoint
         */
        public SingleProjectRequest(String id, FreelanceAPI apiEndpoint) {
            this.id = id;
            this.apiEndpoint = apiEndpoint;

        }
    }
}
