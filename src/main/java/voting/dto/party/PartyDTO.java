package voting.dto.party;

import voting.dto.candidate.CandidateDTO;
import voting.model.Party;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domas on 1/12/17.
 */
public class PartyDTO {

    private Long id;
    private String name;
    private List<CandidateDTO> candidates;


    public PartyDTO(Party p) {
        this.id = p.getId();
        this.name = p.getName();
        this.candidates = new ArrayList<>();
        if (p.getCandidates() != null) {
            p.getCandidates().forEach(c -> candidates.add(new CandidateDTO(c)));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }
}
