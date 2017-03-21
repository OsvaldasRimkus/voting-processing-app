package voting.dto.results.multimandate;

import voting.dto.results.CountyResultDTO;
import voting.dto.results.vote.PartyVoteDTO;
import voting.results.model.result.CountyMMResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/27/17.
 */
public class CountyMMResultDTO extends CountyResultDTO {

    private List<PartyVoteDTO> votes;

    public CountyMMResultDTO(CountyMMResult result) {
        super(result);
        votes = result.getVotes().stream().map(PartyVoteDTO::new).collect(Collectors.toList());
    }

    public List<PartyVoteDTO> getVotes() {
        return votes;
    }

}
