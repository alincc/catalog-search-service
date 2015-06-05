package no.nb.microservices.catalogsearch.core.search.model;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

import org.springframework.data.domain.Page;

public class SearchAggregated {

    private Page<ItemResource> page;

    public SearchAggregated(Page<ItemResource> page) {
        super();
        this.page = page;
    }

    public Page<ItemResource> getPage() {
        return page;
    }

}
