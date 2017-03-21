package voting.dto.county;

import voting.dto.district.DistrictShortDTO;
import voting.dto.results.multimandate.CountyMMResultDTO;
import voting.dto.results.singlemandate.CountySMResultDTO;
import voting.model.County;

/**
 * Created by domas on 1/12/17.
 */
public class CountyDTO {

    private Long id;
    private String name;
    private Long voterCount;
    private String address;
    private DistrictShortDTO district;
    private CountySMResultDTO smResult;
    private CountyMMResultDTO mmResult;


    public CountyDTO(County c) {
        this.id = c.getId();
        this.name = c.getName();
        this.voterCount = c.getVoterCount();
        this.address = c.getAddress();
        this.district = new DistrictShortDTO(c.getDistrict());
        this.smResult = (c.getSmResult() == null) ? null : new CountySMResultDTO(c.getSmResult());
        this.mmResult = (c.getMmResult() == null) ? null : new CountyMMResultDTO(c.getMmResult());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getVoterCount() {
        return voterCount;
    }

    public String getAddress() {
        return address;
    }

    public DistrictShortDTO getDistrict() {
        return district;
    }

    public CountySMResultDTO getSmResult() {
        return smResult;
    }

    public CountyMMResultDTO getMmResult() {
        return mmResult;
    }
}
