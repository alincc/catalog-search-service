package no.nb.microservices.catalogsearch.core.search.model;

import org.springframework.hateoas.Identifiable;

public class SearchResult implements Identifiable<String> {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
