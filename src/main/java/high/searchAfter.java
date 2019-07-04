package high;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class searchAfter {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        Object[] objects = new Object[]{"start","start"};
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        boolean type = true;
        while (type) {
            SearchHit[] hits = searchAfter(client, objects);
            objects = hits[hits.length-1].getSortValues();
            if (hits.length < 10000) type = false;
            for (SearchHit hit : hits) {
                data.add(hit.getSourceAsMap());
            }
        }
        Iterator<Map<String, Object>> iterator = data.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
        System.out.println(data.size() + "-----------------");
        client.close();
    }

    public static SearchHit[] searchAfter(RestHighLevelClient client, Object[] objects) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("name", "鬼谷子"));
        sourceBuilder.size(10000);
        sourceBuilder.sort("name", SortOrder.DESC);
        sourceBuilder.sort("_id", SortOrder.DESC);
        if(!objects[1].toString().equals("start") && !objects[1].toString().equals("start")) {
            sourceBuilder.searchAfter(objects);
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("phonebills");
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        return hits;
    }
}
