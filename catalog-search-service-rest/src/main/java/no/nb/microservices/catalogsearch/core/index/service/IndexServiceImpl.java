package no.nb.microservices.catalogsearch.core.index.service;

import java.util.ArrayList;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.repository.IIndexRepository;
import no.nb.microservices.catalogsearch.rest.SearchRequest;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ronnymikalsen
 *
 */
@Service
public class IndexServiceImpl implements IIndexService {

    private final IIndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IIndexRepository indexRepository) {
        super();
        this.indexRepository = indexRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "defaultSearch")
    public SearchResult search(SearchRequest searchRequest, Pageable pageable) {
        SearchResource result = indexRepository.search(searchRequest.getQ(), searchRequest.getFields(), pageable.getPageNumber(), pageable.getPageSize(), searchRequest.getSort());

        List<String> ids = new ArrayList<>();
        for (ItemResource item : result.getEmbedded().getItems()) {
            ids.add(item.getItemId());
        }
        return new SearchResult(ids, result.getMetadata().getTotalElements());
    }

    public SearchResult defaultSearch(SearchRequest searchRequest, Pageable pageable) {
        return new SearchResult(new ArrayList<String>(), 0);
    }
}
