package no.nb.microservices.catalogsearch.core.item.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogsearch.core.item.repository.IItemRepository;

public class ItemsServiceImpl implements IItemService {

    final IItemRepository itemRepository;
    
    public ItemsServiceImpl(IItemRepository itemRepository) {
        super();
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemResource getById(String id) {
        return itemRepository.getById(id);
    }

}
