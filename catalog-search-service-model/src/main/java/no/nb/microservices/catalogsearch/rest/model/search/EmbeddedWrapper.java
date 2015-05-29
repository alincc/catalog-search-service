package no.nb.microservices.catalogsearch.rest.model.search;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedWrapper {
    private List<ItemResource> items = new ArrayList<>();

    public List<ItemResource> getItems() {
        return items;
    }

    public void setItems(List<ItemResource> items) {
        this.items = items;
    }
}
