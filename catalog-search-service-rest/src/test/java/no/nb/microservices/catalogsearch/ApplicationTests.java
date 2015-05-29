package no.nb.microservices.catalogsearch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port: 0")
public class ApplicationTests {

    @Value("${local.server.port}")
    int port;
    
    RestTemplate template = new TestRestTemplate();
    
	@Test
	public void testSearch() {
	    ResponseEntity<SearchResource> result = template.getForEntity("http://localhost:" + port + "/search?q=Ola&size=2", SearchResource.class);
	    
	    assertTrue(result.getStatusCode().is2xxSuccessful());
	    assertNotNull(result.getBody().getMetadata());
	    assertNotNull(result.getBody().getEmbedded());
	    assertNotNull(result.getBody().getLinks());
	}

}
