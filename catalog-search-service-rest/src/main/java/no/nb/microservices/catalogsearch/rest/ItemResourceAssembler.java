package no.nb.microservices.catalogsearch.rest;

import no.nb.microservices.catalogsearch.core.search.model.Item;
import no.nb.microservices.catalogsearch.rest.model.search.ItemResource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ItemResourceAssembler extends ResourceAssemblerSupport<Item, ItemResource> {

    public ItemResourceAssembler() {
        super(SearchController.class, ItemResource.class);
    }
    
    @Override
    public ItemResource toResource(Item item) {
        ItemResource resource = createResourceWithId(item.getId(), item);
        
        return resource;
    }

}