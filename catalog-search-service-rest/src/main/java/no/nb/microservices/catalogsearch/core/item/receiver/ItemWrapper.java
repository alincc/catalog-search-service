package no.nb.microservices.catalogsearch.core.item.receiver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.htrace.Span;

import com.fasterxml.jackson.databind.JsonNode;

public class ItemWrapper {

    private String id;
    private CountDownLatch latch;
    private List<JsonNode> items;
    private Span span;
    
    private RequestInfo requestInfo = new RequestInfo();
    
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

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

}
