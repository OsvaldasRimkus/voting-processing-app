package voting.dto.results.vote;

import voting.dto.candidate.CandidateShortDTO;
import voting.results.model.votecount.CandidateVote;

/**
 * Created by andrius on 2/9/17.
 */

public class CandidateVoteDTO {

    private CandidateShortDTO candidate;
    private Long voteCount;

    public CandidateVoteDTO() { }

    public CandidateVoteDTO(CandidateVote cv) {
        this.candidate = new CandidateShortDTO(cv.getCandidate());
        this.voteCount = cv.getVoteCount();
    }

    public CandidateShortDTO getCandidate() {
        return candidate;
    }

    public Long getVoteCount() {
        return voteCount;
    }
}
