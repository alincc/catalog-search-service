package no.nb.microservices.catalogsearch.core.index.repository;

import no.nb.microservices.catalogsearchindex.SearchResource;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "catalog-search-index-service")
public interface IIndexRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    SearchResource search(@RequestParam(value = "q") String query, Pageable pageable);

}
