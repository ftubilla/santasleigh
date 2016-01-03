package problem;

import java.io.IOException;
import java.util.Optional;

import core.Constants;
import core.InfeasiblePathException;
import data.GiftDao;
import domain.SleighCycle;
import domain.SleighLocation;

public class GreedySolver implements Solver {

    private final Options options;
    
    public GreedySolver(Options options) {
        this.options = options;
    }
    
    @Override
    public Solution solve(GiftDao giftDao) throws InfeasiblePathException {

        SleighLocationGridHolder locations = new SleighLocationGridHolder(options.latitudeGridSizeDegrees,
                options.longitudeGridSizeDegrees);
        
        for (SleighLocation location : giftDao) {
            locations.add(location);
        }
        
        Solution solution = new Solution();
        
        while ( locations.getNumLocations() > 0 ) {
            
            //Create a new cycle
            System.out.println("creating new cycle");
            SleighCycle cycle = new SleighCycle();
            SleighLocation previousLocation = Constants.SLEIGH_NORTH_POLE;
            System.out.println(locations.getClosestLocation(previousLocation));
            Optional<SleighLocation> optionalOfNextLocation;
            //Add locations until no longer possible
            while ( ( optionalOfNextLocation = 
                    locations.getClosestLocation(previousLocation) ).isPresent() &&
                    cycle.canAdd(optionalOfNextLocation.get()) ) {
                SleighLocation nextLocation = optionalOfNextLocation.get();
                cycle.add(nextLocation);
                locations.remove(nextLocation);
                previousLocation = nextLocation;
            }
            
            cycle.close();
            solution.add(cycle);
        }
        
        return solution;
    }

    public static class Options {
        public double latitudeGridSizeDegrees = 15;
        public double longitudeGridSizeDegrees = 15;
    }
    
    public static void main(String[] args) throws ClassNotFoundException,
            IOException, InfeasiblePathException {
        GiftDao dao = GiftDao.load("data/gifts.ser");
        Options options = new Options();
        options.latitudeGridSizeDegrees = 1;
        options.longitudeGridSizeDegrees = 1;
        Solver solver = new GreedySolver(options);
        Solution solution = solver.solve(dao);
        System.out.println(solution);
        solution.writeCsv("data/solutions/greedy_solver_1gridsize.csv");
    }
}
