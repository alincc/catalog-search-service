package no.nb.microservices.catalogsearch.rest;

import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.core.search.service.ISearchService;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "/", description = "Home api")
public class SearchController {

    private final ISearchService searchService;

    @Autowired
    public SearchController(ISearchService searchService) {
        super();
        this.searchService = searchService;
    }
    
    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<SearchResource> search(
            @RequestParam(value="q", required=false) String query,
            @PageableDefault Pageable pageable) {
        SearchAggregated result = searchService.search(query, pageable);
        
        SearchResource resource = new SearchResultResourceAssembler().toResource(result);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
