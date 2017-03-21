package voting.dto.results.singlemandate;

import voting.dto.results.CountyResultDTO;
import voting.dto.results.vote.CandidateVoteDTO;
import voting.results.model.result.CountySMResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/27/17.
 */
public class CountySMResultDTO extends CountyResultDTO {

    private List<CandidateVoteDTO> votes;


    public CountySMResultDTO(CountySMResult result) {
        super(result);
        votes = result.getVotes().stream().map(CandidateVoteDTO::new).collect(Collectors.toList());
    }

    public List<CandidateVoteDTO> getVotes() {
        return votes;
    }

}
