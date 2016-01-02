package problem;

import java.io.IOException;

import core.InfeasiblePathException;
import data.GiftDao;

public class GreedyPlust2OptSolver {

    public static void main(String[] args) throws ClassNotFoundException,
        IOException, InfeasiblePathException {
        GiftDao dao = GiftDao.load("data/gifts.ser");
        GreedySolver.Options options = new GreedySolver.Options();
        options.latitudeGridSizeDegrees = 1;
        options.longitudeGridSizeDegrees = 1;
        Solver solver = new GreedySolver(options);
        Solution solution = solver.solve(dao);
        
        TwoOptSleighSolver.Options options2 = new TwoOptSleighSolver.Options();
        options2.maxIterations = 50;
        options2.maxMergersPerIteration = 20_000;
        options2.latitudeGridSizeDegrees = 45;
        options2.longitudeGridSizeDegrees = 45;
        options2.numLatitudeNeighbors = 1;
        options2.numLongitudeNeighbors = 1;
        Solver solver2 = new TwoOptSleighSolver(options2);
        Solution solution2 = solver2.solve(dao, solution);
        
        System.out.println(solution2);
        solution2.writeCsv("data/solutions/greedy_plus_2opt.csv");
    }
}
