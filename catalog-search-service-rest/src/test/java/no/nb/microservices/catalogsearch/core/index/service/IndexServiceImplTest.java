package no.nb.microservices.catalogsearch.core.index.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.repository.IIndexRepository;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;
import no.nb.microservices.catalogsearchindex.EmbeddedWrapper;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources.PageMetadata;

@RunWith(MockitoJUnitRunner.class)
public class IndexServiceImplTest {

    private IndexServiceImpl indexService;
    
    @Mock
    IIndexRepository indexRepository;
    
    @Before
    public void setUp() {
        indexService = new IndexServiceImpl(indexRepository);
    }
    
    
    @Test
    public void whenBlaThenResult() {
        
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("I love a good book");
        
        EmbeddedWrapper wrapper = new EmbeddedWrapper();
        wrapper.getItems().add(createItem("id1"));
        wrapper.getItems().add(createItem("id2"));
        wrapper.getItems().add(createItem("id3"));

        Pageable pageable = new PageRequest(0, 10);
        PageMetadata page = new PageMetadata(wrapper.getItems().size(), 0, 3);
        SearchResource searchResult = new SearchResource(page );
        searchResult.setEmbedded(wrapper);
        
        
        /*
        when(indexRepository.search(searchRequest.getQ(), searchRequest.getFields() , pageable.getPageNumber(), pageable.getPageSize(), searchRequest.getSort())).thenReturn(searchResult);
        
        SearchResult result = indexService.search(searchRequest, pageable);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Total elements should be 3", 3, result.getTotalElements());
        assertEquals("Result should have 3 elements", 3, result.getIds().size());
        */
    }


    private ItemResource createItem(String id) {
        ItemResource item = new ItemResource();
        item.setItemId(id);
        return item;
    }

}
