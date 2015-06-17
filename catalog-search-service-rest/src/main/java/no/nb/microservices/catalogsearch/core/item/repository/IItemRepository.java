package no.nb.microservices.catalogsearch.core.item.repository;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author ronnymikalsen
 *
 */
@FeignClient("catalog-item-service")
public interface IItemRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ItemResource getById(@PathVariable("id") String id);

}
