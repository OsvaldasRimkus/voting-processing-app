package voting.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by domas on 2/14/17.
 */
public class MultiErrorException extends RuntimeException {

    private List<ObjectError> errors;

    public MultiErrorException(String message, List<ObjectError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

}