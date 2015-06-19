package no.nb.microservices.catalogsearch.core.search.service;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import reactor.event.Event;
import reactor.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class ItemReceiverMock implements Consumer<Event<ItemWrapper>> {

    @Override
    public void accept(Event<ItemWrapper> itemWrapper) {
        itemWrapper.getData().getItems().add(createItem(itemWrapper.getData().getId()));
        itemWrapper.getData().getLatch().countDown();
    }

    private JsonNode createItem(String id) {
        ObjectNode item = JsonNodeFactory.instance.objectNode();
        item.put("id", id);
        return item;
    }

}
