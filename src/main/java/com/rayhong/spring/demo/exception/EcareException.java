package com.rayhong.spring.demo.exception;

public class EcareException  extends RuntimeException {

    public EcareException() {
        super();
    }
    public EcareException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EcareException(final String message) {
        super(message);
    }

    public EcareException(final Throwable cause) {
        super(cause);
    }
}
