package no.nb.microservices.catalogsearch.rest.model.search;

import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResource extends ResourceSupport {
    private PageMetadata metadata;
    private EmbeddedWrapper wrapper = new EmbeddedWrapper(); 

    @JsonCreator
    public SearchResource() {
    }
    
    public SearchResource(PageMetadata metadata) {
        super();
        this.metadata = metadata;
    }

    @JsonProperty("page")
    public PageMetadata getMetadata() {
        return metadata;
    }

    @JsonProperty("_embedded")
    public EmbeddedWrapper getEmbedded() {
        return wrapper;
    }

}