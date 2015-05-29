package no.nb.microservices.catalogsearch.core.index.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class IndexRepositoryImpl implements IIndexRepository {

    @Override
    public List<String> search(String query, Pageable pageable) {
        return Arrays.asList("id1", "id2", "id3", "id4", "id5");
    }

}
