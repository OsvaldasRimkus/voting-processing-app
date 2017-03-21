package voting.dto.results.vote;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by andrius on 1/24/17.
 */

public class VoteListData {

    @Valid
    @NotNull
    List<VoteData> voteList;

    public VoteListData() {
    }

    public List<VoteData> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<VoteData> voteList) {
        this.voteList = voteList;
    }
}
