package no.nb.microservices.catalogsearch.rest.model.search;

import com.fasterxml.jackson.databind.JsonNode;
import no.nb.microservices.catalogsearchindex.AggregationResource;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class EmbeddedWrapper {
    private List<JsonNode> items = new ArrayList<>();
    private List<AggregationResource> aggregations = new ArrayList<>();

    public List<JsonNode> getItems() {
        return items;
    }

    public void setItems(List<JsonNode> items) {
        this.items = items;
    }

    public List<AggregationResource> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<AggregationResource> aggregations) {
        this.aggregations = aggregations;
    }
}
