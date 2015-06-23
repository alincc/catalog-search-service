package no.nb.microservices.catalogsearch.core.item.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author ronnymikalsen
 *
 */
@FeignClient("catalog-item-service")
public interface IItemRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    JsonNode getById(@PathVariable("id") String id, @RequestParam("X-Forwarded-Host") String xHost, @RequestParam("X-Forwarded-Port") String xPort, @RequestParam("X-Original-IP-Fra-Frontend") String xRealIp, @RequestParam("amsso") String ssoToken);

}
