package no.nb.microservices.catalogsearch.core.search.model;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonNode;

public class SearchAggregated {

    private Page<JsonNode> page;

    public SearchAggregated(Page<JsonNode> page) {
        super();
        this.page = page;
    }

    public Page<JsonNode> getPage() {
        return page;
    }

}
