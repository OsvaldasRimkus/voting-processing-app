package voting.service;

import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import voting.dto.candidate.CandidateData;
import voting.dto.party.PartyData;
import voting.exception.NotFoundException;
import voting.model.Candidate;
import voting.model.Party;
import voting.repository.PartyRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by domas on 1/15/17.
 */

@Transactional(rollbackFor = Exception.class)
@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final CandidateService candidateService;
    private final StorageService storageService;
    private final ParsingService parsingService;

    @Autowired
    public PartyServiceImpl(PartyRepository partyRepository,
                            CandidateService candidateService,
                            StorageService storageService,
                            ParsingService parsingService) {
        this.partyRepository = partyRepository;
        this.candidateService = candidateService;
        this.storageService = storageService;
        this.parsingService = parsingService;
    }

    @Override
    public Party getParty(Long id) {
        Party party = partyRepository.findOne(id);
        throwNotFoundIfNull(party, "Nepavyko rasti partijos su id " + id);
        return party;
    }

    @Override
    public Party getParty(String name) {
        Party party = partyRepository.findByName(name);
        throwNotFoundIfNull(party, String.format("Nepavyko rasti partijos su pavadinimu \"%s\" ", name));
        return party;
    }

    @Override
    public List<Party> getParties() {
        return (List<Party>) partyRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Party saveParty(PartyData partyData, MultipartFile file) throws IOException, CsvException {

        Party party;
        if (partyData.getId() != null) {
            party = getParty(partyData.getId());
            party.setName(partyData.getName());
        } else if (partyRepository.existsByName(partyData.getName())) {
            throw (new IllegalArgumentException(String.format("Partija \"%s\" jau egzistuoja", partyData.getName())));
        } else {
            party = new Party(partyData.getName());
            party = partyRepository.save(party);
        }

        setCandidateList(party, file);
        return party;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Party updateParty(PartyData partyData, Long partyId) {
        Party party = partyRepository.findOne(partyId);
        party.setName(partyData.getName());
        return partyRepository.save(party);
    }

    /**
     * Given party id and csv file with list of candidates, binds those candidates to a given party.
     * Previously bound candidates are unbound from a party.
     * @param id - party id
     * @param file - csv file with a list of candidates
     * @return
     * @throws IOException
     * @throws CsvException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Party setCandidateList(Long id, MultipartFile file) throws IOException, CsvException {
        return setCandidateList(getParty(id), file);
    }

    private Party setCandidateList(Party party, MultipartFile file) throws IOException, CsvException {
        deleteCandidateList(party);
        List<CandidateData> candidateListData = extractCandidateList(file);
        party.removeAllCandidates();

        candidateListData.forEach(
                candidateData -> {
                    candidateData.setPartyId(party.getId());
                    candidateData.setPartyName(party.getName());
                    Candidate candidate = candidateService.addNewOrGetIfExists(candidateData);
                    party.addCandidate(candidate);
                });
        partyRepository.save(party);

        String fileName = String.format("party_%d.csv", party.getId());
        storageService.store(fileName, file);

        return party;
    }

    /**
     * Unbinds all candidates from party with given id.
     * If candidate is no longer bound to any party or district after unbounding, he is deleted from db.
     * @param id - party id
     */
    @Transactional
    @Override
    public void deleteCandidateList(Long id) {
        deleteCandidateList(getParty(id));
    }

    private void deleteCandidateList(Party party) {
        Stream<Candidate> orphanCandidates = party.getCandidates().stream().filter(c -> c.getDistrict() == null);
        party.removeAllCandidates();
        orphanCandidates.forEach(c -> candidateService.deleteCandidate(c.getId()));
        partyRepository.save(party);
    }

    @Transactional
    @Override
    public void deleteParty(Long id) {
        Party party = getParty(id);
        deleteCandidateList(party);
        partyRepository.delete(id);
    }

    @Override
    public boolean exists(Long id) {
        return partyRepository.exists(id);
    }

    @Override
    public boolean exists(String name) {
        return partyRepository.existsByName(name);
    }

    private List<CandidateData> extractCandidateList(MultipartFile file) throws IOException, CsvException {
        Path tempFile = storageService.storeTemporary(file);

        List<CandidateData> candidateListData;
        try {
            candidateListData = parsingService.parseMultiMandateCandidateList(tempFile.toFile());
        } finally {
            storageService.delete(tempFile);
        }
        return candidateListData;
    }

    private void throwNotFoundIfNull(Object obj, String message) {
        if (obj == null) {
            throw (new NotFoundException(message));
        }
    }
}
