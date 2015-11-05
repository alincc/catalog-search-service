package no.nb.microservices.catalogsearch.core.item.service;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;

import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.JsonNode;

public interface IItemService {

    Future<JsonNode> getById(ItemWrapper itemWrapper);

}
