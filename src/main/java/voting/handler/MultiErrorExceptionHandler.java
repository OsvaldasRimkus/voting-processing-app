package voting.handler;

import com.opencsv.exceptions.CsvException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import voting.exception.MultiErrorException;
import voting.exception.NotFoundException;
import voting.exception.StorageException;
import voting.exception.StorageFileNotFoundException;
import voting.exception.response.MultiErrorResponse;

import java.io.IOException;

/**
 * Created by domas on 2/13/17.
 */

@ControllerAdvice
public class MultiErrorExceptionHandler {

    @ExceptionHandler(MultiErrorException.class)
    public ResponseEntity<Object> handleMultiErrorException(MultiErrorException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MultiErrorResponse response = new MultiErrorResponse(status.value(), ex.getMessage(), ex.getErrors());
        return new ResponseEntity<Object>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler({CsvException.class, StorageException.class, IOException.class, NotFoundException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleSomeException(Exception ex) {
        HttpStatus status = null;
        if (ex instanceof CsvException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof StorageFileNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof StorageException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof IOException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.CONFLICT;
        }
        MultiErrorResponse body = new MultiErrorResponse(status.value(), ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        MultiErrorResponse body = new MultiErrorResponse(status.value(), ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }

}
