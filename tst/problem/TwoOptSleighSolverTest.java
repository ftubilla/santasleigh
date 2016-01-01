package problem;

import org.junit.Test;

import data.GiftDao;
import data.GiftsCsvParser;

public class TwoOptSleighSolverTest {

    @Test
    public void test() throws Exception {
        GiftsCsvParser parser = new GiftsCsvParser("data/gifts_test.csv");
        GiftDao dao = parser.parse();
        Solver baseSolver = new OneGiftPerSleighSolver();
        Solution baseSolution = baseSolver.solve(dao);
        System.out.println("Base solution cost " + baseSolution.getTotalCost());
        Solver solver = new TwoOptSleighSolver(1000);
        Solution solution = solver.solve(dao, baseSolution);
        System.out.println("Two Opt solution cost " + solution.getTotalCost());
        solution.writeCsv("data/sol_test.csv");
       
    }

}
