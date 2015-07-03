package no.nb.microservices.catalogsearch.core.item.service;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.receiver.RequestInfo;
import no.nb.microservices.catalogsearch.core.item.repository.IItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ItemsServiceImpl implements IItemService {

    final IItemRepository itemRepository;
    
    @Autowired
    public ItemsServiceImpl(IItemRepository itemRepository) {
        super();
        this.itemRepository = itemRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultItem")
    public JsonNode getById(ItemWrapper itemWrapper) {
        RequestInfo requestInfo = itemWrapper.getRequestInfo();
        
        System.out.println("SPAN");
        System.out.println(itemWrapper.getSpan());
        System.out.println(itemWrapper.getSpan().getTraceId());
        System.out.println(itemWrapper.getSpan().getSpanId());
        return itemRepository.getById(itemWrapper.getId(), 
                requestInfo.getxHost(), 
                requestInfo.getxPort(), 
                requestInfo.getxRealIp(), 
                requestInfo.getSsoToken(),
                itemWrapper.getSpan().getTraceId(),
                itemWrapper.getSpan().getSpanId());
    }

    @HystrixCommand
    public JsonNode getDefaultItem(ItemWrapper itemWrapper) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", itemWrapper.getId());
        return node;
    }

}
