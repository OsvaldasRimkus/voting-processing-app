//package voting.utils;
//
//import org.springframework.stereotype.Component;
//import voting.dto.CandidateDTO;
//import voting.dto.PartyDTO;
//import voting.dto.results.CandidateVoteDTO;
//import voting.dto.results.PartyVoteDTO;
//import voting.dto.results.UnitVoteRepresentation;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by andrius on 2/9/17.
// */
//
//@Component
//public class RepresentationFactory {
//
//    public static CountyResultDTO makeRepresentationOf(CountyResult cr) {
//        CountyResultDTO crr = new CountyResultDTO();
//        crr.setId(cr.getId());
//        crr.setSpoiledBallots(cr.getSpoiledBallots());
//        crr.setSingleMandateSystem(cr.isSingleMandateSystem());
//        crr.setCreatedOn(cr.getCreatedOn());
//        crr.setCountyId(cr.getCounty().getId());
//        crr.setConfirmed(cr.isConfirmed());
//        crr.setConfirmedOn(cr.getConfirmedOn());
//        crr.setUnitVotesList(makeUnitsVotesListRepresentation(cr.getUnitVotesList()));
//        return crr;
//    }
//
//    public static List<CountyResultDTO> makeRepresentationOfCountyResultsList(List<CountyResult> list) {
//        List<CountyResultDTO> crrl = new ArrayList<>();
//        list.forEach(cr -> crrl.add(makeRepresentationOf(cr)));
//        return crrl;
//    }
//
//    public static CandidateVoteDTO makeRepresentationOf(CandidateVote cv) {
//        CandidateVoteDTO cvr = new CandidateVoteDTO();
//        cvr.setId(cv.getId());
//        cvr.setVoteCount(cv.getVoteCount());
//        cvr.setCandidate(new CandidateDTO(cv.getCandidate()));
//
//        return cvr;
//    }
//
//    public static PartyVoteDTO makeRepresentationOf(PartyVote cv) {
//        PartyVoteDTO pvr = new PartyVoteDTO();
//        pvr.setId(cv.getId());
//        pvr.setVoteCount(cv.getVoteCount());
//        pvr.setParty(new PartyDTO(cv.getParty()));
//        return pvr;
//    }
//
//    public static List<UnitVoteRepresentation> makeUnitsVotesListRepresentation(List<Vote> unitVotesList) {
//        List<UnitVoteRepresentation> representations = new ArrayList<>();
//        if (CandidateVote.class.isAssignableFrom(unitVotesList.get(0).getClass())) {
//            unitVotesList.forEach(uv -> representations.add(makeRepresentationOf((CandidateVote)uv)));
//        } else {
//            unitVotesList.forEach(uv -> {
//               representations.add(makeRepresentationOf((PartyVote)uv));
//            });
//        }
//        return representations;
//    }
//}
//
