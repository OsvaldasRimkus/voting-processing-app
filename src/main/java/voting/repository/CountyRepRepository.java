package voting.repository;

import org.springframework.data.repository.CrudRepository;
import voting.model.CountyRep;

/**
 * Created by domas on 1/10/17.
 */

public interface CountyRepRepository extends CrudRepository<CountyRep, Long> {

    CountyRep findOneByUsername(String username);

}
