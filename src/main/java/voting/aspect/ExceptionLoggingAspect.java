package voting.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import voting.exception.MultiErrorException;

import java.util.stream.Collectors;

/**
 * Created by andrius on 2/26/17.
 */

@Aspect
@Component
public class ExceptionLoggingAspect {

    private final Logger logger = Logger.getLogger(ExceptionLoggingAspect.class);

    @Pointcut(value = "execution(* voting.handler.MultiErrorExceptionHandler.handleMultiErrorException(..)) && args(ex)")
    void handleMultiErrorException(MultiErrorException ex) { }

    @After(value = "handleMultiErrorException(ex)", argNames = "ex")
    public void afterMultiErrorExceptionThrown(MultiErrorException ex) {
        String errors = ex.getErrors().stream()
                                      .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                      .collect(Collectors.joining(", "));
        logger.warn(String.format("Resolved exception with root cause : %s [%s: {%s}] at %s",
                ex.getClass(), ex.getMessage(), errors, ex.getStackTrace()[0]));
    }

    @Pointcut(
            value = "execution(* voting.handler.MultiErrorExceptionHandler.*(..)) &&" +
            "!execution(* voting.handler.MultiErrorExceptionHandler.handleMultiErrorException(..)) && args(ex)")
    void handleSomeException(Exception ex) { }

    @After(value = "handleSomeException(ex)", argNames = "ex")
    public void afterSomeExceptionThrown(Exception ex) {
        String tester4 = ex.getClass().getName().substring(0, 4);
        Exception exception = ex;
        if (tester4.equals("java") || tester4.equals("voti")) {
            Exception cause = (Exception) ex.getCause();
            if (cause != null) exception = cause;
        }
        logger.warn(String.format("Resolved exception with root cause : %s [%s] at %s",
                exception.getClass(), exception.getMessage(), exception.getStackTrace()[0]));
    }
}
