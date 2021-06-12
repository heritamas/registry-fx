package regfx;

import com.hortonworks.registries.schemaregistry.utils.ObjectMapperUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class HttpUtil {
    private static Logger log = Logger.getLogger("regfx");

    public static class Rest {
        private final static String[] parameterKeys = new String[]{"path", "query", "fragment"};
        private final URI registryURI;
        private final boolean invalidUri;

        private Rest() {
            this.registryURI = null;
            invalidUri = true;
        }

        private Rest(Map<String, String> props, String... optionals) throws URISyntaxException {
            Map<String, String> extendable = new HashMap<>();

            for (int i = 0; i < optionals.length; ++i) {
                extendable.put(parameterKeys[i], optionals[i]);
            }

            registryURI = new URI("http", null,
                    props.get("hostname"), Integer.parseInt(props.get("port")),
                    extendable.get("path"), extendable.get("query"), extendable.get("fragment"));
            invalidUri = false;
        }

        public static Rest of(Map<String, String> props, String... optionals) {
            Rest result;
            try {
                result = new Rest(props, optionals);
            } catch (URISyntaxException e) {
                log.throwing("HttpUtil.Rest", "of", e);
                log.warning("Won't make REST calls");
                result = new Rest();
            }
            return result;
        }

        public <T> Optional<T> execute(Class<T> klazz) {
            Optional<T> result = Optional.ofNullable(null);
            if (!invalidUri) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(registryURI)
                            .GET()
                            .build();

                    HttpResponse<String> response = HttpClient.newBuilder()
                            .build()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        result = Optional.of(ObjectMapperUtils.deserialize(response.<String>body(), klazz));
                    }

                } catch (Exception e) {
                    log.throwing("HttpUtil.Rest", "execute", e);
                }
            }

            return result;
        }

    }
}
