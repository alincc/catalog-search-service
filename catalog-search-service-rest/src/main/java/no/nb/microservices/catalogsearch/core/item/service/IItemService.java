package no.nb.microservices.catalogsearch.core.item.service;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;

import com.fasterxml.jackson.databind.JsonNode;

public interface IItemService {

    JsonNode getById(ItemWrapper itemWrapper);

}
