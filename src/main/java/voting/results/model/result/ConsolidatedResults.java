package voting.results.model.result;

import voting.model.Candidate;
import voting.model.District;
import voting.model.Party;
import voting.results.model.votecount.PartyVote;

import java.util.*;
import java.util.stream.Collectors;

import static voting.utils.Constants.PARTY_ENTRY_MARK;
import static voting.utils.Constants.TOTAL_MANDATES;

/**
 * Created by domas on 3/9/17.
 */
public class ConsolidatedResults {

    private Long nonPartyElectees;

    private int mandatesForMmVoting;
    private Set<Candidate> smElectedCandidates;
    private Set<Candidate> electedCandidates;

    private Map<Party, Long> partyVotecount;
    private Map<Party, Long> preliminaryMmPartyMandates;
    private Map<Party, Long> totalPartyMandates;

    private int completedSmResults = 0;
    private int completedMmResults = 0;

    private Long validBallots = 0L;
    private Long totalBallots = 0L;
    private Long spoiledBallots = 0L;

    private List<DistrictMMResult> mmResults;
    private int totalDistricts;


    public ConsolidatedResults(List<Party> parties, List<District> districts) {

        this.partyVotecount = parties.stream().collect(Collectors.toMap(p -> p, p -> 0L));
        this.preliminaryMmPartyMandates = parties.stream().collect(Collectors.toMap(p -> p, p -> 0L));
        this.totalPartyMandates = parties.stream().collect(Collectors.toMap(p -> p, p -> 0L));
        this.nonPartyElectees = 0L;

        this.mandatesForMmVoting = TOTAL_MANDATES - districts.size();

        this.totalDistricts = districts.size();

        this.mmResults = districts.stream().map(District::getMmResult).collect(Collectors.toList());
        this.mmResults.forEach(this::mergeDistrictResult);

        this.smElectedCandidates = new HashSet<>();
        this.electedCandidates = new HashSet<>();

        districts.stream()
                .map(District::getSmResult)
                .filter(r -> r!= null)
                .filter(this::resultIsComplete)
                .forEach(this::processCompletedSmResult);
    }


    private void mergeDistrictResult(DistrictMMResult result) {
        validBallots += result.getValidBallots();
        spoiledBallots += result.getSpoiledBallots();
        totalBallots = validBallots + spoiledBallots;
        mergePartyVotes(result.getVotes());
        computePreliminaryMmPartyMandates();

        if (resultIsComplete(result)) {
            processCompletedMmResult(result);
        }
    }

    public void mergeCountyResult(CountyMMResult result) {
        validBallots += result.getValidBallots();
        spoiledBallots += result.getSpoiledBallots();
        totalBallots = validBallots + spoiledBallots;
        mergePartyVotes(result.getVotes());
        computePreliminaryMmPartyMandates();

        DistrictMMResult dr = result.getCounty().getDistrict().getMmResult();
        if (resultIsComplete(dr)) {
            processCompletedMmResult(dr);
        }
    }

    private void computePreliminaryMmPartyMandates() {
        Map<Party, Double> mandateFractions = new HashMap<>();

        Long validBallotsForPassingParties = partyVotecount.entrySet().stream()
                .mapToLong(Map.Entry::getValue)
                .filter(this::partyPassesEntryMark)
                .sum();

        partyVotecount.forEach((party, votecount) -> {
            if (partyPassesEntryMark(votecount)) {
                Double floatMandates = computeMandates(votecount, validBallotsForPassingParties);
                Long longMandates = floatMandates.longValue();
                preliminaryMmPartyMandates.put(party, longMandates);
                mandateFractions.put(party, floatMandates - longMandates);
            } else {
                preliminaryMmPartyMandates.put(party, 0L);
            }
        });

        Long mandatesGiven = preliminaryMmPartyMandates.values().stream().reduce(Long::sum).get();
        Long mandatesRemaining = mandatesForMmVoting - mandatesGiven;

        if (validBallotsForPassingParties > 0) {
            mandateFractions.entrySet().stream()
                    .sorted(Map.Entry.<Party, Double>comparingByValue().reversed())
                    .limit(mandatesRemaining)
                    .map(Map.Entry::getKey)
                    .forEach(party -> preliminaryMmPartyMandates.merge(party, 1L, Long::sum));
        }
    }

    private boolean partyPassesEntryMark(Long votecount) {
        return (double) votecount / validBallots > PARTY_ENTRY_MARK;
    }

    private Double computeMandates(Long votecount, Long validBallots) {
        if (validBallots == 0) {
            return 0d;
        }
        double percentOfAllVotes = (double) votecount / validBallots;
        return percentOfAllVotes > PARTY_ENTRY_MARK ?
               percentOfAllVotes * mandatesForMmVoting :
               0d;
    }

    private void mergePartyVotes(List<PartyVote> votes) {
        votes.forEach(v -> partyVotecount.merge(v.getParty(), v.getVoteCount(), Long::sum));
    }


    public void processCompletedSmResult(DistrictSMResult result) {
        completedSmResults++;
        Candidate winner = result.getVotes().get(0).getCandidate();

        smElectedCandidates.add(winner);
        if (winner.getParty() == null) {
            nonPartyElectees++;
        } else {
            totalPartyMandates.merge(winner.getParty(), 1L, Long::sum);
        }

        if (!electedCandidates.add(winner)) {
            // TODO; padaryti exceptiona, jeigu pritruksta nariu partijoj
            Candidate nextCandidate = winner.getParty().getCandidates().stream()
                    .filter(this::candidateIsNotYetElected)
                    .findFirst()
                    .get();
            electedCandidates.add(nextCandidate);
        }
    }

    private void processCompletedMmResult(DistrictMMResult result) {
        completedMmResults++;
        if (completedMmResults == totalDistricts) {
            addMandatesWonInMultiMandateVoting();
        }
    }

    private void addMandatesWonInMultiMandateVoting() {
        preliminaryMmPartyMandates.forEach((party, mandates) -> {
            party.getCandidates().stream()
                    .filter(this::candidateIsNotYetElected)
                    .limit(mandates)
                    .forEach(electedCandidates::add);
        });
        preliminaryMmPartyMandates.forEach((party, mandates) -> {
            totalPartyMandates.merge(party, mandates, Long::sum);
        });
    }


    private boolean resultIsComplete(DistrictResult result) {
        return result.getConfirmedCountyResults() == result.getTotalCountyResults();
    }

    private boolean candidateIsNotYetElected(Candidate candidate) {
        return !electedCandidates.contains(candidate);
    }

    public void setMmResults(List<DistrictMMResult> mmResults) {
        this.mmResults = mmResults;
    }

    public Long getNonPartyElectees() {
        return nonPartyElectees;
    }

    public Set<Candidate> getElectedCandidates() {
        return electedCandidates;
    }

    public Map<Party, Long> getPreliminaryMmPartyMandates() {
        return preliminaryMmPartyMandates;
    }

    public Map<Party, Long> getPartyVotecount() {
        return partyVotecount;
    }

    public Map<Party, Long> getTotalPartyMandates() {
        return totalPartyMandates;
    }

    public int getCompletedSmResults() {
        return completedSmResults;
    }

    public int getCompletedMmResults() {
        return completedMmResults;
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

    public List<DistrictMMResult> getMmResults() {
        return mmResults;
    }

    public int getTotalDistricts() {
        return totalDistricts;
    }
}
