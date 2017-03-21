package voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import voting.model.Party;

/**
 * Created by domas on 1/10/17.
 */
public interface PartyRepository extends CrudRepository<Party, Long> {

    Party findByName(String name);

    @Query("select count(p)>0 from Party p where p.name = ?1")
    boolean existsByName(String name);
}
