package voting.dto.county;

import voting.model.County;
import voting.utils.Constants;

/**
 * Created by domas on 2/27/17.
 */
public class CountyShortDTO {

    private Long id;
    private String name;
    private String link;

    public CountyShortDTO(County c) {
        id = c.getId();
        name = c.getName();
        link = String.format("%s/county/%d", Constants.API_ROOT_URL, id);
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
