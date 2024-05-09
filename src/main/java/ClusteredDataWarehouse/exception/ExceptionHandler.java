package ClusteredDataWarehouse.exception;

import ClusteredDataWarehouse.model.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleGenericException(Exception exception) {
        log.error("An unexpected error occurred: ", exception);
        ResponseError response = new ResponseError();
        response.setErrorStatus(BAD_REQUEST.value());
        response.setErrorDescription("An unexpected error occurred: " + exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({
            NoDealFoundException.class,
            DealExistsException.class,
            InvalidCurrencyException.class,
            FutureTimestampException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleSpecificException(RuntimeException exception) {
        log.error("Deal failed because: ", exception);
        ResponseError response = new ResponseError();
        response.setErrorStatus(BAD_REQUEST.value());
        response.setErrorDescription(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("There is a failure with request fields", exception);
        BindingResult result = exception.getBindingResult();
        ResponseError response = new ResponseError();
        response.setErrorStatus(BAD_REQUEST.value());
        response.setErrorDescription(getErrorDescription(result.getFieldErrors()));
        return ResponseEntity.badRequest().body(response);
    }


    private String getErrorDescription(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getDefaultMessage());
        }
        return String.join(", ", errors);
    }
}
