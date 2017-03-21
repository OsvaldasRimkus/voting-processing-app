package voting.results.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import voting.model.County;
import voting.model.District;
import voting.results.model.result.*;

import java.util.List;

/**
 * Created by domas on 2/23/17.
 */

public interface ResultRepository extends CrudRepository<Result, Long> {

    @Query("select r from CountySMResult r where r.county = ?1")
    public CountySMResult findSmResultByCounty(County county);

    @Query("select r from CountyMMResult r where r.county = ?1")
    public CountyMMResult findMmResultByCounty(County county);

    @Query("select r from DistrictSMResult r where r.district = ?1")
    public DistrictSMResult findSmResultByDistrict(District district);

    @Query("select r from DistrictMMResult r where r.district = ?1")
    public DistrictMMResult findMmResultByDistrict(District district);

    @Query("select r from DistrictMMResult r")
    public List<DistrictMMResult> findAllDistrictMmResults();

    @Query("select count(r)>0 from CountySMResult r where r.county = ?1")
    boolean existsSmResultByCounty(County county);

    @Query("select count(r)>0 from CountyMMResult r where r.county = ?1")
    boolean existsMmResultByCounty(County county);

    @Query("select r from CountyResult r where r.id = ?1")
    CountyResult findOneCountyResult(Long id);
}