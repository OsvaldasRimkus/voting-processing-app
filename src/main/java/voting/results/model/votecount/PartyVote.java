package voting.results.model.votecount;

import voting.model.Party;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by domas on 2/22/17.
 */
@Entity
public class PartyVote extends Vote {

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    public PartyVote() {
    }

    @Override
    public Long getUnitId() {
        return party.getId();
    }

    public PartyVote(Party party, Long voteCount) {
        super(voteCount);
        this.party = party;
    }


    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
