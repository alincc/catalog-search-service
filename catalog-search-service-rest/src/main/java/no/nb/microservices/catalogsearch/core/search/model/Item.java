package no.nb.microservices.catalogsearch.core.search.model;

import org.springframework.hateoas.Identifiable;

public class Item implements Identifiable<String> {

    private String id;

    public Item(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
