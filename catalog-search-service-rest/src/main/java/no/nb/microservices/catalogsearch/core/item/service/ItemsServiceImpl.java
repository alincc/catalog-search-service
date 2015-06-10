package no.nb.microservices.catalogsearch.core.item.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
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
    public ItemResource getById(String id) {
        return itemRepository.getById(id);
    }

    public ItemResource getDefaultItem(String id) {
        return new ItemResource(id);
    }

}
