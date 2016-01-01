package domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.InfeasiblePathException;
import domain.SleighLocation;
import domain.SleighCycle;

public class SleighCycleTest {

    @Test
    public void test() throws Exception {

        SleighCycle cycle = new SleighCycle();
        SleighLocation loc1 = new SleighLocation.Builder(1L)
                                            .setWeight(600)
                                            .setLatitude(90)
                                            .setLongitude(0)
                                            .build();
        
        SleighLocation loc2 = new SleighLocation.Builder(2L)
                                            .setWeight(600)
                                            .setLatitude(0)
                                            .setLongitude(0)
                                            .build();

        cycle.add( loc1 );
        assertFalse( "Cannot add a repeated location to a simple path", cycle.canAdd(loc1) );
        assertFalse( "Cannot add a location that exceeds the weight limit",
                cycle.canAdd(loc2) );
        
        boolean exceptionRaised = false;
        try {
            cycle.add( loc2 );
        } catch (InfeasiblePathException e) {
            exceptionRaised = true;
        }
        assertTrue("An exception should have been raised", exceptionRaised );
        
        cycle.close();
    }

}
