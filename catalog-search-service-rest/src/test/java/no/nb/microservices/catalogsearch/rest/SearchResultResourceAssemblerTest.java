package no.nb.microservices.catalogsearch.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.FacetValueResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
    @Ignore("Ignored test due to bug resolving generics with sonar")
    public void whenOnAnyPageReturnValueShouldHaveAPageElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();


        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(2, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);

        /**
            SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
            assertNotNull("The page element should not be null", searchResultResource.getMetadata());
            assertEquals("The number should be 2", 2, searchResultResource.getMetadata().getNumber());
            assertEquals("The size should be 10", 10, searchResultResource.getMetadata().getSize());
            assertEquals("The total elements should be 1000", 1000, searchResultResource.getMetadata().getTotalElements());
            assertEquals("The total pages should be 100", 100, searchResultResource.getMetadata().getTotalPages());
         **/

    }
    
    @Test
    public void whenOnAnyPageReturnValueShouldHaveASelfLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Should have a self-referential link element", "self", searchResultResource.getId().getRel());
        
    }

    @Test
    public void whenOnFirstPageThenReturnValueShouldNotHaveAPreviousLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNull(searchResultResource.getLink(Link.REL_PREVIOUS));
        
    }

    @Test
    public void whenOnLastPageThenReturnValueShouldNotHaveANextLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(100, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNull(searchResultResource.getLink(Link.REL_NEXT));
        
    }

    @Test
    public void whenOnFirstPageAndSizeIsSmallerThanSizeThenReturnValueShouldHaveANextLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        ArrayList<JsonNode> items = new ArrayList<>();
        items.add(createItem("id1"));
        items.add(createItem("id2"));
        items.add(createItem("id3"));

        Page<JsonNode> page = new PageImpl<JsonNode>(items, new PageRequest(0, 2) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_NEXT));
        
    }

    @Test
    public void whenNotOnFirstPageThenReturnValueShouldHaveAFirstLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(2, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_FIRST));
        
    }

    @Test
    public void whenNotOnLastPageThenReturnValueShouldHaveALastLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(10, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_LAST));
        
    }

    @Test
    public void whenItNotOnLastPageThenReturnValueShouldHaveALastLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(10, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_LAST));
        
    }

    @Test
    public void whenSearchResultHasItemsThenReturnValueShouldHaveItemsElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        ArrayList<JsonNode> items = new ArrayList<>();
        items.add(createItem("id1"));
        items.add(createItem("id2"));
        
        Page<JsonNode> page = new PageImpl<JsonNode>(items, new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Items should have 2 items", 2, searchResultResource.getEmbedded().getItems().size());
        
    }
    
    @Test
    public void whenSearchResultHasNoItemsThenReturnValueShouldHaveNoItems() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null);
        
        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Items should have 0 items", 0, searchResultResource.getEmbedded().getItems().size());
        
    }

    @Test
    public void whenSearchResultsHasAggregationsThenAggregationShouldHaveNameAndFacetValues() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();
        Page<JsonNode> page = new PageImpl<JsonNode>(new ArrayList<JsonNode>(), new PageRequest(0, 10) , 1000);

        List<AggregationResource> aggregations = new ArrayList<>();

        AggregationResource a1 = new AggregationResource("ddc1");
        List<FacetValueResource> facetValues1 = new ArrayList<>();
        facetValues1.add(new FacetValueResource("01", 5));
        a1.setFacetValues(facetValues1);

        AggregationResource a2 = new AggregationResource("mediatype");
        List<FacetValueResource> facetValues2 = new ArrayList<>();
        facetValues2.add(new FacetValueResource("Bøker", 10));
        a2.setFacetValues(facetValues2);

        aggregations.add(a1);
        aggregations.add(a2);

        SearchAggregated searchAggregated = new SearchAggregated(page, aggregations);

        SearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);

        assertEquals("Should have 2 aggregations", 2, searchResultResource.getEmbedded().getAggregations().size());
        for (AggregationResource aggregationResource : searchResultResource.getEmbedded().getAggregations()) {
            assertNotNull("Aggragations should have name", aggregationResource.getName());
            assertNotNull("Aggregations facetValue should not be null", aggregationResource.getFacetValues());
        }

    }
    
    private JsonNode createItem(String id) {
        ObjectNode item = JsonNodeFactory.instance.objectNode();
        item.put("id", id);
        return item;
    }
}
