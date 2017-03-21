package voting.dto.results;


import voting.dto.county.CountyShortDTO;
import voting.results.model.result.CountyResult;

import java.util.Date;

/**
 * Created by andrius on 2/9/17.
 */

public abstract class CountyResultDTO extends ResultDTO {

    private CountyShortDTO county;
    private boolean confirmed;
    private Date createdOn;
    private Date confirmedOn;

    public CountyResultDTO(CountyResult result) {
        super(result);
        confirmed = result.isConfirmed();
        createdOn = result.getCreatedOn();
        confirmedOn = result.getConfirmedOn();
        county = new CountyShortDTO(result.getCounty());
        setVoterCount(result.getCounty().getVoterCount());
    }

    public CountyShortDTO getCounty() {
        return county;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getConfirmedOn() {
        return confirmedOn;
    }

}
