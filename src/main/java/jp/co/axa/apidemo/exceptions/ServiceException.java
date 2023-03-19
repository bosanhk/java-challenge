package jp.co.axa.apidemo.exceptions;

/*
This exception is used for throwing in service
 */
public class ServiceException extends Throwable {

    public ServiceException(String message) {
        super(message);
    }
}
