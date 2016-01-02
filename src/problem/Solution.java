package problem;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import core.Constants;
import domain.SleighCycle;
import domain.SleighCycleCostCalculator;
import domain.SleighLocation;

public class Solution implements Iterable<SleighCycle>, Serializable {

    private static final long serialVersionUID = 1L;
    private final Set<SleighCycle> cycles = new LinkedHashSet<SleighCycle>();
    private final SleighCycleCostCalculator costCalculator = new SleighCycleCostCalculator();
    @Getter private double totalCost = 0d;
    
    void add(SleighCycle cycle) {
        cycles.add(cycle);
        totalCost += costCalculator.computeCost(cycle);
    }

    public int getNumSleighs() {
        return cycles.size();
    }
    
    @Override
    public Iterator<SleighCycle> iterator() {
        return cycles.iterator();
    }
    
    @Override
    public String toString() {
        return "Solution with total cost " + getTotalCost() + " and " + getNumSleighs() + " sleighs.";
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

    public void save(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }
    
    public static Solution load(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Solution result = (Solution) ois.readObject();
        ois.close();
        return result;
    }
    
}
