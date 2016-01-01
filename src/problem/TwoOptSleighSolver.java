package problem;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import core.InfeasiblePathException;
import data.GiftDao;
import domain.SleighCycle;
import domain.SleighCycleCostCalculator;

public class TwoOptSleighSolver implements Solver {

    private final int maxIt;
    
    public TwoOptSleighSolver(int maxIterations) {
        this.maxIt = maxIterations;
    }
    
    @Override
    public Solution solve(GiftDao giftDao) throws InfeasiblePathException {
        OneGiftPerSleighSolver auxSolver = new OneGiftPerSleighSolver();
        return solve(giftDao, auxSolver.solve(giftDao));
    }
    
    @Override
    public Solution solve(GiftDao giftDao, Solution startingSolution) throws InfeasiblePathException {
        
        //Get all cycles from the solution
        Set<SleighCycle> cycles = StreamSupport.stream(startingSolution.spliterator(), false)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        
        
        SleighCycleCostCalculator costCalculator = new SleighCycleCostCalculator();
        boolean movesLeft = true;
        int it = 0;
        while (it++ < maxIt && movesLeft) {
            
            System.out.println("Iteration " + it);
            //Find the best merge for each pair of cycles
            SleighCycle[] bestCyclesToMerge = {null, null};
            SleighCycle bestMergedCycle = null;
            double maxCostDecrease = 0.0;
            movesLeft = false;
            for (SleighCycle cycle1 : cycles) {
                for (SleighCycle cycle2 : cycles) {
                    if (cycle1.equals(cycle2)) {
                        continue;
                    }
                    Optional<SleighCycle> mergedCycleOpt = SleighCycleTransformer.merge(cycle1, cycle2);
                    if ( mergedCycleOpt.isPresent() ) {
                        SleighCycle mergedCycle = mergedCycleOpt.get();
                        double mergedCycleCost = costCalculator.computeCost(mergedCycle);
                        double cycle1Cost = costCalculator.computeCost(cycle1);
                        double cycle2Cost = costCalculator.computeCost(cycle2);
                        double costReduction = cycle1Cost + cycle2Cost - mergedCycleCost;
                        if ( costReduction > maxCostDecrease ) {
                            bestCyclesToMerge[0] = cycle1;
                            bestCyclesToMerge[1] = cycle2;
                            bestMergedCycle = mergedCycle;
                            maxCostDecrease = costReduction;
                            movesLeft = true;
                        }
                    }
                }
            }
            
            //Replace the cycles and do a new iteration
            if ( movesLeft ) {
                cycles.remove(bestCyclesToMerge[0]);
                cycles.remove(bestCyclesToMerge[1]);
                cycles.add(bestMergedCycle);
            }
        }
        
        System.out.println("Finished after " + it + " iterations. Moves left? " + movesLeft);
        
        Solution solution = new Solution();
        for (SleighCycle cycle : cycles) {
            solution.add(cycle);
        }
        
        return solution;
    }

    public static void main(String[] args) throws ClassNotFoundException,
        IOException, InfeasiblePathException {
        GiftDao dao = GiftDao.load("data/gifts.ser");
        Solver solver = new TwoOptSleighSolver(25);
        Solution solution = solver.solve(dao);
        System.out.println("Solution cost " + solution.getTotalCost());
        solution.writeCsv("data/solutions/two_opt.csv");
    }

}
