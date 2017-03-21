package voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import voting.dto.candidate.CandidateDTO;
import voting.service.CandidateService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 1/10/17.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/candidate")
public class CandidateController {

    private CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public List<CandidateDTO> getCandidates() {
        return candidateService.getCandidates().stream()
                                               .map(CandidateDTO::new)
                                               .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("{id}")
    public CandidateDTO getCandidate(@PathVariable Long id) {
        return new CandidateDTO(candidateService.getCandidate(id));
    }

}
