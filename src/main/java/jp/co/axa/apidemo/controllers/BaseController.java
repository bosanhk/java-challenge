package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.exceptions.ApiException;
import jp.co.axa.apidemo.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected static final String API_KEY_PARAM = "X-Custom-ApiKey";

    /*
    Catch the JPA validation exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseDTO handleConstraintViolationException(ConstraintViolationException e) {

        List<String> errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return new ResponseDTO(false, errors.toString());
    }

    /*
    Catch the custom API exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseDTO handleApiException(ApiException e) {
        return new ResponseDTO(false, e.getMessage());
    }

    /*
    Catch the custom service exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseDTO handleServiceException(ServiceException e) {
        return new ResponseDTO(false, e.getMessage());
    }
}
