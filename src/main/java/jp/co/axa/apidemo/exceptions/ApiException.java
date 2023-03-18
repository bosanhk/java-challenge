package jp.co.axa.apidemo.exceptions;

public class ApiException extends Throwable {

    public ApiException(String message) {
        super(message);
    }
}
