package ca.uqac.io;

public class ScriptParserException extends RuntimeException {

    public ScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptParserException(Throwable cause) {
        super(cause);
    }
}
