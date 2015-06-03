package no.nb.microservices.catalogsearch.core.index.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "catalog-metadata-service")
public interface IIndexRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    String search(@RequestParam(value = "q") String query, Pageable pageable);

}
