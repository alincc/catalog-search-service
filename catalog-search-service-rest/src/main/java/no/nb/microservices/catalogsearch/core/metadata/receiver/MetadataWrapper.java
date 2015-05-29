package no.nb.microservices.catalogsearch.core.metadata.receiver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import no.nb.microservices.catalogsearch.core.search.model.Item;

public class MetadataWrapper {

    private CountDownLatch latch;
    private List<Item> metadata;
    
    public MetadataWrapper(CountDownLatch latch, List<Item> metadata) {
        super();
        this.latch = latch;
        this.metadata = metadata;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<Item> getMetadata() {
        return metadata;
    }

}
