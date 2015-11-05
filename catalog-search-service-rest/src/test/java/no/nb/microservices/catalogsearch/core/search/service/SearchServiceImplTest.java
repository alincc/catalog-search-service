package no.nb.microservices.catalogsearch.core.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.service.IIndexService;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.service.IItemService;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceImplTest {

    @Mock
    IIndexService indexService;
    
    SearchServiceImpl searchService;
    
    @Before
    public void setUp() {
        searchService = new SearchServiceImpl(indexService, new ItemServiceStub());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");

        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);

        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }
    
    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void whenSearchingAndIndexReturnResultsThenResultShouldContainItems() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest .setQ("I love Okstindan");
        Pageable pageable = new PageRequest(0, 10);
        SearchResult searchResult = new SearchResult(Arrays.asList("1", "2"), 100, null);
        when(indexService.search(searchRequest, pageable )).thenReturn(searchResult);
        
        SearchAggregated result = searchService.search(searchRequest, pageable);
        
        assertNotNull("The result should not be null", result);
        assertEquals("The result size should be 2", 2, result.getPage().getContent().size());
    }

}

class ItemServiceStub implements IItemService {

    @Override
    public Future<JsonNode> getById(ItemWrapper itemWrapper) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new AsyncResult<JsonNode>(mapper.readTree("{\"id\": \"123\"}"));
        } catch (IOException e) {
        } finally {
            itemWrapper.getLatch().countDown();    
        }
        return null;
    }
    
}


