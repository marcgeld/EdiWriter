package se.redseven.ediwriter.error;

@SuppressWarnings("serial")
public class ParserException extends RuntimeException {

    public ParserException() {

        super();
    }

    public ParserException(String message) {

        super(message);
    }

    public ParserException(Throwable cause) {

        super(cause);
    }

    public ParserException(String message, Throwable cause) {

        super(message, cause);
    }

    public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
