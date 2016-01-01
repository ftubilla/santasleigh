package core;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimplePathTest {

    @Test
    public void test() throws Exception {

        Path<Location> path = new SimplePath<>();
        Location loc1 = new Location(90, 0);
        Location loc2 = new Location(0, 0);
        Location loc3 = new Location(-90, 0);
        path.add( loc1 );
        path.add( loc2 );
        path.add( loc3 );
        
        assertFalse( "Cannot add a repeated location to a simple path", path.canAdd(loc1) );
        boolean exceptionRaised = false;
        try {
            path.add( loc1 );
        } catch (InfeasiblePathException e) {
            exceptionRaised = true;
        }
        assertTrue("An exception should have been raised", exceptionRaised );
    }

}
