package problem;

import org.junit.Test;

import data.GiftDao;
import data.GiftsCsvParser;

public class SolutionTest {

    @Test
    public void test() throws Exception {
        GiftsCsvParser parser = new GiftsCsvParser("data/gifts_test.csv");
        GiftDao dao = parser.parse();
        Solver solver = new OneGiftPerSleighSolver();
        Solution solution = solver.solve(dao);
        solution.save("data/test/sol_test2.ser");
        Solution solution2 = Solution.load("data/test/sol_test2.ser");
        solution2.writeCsv("data/test/sol_test2.csv");
    }

}
