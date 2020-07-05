package com.rayhong.spring.demo.exception;

public class EcareBusyException extends RuntimeException {

    public EcareBusyException() {
        super();
    }
    public EcareBusyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EcareBusyException(final String message) {
        super(message);
    }

    public EcareBusyException(final Throwable cause) {
        super(cause);
    }
}
