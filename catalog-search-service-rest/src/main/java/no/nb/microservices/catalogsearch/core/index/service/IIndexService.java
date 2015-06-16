package no.nb.microservices.catalogsearch.core.index.service;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;

import org.springframework.data.domain.Pageable;

public interface IIndexService {

    SearchResult search(SearchRequest searchRequest, Pageable pageable);
}
