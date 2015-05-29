package no.nb.microservices.catalogsearch.core.metadata.receiver;

import org.springframework.stereotype.Service;

import reactor.event.Event;
import reactor.function.Consumer;

@Service
public class MetadataReceiver implements Consumer<Event<MetadataWrapper>> {

    @Override
    public void accept(Event<MetadataWrapper> metadataWrapper) {
        System.out.println("RECEIVER");
        metadataWrapper.getData().getLatch().countDown();
    }

}
