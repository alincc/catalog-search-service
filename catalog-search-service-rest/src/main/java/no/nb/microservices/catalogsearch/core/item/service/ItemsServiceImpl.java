package no.nb.microservices.catalogsearch.core.item.service;

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
    public ItemResource getById(String id) {
        return itemRepository.getById(id);
    }

}
