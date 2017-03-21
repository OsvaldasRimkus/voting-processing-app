package voting.dto.countyrep;

import voting.dto.county.CountyDTO;
import voting.dto.district.DistrictDTO;
import voting.dto.district.DistrictShortDTO;
import voting.model.CountyRep;

/**
 * Created by domas on 1/12/17.
 */

public class CountyRepFullTerritorialsDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private CountyDTO county;
    private DistrictShortDTO district;
    private String[] roles;


    public CountyRepFullTerritorialsDTO(CountyRep cr) {
        this.id = cr.getId();
        this.firstName = cr.getFirstName();
        this.lastName = cr.getLastName();
        this.username = cr.getUsername();
        this.email = cr.getEmail();
        this.county = new CountyDTO(cr.getCounty());
        this.district = new DistrictShortDTO(cr.getCounty().getDistrict());
        this.roles = cr.getRoles();
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public CountyDTO getCounty() {
        return county;
    }

    public DistrictShortDTO getDistrict() {
        return district;
    }

    public String[] getRoles() {
        return roles;
    }
}
