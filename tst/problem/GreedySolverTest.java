package problem;

import org.junit.Test;

import problem.GreedySolver.Options;
import data.GiftDao;
import data.GiftsCsvParser;

public class GreedySolverTest {

    @Test
    public void test() throws Exception {
        GiftsCsvParser parser = new GiftsCsvParser("data/gifts_test.csv");
        GiftDao dao = parser.parse();
        Options options = new Options();
        Solver solver = new GreedySolver(options);
        Solution solution = solver.solve(dao);
        solution.writeCsv("data/test/greedy_solver_test.csv");
        System.out.println(solution);
    }

}
