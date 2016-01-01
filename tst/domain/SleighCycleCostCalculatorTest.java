package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.Constants;
import cost.HaversineDistance;

public class SleighCycleCostCalculatorTest {

    @Test
    public void test() throws Exception {

        SleighCycle cycle = new SleighCycle();
        SleighLocation loc0 = new SleighLocation.Builder(1L)
                                            .setWeight(0)
                                            .setLatitude(90)
                                            .setLongitude(0)
                                            .build();
        
        SleighLocation loc1 = new SleighLocation.Builder(1L)
                                            .setWeight(600)
                                            .setLatitude(45)
                                            .setLongitude(0)
                                            .build();
        
        SleighLocation loc2 = new SleighLocation.Builder(2L)
                                            .setWeight(300)
                                            .setLatitude(0)
                                            .setLongitude(0)
                                            .build();

        cycle.add(loc0);
        cycle.add(loc1);
        cycle.add(loc2);
        cycle.close();
        
        double dist01 = HaversineDistance.compute(loc0, loc1);
        double dist12 = HaversineDistance.compute(loc1, loc2);
        double dist20 = HaversineDistance.compute(loc2, loc0);
        
        SleighCycleCostCalculator calc = new SleighCycleCostCalculator();
        double cost = calc.computeCost(cycle);
        double expectedCost = (600 + 300 + Constants.SLEIGH_BASE_WEIGHT) * dist01 +
                (300 + Constants.SLEIGH_BASE_WEIGHT) * dist12 +
                Constants.SLEIGH_BASE_WEIGHT * dist20;
        
        assertTrue( Math.abs( cost - expectedCost ) < 1e-3 );
    }

}
