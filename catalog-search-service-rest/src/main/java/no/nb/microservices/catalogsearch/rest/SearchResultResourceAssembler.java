package no.nb.microservices.catalogsearch.rest;

import no.nb.microservices.catalogsearch.core.search.model.SearchResult;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResultResource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class SearchResultResourceAssembler extends ResourceAssemblerSupport<SearchResult, SearchResultResource> {

    public SearchResultResourceAssembler() {
        super(SearchController.class, SearchResultResource.class);
    }
    
    @Override
    public SearchResultResource toResource(SearchResult searchResult) {
        SearchResultResource resource = createResourceWithId(searchResult.getId(), searchResult);
        
        return resource;
    }

}
