package no.nb.microservices.catalogsearch.core.item.model;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

public interface IItemService {

    ItemResource getById(String id);

}
