package voting.dto.results;

import voting.dto.district.DistrictShortDTO;
import voting.model.District;
import voting.results.model.result.DistrictResult;
import voting.results.model.result.DistrictSMResult;
import voting.results.model.result.ResultType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/27/17.
 */
public abstract class DistrictResultDTO extends ResultDTO {

    private DistrictShortDTO district;
    private int confirmedCountyResults;
    private int totalCounties;
    private List<CountyResultSummaryDTO> countyResults;


    public DistrictResultDTO(DistrictResult result) {
        super(result);
        District district = result.getDistrict();
        this.district = new DistrictShortDTO(district);
        this.setVoterCount(district.getVoterCount());

        ResultType type = result instanceof DistrictSMResult ?
                          ResultType.SINGLE_MANDATE :
                          ResultType.MULTI_MANDATE;

        this.countyResults = district.getCounties().stream()
                .map(c -> new CountyResultSummaryDTO(c, c.getResultByType(type)))
                .collect(Collectors.toList());

        this.confirmedCountyResults = result.getConfirmedCountyResults();
        this.totalCounties = result.getTotalCountyResults();
    }

    public DistrictShortDTO getDistrict() {
        return district;
    }

    public int getConfirmedCountyResults() {
        return confirmedCountyResults;
    }

    public int getTotalCounties() {
        return totalCounties;
    }

    public List<CountyResultSummaryDTO> getCountyResults() {
        return countyResults;
    }
}
