package voting.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import voting.dto.candidate.CandidateDTO;
import voting.dto.county.CountyDTO;
import voting.dto.county.CountyData;
import voting.dto.district.DistrictDTO;
import voting.service.DistrictService;
import voting.utils.Extractor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andrius on 2/26/17.
 */

@Aspect
@Component
public class DistrictServiceLoggingAspect {

    private final DistrictService districtService;
    private final Logger logger = Logger.getLogger(DistrictServiceLoggingAspect.class);

    @Autowired
    public DistrictServiceLoggingAspect(DistrictService districtService) {
        this.districtService = districtService;
    }

    @Pointcut("execution(* voting.controller.DistrictController.getDistricts())")
    void getDistricts() { }

    @After("getDistricts()")
    public void afterGetAllDistricts(JoinPoint jp) {
        logger.debug("All districts requested : " + jp.toLongString());
    }

    @Pointcut("execution(* voting.controller.DistrictController.getDistrict(Long))")
    void getDistrictById() { }

    @AfterReturning(pointcut = "getDistrictById()", returning = "returnValue")
    public void afterGetDistrictById(JoinPoint jp, DistrictDTO returnValue) {
        logger.debug(String.format("District requested [id: %d] : %s", returnValue.getId(), jp.toLongString()));
    }

    @Pointcut("execution(* voting.controller.DistrictController.addNewDistrict(..))")
    void addNewDistrict() { }

    @AfterReturning(value = "addNewDistrict()", returning = "returnValue")
    public void afterCreatingNewDistrict(JoinPoint jp, DistrictDTO returnValue) {
        String[] ids = Extractor.extractIdsFromCounties(returnValue.getCounties());
        logger.debug(String.format(
                "District [id: %d] created with counties [ids: %s] : %s",
                returnValue.getId(), Arrays.toString(ids), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.DistrictController.updateDistrict(..))")
    void updateDistrict() { }

    @AfterReturning(value = "updateDistrict()", returning = "returnValue")
    public void afterUpdatingDistrict(JoinPoint jp, DistrictDTO returnValue) {
        logger.debug(String.format(
                "District [id: %d] updated : %s",
                returnValue.getId(), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.DistrictController.deleteDistrict(..)) && args(id)")
    void deleteDistrict(Long id) { }

    @AfterReturning(value = "deleteDistrict(id)", argNames = "jp,id")
    public void afterDeletingDistrict(JoinPoint jp, Long id) {
        logger.debug(String.format(
                "District deleted [id: %d] : %s",
                id, jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.DistrictController.deleteCandidateList(..)) && args(id)")
    void deleteCandidateList(Long id) { }

    @Around(value = "deleteCandidateList(id)", argNames = "jp,id")
    public void afterDeletingDistrictCandidateList(ProceedingJoinPoint jp, Long id) throws Throwable {
        String[] ids = Extractor.extractIdsFromCandidates(
                new DistrictDTO(districtService.getDistrict(id)).getCandidates()
        );
        jp.proceed();
        logger.debug(String.format(
                "District [id: %d] candidates removed [ids: %s] : %s",
                id, Arrays.toString(ids), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.DistrictController.setCandidateList(..))")
    void setCandidateList() { }

    @AfterReturning(value = "setCandidateList()", returning = "returnValue")
    public void afterSettingDistrictCandidates(JoinPoint jp, DistrictDTO returnValue) {
        String[] ids = Extractor.extractIdsFromCandidates(returnValue.getCandidates());
        logger.debug(String.format(
                "District [id: %d] uploaded candidates [ids: %s] : %s",
                returnValue.getId(), Arrays.toString(ids), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.DistrictController.getCandidateList(..)) && args(id)")
    void getCandidateList(Long id) { }

    @AfterReturning(value = "getCandidateList(id)", argNames = "jp,id,returnValue", returning = "returnValue")
    public void afterGettingDistrictCandidates(JoinPoint jp, Long id, List<CandidateDTO> returnValue) {
        String[] ids = Extractor.extractIdsFromCandidates(returnValue);
        logger.debug(String.format(
                "District [id: %d] requested its candidates [ids: %s] : %s",
                id, Arrays.toString(ids), jp.toLongString())
        );
    }

    @Pointcut("execution(* voting.controller.DistrictController.getCounties(..)) && args(id)")
    void getCounties(Long id) { }

    @AfterReturning(value = "getCounties(id)", argNames = "jp,id,returnValue", returning = "returnValue")
    public void afterGettingDistrictCounties(JoinPoint jp, Long id, List<CountyDTO> returnValue) {
        String[] ids = Extractor.extractIdsFromCounties(returnValue);
        logger.debug(String.format(
                "District [id: %d] requested its counties [ids: %s] : %s",
                id, Arrays.toString(ids), jp.toLongString())
        );
    }

    @Pointcut(value = "execution(* voting.controller.DistrictController.addCounty(..))")
    void addCounty() { }

    @AfterReturning(value = "addCounty()", returning = "returnValue")
    public void afterAddingCounty(JoinPoint jp, DistrictDTO returnValue) {
        String countyName = ((CountyData)jp.getArgs()[1]).getName();

        Long cid = returnValue.getCounties().stream()
                                            .filter(c -> c.getName().equals(countyName))
                                            .findFirst()
                                            .get()
                                            .getId();
        logger.debug(String.format(
                "County [id: %d] created [district id: %d] : %s",
                cid, returnValue.getId(), jp.toLongString())
        );
    }

    // CountyController
    @Pointcut("execution(* voting.controller.CountyController.getCounties())")
    void getAllCounties() { }

    @AfterReturning(value = "getAllCounties()")
    public void afterGettingCounties(JoinPoint jp) {
        logger.debug(String.format("All counties requested : %s", jp.toLongString()));
    }

    // CountyController
    @Pointcut("execution(* voting.controller.CountyController.getCounty(..)) && args(id)")
    void getCounty(Long id) { }

    @AfterReturning(value = "getCounty(id)", argNames = "jp,id")
    public void afterGetCounty(JoinPoint jp, Long id) {
        logger.debug(String.format("County [id: %d] requested : %s", id, jp.toLongString()));
    }

    // CountyController
    @Pointcut("execution(* voting.controller.CountyController.deleteCounty(..)) && args(id)")
    void deleteCounty(Long id) { }

    @AfterReturning(value = "deleteCounty(id)", argNames = "jp,id")
    public void afterDeleteCounty(JoinPoint jp, Long id) {
        logger.debug(String.format(
                "County [id: %d] deleted : %s",
                id, jp.toLongString())
        );
    }

    // CountyController
    @Pointcut("execution(* voting.controller.CountyController.updateCounty(..))")
    void updateCounty() { }

    @AfterReturning(value = "updateCounty()", returning = "returnValue")
    public void afterUpdateCounty(JoinPoint jp, CountyDTO returnValue) {
        logger.debug(String.format(
                "County [id: %d] updated : %s",
                returnValue.getId(), jp.toLongString())
        );
    }
}
