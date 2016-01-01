package problem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import core.Constants;
import domain.SleighCycle;
import domain.SleighLocation;

public class SleighCycleTransformerTest {

    @Test
    public void testMerge() throws Exception {
        
        SleighLocation loc1 = new SleighLocation.Builder(1L)
                                    .setLatitude(90)
                                    .setLongitude(0)
                                    .setWeight(100).build();
        
        SleighLocation loc2 = new SleighLocation.Builder(2L)
                                    .setLatitude(80)
                                    .setLongitude(0)
                                    .setWeight(100).build(); 
        
        SleighLocation loc3 = new SleighLocation.Builder(3L)
                                    .setLatitude(50)
                                    .setLongitude(0)
                                    .setWeight(100).build();
        
        SleighLocation loc4 = new SleighLocation.Builder(4L)
                                    .setLatitude(70)
                                    .setLongitude(0)
                                    .setWeight(100).build();
        
        SleighLocation loc5 = new SleighLocation.Builder(5L)
                                    .setLatitude(60)
                                    .setLongitude(0)
                                    .setWeight(100).build();
        
        SleighLocation loc6 = new SleighLocation.Builder(6L)
                                    .setLatitude(10)
                                    .setLongitude(0)
                                    .setWeight(Constants.SLEIGH_CARGO_WEIGHT_LIMIT).build();

        SleighCycle cycle1 = new SleighCycle();
        cycle1.add(loc1);
        cycle1.add(loc2);
        cycle1.add(loc3);
        
        SleighCycle cycle2 = new SleighCycle();
        cycle2.add(loc4);
        cycle2.add(loc5);
        
        SleighCycle mergedCycle = SleighCycleTransformer.merge(cycle1, cycle2).get();
        SleighLocation[] expectedOrder = {loc1, loc2, loc4, loc5, loc3};
        int i = 0;
        for (SleighLocation loc : mergedCycle) {
            if ( loc.isNorthPole() ) {
                continue;
            }
            assertEquals( expectedOrder[i++], loc);
        }
        
        SleighCycle cycle3 = new SleighCycle();
        cycle3.add(loc6);
        assertFalse( SleighCycleTransformer.merge(cycle1, cycle3).isPresent() );
        
    }

}
