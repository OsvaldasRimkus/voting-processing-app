package voting.service;

import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;
import voting.dto.party.PartyData;
import voting.model.Party;

import java.io.IOException;
import java.util.List;

/**
 * Created by domas on 1/10/17.
 */
public interface PartyService {

    Party getParty(Long id);

    Party getParty(String name);

    List<Party> getParties();

    Party saveParty(PartyData partyData, MultipartFile file) throws IOException, CsvException;

    void deleteParty(Long id);

    Party updateParty(PartyData partyData, Long partyId);

    Party setCandidateList(Long id, MultipartFile file) throws IOException, CsvException;

    void deleteCandidateList(Long id);

    boolean exists(Long id);

    boolean exists(String name);
}
