package no.nb.microservices.catalogsearch.core.item.receiver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import no.nb.microservices.catalogsearch.core.search.model.Item;

public class ItemWrapper {

    private String id;
    private CountDownLatch latch;
    private List<Item> items;
    
    public ItemWrapper(String id, CountDownLatch latch, List<Item> items) {
        super();
        this.id = id;
        this.latch = latch;
        this.items = items;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

}
