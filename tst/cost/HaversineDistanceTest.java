package cost;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.Constants;
import core.Location;

public class HaversineDistanceTest {

    @Test
    public void testDistanceNorthPoleToSouthPole() {
        
        Location northPole = new Location(90, 0);
        Location southPole = new Location(-90, 0);
        
        double distance = HaversineDistance.compute(northPole, southPole);
        
        assertTrue( Math.abs( distance - Constants.EARTH_RADIUS_KM * Math.PI ) < 1e-2 );
        
    }
    
    @Test
    public void testEquatorCircunference() {
        
        Location loc1 = new Location(0, 0);
        Location loc2 = new Location(0, 15);
        
        double distance = HaversineDistance.compute(loc1, loc2);
        
        assertTrue( Math.abs( distance * 24 - 2 * Constants.EARTH_RADIUS_KM * Math.PI ) < 1e-2 );
        
    }

}
