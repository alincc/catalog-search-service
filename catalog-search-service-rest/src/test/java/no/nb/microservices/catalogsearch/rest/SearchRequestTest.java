package no.nb.microservices.catalogsearch.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SearchRequestTest {

    @Test
    public void testRemoveEncoding() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSort(new String[]{"title%2Cdesc"});
        assertEquals("title,desc", searchRequest.getSort().get(0));
    }

}
