package domain;

import java.util.Iterator;

import cost.HaversineDistance;
import cost.PathCostCalculator;

public class SleighCycleCostCalculator implements PathCostCalculator<SleighCycle> {
    
    @Override
    public double computeCost(SleighCycle cycle) {
        
        double currentWeight = cycle.getTakeoffWeight();
        double cost = 0.0;
        
        Iterator<SleighLocation> locIt = cycle.iterator();
        
        SleighLocation prevLoc = locIt.next();
        while ( locIt.hasNext() ) {
            SleighLocation nextLoc = locIt.next();
            cost += currentWeight * HaversineDistance.compute(prevLoc, nextLoc);
            currentWeight -= nextLoc.getWeight();
            prevLoc = nextLoc;
        }

        return cost;
    }

}
