package voting.results.model.result;


import voting.results.model.votecount.PartyVote;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/22/17.
 */
@Entity
@DiscriminatorValue(value = "county_mm")
public class CountyMMResult extends CountyResult {

    public List<PartyVote> getVotes() {
        return super.getUnitVotes().stream().map(vc -> (PartyVote) vc).collect(Collectors.toList());
    }

}
