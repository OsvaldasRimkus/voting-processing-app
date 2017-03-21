package voting.dto.candidate;

import voting.dto.district.DistrictShortDTO;
import voting.dto.party.PartyShortDTO;
import voting.model.Candidate;
import voting.utils.DateUtils;

/**
 * Created by domas on 1/12/17.
 */
public class CandidateDTO {

    private Long id;
    private String personId;
    private String firstName;
    private String lastName;
    private String description;
    private String birthDate;

    private DistrictShortDTO district;
    private PartyShortDTO party;
    private Long positionInPartyList;

    public CandidateDTO(Candidate c) {
        this.id = c.getId();
        this.personId = c.getPersonId();
        this.firstName = c.getFirstName();
        this.lastName = c.getLastName();
        this.description = c.getDescription();
        this.birthDate = DateUtils.stringifyCalendar(c.getBirthDate(), "-");
        this.district = c.getDistrict() == null ? null: new DistrictShortDTO(c.getDistrict());
        this.party = c.getParty() == null ? null : new PartyShortDTO(c.getParty());
        this.positionInPartyList = c.getPositionInPartyList() != null ? c.getPositionInPartyList() : 0;
    }

    public Long getId() {
        return id;
    }

    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDescription() {
        return description;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public DistrictShortDTO getDistrict() {
        return district;
    }

    public PartyShortDTO getParty() {
        return party;
    }

    public Long getPositionInPartyList() {
        return positionInPartyList;
    }
}
