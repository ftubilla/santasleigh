package core;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathTest {

    @Test
    public void test() throws Exception {

        Path<Location> path = new Path<>();
        Location loc1 = new Location(90, 0);
        Location loc2 = new Location(0, 0);
        Location loc3 = new Location(-90, 0);
        path.add( loc1 );
        path.add( loc2 );
        path.add( loc3 );
        
        assertEquals(3, path.getNumLocations());
        assertEquals(2, path.getNumEdges());
        
        Edge[] edges = { new Edge(loc1, loc2), new Edge(loc2, loc3) };
        int e = 0;
        for ( Edge edge : path.getEdges() ) {
            assertEquals( edges[e++], edge );
        }

    }

}
