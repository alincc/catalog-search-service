package no.nb.microservices.catalogsearch.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class SearchResultResourceAssemblerTest {

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
  
    @Test
    public void whenOnAnyPageReturnValueShouldHaveAPageElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        
        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(2, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull("The page element should not be null", searchResultResource.getMetadata());
        assertEquals("The number should be 2", 2, searchResultResource.getMetadata().getNumber());
        assertEquals("The size should be 10", 10, searchResultResource.getMetadata().getSize());
        assertEquals("The total elements should be 1000", 1000, searchResultResource.getMetadata().getTotalElements());
        assertEquals("The total pages should be 100", 100, searchResultResource.getMetadata().getTotalPages());
        
    }
    
    @Test
    public void whenOnAnyPageReturnValueShouldHaveASelfLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Should have a self-referential link element", "self", searchResultResource.getId().getRel());
        
    }

    @Test
    public void whenOnFirstPageThenReturnValueShouldNotHaveAPreviousLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNull(searchResultResource.getLink(Link.REL_PREVIOUS));
        
    }

    @Test
    public void whenOnLastPageThenReturnValueShouldNotHaveANextLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(100, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNull(searchResultResource.getLink(Link.REL_NEXT));
        
    }

    @Test
    public void whenOnFirstPageAndSizeIsSmallerThanSizeThenReturnValueShouldHaveANextLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        ArrayList<ItemResource> items = new ArrayList<>();
        items.add(createItem("id1"));
        items.add(createItem("id2"));
        items.add(createItem("id3"));

        Page<ItemResource> page = new PageImpl<ItemResource>(items, new PageRequest(0, 2) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_NEXT));
        
    }

    @Test
    public void whenNotOnFirstPageThenReturnValueShouldHaveAFirstLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(2, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_FIRST));
        
    }

    @Test
    public void whenNotOnLastPageThenReturnValueShouldHaveALastLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(10, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_LAST));
        
    }

    @Test
    public void whenItNotOnLastPageThenReturnValueShouldHaveALastLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<ItemResource>(), new PageRequest(10, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_LAST));
        
    }

    @Test
    public void whenSearchResultHasItemsThenReturnValueShouldHaveItemsElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        ArrayList<ItemResource> items = new ArrayList<>();
        items.add(createItem("id1"));
        items.add(createItem("id2"));
        
        Page<ItemResource> page = new PageImpl<ItemResource>(items, new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Items should have 2 items", 2, searchResultResource.getEmbedded().getItems().size());
        
    }
    
    @Test
    public void whenSearchResultHasNoItemsThenReturnValueShouldHaveNoItems() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<ItemResource> page = new PageImpl<ItemResource>(new ArrayList<>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Items should have 0 items", 0, searchResultResource.getEmbedded().getItems().size());
        
    }
    
    private ItemResource createItem(String id) {
        ItemResource item = new ItemResource(id);
        return item;
    }
}
