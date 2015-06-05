package no.nb.microservices.catalogsearch.core.index.model;

import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class SearchResult {

    private long totalElements;
    private List<String> ids;

    public SearchResult(List<String> ids, long totalElements) {
        super();
        this.ids = ids;
        this.totalElements = totalElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<String> getIds() {
        return ids;
    }

}
