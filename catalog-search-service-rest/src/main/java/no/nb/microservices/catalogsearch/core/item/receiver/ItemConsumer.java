package no.nb.microservices.catalogsearch.core.item.receiver;

import org.springframework.stereotype.Service;

import reactor.event.Event;
import reactor.function.Consumer;

@Service
public class ItemConsumer implements Consumer<Event<ItemWrapper>> {

    @Override
    public void accept(Event<ItemWrapper> metadataWrapper) {
        metadataWrapper.getData().getLatch().countDown();
    }

}
