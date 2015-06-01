package no.nb.microservices.catalogsearch.core.search.service;

import no.nb.microservices.catalogsearch.core.metadata.receiver.MetadataWrapper;
import no.nb.microservices.catalogsearch.core.search.model.Item;
import reactor.event.Event;
import reactor.function.Consumer;

public class MetadataReceiverMock implements Consumer<Event<MetadataWrapper>> {

    @Override
    public void accept(Event<MetadataWrapper> metadataWrapper) {
        metadataWrapper.getData().getMetadata().add(new Item(metadataWrapper.getData().getId()));
        metadataWrapper.getData().getLatch().countDown();
    }

}
