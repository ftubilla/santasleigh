package problem;

import org.junit.Test;

import problem.TwoOptSleighSolver.Options;
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
        Options options = new Options();
        options.maxIterations = 10;
        options.maxMergersPerIteration = 20;
        options.latitudeGridSizeDegrees = 15;
        options.longitudeGridSizeDegrees = 15;
        options.numLatitudeNeighbors = 24;
        options.numLongitudeNeighbors = 24;
        Solver solver = new TwoOptSleighSolver(options);
        Solution solution = solver.solve(dao, baseSolution);
        System.out.println("Two Opt solution cost " + solution.getTotalCost());
        solution.writeCsv("data/sol_test.csv");
       
    }

}
