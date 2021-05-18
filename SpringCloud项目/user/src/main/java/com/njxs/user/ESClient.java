package com.njxs.user;

import com.google.gson.Gson;
import com.njxs.user.entity.UserInfo;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class ESClient {

    private RestHighLevelClient esClient;

    @Before
    public void createClient() {
        // 创建es客户端
        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
        );
    }

    @After
    public void closeClient() throws IOException {
        // 关闭客户端
        esClient.close();
    }

    @Test
    public void testIndex() throws IOException {
        // 创建索引
        CreateIndexRequest request = new CreateIndexRequest("emp");
        CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);
        boolean result = response.isAcknowledged();
        if (result) {
            System.out.println("索引创建成功...");
        } else {
            System.out.println("索引创建失败...");
        }
    }
    @Test
    public void queryIndex() throws IOException {
        //查询索引
        GetIndexRequest request = new GetIndexRequest("emp");
        GetIndexResponse response = esClient.indices().get(request, RequestOptions.DEFAULT);
        System.out.println(response.getAliases());
        System.out.println(response.getSettings());
        System.out.println(response.getMappings());
    }

    @Test
    public void deleteIndex() throws IOException {
        // 删除索引
        DeleteIndexRequest request = new DeleteIndexRequest("emp");
        AcknowledgedResponse response = esClient.indices().delete(request, RequestOptions.DEFAULT);
        boolean result = response.isAcknowledged();
        if (result) {
            System.out.println("索引删除成功");
        }else {
            System.out.println("索引删除失败");
        }
    }

    @Test
    public void createDoc() throws IOException {
        // 创建文档
        IndexRequest request = new IndexRequest();
        // 指定索引
        request.index("emp");
        // 指定文档id
        request.id("1");
        UserInfo userInfo = UserInfo.builder().account("hello")
                .name("你好")
                .telephone("1333333333").build();

        // 将对象转为json数据
        Gson gson = new Gson();
        String userJson = gson.toJson(userInfo);
        // 传入json数据
        request.source(userJson, XContentType.JSON);
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }
    @Test
    public void updateDoc() throws IOException {
        // 更新文档
        UpdateRequest request = new UpdateRequest();
        // 指定索引
        request.index("emp");
        // 指定文档id
        request.id("1");
        // 更新
        request.doc(XContentType.JSON,"telephone","188888888");
        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }
    @Test
    public void queryDoc() throws IOException {
        // 查询文档
        GetRequest request = new GetRequest();
        // 指定索引
        request.index("emp");
        // 指定id
        request.id("1");
        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }
    @Test
    public void deleteDoc() throws IOException {
        // 删除文档
        DeleteRequest request = new DeleteRequest();
        // 指定索引
        request.index("emp");
        // 指定id
        request.id("1");
        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @Test
    public void multiCreateDoc() throws IOException {
        // 批量创建文档
        BulkRequest request = new BulkRequest();
        // 创建对象
        UserInfo userInfo = UserInfo.builder().account("hello")
                .name("你好")
                .telephone("1333333333").build();
        UserInfo userInfo2 = UserInfo.builder().account("hello2")
                .name("你好2")
                .telephone("1444444444").build();
        UserInfo userInfo3 = UserInfo.builder().account("hello3")
                .name("你好3")
                .telephone("1555555555").build();
        // 添加到request中
        request.add(new IndexRequest().index("emp").id("1").source(new Gson().toJson(userInfo), XContentType.JSON));
        request.add(new IndexRequest().index("emp").id("2").source(new Gson().toJson(userInfo2), XContentType.JSON));
        request.add(new IndexRequest().index("emp").id("3").source(new Gson().toJson(userInfo3), XContentType.JSON));
        // 执行批量创建
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(Arrays.toString(response.getItems()));
    }
    @Test
    public void multiDeleteDoc() throws IOException {
        // 批量删除文档
        BulkRequest request = new BulkRequest();
        // 添加到request中
        request.add(new DeleteRequest().index("emp").id("1"));
        request.add(new DeleteRequest().index("emp").id("2"));
        request.add(new DeleteRequest().index("emp").id("3"));
        // 执行批量删除
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(Arrays.toString(response.getItems()));
    }
    @Test
    public void testQuery() throws IOException {
        // 高级查询
        SearchRequest request = new SearchRequest();
        // 指定索引
        request.indices("emp");
        // 指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());// 匹配所有
        request.source(searchSourceBuilder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits()); // 获取结果总记录数
        System.out.println(response.getTook()); // 获取查询耗费时间
        hits.forEach(System.out::println);
    }
    @Test
    public void testQueryWithCondition() throws IOException {
        // 高级查询
        SearchRequest request = new SearchRequest();
        // 指定索引
        request.indices("emp");
        // 指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.prefixQuery("telephone","155"));
        //分页
        searchSourceBuilder.from(0); // 指定页码
        searchSourceBuilder.size(2); // 指定每页的记录数
        request.source(searchSourceBuilder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        hits.forEach(System.out::println);
    }
}
