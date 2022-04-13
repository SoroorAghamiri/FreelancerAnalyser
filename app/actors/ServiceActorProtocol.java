package actors;

import Helpers.FreelanceAPI;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.ws.WSClient;

public class ServiceActorProtocol {

    public static class RequestMessage {
        public final String query;
        public final FreelanceAPI apiEndpoint;

        public RequestMessage(String query, FreelanceAPI apiEndpoint) {
            this.query = query;
            this.apiEndpoint = apiEndpoint;
            }
        }

        public static class SingleProjectRequest {
            public final String id;
            public final FreelanceAPI apiEndpoint;

            public SingleProjectRequest(String id, FreelanceAPI apiEndpoint) {
                this.id = id;
                this.apiEndpoint = apiEndpoint;

            }
        }

        public static class JsonMessage {
            public final JsonNode jsonNode;

            public JsonMessage(JsonNode jsonNode) {
                this.jsonNode = jsonNode;

            }
        }
    }

