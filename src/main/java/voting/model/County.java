package voting.model;

import voting.results.model.result.CountyMMResult;
import voting.results.model.result.CountyResult;
import voting.results.model.result.CountySMResult;
import voting.results.model.result.ResultType;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by domas on 1/10/17.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name", "district_id" }))
public class County {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
  
    private Long voterCount;
    private String address;



    @OneToOne(mappedBy = "county", cascade = CascadeType.ALL, orphanRemoval = true)
    private CountyMMResult mmResult;

    @OneToOne(mappedBy = "county", cascade = CascadeType.ALL, orphanRemoval = true)
    private CountySMResult smResult;



    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {}
    )
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @OneToOne(
            mappedBy = "county",
            cascade = {CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private CountyRep countyRep;

    public County() { }

    public County(String name, Long voterCount, String address) {
        this.name = name;
        this.voterCount = voterCount;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVoterCount() {
        return voterCount;
    }

    public void setVoterCount(Long voterCount) {
        this.voterCount = voterCount;
    }

    public CountyMMResult getMmResult() {
        return mmResult;
    }

    public void setMmResult(CountyMMResult mmResult) {
        this.mmResult = mmResult;
        if (mmResult != null) {
            mmResult.setCounty(this);
        }
    }

    public CountySMResult getSmResult() {
        return smResult;
    }

    public void setSmResult(CountySMResult smResult) {
        this.smResult = smResult;
        if (smResult != null) {
            smResult.setCounty(this);
        }
    }

    public CountyResult getResultByType(ResultType type) {
        if (type == ResultType.SINGLE_MANDATE) {
            return getSmResult();
        } else {
            return getMmResult();
        }
    }

    public void setResultByType(CountyResult result, ResultType type) {
        if (type == ResultType.SINGLE_MANDATE) {
            setSmResult((CountySMResult) result);
        } else {
            setMmResult((CountyMMResult) result);
        }
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public CountyRep getCountyRep() {
        return countyRep;
    }

    public void setCountyRep(CountyRep countyRep) {
        countyRep.setCounty(this);
        this.countyRep = countyRep;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        County county = (County) o;
        return Objects.equals(id, county.id) &&
                Objects.equals(name, county.name) &&
                Objects.equals(voterCount, county.voterCount) &&
                Objects.equals(address, county.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, voterCount, address);
    }

    @Override
    public String toString() {
        return String.format("%s (id %d), adresas - %s, balsuotojų kiekis - %d", name, id, address, voterCount);
    }
}
