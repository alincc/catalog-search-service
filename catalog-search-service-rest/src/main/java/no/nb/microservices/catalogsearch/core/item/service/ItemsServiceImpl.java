package no.nb.microservices.catalogsearch.core.item.service;

import java.util.concurrent.Future;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.receiver.SecurityInfo;
import no.nb.microservices.catalogsearch.core.item.repository.IItemRepository;

@Service
public class ItemsServiceImpl implements IItemService {

    final IItemRepository itemRepository;
    
    @Autowired
    public ItemsServiceImpl(IItemRepository itemRepository) {
        super();
        this.itemRepository = itemRepository;
    }

    @Override
    @Async
    public Future<JsonNode> getById(ItemWrapper itemWrapper) {
        JsonNode item = null;
        try {
            SecurityInfo requestInfo = itemWrapper.getSecurityInfo();
        
            Trace.continueSpan(itemWrapper.getSpan());
            item = itemRepository.getById(itemWrapper.getId(),
                requestInfo.getxHost(), 
                requestInfo.getxPort(), 
                requestInfo.getxRealIp(), 
                requestInfo.getSsoToken());
        } finally {
            itemWrapper.getLatch().countDown();
        }
        return new AsyncResult<JsonNode>(item);
    }

    @HystrixCommand
    public JsonNode getDefaultItem(ItemWrapper itemWrapper) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", itemWrapper.getId());
        return node;
    }

}
