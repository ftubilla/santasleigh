package problem.networksolver;

import org.junit.Test;

import data.GiftDao;
import data.GiftsCsvParser;

public class NetworkSleighProblemTest {

    @Test
    public void testProblemConstruction() throws Exception {
        GiftsCsvParser parser = new GiftsCsvParser("data/gifts_test.csv");
        GiftDao dao = parser.parse();
        NetworkSleighProblem.Options options = new NetworkSleighProblem.Options();
        NetworkSleighProblem problem = new NetworkSleighProblem(options, dao, 2);
        problem.init();
    }

}
