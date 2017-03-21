package voting.repository;

import org.springframework.data.repository.CrudRepository;
import voting.model.County;

/**
 * Created by andrius on 1/21/17.
 */

public interface CountyRepository extends CrudRepository<County, Long> { }
