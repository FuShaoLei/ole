package ole.core.exception;

public class OleException extends Exception {

    public OleException(String message) {
        super("OleException = " + message);
    }
}
