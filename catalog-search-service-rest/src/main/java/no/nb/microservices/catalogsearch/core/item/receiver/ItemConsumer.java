package no.nb.microservices.catalogsearch.core.item.receiver;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogsearch.core.item.model.IItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.event.Event;
import reactor.function.Consumer;

@Service
public class ItemConsumer implements Consumer<Event<ItemWrapper>> {

    private final IItemService itemService;
    
    @Autowired
    public ItemConsumer(IItemService itemService) {
        super();
        this.itemService = itemService;
    }

    @Override
    public void accept(Event<ItemWrapper> metadataWrapper) {
        try {
            ItemResource item = itemService.getById(metadataWrapper.getData().getId());
            metadataWrapper.getData().getItems().add(item);
        } finally {
            metadataWrapper.getData().getLatch().countDown();    
        }
        
    }

}
