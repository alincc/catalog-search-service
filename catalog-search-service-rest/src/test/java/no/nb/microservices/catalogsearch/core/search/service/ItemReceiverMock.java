package no.nb.microservices.catalogsearch.core.search.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import reactor.event.Event;
import reactor.function.Consumer;

public class ItemReceiverMock implements Consumer<Event<ItemWrapper>> {

    @Override
    public void accept(Event<ItemWrapper> itemWrapper) {
        itemWrapper.getData().getItems().add(new ItemResource(itemWrapper.getData().getId()));
        itemWrapper.getData().getLatch().countDown();
    }

}
