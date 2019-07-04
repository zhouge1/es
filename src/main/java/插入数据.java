import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class 插入数据 {
    public static void main(String[] args) throws IOException {
        String[] names = CreateData.getNames();
        int namesLen = names.length;
        String[] contens = CreateData.getSentences();
        int contensLen = contens.length;
        TransportClient client = EsUtil.getClient();
        Random random = new Random();
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                    }
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                    }
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                    }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

        for (int i = 0; i < 10000000; i++) {
            int n = random.nextInt(namesLen);
            int l = random.nextInt(namesLen);
            while (n == l) {
                l = random.nextInt(namesLen);
            }
            Map<String, Date> date = CreateData.getTime();
            Map<String, Double> pos = CreateData.getPos();
            bulkProcessor.add(new IndexRequest("phonebills", "_doc")
                    .source(jsonBuilder()
                            .startObject()
                            .field("name", names[n])
                            .field("listeners", names[l])
                            .field("starttime", date.get("start"))
                            .field("endtime", date.get("end"))
                            .field("position", new Double[]{pos.get("lon"), pos.get("lat")})
                            .field("content", contens[random.nextInt(contensLen)])
                            .endObject()
                    ));
        }
        // Flush any remaining requests
        bulkProcessor.flush();
        // Or close the bulkProcessor if you don't need it anymore
        bulkProcessor.close();
        // Refresh your indices
        client.admin().indices().prepareRefresh().get();
        // Now you can start searching!
        client.prepareSearch().get();
    }
}
