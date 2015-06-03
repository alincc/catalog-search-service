package no.nb.microservices.catalogsearch.core.metadata.receiver;

import org.springframework.stereotype.Service;

import reactor.event.Event;
import reactor.function.Consumer;

@Service
public class MetadataConsumer implements Consumer<Event<MetadataWrapper>> {

    @Override
    public void accept(Event<MetadataWrapper> metadataWrapper) {
        metadataWrapper.getData().getLatch().countDown();
    }

}
