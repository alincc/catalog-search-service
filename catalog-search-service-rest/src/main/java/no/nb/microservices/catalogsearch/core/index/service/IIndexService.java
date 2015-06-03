package no.nb.microservices.catalogsearch.core.index.service;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;

import org.springframework.data.domain.Pageable;

public interface IIndexService {

    SearchResult search(String query, Pageable pageable);
}
