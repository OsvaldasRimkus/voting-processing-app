package voting.dto.results;

import voting.dto.county.CountyShortDTO;
import voting.model.County;
import voting.results.model.result.CountyResult;

import java.util.Date;

/**
 * Created by domas on 3/7/17.
 */
public class CountyResultSummaryDTO {

    private CountyShortDTO county;
    private ResultShortDTO result;
    private Date createdOn;
    private Long voterCount;
    private Long validBallots;
    private Long totalBallots;
    private Long spoiledBallots;

    public CountyResultSummaryDTO(County county, CountyResult result) {
        this.county = new CountyShortDTO(county);
        this.voterCount = county.getVoterCount();
        this.result = (result != null) ? new ResultShortDTO(result) : null;

        if (result != null && result.isConfirmed()) {
            this.createdOn = result.getCreatedOn();
            this.validBallots = result.getValidBallots();
            this.totalBallots = result.getTotalBallots();
            this.spoiledBallots = result.getSpoiledBallots();
        }
    }

    public CountyShortDTO getCounty() {
        return county;
    }

    public ResultShortDTO getResult() {
        return result;
    }

    public Date getCreatedOn() {
        return createdOn;
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
