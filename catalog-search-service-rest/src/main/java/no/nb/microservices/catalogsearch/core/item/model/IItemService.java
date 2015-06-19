package no.nb.microservices.catalogsearch.core.item.model;

import com.fasterxml.jackson.databind.JsonNode;

public interface IItemService {

    JsonNode getById(String id);

}
