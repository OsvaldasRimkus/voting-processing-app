package voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import voting.model.District;

/**
 * Created by domas on 1/10/17.
 */

public interface DistrictRepository extends PagingAndSortingRepository<District, Long> {

    public District findByName(String name);

    @Query("select count(d)>0 from District d where d.name = ?1")
    boolean existsByName(String name);

}
