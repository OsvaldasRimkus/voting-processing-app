package voting.service;

import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import voting.dto.candidate.CandidateData;
import voting.dto.county.CountyData;
import voting.dto.district.DistrictData;
import voting.exception.NotFoundException;
import voting.model.Candidate;
import voting.model.County;
import voting.model.District;
import voting.repository.CountyRepository;
import voting.repository.DistrictRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by domas on 1/10/17.
 */
@Service
public class DistrictServiceImpl implements DistrictService {

    private DistrictRepository districtRepository;
    private CountyRepository countyRepository;
    private CandidateService candidateService;
    private StorageService storageService;
    private ParsingService parsingService;


    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository,
                               CountyRepository countyRepository,
                               CandidateService candidateService,
                               StorageService storageService,
                               ParsingService parsingService) {
        this.districtRepository = districtRepository;
        this.countyRepository = countyRepository;
        this.candidateService = candidateService;
        this.storageService = storageService;
        this.parsingService = parsingService;
    }

    @Override
    public District getDistrict(Long id) {
        District district = districtRepository.findOne(id);
        throwNotFoundIfNull(district, "Nepavyko rasti apygardos su id " + id);
        return district;
    }

    @Override
    public District getDistrict(String name) {
        District district = districtRepository.findByName(name);
        throwNotFoundIfNull(district, String.format("Nepavyko rasti apygardos pavadinimu \"%s\"", name));
        return district;
    }

    @Override
    public District save(District district) {
        return districtRepository.save(district);
    }

    @Transactional
    @Override
    public District addNewDistrict(DistrictData districtData) {
        if (exists(districtData.getName())) {
            throw new IllegalArgumentException(String.format("Apygarda \"%s\" jau egzistuoja", districtData.getName()));
        }
        District district = new District(districtData.getName());

        if (districtData.getCountiesData() != null) {
            districtData.getCountiesData().forEach(
                    countyData -> district.addCounty(convertCountyDTOtoEntity(countyData)));
        }

        // TODO: fix this temporary hack
        District d;
        try {
            d = districtRepository.save(district);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Apylinkių pavadinimai turi būti skirtingi", ex);
        }
        return d;
    }

    @Transactional
    @Override
    public void deleteDistrict(Long id) {
        District district = getDistrict(id);
        deleteCandidateList(district);
        districtRepository.delete(id);
    }

    @Override
    public List<District> getDistricts() {
        return (List<District>) districtRepository.findAll();
    }

    @Override
    public Page<District> listAllByPage(Pageable pageable) {
        return districtRepository.findAll(pageable);
    }

    @Override
    public List<Candidate> getCandidateList(Long id) {
        return getDistrict(id).getCandidates();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public District setCandidateList(Long id, MultipartFile file) throws IOException, CsvException {
        return setCandidateList(getDistrict(id), file);
    }

    @Transactional(rollbackFor = Exception.class)
    private District setCandidateList(District district, MultipartFile file) throws IOException, CsvException {

        List<CandidateData> candidateListData = extractCandidateList(file);
        deleteCandidateList(district);

        candidateListData.forEach(
                candidateData -> {
                    candidateData.setDistrictId(district.getId());
                    candidateData.setDistrictName(district.getName());
                    Candidate candidate = candidateService.addNewOrGetIfExists(candidateData);
                    district.addCandidate(candidate);
                });
        districtRepository.save(district);

        String fileName = String.format("district_%d.csv", district.getId());
        storageService.store(fileName, file);

        return district;
    }

    /**
     * Unbinds all candidates from district with given id.
     * If candidate is no longer bound to any party or district after unbounding, he is deleted from db.
     * @param id - district id
     */
    @Transactional
    @Override
    public void deleteCandidateList(Long id) {
        deleteCandidateList(getDistrict(id));
    }

    private void deleteCandidateList(District district) {
        Stream<Candidate> orphanCandidates = district.getCandidates().stream().filter(c -> c.getParty() == null);
        district.removeAllCandidates();
        orphanCandidates.forEach(c -> candidateService.deleteCandidate(c.getId()));
        districtRepository.save(district);
    }

    @Override
    public boolean exists(Long id) {
        return districtRepository.exists(id);
    }

    @Override
    public boolean exists(String name) {
        return districtRepository.existsByName(name);
    }

    @Override
    public List<County> getCounties() {
        return (List<County>) countyRepository.findAll();
    }

    @Override
    public County getCounty(Long id) {
        County county = countyRepository.findOne(id);
        throwNotFoundIfNull(county, "Nepavyko rasti apskrities su id " + id);
        return county;
    }

    @Override
    public District addCounty(Long districtId, CountyData countyData) {
        District district = getDistrict(districtId);
        County county = convertCountyDTOtoEntity(countyData);
        district.addCounty(county);

        // TODO: fix this temporary hack
        try {
            district = districtRepository.save(district);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Apylinkių pavadinimai turi būti skirtingi", ex);
        }
        return district;
    }

    @Transactional
    @Override
    public District updateDistrict(DistrictData districtData, Long districtId) {
        District district = districtRepository.findOne(districtId);
        district.setName(districtData.getName());
        return districtRepository.save(district);
    }

    @Transactional
    @Override
    public County updateCounty(Long countyId, CountyData countyData) {
        County county = getCounty(countyId);
        county.setName(countyData.getName());
        county.setVoterCount(countyData.getVoterCount());
        county.setAddress(countyData.getAddress());

        // TODO: fix this temporary hack
        try {
            districtRepository.save(county.getDistrict());
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Apylinkių pavadinimai turi būti skirtingi", ex);
        }
        return county;
    }

    @Override
    public void deleteCounty(Long countyId) {
        County county = getCounty(countyId);
        District district = county.getDistrict();
        district.removeCounty(county);
        districtRepository.save(district);
    }

    @Override
    public List<County> getCountiesByDistrictId(Long id) {
        return getDistrict(id).getCounties();
    }

    private List<CandidateData> extractCandidateList(MultipartFile file) throws IOException, CsvException {
        Path tempFile = storageService.storeTemporary(file);

        List<CandidateData> candidateListData;
        try {
            candidateListData = parsingService.parseSingleMandateCandidateList(tempFile.toFile());
        } finally {
            storageService.delete(tempFile);
        }
        return candidateListData;
    }

    private County convertCountyDTOtoEntity(CountyData cd) {
        return new County(cd.getName(), cd.getVoterCount(), cd.getAddress());
    }

    private void throwNotFoundIfNull(Object object, String message) {
        if (object == null) {
            throw (new NotFoundException(message));
        }
    }

}
