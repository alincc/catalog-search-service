package no.nb.microservices.catalogsearch.core.search.model;

import org.springframework.data.domain.Page;

public class SearchAggregated {

    private Page<Item> page;

    public SearchAggregated(Page<Item> page) {
        super();
        this.page = page;
    }

    public Page<Item> getPage() {
        return page;
    }

}
