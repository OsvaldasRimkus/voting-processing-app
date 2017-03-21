package voting;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import voting.dto.candidate.CandidateData;
import voting.exception.NotFoundException;
import voting.model.Candidate;
import voting.model.District;
import voting.model.Party;
import voting.repository.CandidateRepository;
import voting.service.CandidateService;
import voting.service.CandidateServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by domas on 2/1/17.
 */

@RunWith(SpringRunner.class)
public class CandidateServiceTest {

    CandidateRepository repo = mock(CandidateRepository.class);

    CandidateService sut = new CandidateServiceImpl(repo);

    private static String personId = "55500055501";
    private static String firstName = "Petras";
    private static String lastName = "Petraitis";
    private static String description = "Ilgas descriptionas kad butu ilgesnis nei 20 simboliu";

    private static Party party;
    private static District district;
    private static Candidate existingCandidate;

    private CandidateData newCandidate;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @BeforeClass
    public static void beforeClassSetup() {
        party = new Party("party 1");
        district = new District("District 1");
        existingCandidate = new Candidate(firstName, lastName, personId, description);
    }

    @Before
    public void setup() {
        party.removeAllCandidates();
        district.removeAllCandidates();

        newCandidate = new CandidateData();
        newCandidate.setPersonId(personId);
        newCandidate.setFirstName(firstName);
        newCandidate.setLastName(lastName);
        newCandidate.setDescription(description);
    }


    @Test(expected = NotFoundException.class)
    public void gettingCandidateWithNonExistingIdShouldThrowNotFound() {
        when(repo.findOne(1L)).thenReturn(null);
        sut.getCandidate(1L);
    }

    @Test(expected = NotFoundException.class)
    public void gettingCandidateWithNonExistingPersonIdShouldThrowNotFound() {
        when(repo.findByPersonId("55500055501")).thenReturn(null);
        sut.getCandidate("55500055501");
    }

    @Test(expected = NotFoundException.class)
    public void deletingCandidateWithNonExistingIdShouldThrowNotFound() {
        when(repo.exists(1L)).thenReturn(false);
        sut.deleteCandidate(1L);
    }

    @Test
    public void nonMatchingNameShouldThrowIllegalArgument() {
        //Setup
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("jau įrašytas kaip");

        newCandidate.setFirstName("Jonas");
        newCandidate.setLastName("Jonaitis");

        //Exercise
        sut.checkCandidateIntegrity(newCandidate, existingCandidate);
    }

    @Test
    public void candidatesBoundToDifferentPartiesShouldThrowIllegalArgument() {
        //Setup
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("priskirtas kitai partijai");

        party.addCandidate(existingCandidate);
        newCandidate.setPartyName("party XXX");

        //Exercise
        sut.checkCandidateIntegrity(newCandidate, existingCandidate);
    }

    @Test
    public void candidatesBoundToDifferentDistrictsShouldThrowIllegalArgument() {
        //Setup
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("priskirtas kitai apygardai");

        district.addCandidate(existingCandidate);
        party.addCandidate(existingCandidate);

        newCandidate.setPartyName(party.getName());
        newCandidate.setDistrictName("District XXX");

        //Exercise
        sut.checkCandidateIntegrity(newCandidate, existingCandidate);
    }

    @Test
    public void existingCandidateNotBoundToADistrictAndNewCandidateBoundToADistrictShouldHaveNoConflict() {
        //Setup
        newCandidate.setDistrictName("District XXX");

        //Exercise
        sut.checkCandidateIntegrity(newCandidate, existingCandidate);
    }

    @Test
    public void existingCandidateNotBoundToAPartyAndNewCandidateBoundToAPartyShouldHaveNoConflict() {
        //Setup
        newCandidate.setPartyName("party XXX");

        //Exercise
        sut.checkCandidateIntegrity(newCandidate, existingCandidate);
    }

}
