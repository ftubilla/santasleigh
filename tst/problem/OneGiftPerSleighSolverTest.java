package problem;

import org.junit.Test;

import data.GiftDao;
import data.GiftsCsvParser;

public class OneGiftPerSleighSolverTest {

    @Test
    public void test() throws Exception {
        GiftsCsvParser parser = new GiftsCsvParser("data/gifts_test.csv");
        GiftDao dao = parser.parse();
        Solver solver = new OneGiftPerSleighSolver();
        Solution solution = solver.solve(dao);
        solution.writeCsv("data/sol_test.csv");
       
    }

}
