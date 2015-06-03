package no.nb.microservices.catalogsearch.core.index.service;

import java.util.Arrays;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.repository.IIndexRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IIndexService {

    private final IIndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IIndexRepository indexRepository) {
        super();
        this.indexRepository = indexRepository;
    }

    @Override
    public SearchResult search(String query, Pageable pageable) {
        String result = indexRepository.search(query, pageable);
        System.out.println("result=" + result);
        return new SearchResult(Arrays.asList("id1, id2, id3"), 100);
    }

}
