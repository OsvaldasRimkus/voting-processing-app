package voting.dto.results;

import voting.results.model.result.Result;

/**
 * Created by domas on 2/27/17.
 */
public abstract class ResultDTO {

    private Long id;
    private Long validBallots;
    private Long spoiledBallots;
    private Long totalBallots;
    private Long voterCount;

    public ResultDTO(Result result) {
        this.id = result.getId();
        this.validBallots = result.getValidBallots();
        this.spoiledBallots = result.getSpoiledBallots();
        this.totalBallots = result.getTotalBallots();
    }

    public Long getId() {
        return id;
    }

    public Long getValidBallots() {
        return validBallots;
    }

    public Long getSpoiledBallots() {
        return spoiledBallots;
    }

    public Long getTotalBallots() {
        return totalBallots;
    }

    public Long getVoterCount() {
        return voterCount;
    }

    public void setVoterCount(Long voterCount) {
        this.voterCount = voterCount;
    }
}
