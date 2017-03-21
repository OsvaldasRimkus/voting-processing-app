package voting.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by domas on 1/21/17.
 */
public class CandidateListData {

    @Valid
    @NotNull
    @JsonProperty("candidates")
    private List<CandidateData> candidateListData;

    public List<CandidateData> getCandidateListData() {
        return candidateListData;
    }

    public void setCandidateListData(List<CandidateData> candidateListData) {
        this.candidateListData = candidateListData;
    }
}
