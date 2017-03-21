package voting.dto.results;

import voting.dto.district.DistrictShortDTO;
import voting.model.District;
import voting.results.model.result.DistrictResult;

/**
 * Created by domas on 2/27/17.
 */
public abstract class DistrictResultSummaryDTO {

    private DistrictShortDTO district;
    private ResultShortDTO result;
    private int confirmedCountyResults = 0;
    private int totalCounties = 0;
    private Long voterCount;
    private Long validBallots = 0L;
    private Long totalBallots = 0L;
    private Long spoiledBallots = 0L;


    public DistrictResultSummaryDTO(DistrictResult result) {
        District district = result.getDistrict();
        this.district = new DistrictShortDTO(district);
        this.result = new ResultShortDTO(result);
        this.confirmedCountyResults = result.getConfirmedCountyResults();
        this.totalCounties = result.getTotalCountyResults();
        this.voterCount = district.getVoterCount();
        this.validBallots = result.getValidBallots();
        this.totalBallots = result.getTotalBallots();
        this.spoiledBallots = result.getSpoiledBallots();
    }


    public DistrictShortDTO getDistrict() {
        return district;
    }

    public ResultShortDTO getResult() {
        return result;
    }

    public int getConfirmedCountyResults() {
        return confirmedCountyResults;
    }

    public int getTotalCounties() {
        return totalCounties;
    }

    public Long getVoterCount() {
        return voterCount;
    }

    public Long getValidBallots() {
        return validBallots;
    }

    public Long getTotalBallots() {
        return totalBallots;
    }

    public Long getSpoiledBallots() {
        return spoiledBallots;
    }
}
