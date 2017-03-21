package voting.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import voting.dto.candidate.CandidateDTO;

/**
 * Created by andrius on 2/27/17.
 */

@Aspect
@Component
public class CandidateServiceLoggingAspect {

    private final Logger logger = Logger.getLogger(CandidateServiceLoggingAspect.class);

    @Pointcut("execution(* voting.controller.CandidateController.getCandidates())")
    void getCandidates() { }

    @After("getCandidates()")
    public void afterGetAllCandidates(JoinPoint jp) {
        logger.debug("All candidates requested : " + jp.toLongString());
    }

    @Pointcut("execution(* voting.controller.CandidateController.getCandidate(..))")
    void getCandidate() { }

    @AfterReturning(pointcut = "getCandidate()", returning = "returnValue")
    public void afterGetCandidate(JoinPoint jp, CandidateDTO returnValue) {
        logger.debug(String.format("Candidate [id: %d] requested : %s", returnValue.getId(), jp.toLongString()));
    }
}
