package jp.co.axa.apidemo.exceptions;

/*
This exception is used for throwing in controller
 */
public class ApiException extends Throwable {

    public ApiException(String message) {
        super(message);
    }
}
