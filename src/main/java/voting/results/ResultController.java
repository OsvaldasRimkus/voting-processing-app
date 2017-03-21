package voting.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import voting.dto.results.*;
import voting.dto.results.multimandate.CountyMMResultDTO;
import voting.dto.results.multimandate.DistrictMMResultDTO;
import voting.dto.results.multimandate.MMSummaryDTO;
import voting.dto.results.singlemandate.CountySMResultDTO;
import voting.dto.results.singlemandate.DistrictSMResultDTO;
import voting.dto.results.singlemandate.DistrictSmResultSummaryDTO;
import voting.exception.MultiErrorException;
import voting.results.model.result.CountyMMResult;
import voting.results.model.result.CountySMResult;
import voting.results.model.result.DistrictMMResult;
import voting.results.model.result.DistrictSMResult;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andrius on 1/24/17.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/results")
public class ResultController {

    private ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping(path = "county/{id}/single-mandate")
    public CountySMResultDTO getCountySingleMandateResult(@PathVariable("id") Long countyId) {
        CountySMResult result = resultService.getCountySmResult(countyId);
        if (result == null) {
            return null;
        }
        return new CountySMResultDTO(result);
    }

    @GetMapping(path = "county/{id}/multi-mandate")
    public CountyMMResultDTO getCountyMultiMandateResult(@PathVariable("id") Long countyId) {
        CountyMMResult result = resultService.getCountyMmResult(countyId);
        if (result == null) {
            return null;
        }
        return new CountyMMResultDTO(result);
    }

    @GetMapping(path = "district/{id}/single-mandate")
    public DistrictSMResultDTO getDistrictSingleMandateResult(@PathVariable("id") Long districtId) {
        DistrictSMResult result = resultService.getDistrictSmResult(districtId);
        return new DistrictSMResultDTO(result);
    }

    @GetMapping(path = "district/{id}/multi-mandate")
    public DistrictMMResultDTO getDistrictMultiMandateResult(@PathVariable("id") Long districtId) {
        DistrictMMResult result = resultService.getDistrictMmResult(districtId);
        return new DistrictMMResultDTO(result);
    }

    @GetMapping(path = "single-mandate")
    public List<DistrictSmResultSummaryDTO> getSingleMandateResultsSummary() {
        return resultService.getAllDistrictSmResults()
                    .stream()
                    .map(DistrictSmResultSummaryDTO::new)
                    .collect(Collectors.toList());
    }

    @GetMapping(path = "multi-mandate")
    public MMSummaryDTO getMultiMandateResultsSummary() {
        return new MMSummaryDTO(resultService.getConsolidatedResults());
    }

    @GetMapping(path = "consolidated")
    public ConsolidatedResultsDTO getConsolidatedResults() {
        return new ConsolidatedResultsDTO(resultService.getConsolidatedResults());
    }

    @PreAuthorize("hasRole('ROLE_REPRESENTATIVE')")
    @PostMapping(path = "county/single-mandate")
    public CountySMResultDTO addCountySMResult(@Valid @RequestBody CountyResultData resultData, BindingResult result) {
        if (result.hasErrors()) {
            throw new MultiErrorException("Klaida apylinkės rezultatuose!", result.getAllErrors());
        }
        return new CountySMResultDTO(resultService.addCountySmResult(resultData));
    }

    @PreAuthorize("hasRole('ROLE_REPRESENTATIVE')")
    @PostMapping(path = "county/multi-mandate")
    public CountyMMResultDTO addCountyMMResult(@Valid @RequestBody CountyResultData resultData, BindingResult result) {
        if (result.hasErrors()) {
            throw new MultiErrorException("Klaida apylinkės rezultatuose!", result.getAllErrors());
        }
        return new CountyMMResultDTO(resultService.addCountyMmResult(resultData));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "confirm")
    public void confirmCountyResult(@RequestParam Long resultId) {
        resultService.confirmCountyResult(resultId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "{id}")
    public void deleteCountyResult(@PathVariable(name = "id") Long resultId) {
        resultService.deleteCountyResult(resultId);
    }

}