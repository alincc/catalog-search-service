package no.nb.microservices.catalogsearch.core.search.exception;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class LatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LatchException(Exception ex) {
        super(ex);
    }


}
