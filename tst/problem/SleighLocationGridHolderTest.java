package problem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import domain.SleighLocation;

public class SleighLocationGridHolderTest {

    @Test
    public void testGetClosestLocation() {
        
        SleighLocation loc1 = new SleighLocation.Builder(1L)
                                    .setLatitude(0)
                                    .setLongitude(0)
                                    .build();
        
        SleighLocation loc2 = new SleighLocation.Builder(2L)
                                    .setLatitude(5)
                                    .setLongitude(5)
                                    .build();
        
        SleighLocation loc3 = new SleighLocation.Builder(3L)
                                    .setLatitude(6)
                                    .setLongitude(6)
                                    .build();
        
        SleighLocationGridHolder holder = new SleighLocationGridHolder(1, 1);
        holder.add(loc1);
        holder.add(loc2);
        holder.add(loc3);
        
        Collection<SleighLocation> neighbors = holder.getNeighbors(loc1, 5, 5);
        assertFalse( neighbors.contains(loc1) );
        assertTrue( neighbors.contains(loc2) );
        assertFalse( neighbors.contains(loc3) );
        
        neighbors = holder.getNeighbors(loc1, 6, 6);
        assertFalse( neighbors.contains(loc1) );
        assertTrue( neighbors.contains(loc2) );
        assertTrue( neighbors.contains(loc3) );
        
        assertEquals(loc2, holder.getClosestLocation(loc1).get() );
        assertEquals(loc2, holder.getClosestLocation(loc3).get() );
        assertEquals(loc3, holder.getClosestLocation(loc2).get() );

        assertEquals(3, holder.getNumLocations());
        holder.remove(loc2);
        assertEquals(loc3, holder.getClosestLocation(loc1).get() );
        assertEquals(2, holder.getNumLocations());
        
    }

}
