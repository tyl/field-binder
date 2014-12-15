package org.tylproject.vaadin.addon.datanav.events;

/**
 * Created by evacchi on 02/12/14.
 */
public class RejectOperationException extends RuntimeException {
    public static final RejectOperationException Instance = new RejectOperationException();

    private RejectOperationException() {}
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
