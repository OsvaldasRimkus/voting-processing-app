package voting.dto.results.multimandate;

import voting.dto.party.PartyShortDTO;
import voting.model.Party;

/**
 * Created by domas on 3/7/17.
 */
public class PartyResultDTO {

    private PartyShortDTO party;
    private Long voteCount;
    private Long mandates;

    public PartyResultDTO(Party party, Long voteCount, Long mandates) {
        this.party = new PartyShortDTO(party);
        this.voteCount = voteCount;
        this.mandates = mandates;
    }

    public PartyShortDTO getParty() {
        return party;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public Long getMandates() {
        return mandates;
    }
}
