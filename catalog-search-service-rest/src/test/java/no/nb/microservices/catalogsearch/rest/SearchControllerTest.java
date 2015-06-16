package no.nb.microservices.catalogsearch.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.core.search.service.ISearchService;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author ronnymikalsen
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private ISearchService searchService;

    private SearchController searchController;

    @Before
    public void init() {
        
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    
    @Before
    public void setup() {
        searchController = new SearchController(searchService);
    }

    @Test
    public void whenSearchThenReturnItems() {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("Supersonic");
        
        PageRequest pageable = new PageRequest(0, 10);
        List<ItemResource> items = Arrays.asList(new ItemResource("id1"), new ItemResource("id2"));
        
        SearchAggregated searchResult = new SearchAggregated(new PageImpl<ItemResource>(items, pageable, 100));
        when(searchService.search(searchRequest, pageable)).thenReturn(searchResult);
        
        ResponseEntity<SearchResource> result = searchController.search(searchRequest, pageable);
        
        assertNotNull("Search result should not be null", result);
        assertTrue("Status code should be successful", result.getStatusCode().is2xxSuccessful());
        assertEquals("It should have two items", 2, result.getBody().getEmbedded().getItems().size());

    }

}
