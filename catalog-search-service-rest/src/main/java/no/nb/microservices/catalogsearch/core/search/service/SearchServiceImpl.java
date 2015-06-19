package no.nb.microservices.catalogsearch.core.search.service;

import static reactor.event.selector.Selectors.$;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.service.IIndexService;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;
import no.nb.microservices.catalogsearch.core.search.exception.LatchException;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class SearchServiceImpl implements ISearchService {

    private final IIndexService indexService;
    private final Reactor reactor;
    
    private Consumer<Event<ItemWrapper>> itemConsumer;
    
    @Autowired
    public SearchServiceImpl(Reactor reactor, IIndexService indexService, Consumer<Event<ItemWrapper>> itemConsumer) {
        super();
        this.reactor = reactor;
        this.indexService = indexService;
        this.itemConsumer = itemConsumer;
    }

    @PostConstruct
    void init() {
        reactor.on($("item"), itemConsumer);
    }

    @Override
    public SearchAggregated search(SearchRequest searchRequest, Pageable pageable) {
        SearchResult result = indexService.search(searchRequest, pageable);

        List<JsonNode> items = consumeItems(result);
        
        Page<JsonNode> page = new PageImpl<JsonNode>(items, pageable, result.getTotalElements());
        return new SearchAggregated(page);
    }


    private List<JsonNode> consumeItems(SearchResult result) {
        final CountDownLatch latch = new CountDownLatch(result.getIds().size());

        List<JsonNode> items = Collections.synchronizedList(new ArrayList<>());
        for (String id : result.getIds()) {
            reactor.notify("item", Event.wrap(new ItemWrapper(id, latch, items)));
        }
        waitForAllItemsToFinish(latch);
        
        return items;
    }


    private void waitForAllItemsToFinish(final CountDownLatch latch) {

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new LatchException(e);
            }
    }

}
