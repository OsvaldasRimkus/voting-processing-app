package voting.dto.results.singlemandate;

import voting.dto.results.DistrictResultDTO;
import voting.dto.results.vote.CandidateVoteDTO;
import voting.results.model.result.DistrictSMResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/27/17.
 */
public class DistrictSMResultDTO extends DistrictResultDTO {

    private List<CandidateVoteDTO> votes;

    public DistrictSMResultDTO(DistrictSMResult result) {
        super(result);
        votes = result.getVotes().stream().map(CandidateVoteDTO::new).collect(Collectors.toList());
    }

    public List<CandidateVoteDTO> getVotes() {
        return votes;
    }

}
