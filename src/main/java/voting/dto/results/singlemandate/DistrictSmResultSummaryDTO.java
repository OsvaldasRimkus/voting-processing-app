package voting.dto.results.singlemandate;

import voting.dto.candidate.CandidateShortDTO;
import voting.dto.results.DistrictResultSummaryDTO;
import voting.results.model.result.DistrictSMResult;
import voting.results.model.votecount.CandidateVote;

/**
 * Created by domas on 3/7/17.
 */
public class DistrictSmResultSummaryDTO extends DistrictResultSummaryDTO {

    private CandidateShortDTO topCandidate;
    private Long votesForTopCandidate;

    public DistrictSmResultSummaryDTO(DistrictSMResult result) {
        super(result);
        if (result.getTotalBallots() > 0) {
            CandidateVote top1vote = result.getVotes().get(0);
            this.topCandidate = new CandidateShortDTO(top1vote.getCandidate());
            this.votesForTopCandidate = top1vote.getVoteCount();
        }
    }

    public CandidateShortDTO getTopCandidate() {
        return topCandidate;
    }

    public Long getVotesForTopCandidate() {
        return votesForTopCandidate;
    }
}
