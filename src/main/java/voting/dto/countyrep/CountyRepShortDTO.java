package voting.dto.countyrep;

import voting.model.CountyRep;
import voting.utils.Constants;

/**
 * Created by domas on 2/27/17.
 */
public class CountyRepShortDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String link;

    public CountyRepShortDTO(CountyRep cr) {
        this.id = cr.getId();
        this.firstName = cr.getFirstName();
        this.lastName = cr.getLastName();
        this.link = String.format("%s/county-rep/%d", Constants.API_ROOT_URL, id);
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

    public String getLink() {
        return link;
    }
}