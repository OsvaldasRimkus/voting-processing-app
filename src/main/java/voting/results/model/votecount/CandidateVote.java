package voting.results.model.votecount;

import voting.model.Candidate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by domas on 2/22/17.
 */
@Entity
public class CandidateVote extends Vote {

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    public CandidateVote() {
    }

    @Override
    public Long getUnitId() {
        return candidate.getId();
    }

    public CandidateVote(Candidate candidate, Long votes) {
        super(votes);
        this.candidate = candidate;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }



}
