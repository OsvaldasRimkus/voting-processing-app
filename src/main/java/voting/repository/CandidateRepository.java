package voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import voting.model.Candidate;

/**
 * Created by domas on 1/12/17.
 */
public interface CandidateRepository extends CrudRepository<Candidate, Long> {

    public Candidate findByPersonId(String personId);

    @Query("select count(c)>0 from Candidate c where c.personId = ?1")
    boolean existsByPersonId(String personId);
}
