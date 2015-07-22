package no.nb.microservices.catalogsearch.core.search.service;

import java.io.Serializable;

import org.apache.htrace.Span;

import no.nb.htrace.core.Traceable;
import no.nb.microservices.catalogsearch.core.item.receiver.SecurityInfo;

public class TraceableId implements Traceable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Span span;
    private final SecurityInfo securityInfo;
    
    public TraceableId(Span span, String id, SecurityInfo securityInfo) {
        this.span = span;
        this.id = id;
        this.securityInfo = securityInfo;
    }
    
    @Override
    public Span getSpan() {
        return span;
    }

    public String getId() {
        return id;
    }

    public SecurityInfo getSecurityInfo() {
        return securityInfo;
    }

}
