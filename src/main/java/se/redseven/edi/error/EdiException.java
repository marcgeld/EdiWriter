package se.redseven.edi.error;

@SuppressWarnings("serial")
public class EdiException extends RuntimeException {

    public EdiException() {

        super();
    }

    public EdiException(String message) {

        super(message);
    }

    public EdiException(Throwable cause) {

        super(cause);
    }

    public EdiException(String message, Throwable cause) {

        super(message, cause);
    }

    public EdiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
