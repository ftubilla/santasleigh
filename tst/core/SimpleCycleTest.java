package core;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleCycleTest {

    @Test
    public void test() throws Exception {

        SimpleCycle<Location> cycle = new SimpleCycle<>();
        Location loc1 = new Location(90, 0);
        Location loc2 = new Location(0, 0);
        Location loc3 = new Location(-90, 0);
        cycle.add( loc1 );
        cycle.add( loc2 );
        cycle.add( loc3 );
        
        assertFalse( "Cannot add a repeated location to a simple cycle unless it's been closed",
                cycle.canAdd(loc1) );
        boolean exceptionRaised = false;
        try {
            cycle.add( loc1 );
        } catch (InfeasiblePathException e) {
            exceptionRaised = true;
        }
        assertTrue("An exception should have been raised", exceptionRaised );
        
        cycle.close();
        
        assertFalse( "Cannot add to a closed cycle",
                cycle.canAdd(loc1) );
        assertFalse( "Cannot add to a closed cycle",
                cycle.canAdd(loc2) );
        assertFalse( "Cannot add to a closed cycle",
                cycle.canAdd(loc3) );
        
        exceptionRaised = false;
        try {
            cycle.close();
        } catch (InfeasiblePathException e) {
            exceptionRaised = true;
        }
        assertTrue("An exception should have been raised", exceptionRaised);
        
    }

}
