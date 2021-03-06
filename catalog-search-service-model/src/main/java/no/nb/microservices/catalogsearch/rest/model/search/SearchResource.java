package no.nb.microservices.catalogsearch.rest.model.search;

import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchResource extends ResourceSupport {
    @JsonProperty("page")
    private PageMetadata metadata;
    @JsonProperty("_embedded")
    private EmbeddedWrapper wrapper = new EmbeddedWrapper(); 

    @JsonCreator
    public SearchResource() {
    }
    
    public SearchResource(PageMetadata metadata) {
        super();
        this.metadata = metadata;
    }

    @JsonIgnore
    public PageMetadata getMetadata() {
        return metadata;
    }

    @JsonIgnore
    public EmbeddedWrapper getEmbedded() {
        return wrapper;
    }

}
