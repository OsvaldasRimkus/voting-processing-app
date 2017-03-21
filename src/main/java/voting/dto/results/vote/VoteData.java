package voting.dto.results.vote;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by domas on 2/27/17.
 */
public class VoteData {

    @NotNull(message = "Balsavimo objekto identifikatorius privalomas")
    private Long unitId;

    @NotNull(message = "Trūksta balsų skaičiaus")
    @Min(value = 0, message = "Balsu skaičius privalo būti teigiamas")
    @Max(value = 500000, message = "Skirtų balsų negali būti tiek daug")
    private Long votes;

    public VoteData() {
    }

    public VoteData(Long unitId, Long votes) {
        this.unitId = unitId;
        this.votes = votes;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

}
