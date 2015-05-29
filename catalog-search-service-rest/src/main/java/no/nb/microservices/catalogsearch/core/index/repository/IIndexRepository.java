package no.nb.microservices.catalogsearch.core.index.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface IIndexRepository {

    List<String> search(String query, Pageable pageable);

}
