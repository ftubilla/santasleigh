package problem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import core.Constants;
import domain.SleighCycle;
import domain.SleighCycleCostCalculator;
import domain.SleighLocation;

public class Solution implements Iterable<SleighCycle> {

    private final Set<SleighCycle> cycles = new LinkedHashSet<SleighCycle>();
    private final SleighCycleCostCalculator costCalculator = new SleighCycleCostCalculator();
    @Getter private double totalCost = 0d;
    
    void add(SleighCycle cycle) {
        cycles.add(cycle);
        totalCost += costCalculator.computeCost(cycle);
    }

    @Override
    public Iterator<SleighCycle> iterator() {
        return cycles.iterator();
    }
    
    public void writeCsv(String filename) throws IOException {
        
        BufferedWriter writer = new BufferedWriter( new FileWriter(filename) );
        writer.write("GiftId,TripId\n");
        for (SleighCycle cycle : cycles) {
            for (SleighLocation location : cycle) {
                if ( location.equals(Constants.SLEIGH_NORTH_POLE) ) {
                    continue;
                } else {
                    writer.write(location.getId() + "," + cycle.getId() + "\n");
                }
            }
        }
        writer.close();
    }

}
