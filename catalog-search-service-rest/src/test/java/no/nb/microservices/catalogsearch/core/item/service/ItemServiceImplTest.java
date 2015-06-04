package no.nb.microservices.catalogsearch.core.item.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.TitelInfo;
import no.nb.microservices.catalogsearch.core.item.repository.IItemRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

        ItemResource itemResource = new ItemResource(id);
        Metadata metadata = new Metadata();
        TitelInfo titleInfo = new TitelInfo();
        titleInfo.setTitle(title);
        metadata.setTitleInfo(titleInfo);
        itemResource.setMetadata(metadata );

        when(itemRepository.getById(id)).thenReturn(itemResource );
        
        ItemResource item = itemService.getById(id);
        
        assertNotNull("Item should not be null", item);
        assertEquals("Title should be \"Bla Title\"", title, item.getMetadata().getTitleInfo().getTitle());
        
    }
    

}
