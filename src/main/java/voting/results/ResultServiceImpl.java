package voting.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voting.dto.results.CountyResultData;
import voting.dto.results.vote.VoteData;
import voting.exception.NotFoundException;
import voting.model.Candidate;
import voting.model.County;
import voting.model.District;
import voting.model.Party;
import voting.results.model.result.*;
import voting.results.model.votecount.CandidateVote;
import voting.results.model.votecount.PartyVote;
import voting.results.model.votecount.Vote;
import voting.results.repository.ResultRepository;
import voting.service.CandidateService;
import voting.service.DistrictService;
import voting.service.PartyService;

import java.util.List;
import java.util.stream.Collectors;

import static voting.results.model.result.ResultType.*;

/**
 * Created by domas on 2/23/17.
 */
@Service
public class ResultServiceImpl implements ResultService {

    private final DistrictService districtService;
    private final PartyService partyService;
    private final CandidateService candidateService;
    private final ResultRepository resultRepository;

    private ConsolidatedResults consolidated;


    @Autowired
    public ResultServiceImpl(DistrictService districtService,
                             PartyService partyService,
                             CandidateService candidateService,
                             ResultRepository resultRepository) {
        this.districtService = districtService;
        this.partyService = partyService;
        this.candidateService = candidateService;
        this.resultRepository = resultRepository;
    }


    @Override
    public CountySMResult getCountySmResult(Long countyId) {
        return resultRepository.findSmResultByCounty(districtService.getCounty(countyId));
    }

    @Override
    public CountyMMResult getCountyMmResult(Long countyId) {
        return resultRepository.findMmResultByCounty(districtService.getCounty(countyId));
    }

    @Override
    public DistrictSMResult getDistrictSmResult(Long districtId) {
        District district = districtService.getDistrict(districtId);
        DistrictSMResult result = resultRepository.findSmResultByDistrict(district);
        if (result == null) {
            result = (DistrictSMResult) saveNewDistrictResult(district, SINGLE_MANDATE);
        }
        return result;
    }

    @Override
    public DistrictMMResult getDistrictMmResult(Long districtId) {
        District district = districtService.getDistrict(districtId);
        DistrictMMResult result = resultRepository.findMmResultByDistrict(district);
        if (result == null) {
            result = (DistrictMMResult) saveNewDistrictResult(district, MULTI_MANDATE);
        }
        return result;
    }

