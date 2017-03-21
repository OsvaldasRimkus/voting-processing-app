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
import voting.dto.county.CountyData;
import voting.dto.district.DistrictData;
import voting.dto.party.PartyData;
import voting.dto.results.CountyResultData;
import voting.dto.results.vote.VoteData;
import voting.model.Candidate;
import voting.model.County;
import voting.model.District;
import voting.model.Party;
import voting.results.ResultService;
import voting.results.model.result.CountyMMResult;
import voting.results.model.result.CountySMResult;
import voting.results.model.result.DistrictSMResult;
import voting.results.model.result.Result;
import voting.results.model.votecount.CandidateVote;
import voting.results.model.votecount.PartyVote;
import voting.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by domas on 2/22/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ResultServiceIntegrationTest {

    private static Path tmpFilePath;
    private static CandidateData candidateData1; // both SM (district1) & MM, belongs to party1
    private static CandidateData candidateData2; // only SM (district1), belongs to party1, but not in a list
    private static CandidateData candidateData3; // only SM (district1), doesn't belong to party
    private static CandidateData candidateData4; // only MM, belongs to party2
    private static CountyData countyData1; // bound to district1
    private static CountyData countyData2; // bound to district1
    private static CountyData countyData3; // bound to district2

    private static DistrictData districtData1;
    private static DistrictData districtData2;

    private static PartyData partyData1;
    private static PartyData partyData2;

    private static List<VoteData> voteList1;
    private static List<VoteData> voteList2;
    private static CountyResultData smResultDto;
    private static CountyResultData smResultDto2;
    private static CountyResultData mmResultDto;

    District district1;
    District district2;
    County county1;
    County county2;
    County county3;
    Candidate candidate1;
    Candidate candidate2;
    Candidate candidate3;
    Candidate candidate4;
    Party party1;
    Party party2;
    private List<CountyData> countyDataList;
    private List<CandidateData> candidateDataList;
    private MockMultipartFile multiPartFile = new MockMultipartFile("file.csv", new byte[] {});


    @MockBean
    private ParsingService parsingService;
    @MockBean
    private StorageService storageService;

    @InjectMocks
    @Autowired
    private DistrictService districtService;
    @InjectMocks
    @Autowired
    private PartyService partyService;
    @Autowired
    private CandidateService candidateService;


    @Autowired
    private ResultService resultService;


    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @BeforeClass
    public static void beforeClassSetup() throws IOException {
        tmpFilePath = Files.createTempFile("file", "tmp");
        candidateData1 = createCandidateData("Pirmas", "Pirmas", "55500055501", "Partija 1", 1L);
        candidateData2 = createCandidateData("Antras", "Antras", "55500055502", "Partija 1", null);
        candidateData3 = createCandidateData("Trecias", "Trecias", "55500055503", "Išsikėlęs pats", null);
        candidateData4 = createCandidateData("Ketvirtas", "Ketvirtas", "55500055504", "Partija 2", 1L);
        countyData1 = createCountyData("Apylinkė 1", 3000L);
        countyData2 = createCountyData("Apylinkė 2", 4000L);
        countyData3 = createCountyData("Apylinkė 3", 5000L);

        districtData1 = new DistrictData();
        districtData1.setName("APYGARDA 1");
        districtData1.setCountiesData(Arrays.asList(countyData1, countyData2));

        districtData2 = new DistrictData();
        districtData2.setName("APYGARDA 2");
        districtData2.setCountiesData(Arrays.asList(countyData3));

        partyData1 = new PartyData();
        partyData1.setName("Partija 1");

        partyData2 = new PartyData();
        partyData2.setName("Partija 2");

        voteList1 = new ArrayList<>();
        voteList1.add(new VoteData(1L, 100L));
        voteList1.add(new VoteData(2L, 200L));
        voteList1.add(new VoteData(3L, 300L));

        smResultDto = new CountyResultData();
        smResultDto.setCountyId(1L);
        smResultDto.setSpoiledBallots(111L);
        smResultDto.setVoteList(voteList1);

        smResultDto2 = new CountyResultData();
        smResultDto2.setCountyId(2L);
        smResultDto2.setSpoiledBallots(111L);
        smResultDto2.setVoteList(voteList1);

        voteList2 = new ArrayList<>();
        voteList2.add(new VoteData(1L, 1000L));
        voteList2.add(new VoteData(2L, 2000L));

        mmResultDto = new CountyResultData();
        mmResultDto.setCountyId(1L);
        mmResultDto.setSpoiledBallots(222L);
        mmResultDto.setVoteList(voteList2);

    }

    @Before
    public void beforeTestSetup() throws IOException, CsvException {
        when(storageService.store(any(), any())).thenReturn(tmpFilePath);
        when(storageService.storeTemporary(any())).thenReturn(tmpFilePath);

        district1 = districtService.addNewDistrict(districtData1);
        district2 = districtService.addNewDistrict(districtData2);

        candidateDataList = Arrays.asList(candidateData1, candidateData2, candidateData3);
        when(parsingService.parseSingleMandateCandidateList(any())).thenReturn(candidateDataList);
        districtService.setCandidateList(district1.getId(), multiPartFile);


        candidateDataList = Arrays.asList(candidateData1);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);
        party1 = partyService.saveParty(partyData1, multiPartFile);

        candidateDataList = Arrays.asList(candidateData4);
        when(parsingService.parseMultiMandateCandidateList(any())).thenReturn(candidateDataList);
        party2 = partyService.saveParty(partyData2, multiPartFile);

        county1 = districtService.getCounty(1L);
        county2 = districtService.getCounty(2L);
        county3 = districtService.getCounty(3L);

        candidate1 = candidateService.getCandidate("55500055501");
        candidate2 = candidateService.getCandidate("55500055502");
        candidate3 = candidateService.getCandidate("55500055503");
        candidate4 = candidateService.getCandidate("55500055504");

    }






    @Test
    public void savesSMResultsCorrectly() {

        //Setup
        //Exercise
        CountySMResult result = resultService.addCountySmResult(smResultDto);

        //Verify
        List<CandidateVote> votes = result.getVotes();
        assertThat(votes.size(), is(3));
        assertThat(votes.get(0).getCandidate(), is(candidate3));
        assertThat(votes.get(0).getVoteCount(), is(300L));
        assertThat(votes.get(1).getCandidate(), is(candidate2));
        assertThat(votes.get(1).getVoteCount(), is(200L));
        assertThat(votes.get(2).getCandidate(), is(candidate1));
        assertThat(votes.get(2).getVoteCount(), is(100L));

    }


    @Test
    public void savesMMResultsCorrectly() {

        //Setup
        //Exercise
        CountyMMResult result = resultService.addCountyMmResult(mmResultDto);
        List<PartyVote> votes = result.getVotes();

        //Verify
        assertThat(votes.size(), is(2));
        assertThat(votes.get(0).getParty(), is(party2));
        assertThat(votes.get(0).getVoteCount(), is(2000L));
        assertThat(votes.get(1).getParty(), is(party1));
        assertThat(votes.get(1).getVoteCount(), is(1000L));

    }


    @Test
    public void getsCountyResultsCorrectly() {

        //Setup
        resultService.addCountySmResult(smResultDto);
        resultService.addCountyMmResult(mmResultDto);

        //Exercise
        CountySMResult smResult = resultService.getCountySmResult(smResultDto.getCountyId());
        CountyMMResult mmResult = resultService.getCountyMmResult(mmResultDto.getCountyId());

        //Verify
        assertThat(smResult.getCounty(), is(county1));
        assertThat(smResult.getVotes().size(), is(3));
        assertThat(smResult.getVotes().get(0).getCandidate(), is(candidate3));
        assertThat(smResult.getVotes().get(0).getVoteCount(), is(300L));
        assertThat(smResult.getVotes().get(1).getCandidate(), is(candidate2));
        assertThat(smResult.getVotes().get(1).getVoteCount(), is(200L));
        assertThat(smResult.getVotes().get(2).getCandidate(), is(candidate1));
        assertThat(smResult.getVotes().get(2).getVoteCount(), is(100L));
        assertThat(mmResult.getCounty(), is(county1));
        assertThat(mmResult.getVotes().size(), is(2));
        assertThat(mmResult.getVotes().get(0).getParty(), is(party2));
        assertThat(mmResult.getVotes().get(0).getVoteCount(), is(2000L));
        assertThat(mmResult.getVotes().get(1).getParty(), is(party1));
        assertThat(mmResult.getVotes().get(1).getVoteCount(), is(1000L));
    }

    @Transactional
    @Test
    public void nonConirmedResultsShouldntAddToDistrictResult() {

        //Setup
        resultService.addCountySmResult(smResultDto);
        resultService.addCountySmResult(smResultDto2);

        //Exercise
        DistrictSMResult result = resultService.getDistrictSmResult(district1.getId());
        List<CandidateVote> votes = result.getVotes();

        //Verify
        assertThat(result.getSpoiledBallots(), is(0L));
        assertThat(result.getTotalBallots(), is(0L));
    }


    @Test
    public void updatesDistrictResultsCorrectly() {

        //Setup
        Result r1 = resultService.addCountySmResult(smResultDto);
        resultService.confirmCountyResult(r1.getId());
        Result r2 = resultService.addCountySmResult(smResultDto2);
        resultService.confirmCountyResult(r2.getId());

        //Exercise
        DistrictSMResult result = resultService.getDistrictSmResult(district1.getId());
        List<CandidateVote> votes = result.getVotes();

        //Verify
        assertThat(result.getSpoiledBallots(), is(222L));
        assertThat(result.getTotalBallots(), is(1422L));


    }


    @Test
    public void addingResultForCountyWhichAlreadyHasResultShouldThrowIllegalArgument() {
        //Setup
        resultService.addCountySmResult(smResultDto);
        resultService.addCountyMmResult(mmResultDto);

        //Exercise
        //Verify
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("jau užregistruotas");
        resultService.addCountySmResult(smResultDto);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("jau užregistruotas");
        resultService.addCountyMmResult(mmResultDto);
    }



    private static CandidateData createCandidateData(String firstName, String lastName, String personId, String partyName, Long position) {
        CandidateData cd = new CandidateData();
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setPersonId(personId);
        cd.setPartyName(partyName);
        cd.setPositionInPartyList(position);
        return cd;
    }

    private static CountyData createCountyData(String name, Long voterCount) {
        CountyData cd = new CountyData();
        cd.setName(name);
        cd.setVoterCount(voterCount);
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
