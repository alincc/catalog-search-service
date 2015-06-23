package no.nb.microservices.catalogsearch.core.search.service;

import static reactor.event.selector.Selectors.$;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author ronnymikalsen
 *
 */
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
            ItemWrapper itemWrapper = createItemWrapper(latch, items, id);

            reactor.notify("item", Event.wrap(itemWrapper ));
        }
        waitForAllItemsToFinish(latch);
        
        return items;
    }

    private ItemWrapper createItemWrapper(final CountDownLatch latch,
            List<JsonNode> items, String id) {
        ItemWrapper itemWrapper = new ItemWrapper(id, latch, items);
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        itemWrapper.getRequestInfo().setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        itemWrapper.getRequestInfo().setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        itemWrapper.getRequestInfo().setxRealIp(UserUtils.getClientIp(request));
        itemWrapper.getRequestInfo().setSsoToken(UserUtils.getSsoToken(request));
        return itemWrapper;
    }


    private void waitForAllItemsToFinish(final CountDownLatch latch) {

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new LatchException(e);
            }
    }

}
