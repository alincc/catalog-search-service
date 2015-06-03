package no.nb.microservices.catalogsearch.core.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.service.IIndexService;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.Event;
import reactor.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceImplTest {

    private SearchServiceImpl searchService;
    
    @Mock
    IIndexService indexService;
    
    @Before
    public void setUp() {
        Reactor reactor = Reactors.reactor().env(new Environment()).dispatcher(Environment.THREAD_POOL).get();
        Consumer<Event<ItemWrapper>> consumer = new ItemReceiverMock();
        searchService = new SearchServiceImpl(reactor, indexService, consumer);
        searchService.init();
    }
    
    @Test
    public void whenSearchingAndIndexReturnResultsThenResultShouldContainItems() {

        String query = "I love Okstindan";
        Pageable pageable = new PageRequest(0, 10);
        
        SearchResult searchResult = new SearchResult(Arrays.asList("1", "2"), 100);

        when(indexService.search(query, pageable )).thenReturn(searchResult);
        
        SearchAggregated result = searchService.search(query, pageable);
        
        assertNotNull("The result should not be null", result);
        assertEquals("The result size should be 2", 2, result.getPage().getContent().size());

    }

}
