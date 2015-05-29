package no.nb.microservices.catalogsearch.core.search.service;

import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

import org.springframework.data.domain.Pageable;

public interface ISearchService {
    
    SearchAggregated search(String query, Pageable pageable);
    
}
