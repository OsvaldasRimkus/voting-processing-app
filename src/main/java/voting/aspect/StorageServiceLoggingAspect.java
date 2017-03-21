package voting.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * Created by andrius on 2/27/17.
 */

@Aspect
@Component
public class StorageServiceLoggingAspect {

    private final Logger logger = Logger.getLogger(StorageServiceLoggingAspect.class);

    @Pointcut("execution(* voting.service.FileSystemStorageService.store(..))")
    void store() { }

    @AfterReturning(pointcut = "store()", returning = "returnValue")
    public void afterStoringFile(JoinPoint jp, Path returnValue) {
        MultipartFile file = (MultipartFile)jp.getArgs()[1];
        logger.debug(
                String.format("File [fileName: %s, type: %s] stored [location: %s] : %s",
                file.getOriginalFilename(), file.getContentType(), returnValue.toAbsolutePath(), jp.toLongString()));
    }

    @Pointcut("execution(* voting.service.FileSystemStorageService.storeTemporary(..))")
    void storeTemporary() { }

    @AfterReturning(
            pointcut = "storeTemporary()",
            returning = "returnValue",
            argNames = "jp,returnValue")
    public void afterStoringTempFile(JoinPoint jp, Path returnValue) {
        logger.debug(
                String.format("Temp file [fileName: %s] stored [location: %s] : %s",
                        returnValue.getFileName(), returnValue.toAbsolutePath(), jp.toLongString()));
    }

    @Pointcut(value = "execution(* voting.service.FileSystemStorageService.delete(..)) && args(filePath)", argNames = "filePath")
    void delete(Path filePath) { }

    @After(value = "delete(filePath)", argNames = "jp,filePath")
    public void afterDeleteTempFile(JoinPoint jp, Path filePath) {
        logger.debug(
                String.format("Temp file [fileName: %s] deleted [location: %s] : %s",
                        filePath.getFileName(), filePath.toAbsolutePath(), jp.toLongString()));
    }


}
