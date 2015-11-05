package no.nb.microservices.catalogsearch.core.item.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.repository.IItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    private IItemService itemService;

    @Mock
    IItemRepository itemRepository;

    @Before
    public void setup() {
        itemService = new ItemsServiceImpl(itemRepository);
    }

    @Test
    public void whenGetItemByIdSuccessThenReturnItem() {
        final String id = "id123";
        final String title = "Blah Title";

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("title", title);
        
        
        ItemWrapper itemWrapper = new ItemWrapper(id, null, null);

        when(itemRepository.getById(id, null, null, null, null)).thenReturn(node);
/*
        JsonNode item = itemService.getById(itemWrapper);
        
        assertNotNull("Item should not be null", item);
        
        assertEquals("Title should be \"Bla Title\"", title, node.get("title").asText());
  */      
    }
    

}
