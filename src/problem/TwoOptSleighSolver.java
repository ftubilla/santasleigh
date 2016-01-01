package problem;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.MinMaxPriorityQueue;

import lombok.ToString;
import core.InfeasiblePathException;
import data.GiftDao;
import domain.SleighCycle;
import domain.SleighCycleCostCalculator;

public class TwoOptSleighSolver implements Solver {

    private final Options options;
    private final SleighCycleGridHolder holder;
    private final MinMaxPriorityQueue<SleighCycleMerge> mergeQueue;
    
    public TwoOptSleighSolver(Options options) {
        this.options = options;
        this.holder = new SleighCycleGridHolder(options.latitudeGridSizeDegrees,
                options.longitudeGridSizeDegrees);
        this.mergeQueue = MinMaxPriorityQueue.maximumSize(options.maxMergersPerIteration).create();
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
        
        //Fill the holder
        for (SleighCycle cycle : cycles) {
            holder.add(cycle);
        }
        
        SleighCycleCostCalculator costCalculator = new SleighCycleCostCalculator();
        boolean movesLeft = true;
        int it = 0;
        while (it++ < options.maxIterations && movesLeft) {
            
            System.out.println("Iteration " + it);
            //Find the best mergers for each pair of cycles
            mergeQueue.clear();
            movesLeft = false;
            for (SleighCycle cycle1 : cycles) {
                for (SleighCycle cycle2 : holder.getNeighbors(cycle1, options.numLatitudeNeighbors,
                        options.numLongitudeNeighbors)) {
                    if (cycle1.equals(cycle2) || !cycles.contains(cycle2)) {
                        continue;
                    }
                    if ( cycle1.getId() % 10_000 == 0 ) {
                        System.out.println("Evaluating merge of " + cycle1.getId() + " and " + cycle2.getId());
                    }
                    Optional<SleighCycle> mergedCycleOpt = SleighCycleTransformer.merge(cycle1, cycle2);
                    if ( mergedCycleOpt.isPresent() ) {
                        SleighCycle mergedCycle = mergedCycleOpt.get();
                        double mergedCycleCost = costCalculator.computeCost(mergedCycle);
                        double cycle1Cost = costCalculator.computeCost(cycle1);
                        double cycle2Cost = costCalculator.computeCost(cycle2);
                        double costReduction = cycle1Cost + cycle2Cost - mergedCycleCost;
                        if ( costReduction > 0 ) {
                            //Add the merge to the list of potential mergers
                            SleighCycleMerge potentialMerge = new SleighCycleMerge();
                            potentialMerge.cycle1 = cycle1;
                            potentialMerge.cycle2 = cycle2;
                            potentialMerge.mergedCycle = mergedCycle;
                            potentialMerge.costReduction = costReduction;
                            mergeQueue.add(potentialMerge);
                            movesLeft = true;
                        }
                    }
                }
            }
            
            //Execute as many mergers as possible and do a new iteration
            if ( movesLeft ) {
                int mergers = 0;
                for (SleighCycleMerge potentialMerge : mergeQueue) {
                    if ( cycles.contains(potentialMerge.cycle1) && 
                            cycles.contains(potentialMerge.cycle2) ) {
                        cycles.remove(potentialMerge.cycle1);
                        cycles.remove(potentialMerge.cycle2);
                        cycles.add(potentialMerge.mergedCycle);
                        mergers++;
                    }
                    if ( mergers == options.maxMergersPerIteration ) {
                        break;
                    }
                }
            }
        }
        
        System.out.println("Finished after " + (it - 1) + " iterations. Moves left? " + movesLeft);
        
        Solution solution = new Solution();
        for (SleighCycle cycle : cycles) {
            solution.add(cycle);
        }
        
        return solution;
    }
    
    @ToString
    public static class Options {
        public int maxIterations = 100;
        public int maxMergersPerIteration = 20;
        public double latitudeGridSizeDegrees = 15;
        public double longitudeGridSizeDegrees = 15;
        public int numLatitudeNeighbors = 1;
        public int numLongitudeNeighbors = 1;
    }
    
    @ToString
    public static class SleighCycleMerge implements Comparable<SleighCycleMerge> {
        public SleighCycle cycle1;
        public SleighCycle cycle2;
        public SleighCycle mergedCycle;
        public double costReduction;
        @Override
        public int compareTo(SleighCycleMerge otherMerge) {
            //Greatest cost reduction first
            return - Double.compare(costReduction, otherMerge.costReduction);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException,
            IOException, InfeasiblePathException {
        GiftDao dao = GiftDao.load("data/gifts.ser");
        Options options = new Options();
        options.maxIterations = 150;
        options.maxMergersPerIteration = 20_000;
        options.latitudeGridSizeDegrees = 1;
        options.longitudeGridSizeDegrees = 1;
        options.numLatitudeNeighbors = 2;
        options.numLongitudeNeighbors = 2;
        Solver solver = new TwoOptSleighSolver(options);
        Solution solution = solver.solve(dao);
        System.out.println("Solution cost " + solution.getTotalCost());
        solution.writeCsv("data/solutions/two_opt_150it_20kmergers.csv");
    }
    
}
