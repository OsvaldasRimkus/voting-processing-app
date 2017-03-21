package voting.dto.results.multimandate;

import voting.dto.party.PartyShortDTO;
import voting.dto.results.DistrictResultSummaryDTO;
import voting.results.model.result.DistrictMMResult;
import voting.results.model.votecount.PartyVote;

/**
 * Created by domas on 3/7/17.
 */
public class DistrictMmResultSummaryDTO extends DistrictResultSummaryDTO {

    private PartyShortDTO topParty;
    private Long votesForTopParty;

    public DistrictMmResultSummaryDTO(DistrictMMResult result) {
        super(result);
        if (result.getTotalBallots() > 0) {
            PartyVote top1vote = result.getVotes().get(0);
            this.topParty = new PartyShortDTO(top1vote.getParty());
            this.votesForTopParty = top1vote.getVoteCount();
        }
    }

    public PartyShortDTO getTopParty() {
        return topParty;
    }

    public Long getVotesForTopParty() {
        return votesForTopParty;
    }
}
