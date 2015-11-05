package no.nb.microservices.catalogsearch.core.search.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.service.IIndexService;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.service.IItemService;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;
import no.nb.microservices.catalogsearch.core.search.exception.LatchException;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

@Service
public class SearchServiceImpl implements ISearchService {

    private final IIndexService indexService;
    private final IItemService itemService; 
    
    @Autowired
    public SearchServiceImpl(IIndexService indexService, IItemService itemService) {
        super();
        this.indexService = indexService;
        this.itemService = itemService;
    }

    @Override
    public SearchAggregated search(SearchRequest searchRequest, Pageable pageable) {
        SearchResult result = indexService.search(searchRequest, pageable);

        List<JsonNode> items = consumeItems(result);

        Page<JsonNode> page = new PageImpl<JsonNode>(items, pageable, result.getTotalElements());
        return new SearchAggregated(page, result.getAggregations());
    }

    private List<JsonNode> consumeItems(SearchResult result) {
        final CountDownLatch latch = new CountDownLatch(result.getIds().size());
        List<JsonNode> items = Collections.synchronizedList(new ArrayList<>());
        List<Future<JsonNode>> workList = new ArrayList<>();
        for (String id : result.getIds()) {
            ItemWrapper itemWrapper = createItemWrapper(latch, items, id);
            Future<JsonNode> item = itemService.getById(itemWrapper);
            workList.add(item);
        }
        
        waitForAllItemsToFinish(latch);
        
        for (Future<JsonNode> item : workList) {
            try {
                items.add(item.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return items;
    }

    private ItemWrapper createItemWrapper(final CountDownLatch latch, List<JsonNode> items, String id) {
        ItemWrapper itemWrapper = new ItemWrapper(id, latch, items);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        itemWrapper.getSecurityInfo().setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        itemWrapper.getSecurityInfo().setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        itemWrapper.getSecurityInfo().setxRealIp(UserUtils.getClientIp(request));
        itemWrapper.getSecurityInfo().setSsoToken(UserUtils.getSsoToken(request));
        itemWrapper.setSpan(Trace.currentSpan());

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