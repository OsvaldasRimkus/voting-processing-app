package voting.dto.district;

import voting.dto.candidate.CandidateDTO;
import voting.dto.county.CountyDTO;
import voting.model.District;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domas on 1/12/17.
 */

public class DistrictDTO {

    private Long id;
    private String name;
    private List<CountyDTO> counties;
    private List<CandidateDTO> candidates;


    public DistrictDTO(District district) {
        this.id = district.getId();
        this.name = district.getName();
        this.counties = new ArrayList<CountyDTO>();
        if (district.getCounties() != null) {
            district.getCounties().forEach(c -> counties.add(new CountyDTO(c)));
        }
        this.candidates = new ArrayList<CandidateDTO>();
        if (district.getCandidates() != null) {
            district.getCandidates().forEach(c -> candidates.add(new CandidateDTO(c)));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CountyDTO> getCounties() {
        return counties;
    }

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }
}
