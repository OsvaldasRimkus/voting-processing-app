package voting.dto.results.multimandate;

import voting.model.Party;
import voting.results.model.result.ConsolidatedResults;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by domas on 3/9/17.
 */
public class MMSummaryDTO {

    private List<PartyResultDTO> partyResults;
    private Long totalBallots = 0L;
    private Long validBallots = 0L;
    private Long spoiledBallots = 0L;

    private List<DistrictMmResultSummaryDTO> districtResults;

    public MMSummaryDTO(ConsolidatedResults summary) {
        Map<Party, Long> partyVotes = summary.getPartyVotecount();
        Map<Party, Long> mmPartyMandates = summary.getPreliminaryMmPartyMandates();
        this.validBallots = summary.getValidBallots();
        this.totalBallots = summary.getTotalBallots();
        this.spoiledBallots = summary.getSpoiledBallots();
        this.partyResults = partyVotes.keySet().stream()
                .map(p -> new PartyResultDTO(p, partyVotes.get(p), mmPartyMandates.get(p)))
                .collect(Collectors.toList());
        this.districtResults = summary.getMmResults().stream()
                .map(DistrictMmResultSummaryDTO::new)
                .collect(Collectors.toList());
    }


    public List<PartyResultDTO> getPartyResults() {
        return partyResults;
    }

    public Long getValidBallots() {
        return validBallots;
    }

    public Long getTotalBallots() {
        return totalBallots;
    }

    public Long getSpoiledBallots() {
        return spoiledBallots;
    }

    public List<DistrictMmResultSummaryDTO> getDistrictResults() {
        return districtResults;
    }
}
