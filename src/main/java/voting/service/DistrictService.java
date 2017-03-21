package voting.service;

import com.opencsv.exceptions.CsvException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import voting.dto.county.CountyData;
import voting.dto.district.DistrictData;
import voting.model.Candidate;
import voting.model.County;
import voting.model.District;
import java.io.IOException;
import java.util.List;

/**
 * Created by domas on 1/10/17.
 */

public interface DistrictService {

    District save(District district);

    District addNewDistrict(DistrictData districtData);

    void deleteDistrict(Long id);

    District getDistrict(Long id);

    District getDistrict(String name);

    List<District> getDistricts();

    Page<District> listAllByPage(Pageable pageable);

    List<Candidate> getCandidateList(Long id);

    District setCandidateList(Long id, MultipartFile file) throws IOException, CsvException;

    void deleteCandidateList(Long id);

    boolean exists(Long id);

    boolean exists(String name);

    List<County> getCounties();

    County getCounty(Long id);

    District addCounty(Long districtId, CountyData countyData);

    County updateCounty(Long countyId, CountyData countyData);

    void deleteCounty(Long countyId);

    List<County> getCountiesByDistrictId(Long id);

    District updateDistrict(DistrictData districtData, Long id);
}
