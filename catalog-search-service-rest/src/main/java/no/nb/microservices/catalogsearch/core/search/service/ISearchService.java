package no.nb.microservices.catalogsearch.core.search.service;

import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.rest.SearchRequest;

import org.springframework.data.domain.Pageable;

public interface ISearchService {
    
    SearchAggregated search(SearchRequest searchRequest, Pageable pageable);
    
}
