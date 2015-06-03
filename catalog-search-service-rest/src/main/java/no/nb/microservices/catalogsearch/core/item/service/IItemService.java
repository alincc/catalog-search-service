package no.nb.microservices.catalogsearch.core.item.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

public interface IItemService {

    ItemResource getById(String id);

}
