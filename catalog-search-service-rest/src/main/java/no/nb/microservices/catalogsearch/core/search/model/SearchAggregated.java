package no.nb.microservices.catalogsearch.core.search.model;

import com.fasterxml.jackson.databind.JsonNode;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import org.springframework.data.domain.Page;

import java.util.List;

public class SearchAggregated {

    private Page<JsonNode> page;
    private List<AggregationResource> aggregations;

    public SearchAggregated(Page<JsonNode> page, List<AggregationResource> aggregations) {
        super();
        this.page = page;
        this.aggregations = aggregations;
    }

    public Page<JsonNode> getPage() {
        return page;
    }

    public List<AggregationResource> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<AggregationResource> aggregations) {
        this.aggregations = aggregations;
    }
}
