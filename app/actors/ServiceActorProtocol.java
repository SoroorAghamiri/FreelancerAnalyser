package actors;

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
}
