package no.nb.microservices.catalogsearch.core.search.service;

import static reactor.event.selector.Selectors.$;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.service.IIndexService;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemConsumer;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.search.model.Item;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

@Service
public class SearchServiceImpl implements ISearchService {

    private final IIndexService indexService;
    private final Reactor reactor;
    
    private Consumer<Event<ItemWrapper>> itemConsumer;
    
    @PostConstruct
    void init() {
        reactor.on($("item"), itemConsumer);
    }

    
    @Autowired
    public SearchServiceImpl(Reactor reactor, IIndexService indexService, Consumer<Event<ItemWrapper>> itemConsumer) {
        super();
        this.reactor = reactor;
        this.indexService = indexService;
        this.itemConsumer = itemConsumer;
    }

    @Override
    public SearchAggregated search(String query, Pageable pageable) {
        
        SearchResult result = indexService.search(query, pageable);

        final CountDownLatch latch = new CountDownLatch(result.getIds().size());

        List<Item> items = consumeItemsAsync(result, latch);
        
        waitForAllItemsToFinish(latch);
        
        Page<Item> page = new PageImpl<Item>(items, pageable, result.getTotalElements());
        
        return new SearchAggregated(page);
    }


    private List<Item> consumeItemsAsync(SearchResult result, 
            final CountDownLatch latch) {
        List<Item> items = Collections.synchronizedList(new ArrayList<>());
        for (String id : result.getIds()) {
            reactor.notify("item", Event.wrap(new ItemWrapper(id, latch, items)));
        }
        return items;
    }


    private void waitForAllItemsToFinish(final CountDownLatch latch) {
        try {
            latch.await();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
