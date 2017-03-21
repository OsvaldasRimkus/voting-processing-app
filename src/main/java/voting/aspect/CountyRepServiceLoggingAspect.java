package voting.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import voting.dto.countyrep.CountyRepresentativeDTO;
import voting.model.CountyRep;
import voting.service.CountyRepService;

/**
 * Created by andrius on 2/27/17.
 */

@Aspect
@Component
public class CountyRepServiceLoggingAspect {

    private final CountyRepService countyRepService;
    private final Logger logger = Logger.getLogger(CountyRepServiceLoggingAspect.class);

    @Autowired
    public CountyRepServiceLoggingAspect(CountyRepService countyRepService) {
        this.countyRepService = countyRepService;
    }

    @Pointcut("execution(* voting.controller.CountyRepController.getCountyReps())")
    void getCountyReps() { }

    @After("getCountyReps()")
    public void afterGetAllCountyReps(JoinPoint jp) {
        logger.debug("All counties representatives requested : " + jp.toLongString());
    }

    @Pointcut("execution(* voting.controller.CountyRepController.getCountyRep(..)) && args(id)")
    void getCountyRep(Long id) { }

    @AfterReturning(pointcut = "getCountyRep(id)", argNames = "jp,id")
    public void afterGetCountyRep(JoinPoint jp, Long id) {
        logger.debug(String.format("County representative [id: %d] requested : %s", id, jp.toLongString()));
    }

    @Pointcut("execution(* voting.controller.CountyRepController.addNewCountyRep(..))")
    void addNewCountyRep() { }

    @AfterReturning(pointcut = "addNewCountyRep()", returning = "crr")
    public void afterAddNewCountyRep(JoinPoint jp, CountyRepresentativeDTO crr) {
        logger.debug(String.format(
                "County representative [id: %d] (in county [id: %d]) created : %s",
                crr.getId(), crr.getCounty().getId(), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.CountyRepController.deleteCountyRep(..)) && args(id)")
    void deleteCountyRep(Long id) { }

    @Around(value = "deleteCountyRep(id)", argNames = "jp,id")
    public void afterDeleteCountyRep(ProceedingJoinPoint jp, Long id) throws Throwable {
        CountyRep cr = countyRepService.getCountyRep(id);
        jp.proceed();

        logger.debug(String.format(
                "County representative [id: %d] (in county [id: %d]) deleted : %s",
                cr.getId(), cr.getCounty().getId(), jp.toLongString())
        );
    }
}
