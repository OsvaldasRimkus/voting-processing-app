package voting.results.model.result;

import voting.model.District;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by domas on 2/22/17.
 */
@Entity
public abstract class DistrictResult extends Result {

    @OneToOne
    @JoinColumn(name = "district_id")
    private District district;

    private int confirmedCountyResults = 0;
    private int totalCountyResults;


    public void addCountyResult(CountyResult cr) {
        confirmedCountyResults++;
        super.combineResults(cr);
    }


    public void setDistrict(District district) {
        this.district = district;
        this.totalCountyResults = district.getCounties().size();
    }

    public District getDistrict() {
        return district;
    }

    public int getConfirmedCountyResults() {
        return confirmedCountyResults;
    }

    public int getTotalCountyResults() {
        return totalCountyResults;
    }
}
