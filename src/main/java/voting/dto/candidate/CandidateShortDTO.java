package voting.dto.candidate;

import voting.dto.district.DistrictShortDTO;
import voting.dto.party.PartyShortDTO;
import voting.model.Candidate;
import voting.utils.Constants;

/**
 * Created by domas on 2/27/17.
 */
public class CandidateShortDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private PartyShortDTO party;
    private DistrictShortDTO district;
    private String link;

    public CandidateShortDTO() {
    }

    public CandidateShortDTO(Candidate c) {
        this.id = c.getId();
        this.firstName = c.getFirstName();
        this.lastName = c.getLastName();
        this.party = c.getParty() == null ? null : new PartyShortDTO(c.getParty());
        this.district = c.getDistrict() == null ? null : new DistrictShortDTO(c.getDistrict());
        this.link = String.format("%s/candidate/%d", Constants.API_ROOT_URL, id);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PartyShortDTO getParty() {
        return party;
    }

    public DistrictShortDTO getDistrict() {
        return district;
    }

    public String getLink() {
        return link;
    }
}
