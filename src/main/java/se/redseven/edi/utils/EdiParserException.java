package se.redseven.edi.utils;

public class EdiParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EdiParserException(String string) {

        super(string);
    }

    public EdiParserException(String errMsg, Exception ex) {

        super(errMsg, ex);
    }
}
