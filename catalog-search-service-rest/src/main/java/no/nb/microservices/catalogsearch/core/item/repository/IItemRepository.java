package no.nb.microservices.catalogsearch.core.item.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author ronnymikalsen
 *
 */
@FeignClient("catalog-item-service")
public interface IItemRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    JsonNode getById(@PathVariable("id") String id);

}
