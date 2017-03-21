package voting;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by domas on 2/4/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CandidateServiceTest.class,
        ParsingServiceTest.class,
        PartyServiceIntegrationTest.class,
        DistrictServiceIntegrationTest.class,
        ResultServiceIntegrationTest.class
})

public class TestSuite {
}
