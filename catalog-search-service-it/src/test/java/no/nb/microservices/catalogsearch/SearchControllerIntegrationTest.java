package no.nb.microservices.catalogsearch;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port: 0")
public class SearchControllerIntegrationTest {

    @Value("${local.server.port}")
    int port;

    RestTemplate template = new TestRestTemplate();

    @Autowired
    ILoadBalancer lb;

    MockWebServer server;

    @Before
    public void setup() throws Exception {
        String searchResultMock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-search-index-service.json"));
        String searchResultMock2 = IOUtils.toString(this.getClass().getResourceAsStream("catalog-search-index-service-2.json"));
        String itemId1Mock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-item-service-id1.json"));
        String itemId2Mock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-item-service-id2.json"));
        String itemId3Mock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-item-service-id3.json"));
        String itemId4Mock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-item-service-id4.json"));

        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/search?q=Ola&fields=-title&page=0&size=10&sort=title%2Cdesc")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().equals("/search?q=Svenno&fields=-title&page=0&size=10&sort=title%2Cdesc")){
                    return new MockResponse().setBody(searchResultMock2).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().equals("/id1")){
                    return new MockResponse().setBody(itemId1Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                } else if (request.getPath().equals("/id2")){
                    return new MockResponse().setBody(itemId2Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                } else if (request.getPath().equals("/id3")){
                    return new MockResponse().setBody(itemId3Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                } else if (request.getPath().equals("/id4")){
                    return new MockResponse().setBody(itemId4Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        server.start();

        BaseLoadBalancer blb = (BaseLoadBalancer) lb;
        blb.setServersList(Arrays.asList(new Server(server.getHostName(), server.getPort())));
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testSearch() throws Exception {
        ResponseEntity<SearchResource> result = template.getForEntity("http://localhost:" + port + "/?q=Ola&fields=-title&size=10&sort=title,desc", SearchResource.class);

        assertTrue("Status code should be 200 ", result.getStatusCode().is2xxSuccessful());
        assertNotNull("Response should have page element", result.getBody().getMetadata());
        assertNotNull("Response should have _embedded element", result.getBody().getEmbedded());
        assertNotNull("Response should have links", result.getBody().getLinks());
        assertEquals("It should be 4 elements in items array", 4, result.getBody().getEmbedded().getItems().size());
    }

    @Test
    public void testSearchIndexServiceOffline() throws Exception {
        ResponseEntity<SearchResource> hystrixResult = template.getForEntity("http://localhost:" + port + "/?q=Kalle&fields=-title&size=10&sort=title,desc", SearchResource.class);

        assertTrue(hystrixResult.getStatusCode().is2xxSuccessful());
        assertNotNull(hystrixResult.getBody().getMetadata());
        assertNotNull(hystrixResult.getBody().getEmbedded());
        assertNotNull(hystrixResult.getBody().getLinks());
        assertEquals(0, hystrixResult.getBody().getEmbedded().getItems().size());
    }

    @Test
    @Ignore("unstable")
    public void testItemServiceOffline() throws Exception {
        ResponseEntity<SearchResource> hystrixResult = template.getForEntity("http://localhost:" + port + "/?q=Svenno&fields=-title&size=10&sort=title,desc", SearchResource.class);

        assertTrue(hystrixResult.getStatusCode().is2xxSuccessful());
        assertNotNull(hystrixResult.getBody().getMetadata());
        assertNotNull(hystrixResult.getBody().getEmbedded());
        assertNotNull(hystrixResult.getBody().getLinks());
        assertEquals(4, hystrixResult.getBody().getEmbedded().getItems().size());
    }
}

@Configuration
class RibbonClientConfiguration {

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}