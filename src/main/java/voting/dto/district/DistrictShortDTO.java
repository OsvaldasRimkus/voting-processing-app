package voting.dto.district;

import voting.model.District;
import voting.utils.Constants;

/**
 * Created by domas on 2/27/17.
 */
public class DistrictShortDTO {

    private Long id;
    private String name;
    private String link;


    public DistrictShortDTO(District d) {
        this.id = d.getId();
        this.name = d.getName();
        this.link = String.format("%s/district/%d", Constants.API_ROOT_URL, id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
