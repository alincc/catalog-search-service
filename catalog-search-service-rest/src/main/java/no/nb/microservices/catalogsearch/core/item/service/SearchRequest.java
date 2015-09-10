package no.nb.microservices.catalogsearch.core.item.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author ronnymikalsen
 *
 */
public class SearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String q;
    private String fields;
    private List<String> sort = new ArrayList<>();
    private String aggs;

    public SearchRequest() {
        super();
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public List<String> getSort() {
        removeEncoding();
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = Arrays.asList(sort);
    }

    public String getAggs() {
        return aggs;
    }

    public void setAggs(String aggs) {
        this.aggs = aggs;
    }

    private void removeEncoding() {
        if (sort != null) {
            for(int i = 0; i < sort.size(); i++) {
                sort.set(i, sort.get(i).replace("%2C", ","));
            }
        }
    }

}
