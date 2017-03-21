package voting.repository;

import org.springframework.data.repository.CrudRepository;
import voting.model.Admin;

/**
 * Created by andrius on 3/7/17.
 */

public interface AdminRepository extends CrudRepository<Admin, Long> {

    Admin findOneByUsername(String username);

}
