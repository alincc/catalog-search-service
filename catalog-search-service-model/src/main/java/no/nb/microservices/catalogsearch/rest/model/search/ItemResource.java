package no.nb.microservices.catalogsearch.rest.model.search;

import org.springframework.hateoas.ResourceSupport;

public class ItemResource extends ResourceSupport {
    private String title = "Tester";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
