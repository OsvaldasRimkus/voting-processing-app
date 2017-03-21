package voting.results.model.result;

import voting.model.County;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.Date;

/**
 * Created by domas on 2/22/17.
 */
@Entity
public abstract class CountyResult extends Result {

    @OneToOne
    @JoinColumn(name = "county_id")
    private County county;

    private boolean confirmed;
    private Date createdOn;
    private Date confirmedOn;

    public CountyResult() {
        super();
        this.confirmed = false;
        this.createdOn = Date.from(Instant.now());
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        if (confirmed) {
            this.confirmedOn = Date.from(Instant.now());
        } else {
            this.confirmedOn = null;
        }
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getConfirmedOn() {
        return confirmedOn;
    }

    @Override
    public String toString() {
        return super.toString() + "countyId=" + county.getId();
    }
}
