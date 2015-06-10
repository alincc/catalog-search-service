package no.nb.microservices.catalogsearch.rest;

import no.nb.microservices.catalogsearch.core.search.model.SearchAggregated;
import no.nb.microservices.catalogsearch.core.search.service.ISearchService;
import no.nb.microservices.catalogsearch.rest.model.search.SearchResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * 
 * @author ronnymikalsen
 *
 */
@RestController
@Api(value = "/", description = "Home api")
public class SearchController {

    private final ISearchService searchService;

    @Autowired
    public SearchController(ISearchService searchService) {
        super();
        this.searchService = searchService;
    }
    
    @InitBinder
    public void sortBinderInit(WebDataBinder binder) {
      binder.registerCustomEditor(String[].class, "sort", new StringArrayPropertyEditor(null));
    }  
    
    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<SearchResource> search(SearchRequest searchRequest, @PageableDefault Pageable pageable) {

        SearchAggregated result = searchService.search(searchRequest, pageable);

        SearchResource resource = new SearchResultResourceAssembler().toResource(result);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The request has a syntax error. Please see documentation at http://api.nb.no/v1/docs")
    private void handleBadRequests() {
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "It looks like we have a internal error in our application. The error have been logged and will be looked at by our development team")
    public void defaultHandler() {
        // TODO: Log error and notify
    }

}
