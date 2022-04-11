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

        public final WSClient ws;
        public final Config config;
        public final FreelanceAPI url;

        public RequestMessage(String query, WSClient ws, Config config , FreelanceAPI url) {
            this.query = query;
            this.ws = ws;
            this.config = config;
            this.url = url;
        }
    }

    public static class JsonMessage
    {
        public final JsonNode jsonNode;

        public JsonMessage(JsonNode jsonNode) {
            this.jsonNode = jsonNode;

        }
    }
}
