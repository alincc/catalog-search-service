package no.nb.microservices.catalogsearch.core.item.receiver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.JsonNode;

public class ItemWrapper {

    private String id;
    private CountDownLatch latch;
    private List<JsonNode> items;
    
    public ItemWrapper(String id, CountDownLatch latch, List<JsonNode> items) {
        super();
        this.id = id;
        this.latch = latch;
        this.items = items;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<JsonNode> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

}
