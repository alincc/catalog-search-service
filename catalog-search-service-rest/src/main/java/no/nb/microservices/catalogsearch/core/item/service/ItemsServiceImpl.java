package no.nb.microservices.catalogsearch.core.item.service;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.receiver.RequestInfo;
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
    @HystrixCommand(fallbackMethod = "getDefaultItem")
    public JsonNode getById(ItemWrapper itemWrapper) {
        RequestInfo requestInfo = itemWrapper.getRequestInfo();
        
        Trace.continueSpan(itemWrapper.getSpan());
        try {
            return itemRepository.getById(itemWrapper.getId(), 
                requestInfo.getxHost(), 
                requestInfo.getxPort(), 
                requestInfo.getxRealIp(), 
                requestInfo.getSsoToken());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @HystrixCommand
    public JsonNode getDefaultItem(ItemWrapper itemWrapper) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", itemWrapper.getId());
        return node;
    }

}