    @Transactional
    public List<DistrictSMResult> getAllDistrictSmResults() {
        return districtService.getDistricts().stream()
                .map(d -> getDistrictSmResult(d.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    private List<DistrictMMResult> getAllDistrictMmResults() {
        return districtService.getDistricts().stream()
                .map(d -> getDistrictMmResult(d.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public ConsolidatedResults getConsolidatedResults() {
        if (consolidated == null) {
            constructNewResultSummary();
        }
        return consolidated;
    }

    @Override
    public CountySMResult addCountySmResult(CountyResultData resultDTO) {
        return (CountySMResult) addCountyResult(resultDTO, SINGLE_MANDATE);
    }

    @Override
    public CountyMMResult addCountyMmResult(CountyResultData resultDTO) {
        return (CountyMMResult) addCountyResult(resultDTO, MULTI_MANDATE);
    }

    @Transactional
    private CountyResult addCountyResult(CountyResultData resultDTO, ResultType type) {
        County county = districtService.getCounty(resultDTO.getCountyId());
        throwIllegalArgumentIfCountyResultExists(county, type,
                String.format("Apylinkės \"%s\" rezultatas jau užregistruotas", county));
        county.setResultByType(convertToCountyResult(resultDTO, type), type);
        districtService.save(county.getDistrict());
        return districtService.getCounty(county.getId()).getResultByType(type);
    }

    @Transactional
    @Override
    public void confirmCountyResult(Long id) {
        CountyResult countyResult = getCountyResult(id);
        throwIllegalArgumentIfConfirmed(countyResult, "Rezultatas jau patvirtintas");

        countyResult.setConfirmed(true);
        District district = countyResult.getCounty().getDistrict();
        districtService.save(district);

        ResultType type = (countyResult instanceof CountySMResult) ? SINGLE_MANDATE : MULTI_MANDATE;

        DistrictResult districtResult = updateDistrictResult(district, countyResult, type);

        // TODO: mega ugly, needs refactoring
        if (consolidated == null) {
            constructNewResultSummary();
        } else {
            if (type == MULTI_MANDATE) {
                consolidated.mergeCountyResult((CountyMMResult) countyResult);
                consolidated.setMmResults(getAllDistrictMmResults());
            } else {
                if (resultIsComplete(districtResult)) {
                    consolidated.processCompletedSmResult((DistrictSMResult) districtResult);
                }
            }
        }
    }

    @Transactional
    private DistrictResult updateDistrictResult(District district, CountyResult countyResult, ResultType type) {
        DistrictResult districtResult = (type == SINGLE_MANDATE) ?
                resultRepository.findSmResultByDistrict(district) :
                resultRepository.findMmResultByDistrict(district);
        if (districtResult == null) {
            saveNewDistrictResult(district, type);
        } else {
            districtResult.addCountyResult(countyResult);
        }
        districtService.save(district);
        return district.getResultByType(type);
    }

    private void constructNewResultSummary() {
        constructMmResultForAllDistricts();
        consolidated = new ConsolidatedResults(partyService.getParties(), districtService.getDistricts());
    }

    @Transactional
    private void constructMmResultForAllDistricts() {
        districtService.getDistricts().stream()
                .filter(d -> d.getMmResult() == null)
                .forEach(d -> saveNewDistrictResult(d, MULTI_MANDATE));
    }

    @Transactional
    private DistrictResult saveNewDistrictResult(District district, ResultType type) {
        DistrictResult result = constructDistrictResult(district, type);
        district.setResultByType(result, type);
        districtService.save(district);
        return district.getResultByType(type);
    }

    @Transactional
    @Override
    public void deleteCountyResult(Long id) {
        CountyResult result = getCountyResult(id);
        throwIllegalArgumentIfConfirmed(result, "Rezultatas jau patvritintas, negalima ištrint");

        County county = result.getCounty();
        if (result instanceof CountySMResult) {
            county.setSmResult(null);
        } else {
            county.setMmResult(null);
        }

        districtService.save(county.getDistrict());
    }

    private Result getResult(Long id) {
        Result result = resultRepository.findOne(id);
        if (result == null) {
            throw new NotFoundException("Nepavyko rasti rezultato su id " + id);
        }
        return result;
    }

    private CountyResult getCountyResult(Long id) {
        Result result = getResult(id);
        if (!isCountyResult(result)) {
            throw new NotFoundException("Nepavyko rasti apylinkės rezultato su id " + id);
        }
        return (CountyResult) result;
    }

    private DistrictResult constructDistrictResult(District district, ResultType type) {
        DistrictResult districtResult = constructBlankDistrictResult(district, type);
        district.getCounties().stream()
                .map(c -> c.getResultByType(type))
                .filter(c -> c != null && c.isConfirmed())
                .forEach(districtResult::addCountyResult);
        return districtResult;
    }

    private DistrictResult constructBlankDistrictResult(District district, ResultType type) {
        DistrictResult result = type == SINGLE_MANDATE ?
                new DistrictSMResult() :
                new DistrictMMResult();
        result.setDistrict(district);
        List<Vote> voteList = type == SINGLE_MANDATE ?
                constructBlankCandidateVoteList(district.getCandidates()):
                constructBlankPartyVoteList(partyService.getParties());
        voteList.forEach(result::addVote);
        return result;
    }

    private List<Vote> constructBlankCandidateVoteList(List<Candidate> candidates) {
        return candidates.stream()
                .map(c -> new CandidateVote(c, 0L))
                .collect(Collectors.toList());
    }

    private List<Vote> constructBlankPartyVoteList(List<Party> parties) {
        return parties.stream()
                .map(p -> new PartyVote(p, 0L))
                .collect(Collectors.toList());
    }

    private void throwIllegalArgumentIfConfirmed(CountyResult cr, String message) {
        if (cr.isConfirmed()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void throwIllegalArgumentIfCountyResultExists(County county, ResultType type, String message) {
        if (type == SINGLE_MANDATE && resultRepository.existsSmResultByCounty(county)
                || type == MULTI_MANDATE && resultRepository.existsMmResultByCounty(county)) {
            throw new IllegalArgumentException(message);
        }
    }

    private boolean resultIsComplete(DistrictResult result) {
        return result.getConfirmedCountyResults() == result.getTotalCountyResults();
    }

    private boolean isCountyResult(Result result) {
        return result instanceof CountyResult;
    }

    private CountyResult convertToCountyResult(CountyResultData resultDTO, ResultType type) {
        CountyResult result = type == SINGLE_MANDATE ?
                              new CountySMResult() :
                              new CountyMMResult();
        County county = districtService.getCounty(resultDTO.getCountyId());
        result.setCounty(county);
        result.setConfirmed(false);
        result.setTotalBallots(resultDTO.getSpoiledBallots());
        result.setSpoiledBallots(resultDTO.getSpoiledBallots());

        List<Vote> voteList = (type == SINGLE_MANDATE) ?
                              convertToCandidateVotes(resultDTO.getVoteList()) :
                              convertToPartyVotes(resultDTO.getVoteList());
        voteList.forEach(result::addVote);

        return result;
    }

    private List<Vote> convertToCandidateVotes(List<VoteData> votes) {
        return votes.stream()
                    .map(v -> new CandidateVote(candidateService.getCandidate(v.getUnitId()), v.getVotes()))
                    .collect(Collectors.toList());
    }

    private List<Vote> convertToPartyVotes(List<VoteData> votes) {
        return votes.stream()
                .map(v -> new PartyVote(partyService.getParty(v.getUnitId()), v.getVotes()))
                .collect(Collectors.toList());
    }
}