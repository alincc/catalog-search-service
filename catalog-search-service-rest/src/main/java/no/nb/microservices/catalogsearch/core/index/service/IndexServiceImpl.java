package no.nb.microservices.catalogsearch.core.index.service;

import java.util.ArrayList;
import java.util.List;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.repository.IIndexRepository;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;

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
        SearchResource result = indexRepository.search(query, pageable);

        List<String> ids = new ArrayList<>();
        for (ItemResource item : result.getEmbedded().getItems()) {
            ids.add(item.getItemId());
        }
        return new SearchResult(ids, result.getMetadata().getTotalElements());
    }

}
