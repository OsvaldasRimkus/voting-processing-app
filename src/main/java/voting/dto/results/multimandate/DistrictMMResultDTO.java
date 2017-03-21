package voting.dto.results.multimandate;

import voting.dto.results.DistrictResultDTO;
import voting.dto.results.vote.PartyVoteDTO;
import voting.results.model.result.DistrictMMResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 2/27/17.
 */
public class DistrictMMResultDTO extends DistrictResultDTO {

    private List<PartyVoteDTO> votes;


    public DistrictMMResultDTO(DistrictMMResult result) {
        super(result);
        votes = result.getVotes().stream().map(PartyVoteDTO::new).collect(Collectors.toList());
    }

    public List<PartyVoteDTO> getVotes() {
        return votes;
    }

}
