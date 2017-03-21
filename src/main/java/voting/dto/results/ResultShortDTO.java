package voting.dto.results;

import voting.results.model.result.*;
import voting.utils.Constants;

/**
 * Created by domas on 3/2/17.
 */
public class ResultShortDTO {

    private Long id;
    private String link;


    public ResultShortDTO(Result result) {
        this.id = result.getId();
        this.link = constructLink(result);
    }

    private String constructLink(Result result) {
        if (result instanceof CountyResult) {
            Long countyId = ((CountyResult) result).getCounty().getId();
            link = String.format("%s/results/county/%d", Constants.API_ROOT_URL, id);
            if (result instanceof CountySMResult) {
                link = link.concat("/single-mandate");
            } else {
                link = link.concat("/multi-mandate");
            }
        } else {
            Long districtId = ((DistrictResult)result).getDistrict().getId();
            link = String.format("%s/results/district/%d", Constants.API_ROOT_URL, id);
            if (result instanceof DistrictSMResult) {
                link = link.concat("/single-mandate");
            } else {
                link = link.concat("/multi-mandate");
            }
        }
        return link;
    }

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }
}
