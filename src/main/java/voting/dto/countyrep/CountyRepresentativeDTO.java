package voting.dto.countyrep;

import voting.dto.county.CountyShortDTO;
import voting.dto.district.DistrictShortDTO;
import voting.model.CountyRep;

/**
 * Created by domas on 1/12/17.
 */
public class CountyRepresentativeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private CountyShortDTO county;
    private DistrictShortDTO district;
    private String[] roles;


    public CountyRepresentativeDTO(CountyRep cr) {
        this.id = cr.getId();
        this.firstName = cr.getFirstName();
        this.lastName = cr.getLastName();
        this.username = cr.getUsername();
        this.email = cr.getEmail();
        this.county = new CountyShortDTO(cr.getCounty());
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

    public CountyShortDTO getCounty() {
        return county;
    }

    public DistrictShortDTO getDistrict() {
        return district;
    }

    public String[] getRoles() {
        return roles;
    }
}
