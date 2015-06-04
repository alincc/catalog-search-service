package no.nb.microservices.catalogsearch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;

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

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port: 0")
public class SearchControllerIT {

    @Value("${local.server.port}")
    int port;
    
    RestTemplate template = new TestRestTemplate();

    @Autowired
    ILoadBalancer lb;
    
	@Test
	public void testSearch() throws Exception {

	    MockWebServer server = new MockWebServer();
	    server.enqueue(new MockResponse().setBody("hello, world!"));
	    server.start();
	    
	    BaseLoadBalancer blb = (BaseLoadBalancer) lb;
	    blb.setServersList(Arrays.asList(new Server(server.getHostName(), server.getPort())));
	    
	    ResponseEntity<SearchResource> result = template.getForEntity("http://localhost:" + port + "/search?q=Ola&size=2", SearchResource.class);
	    
	    assertTrue(result.getStatusCode().is2xxSuccessful());
	    assertNotNull(result.getBody().getMetadata());
	    assertNotNull(result.getBody().getEmbedded());
	    assertNotNull(result.getBody().getLinks());

	}

}

@Configuration
class RibbonClientConfiguration {

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}
