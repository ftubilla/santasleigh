package problem;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;

import core.Constants;
import core.InfeasiblePathException;
import domain.SleighCycle;
import domain.SleighCycleCostCalculator;
import domain.SleighLocation;

public class SleighCycleTransformer {

    /**
     * Find the lowest cost insertion of cycle2 onto cycle1. If it's not possible to merge
     * the two cycles, an empty cycle is returned.
     * @param cycle1
     * @param cycle2
     * @return optionalOfMergedCycle
     * @throws InfeasiblePathException 
     */
    public static Optional<SleighCycle> merge(SleighCycle cycle1, SleighCycle cycle2) 
            throws InfeasiblePathException {
        
        if ( cycle1.getCargoWeight() + cycle2.getCargoWeight() > Constants.SLEIGH_CARGO_WEIGHT_LIMIT ) {
            //Cannot merge the two cycles due to cargo limits
            return Optional.empty();
        }
        SleighCycle bestCycle = null;
        double lowestCost = Double.MAX_VALUE;
        SleighCycleCostCalculator costCalculator = new SleighCycleCostCalculator();
        Iterator<SleighLocation> locIt1 = cycle1.iterator();
        
        SleighLocation insertionPoint = locIt1.next(); //Initialize the insertion point
        LinkedHashSet<SleighLocation> passedLocations = new LinkedHashSet<>();
        while ( locIt1.hasNext() ) {
            //Add the insertion point to the passed locations (unless it's the NP)
            if ( !insertionPoint.isNorthPole() ) {
                passedLocations.add(insertionPoint);
            }
            //Create a new cycle and add all the passed locations first
            SleighCycle mergedCycle = new SleighCycle();
            for (SleighLocation passedLoc : passedLocations) {
                mergedCycle.add(passedLoc);
            }
            //Now add all the locations from cycle 2 (again excluding NP)
            for (SleighLocation cycle2Loc : cycle2) {
                if ( !cycle2Loc.isNorthPole() ) {
                    mergedCycle.add(cycle2Loc);
                }
            }
            //Finally, add the remaining locations from cycle 1
            for (SleighLocation cycle1Loc : cycle1) {
                if (!cycle1Loc.isNorthPole() && 
                        !passedLocations.contains(cycle1Loc)) {
                    mergedCycle.add(cycle1Loc);
                }
            }
            //Close the cycle and save it if it has the lowest cost so far
            mergedCycle.close();
            double mergedCycleCost = costCalculator.computeCost(mergedCycle);
            if ( bestCycle == null || mergedCycleCost < lowestCost ) {
                bestCycle = mergedCycle;
                lowestCost = mergedCycleCost;
            }
            //Get the next location to use as insertion point
            insertionPoint = locIt1.next();
        }
        return Optional.ofNullable(bestCycle);
    }
    
}
