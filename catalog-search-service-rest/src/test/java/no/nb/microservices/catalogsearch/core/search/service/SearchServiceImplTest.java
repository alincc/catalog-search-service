package no.nb.microservices.catalogsearch.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogsearch.core.index.model.SearchResult;
import no.nb.microservices.catalogsearch.core.index.service.IIndexService;
import no.nb.microservices.catalogsearch.core.item.receiver.ItemWrapper;
import no.nb.microservices.catalogsearch.core.item.service.SearchRequest;
import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * 
 * @author ronnymikalsen
 *
 */
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

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");

        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);

        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        
        RequestContextHolder.setRequestAttributes(attributes);
    }
    
    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void whenSearchingAndIndexReturnResultsThenResultShouldContainItems() {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest .setQ("I love Okstindan");
        Pageable pageable = new PageRequest(0, 10);
        
        SearchResult searchResult = new SearchResult(Arrays.asList("1", "2"), 100, null);

        when(indexService.search(searchRequest, pageable )).thenReturn(searchResult);
        
        SearchAggregated result = searchService.search(searchRequest, pageable);
        
        assertNotNull("The result should not be null", result);
        assertEquals("The result size should be 2", 2, result.getPage().getContent().size());

    }

}


