import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EsUtil {
    public static TransportClient getClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("client.transport.sniff", true).build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        return client;
    }
}
