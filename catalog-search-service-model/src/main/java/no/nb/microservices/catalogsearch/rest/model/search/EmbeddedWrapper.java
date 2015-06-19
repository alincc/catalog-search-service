package no.nb.microservices.catalogsearch.rest.model.search;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class EmbeddedWrapper {
    private List<JsonNode> items = new ArrayList<>();

    public List<JsonNode> getItems() {
        return items;
    }

    public void setItems(List<JsonNode> items) {
        this.items = items;
    }
}
