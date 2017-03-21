package voting;

import com.opencsv.exceptions.CsvException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import voting.dto.candidate.CandidateData;
import voting.dto.party.PartyData;
import voting.exception.NotFoundException;
import voting.model.Party;
import voting.service.CandidateService;
import voting.service.ParsingService;
import voting.service.PartyService;
import voting.service.StorageService;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by domas on 2/3/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PartyServiceIntegrationTest {

    private static Path tmpFilePath;
    private static CandidateData candidate1;
    private static CandidateData candidate2;
    private static CandidateData candidate3;
    private static PartyData partyData;

    @MockBean
    private ParsingService parsingService;
    @MockBean
    private StorageService storageService;

    @Autowired
    private CandidateService candidateService;

    @InjectMocks
    @Autowired
    private PartyService sut;

    @Autowired
    private EntityManager em;

    private List<CandidateData> candidateDataList;
    private MockMultipartFile multiPartFile = new MockMultipartFile("file.csv", new byte[] {});

    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @BeforeClass
    public static void beforeClassSetup() throws IOException {
        tmpFilePath = Files.createTempFile("file", "tmp");
        candidate1 = createCandidateData("Petras", "Petraitis", "55500055501", 1L);
        candidate2 = createCandidateData("Jonas", "Jonaitis", "55500055502", 2L);
        candidate3 = createCandidateData("Trecias", "Treciasis", "55500055503", 1L);
        partyData = new PartyData();
        partyData.setName("Pirma Partija");
    }

    @Before
    public void beforeTestSetup() throws IOException, CsvException {
        when(storageService.store(any(), any())).thenReturn(tmpFilePath);
        when(storageService.storeTemporary(any())).thenReturn(tmpFilePath);
    }


    @Test(expected = NotFoundException.class)
    public void gettingNonExistingPartyByIdShouldThrowNotFound() {
        //Sanity check
        assertThat(sut.getParties().size(), is(0));

        //Verify
        sut.getParty(1L);
    }

    @Test(expected = NotFoundException.class)
    public void gettingNonExistingPartyByNameShouldThrowNotFound() {
        //Sanity check
        assertThat(sut.getParties().size(), is(0));

        //Verify
        sut.getParty("nesanti partija");
    }


    @Test
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void savesAndUpdatesPartyCorrectly() throws IOException, CsvException {

        //Setup
        candidateDataList = Arrays.asList(candidate1, candidate2);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        //Exercise
        Party party = sut.saveParty(partyData, multiPartFile);

        //Verify
        assertThat(party.getName(), is("Pirma Partija"));
        assertThat(party.getCandidates().size(), is(2));
        assertThat(party.getCandidates().get(0).getPersonId(), is("55500055501"));
        assertThat(party.getCandidates().get(1).getPersonId(), is("55500055502"));
        assertThat(candidateService.exists("55500055501"), is(true));
        assertThat(candidateService.exists("55500055502"), is(true));


        //Setup
        PartyData updatedPartyData = new PartyData();
        updatedPartyData.setId(party.getId());
        updatedPartyData.setName("UPDATED");

        candidateDataList = Arrays.asList(candidate3);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        //Exercise
        party = sut.saveParty(updatedPartyData, multiPartFile);

        //Verify
        assertThat(party.getName(), is("UPDATED"));
        assertThat(party.getCandidates().size(), is(1));
        assertThat(party.getCandidates().get(0).getPersonId(), is("55500055503"));
        assertThat(candidateService.exists("55500055501"), is(false));
        assertThat(candidateService.exists("55500055502"), is(false));

    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void savingPartyWithDuplicatingNameThrowsIllegalArgument() throws IOException, CsvException {

        //Setup
        candidateDataList = Arrays.asList(candidate1, candidate2);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);
        sut.saveParty(partyData, multiPartFile);

        //Exercise
        //Verify
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("jau egzistuoja");
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);
        sut.saveParty(partyData, multiPartFile);

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldntCreateNewPartyIfExceptionIsThrownWhileParsingCandidateList() throws IOException, CsvException {

        //Setup
        int partyCount = sut.getParties().size();

        when(parsingService.parseMultiMandateCandidateList(any())).thenThrow(CsvException.class);

        //Exercise
        try {
            Party party = sut.saveParty(partyData, multiPartFile);
        } catch (Exception ex) {
            //Verify
            assertThat(sut.getParties().size(), is(partyCount));
            thrown.expect(NotFoundException.class);
            Party saved = sut.getParty("Pirma Partija");
        }
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldntUpdatePartyIfExceptionIsThrownWhileParsingCandidateList() throws IOException, CsvException {

        //Setup
        candidateDataList = Arrays.asList(candidate1, candidate2);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        Party savedParty = sut.saveParty(partyData, multiPartFile);

        PartyData updatedPartyData = new PartyData();
        updatedPartyData.setId(savedParty.getId());
        updatedPartyData.setName("UPDATED");

        when(parsingService.parseMultiMandateCandidateList(any())).thenThrow(CsvException.class);

        //Exercise
        try {
            sut.saveParty(updatedPartyData, multiPartFile);
        } catch (Exception e) {

            //            party updatedParty = sut.getParty(savedParty.getId());
            // LazyInitializationException workaround
            Party updatedParty = (Party) em.createQuery("SELECT p FROM Party p JOIN FETCH p.candidates WHERE p.id = :id")
                    .setParameter("id", savedParty.getId())
                    .getSingleResult();

            //Verify
            assertThat(updatedParty.getName(), is("Pirma Partija"));
            assertThat(updatedParty.getCandidates().size(), is(2));
            thrown.expect(NotFoundException.class);
            sut.getParty("UPDATED");
        }
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void setCandidateListWorksCorrectly() throws IOException, CsvException {

        //Setup
        candidateDataList = Arrays.asList(candidate1, candidate2);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        Party savedParty = sut.saveParty(partyData, multiPartFile);

        candidateDataList = Arrays.asList(candidate1);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        //Exercise
        Party updatedParty = sut.setCandidateList(savedParty.getId(), multiPartFile);

        //Verify
        assertThat(updatedParty.getCandidates().size(), is(1));
        assertThat(updatedParty.getCandidates().get(0).getPersonId(), is("55500055501"));
        assertThat(candidateService.exists("55500055501"), is(true));
    }



    @Test
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteCandidateListWorksCorrectly() throws IOException, CsvException {

        //Setup
        candidateDataList = Arrays.asList(candidate1, candidate2);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        Party savedParty = sut.saveParty(partyData, multiPartFile);

        //Exercise
        sut.deleteCandidateList(savedParty.getId());
        Party updatedParty = sut.getParty(savedParty.getId());

        //Verify
        assertThat(updatedParty.getName(), is("Pirma Partija"));
        assertThat(updatedParty.getCandidates().size(), is(0));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deletingCandidateListShouldDeleteOrphanCandidates() throws IOException, CsvException {

        //Setup
        candidateDataList = Arrays.asList(candidate1);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);

        Party savedParty = sut.saveParty(partyData, multiPartFile);

        //sanity check
        assertThat(candidateService.exists("55500055501"), is(true));

        //Exercise
        sut.deleteCandidateList(savedParty.getId());
        Party updatedParty = sut.getParty(savedParty.getId());

        //Verify
        thrown.expect(NotFoundException.class);
        candidateService.getCandidate(candidate1.getPersonId());
        assertThat(candidateService.exists("55500055501"), is(false));

    }


    private static CandidateData createCandidateData(String firstName, String lastName, String personId, Long position) {
        CandidateData cd = new CandidateData();
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setPersonId(personId);
        cd.setPositionInPartyList(position);
        return cd;
    }


    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public DataPreloader dataPreloader() {
            return mock(DataPreloader.class);
        }
    }
}
