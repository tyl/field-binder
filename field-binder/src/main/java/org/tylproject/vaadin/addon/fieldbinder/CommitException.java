package org.tylproject.vaadin.addon.fieldbinder;

/**
 * Created by evacchi on 02/12/14.
 */
public class CommitException extends RuntimeException {
    public CommitException() {}

    public CommitException(String message) {
        super(message);
    }

    public CommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommitException(Throwable cause) {
        super(cause);
    }
}
