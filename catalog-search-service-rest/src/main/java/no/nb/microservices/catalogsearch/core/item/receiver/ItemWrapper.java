package no.nb.microservices.catalogsearch.core.item.receiver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

public class ItemWrapper {

    private String id;
    private CountDownLatch latch;
    private List<ItemResource> items;
    
    public ItemWrapper(String id, CountDownLatch latch, List<ItemResource> items) {
        super();
        this.id = id;
        this.latch = latch;
        this.items = items;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<ItemResource> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

}
