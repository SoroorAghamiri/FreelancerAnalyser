package actors;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.ws.WSClient;

public class ServiceActorProtocol {

    public static class RequestMessage
    {
        public final String query;
        public final WSClient ws;
        public final Config config;

        public RequestMessage(String query, WSClient ws, Config config) {
            this.query = query;
            this.ws = ws;
            this.config = config;

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
