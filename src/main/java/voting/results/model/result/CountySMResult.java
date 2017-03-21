package voting.results.model.result;


import voting.results.model.votecount.CandidateVote;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/22/17.
 */
@Entity
@DiscriminatorValue(value = "county_sm")
public class CountySMResult extends CountyResult {

    public List<CandidateVote> getVotes() {
        return super.getUnitVotes().stream().map(vc -> (CandidateVote) vc).collect(Collectors.toList());
    }
}
