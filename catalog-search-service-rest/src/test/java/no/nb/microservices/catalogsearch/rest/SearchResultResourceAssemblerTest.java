package no.nb.microservices.catalogsearch.rest;

import static org.junit.Assert.*;
import no.nb.microservices.catalogsearch.core.search.model.SearchResult;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResultResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SearchResultResourceAssemblerTest {

    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void testSingleResult() {
        final String id = "id1";
        
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();
        SearchResult searchResult = new SearchResult();
        searchResult.setId(id);
        
        SearchResultResource searchResultResource = searchResultResourceAssembler.toResource(searchResult);
        assertNotNull(searchResultResource);
        assertEquals("self", searchResultResource.getId().getRel());
        assertEquals("http://localhost/"+id, searchResultResource.getId().getHref());
        
        
    }
}
