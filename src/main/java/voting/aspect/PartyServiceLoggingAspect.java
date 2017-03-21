package voting.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import voting.dto.party.PartyDTO;
import voting.service.PartyService;
import voting.utils.Extractor;

import java.util.Arrays;

/**
 * Created by andrius on 2/27/17.
 */

@Aspect
@Component
public class PartyServiceLoggingAspect {

    private final PartyService partyService;
    private final Logger logger = Logger.getLogger(PartyServiceLoggingAspect.class);

    @Autowired
    public PartyServiceLoggingAspect(PartyService partyService) {
        this.partyService = partyService;
    }

    @Pointcut("execution(* voting.controller.PartyController.getParties())")
    void getParties() { }

    @After("getParties()")
    public void afterGetAllParties(JoinPoint jp) {
        logger.debug("All parties requested : " + jp.toLongString());
    }

    @Pointcut("execution(* voting.controller.PartyController.getParty(..)) && args(id)")
    void getParty(Long id) { }

    @After(value = "getParty(id)", argNames = "jp,id")
    public void afterGetPartyById(JoinPoint jp, Long id) {
        logger.debug(String.format("Political party requested [id: %d] : %s", id, jp.toLongString()));
    }

    @Pointcut("execution(* voting.controller.PartyController.saveParty(..))")
    void saveParty() { }

    @AfterReturning(pointcut = "saveParty()", returning = "returnValue")
    public void afterSavePartyWithCandidates(JoinPoint jp, PartyDTO returnValue) {
        String[] ids = Extractor.extractIdsFromCandidates(returnValue.getCandidates());
        logger.debug(String.format(
                "Political party created [id: %d] with candidates [ids: %s] : %s",
                returnValue.getId(), Arrays.toString(ids), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.PartyController.deleteParty(..)) && args(id)")
    void deleteParty(Long id) { }

    @Around(value = "deleteParty(id)", argNames = "jp,id")
    public void afterDeleteParty(ProceedingJoinPoint jp, Long id) throws Throwable {
        String[] orphansIds = Extractor.extractIdsFromCandidates(partyService.getParty(id), true);
        String[] nonOrphansIds = Extractor.extractIdsFromCandidates(partyService.getParty(id), false);

        jp.proceed();

        logger.debug(String.format(
                "Political party deleted [id: %d]. Candidates [ids: %s] deleted." +
                        " Associations with this party for other candidates [ids: %s] cleared : %s",
                id, Arrays.toString(orphansIds), Arrays.toString(nonOrphansIds), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.PartyController.deleteCandidateList(..)) && args(id)")
    void deleteCandidateList(Long id) { }

    @Around(value = "deleteCandidateList(id)", argNames = "jp,id")
    public void afterDeletingPartyCandidateList(ProceedingJoinPoint jp, Long id) throws Throwable {
        String[] orphansIds = Extractor.extractIdsFromCandidates(partyService.getParty(id), true);
        String[] nonOrphansIds = Extractor.extractIdsFromCandidates(partyService.getParty(id), false);

        jp.proceed();

        logger.debug(String.format(
                "Political party [id: %d] requested to delete candidates. Candidates [ids: %s] deleted." +
                        " Associations with this party for other candidates [ids: %s] cleared : %s",
                id, Arrays.toString(orphansIds), Arrays.toString(nonOrphansIds), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.PartyController.updateParty(..))")
    void updateParty() { }

    @AfterReturning(pointcut = "updateParty()", returning = "returnValue")
    public void afterUpdatingParty(JoinPoint jp, PartyDTO returnValue) {
        logger.debug(String.format(
                "Political party [id: %d] updated : %s",
                returnValue.getId(), jp.toLongString())
        );
    }
}
