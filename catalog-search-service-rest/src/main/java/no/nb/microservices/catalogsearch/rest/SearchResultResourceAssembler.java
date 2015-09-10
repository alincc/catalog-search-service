package no.nb.microservices.catalogsearch.rest;

import com.fasterxml.jackson.databind.JsonNode;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class SearchResultResourceAssembler implements ResourceAssembler<SearchAggregated, SearchResource> {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver = new HateoasPageableHandlerMethodArgumentResolver();

    @Override
    public SearchResource toResource(SearchAggregated result) {

        SearchResource resources = new SearchResource(asPageMetadata(result.getPage()));

        for (JsonNode item : result.getPage().getContent()) {
            resources.getEmbedded().getItems().add(item);
        }
        resources.getEmbedded().setAggregations(result.getAggregations());

        return addPaginationLinks(resources, result.getPage());
    }

    private SearchResource addPaginationLinks(SearchResource resources, Page<?> page) {
        
        UriTemplate base = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());

        if (page.hasPrevious()) {
            resources.add(createLink(base, new PageRequest(0, page.getSize(), page.getSort()), Link.REL_FIRST));
        }

        if (page.hasPrevious()) {
            resources.add(createLink(base, page.previousPageable(), Link.REL_PREVIOUS));
        }
        
        resources.add(createLink(base, null, Link.REL_SELF));

        if (page.hasNext()) {
            resources.add(createLink(base, page.nextPageable(), Link.REL_NEXT));
        }
        
        if (page.hasNext()) {

            int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;

            resources.add(createLink(base, new PageRequest(lastIndex, page.getSize(), page.getSort()), Link.REL_LAST));
        }
        
        return resources;
    }
    
    private Link createLink(UriTemplate base, Pageable pageable, String rel) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        pageableResolver.enhance(builder, null, pageable);

        return new Link(new UriTemplate(builder.build().toString()), rel);
    }
    
    private static <T> PageMetadata asPageMetadata(Page<T> page) {

        return new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }
}

