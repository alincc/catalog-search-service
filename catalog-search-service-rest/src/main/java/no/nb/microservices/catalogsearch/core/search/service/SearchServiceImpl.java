package no.nb.microservices.catalogsearch.core.search.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;

import no.nb.microservices.catalogsearch.core.index.repository.IIndexRepository;
import no.nb.microservices.catalogsearch.core.metadata.receiver.MetadataReceiver;
import no.nb.microservices.catalogsearch.core.metadata.receiver.MetadataWrapper;
import no.nb.microservices.catalogsearch.core.search.model.Item;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import reactor.core.Reactor;
import reactor.event.Event;
import static reactor.event.selector.Selectors.$;

@Service
public class SearchServiceImpl implements ISearchService {

    private final IIndexRepository indexRepository;
    private final Reactor reactor;
    
    @Autowired
    private MetadataReceiver receiver;
    
    @PostConstruct
    private void init() {
        reactor.on($("services"), receiver);
    }

    
    @Autowired
    public SearchServiceImpl(Reactor reactor, IIndexRepository indexRepository) {
        super();
        this.reactor = reactor;
        this.indexRepository = indexRepository;
    }

    @Override
    public SearchAggregated search(String query, Pageable pageable) {
        
        List<String> result = indexRepository.search(query, pageable);
        List<Item> metadata = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(result.size());
        
        MetadataWrapper metadataWrapper = new MetadataWrapper(latch, metadata);
        
        for (String id : result) {
            reactor.notify("services", Event.wrap(metadataWrapper));
        }
        
        try {
            latch.await();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        Page<Item> page = new PageImpl<Item>(metadata, pageable, 1000);
        
        return new SearchAggregated(page);
    }

}
