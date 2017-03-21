package voting.dto.results.vote;

import voting.dto.party.PartyShortDTO;
import voting.results.model.votecount.PartyVote;

/**
 * Created by andrius on 2/18/17.
 */

public class PartyVoteDTO {

    private PartyShortDTO party;
    private Long voteCount;

    public PartyVoteDTO(PartyVote pv) {
        this.party = new PartyShortDTO(pv.getParty());
        this.voteCount = pv.getVoteCount();
    }

    public PartyShortDTO getParty() {
        return party;
    }

    public Long getVoteCount() {
        return voteCount;
    }
}
