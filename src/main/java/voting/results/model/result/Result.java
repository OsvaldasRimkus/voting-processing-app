package voting.results.model.result;


import voting.results.model.votecount.Vote;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/22/17.
 */
@Entity
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long validBallots = 0L;
    private Long spoiledBallots = 0L;
    private Long totalBallots = 0L;

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vote> unitVotes = new ArrayList<>();

    public Result() {
        this.validBallots = 0L;
        this.spoiledBallots = 0L;
        this.totalBallots = 0L;
    }

    protected void combineResults(Result r) {
        this.validBallots += r.getValidBallots();
        this.spoiledBallots += r.getSpoiledBallots();
        this.totalBallots = validBallots + spoiledBallots;
        combineVotes(r.getUnitVotes());
    }

    private void combineVotes(List<Vote> voteList) {
        Map<Long, Long> votemap = convertToMap(voteList);
        unitVotes.stream().forEach(v -> v.setVoteCount(v.getVoteCount() + votemap.get(v.getUnitId())));
    }

    private Map<Long, Long> convertToMap(List<Vote> unitVotes) {
        return unitVotes.stream().collect(Collectors.toMap(Vote::getUnitId, Vote::getVoteCount));
    }

    public void addVote(Vote vote) {
        this.unitVotes.add(vote);
        vote.setResult(this);
        this.validBallots += vote.getVoteCount();
        this.totalBallots += vote.getVoteCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValidBallots() {
        return validBallots;
    }

    public void setValidBallots(Long validBallots) {
        this.validBallots = validBallots;
    }

    public Long getSpoiledBallots() {
        return spoiledBallots;
    }

    public void setSpoiledBallots(Long spoiledBallots) {
        this.spoiledBallots = spoiledBallots;
    }

    public Long getTotalBallots() {
        return totalBallots;
    }

    public void setTotalBallots(Long totalBallots) {
        this.totalBallots = totalBallots;
    }

    public List<Vote> getUnitVotes() {
        unitVotes.sort(Collections.reverseOrder(Comparator.comparing(Vote::getVoteCount)));
        return unitVotes;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(id, result.id) &&
                Objects.equals(validBallots, result.validBallots) &&
                Objects.equals(spoiledBallots, result.spoiledBallots) &&
                Objects.equals(totalBallots, result.totalBallots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, validBallots, spoiledBallots, totalBallots);
    }

    @Override
    public String toString() {
        return "Result{" +
//                "id=" + id +
                ", validBallots=" + validBallots +
                ", spoiledBallots=" + spoiledBallots +
                ", totalBallots=" + totalBallots +
                '}';
    }
}
