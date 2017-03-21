package voting.dto.party;

import voting.model.Party;
import voting.utils.Constants;

/**
 * Created by domas on 2/27/17.
 */
public class PartyShortDTO {

    private Long id;
    private String name;
    private String link;

    public PartyShortDTO(String name) {
        this.name = name;
    }

    public PartyShortDTO(Party party) {
        this.id = party.getId();
        this.name = party.getName();
        this.link = String.format("%s/party/%d", Constants.API_ROOT_URL, id);
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
