import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.UnknownHostException;

public class 分页与遍历 {
    public static void main(String[] args) throws UnknownHostException, FileNotFoundException {
        //System.setOut(new PrintStream(new File("./a.log")));
        int size = 1000;
        TransportClient client = EsUtil.getClient();
        QueryBuilder termQuery = QueryBuilders.termQuery("name", "鲁仲连");
        SearchRequestBuilder srb = client.prepareSearch("phonebills").setTypes("_doc");


        SearchResponse search = srb.setQuery(termQuery).addSort("name", SortOrder.ASC).addSort("_id", SortOrder.ASC).setSize(size).get();
        boolean type = true;
        int i = 0;
        while (type) {

            SearchHit[] hits = search.getHits().getHits();
            int len = hits.length;
            SearchHit endSearchHit = hits[len - 1];
            int id = endSearchHit.docId();
            System.out.println(id);
            String sortField = endSearchHit.getSourceAsMap().get("name").toString();
            System.out.println(sortField);
            for (SearchHit searchHit : hits) {
                System.out.println(searchHit);
                i++;
                System.out.println("sum :" + i);
            }
            if (len < size) {
                type = false;
            } else {
                search = srb.setQuery(termQuery).addSort("name", SortOrder.ASC).addSort("_id", SortOrder.ASC).setSize(size).searchAfter(new Object[]{sortField, id}).get();
            }
        }
        System.out.println("sum :" + i);
        client.close();
    }
}
