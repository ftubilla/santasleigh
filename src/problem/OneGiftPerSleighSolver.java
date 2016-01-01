package problem;

import java.io.IOException;

import core.InfeasiblePathException;
import data.GiftDao;
import domain.SleighCycle;
import domain.SleighLocation;

public class OneGiftPerSleighSolver implements Solver {

    @Override
    public Solution solve(GiftDao giftDao) throws InfeasiblePathException {
        
        Solution solution = new Solution();
        
        for (SleighLocation loc : giftDao) {
            SleighCycle cycle = new SleighCycle();
            cycle.add(loc);
            cycle.close();
            solution.add(cycle);
        }
        
        return solution;
    }

    public static void main(String[] args) throws ClassNotFoundException,
        IOException, InfeasiblePathException {
        GiftDao dao = GiftDao.load("data/gifts.ser");
        OneGiftPerSleighSolver solver = new OneGiftPerSleighSolver();
        Solution solution = solver.solve(dao);
        System.out.println("Solution cost " + solution.getTotalCost());
        solution.writeCsv("data/solutions/one_sleigh_per_gift.csv");
    }
    
}
