package voting.results.model.votecount;


import voting.results.model.result.Result;

import javax.persistence.*;

/**
 * Created by domas on 2/22/17.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long voteCount;

    @ManyToOne
    @JoinColumn(name = "result_id")
    private Result result;

    public Vote() {
    }

    public Vote(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract Long getUnitId();

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
