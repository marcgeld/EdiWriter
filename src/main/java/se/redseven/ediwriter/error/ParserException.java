package se.redseven.ediwriter.error;

/**
 * ParserException.
 * @author ICC
 */
public class ParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * ParserException constructor.
     */
    public ParserException() {

        super();
    }

    /**
     * ParserException constructor with message.
     * @param message the message.
     */
    public ParserException(String message) {

        super(message);
    }

    /**
     * ParserException constructor with cause.
     * @param cause the cause.
     */
    public ParserException(Throwable cause) {

        super(cause);
    }

    /**
     * ParserException constructor with message and cause.
     * @param message the message.
     * @param cause the cause.
     */
    public ParserException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * ParserException constructor with message, cause, enableSuppression and writableStackTrace.
     * @param message the message
     * @param cause the cause
     * @param enableSuppression suppression enabled or disabled
     * @param writableStackTrace writable stack trace enabled or disabled
     */
    public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
