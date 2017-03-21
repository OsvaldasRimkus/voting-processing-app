package voting.exception.response;

import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/13/17.
 */
public class MultiErrorResponse {

    private int errorCode;
    private String rootMessage;
    private List<String> errorsMessages;

    public MultiErrorResponse(int errorCode, String rootMessage, List<ObjectError> errors) {
        this.errorCode = errorCode;
        this.rootMessage = rootMessage;
        this.errorsMessages = errors.stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
    }

    public MultiErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.rootMessage = message;
        this.errorsMessages = new ArrayList<String>();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getRootMessage() {
        return rootMessage;
    }

    public void setRootMessage(String rootMessage) {
        this.rootMessage = rootMessage;
    }

    public List<String> getErrorsMessages() {
        return errorsMessages;
    }

    public void setErrorsMessages(List<String> errorsMessages) {
        this.errorsMessages = errorsMessages;
    }
}
