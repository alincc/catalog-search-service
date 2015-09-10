package no.nb.microservices.catalogsearch.core.index.repository;

import no.nb.microservices.catalogsearchindex.SearchResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
@FeignClient("catalog-search-index-service")
public interface IIndexRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    SearchResource search(@RequestParam("q") String q, @RequestParam("fields") String fields, @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize, @RequestParam("sort") List<String> sort, @RequestParam("aggs") String aggs);

}
