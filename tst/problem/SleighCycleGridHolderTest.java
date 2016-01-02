package problem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import data.GiftDao;
import domain.SleighCycle;
import domain.SleighLocation;

public class SleighCycleGridHolderTest {

    @Test
    public void testSmallCycle() throws Exception {
        
        SleighCycleGridHolder holder = new SleighCycleGridHolder(5, 10);
        
        SleighLocation loc1 = new SleighLocation.Builder(1L)
                .setLatitude(10)
                .setLongitude(0)
            .   setWeight(100).build();

        SleighLocation loc2 = new SleighLocation.Builder(2L)
                .setLatitude(-10)
                .setLongitude(0)
                .setWeight(100).build(); 
        
        SleighLocation loc3 = new SleighLocation.Builder(3L)
                .setLatitude(20)
                .setLongitude(20)
                .setWeight(100).build();
        
        SleighLocation loc4 = new SleighLocation.Builder(4L)
                .setLatitude(10)
                .setLongitude(20)
                .setWeight(100).build();
        
        SleighCycle cycle1 = new SleighCycle();
        cycle1.add(loc1);
        cycle1.add(loc2);
        cycle1.close();
        double[] centroid1 = holder.getCentroid(cycle1);
        assertTrue( Math.abs( centroid1[0] - 0d ) < 1e-3 );
        assertTrue( Math.abs( centroid1[1] - 0d ) < 1e-3 );
        
        SleighCycle cycle2 = new SleighCycle();
        cycle2.add(loc3);
        cycle2.add(loc4);
        cycle2.close();
        double[] centroid2 = holder.getCentroid(cycle2);
        assertTrue( Math.abs( centroid2[0] - 15d ) < 1e-3 );
        assertTrue( Math.abs( centroid2[1] - 20d ) < 1e-3 );
        
        holder.add(cycle1);
        holder.add(cycle2);
        
        assertFalse( holder.getNeighbors(cycle1, 0, 0).contains(cycle2) );
        assertFalse( holder.getNeighbors(cycle1, 2, 2).contains(cycle2) );
        assertTrue( holder.getNeighbors(cycle1, 3, 2).contains(cycle2) );
    }

    @Test
    public void testGiftData() throws Exception {
        
        GiftDao dao = GiftDao.load("data/gifts.ser");
        Solver solver = new OneGiftPerSleighSolver();
        Solution solution = solver.solve(dao);
        SleighCycle cycle1 = solution.iterator().next();
        SleighCycleGridHolder holder = new SleighCycleGridHolder(1, 1);
        for (SleighCycle cycle : solution) {
            holder.add(cycle);
        }
        double[] centroid1 = holder.getCentroid(cycle1);
        System.out.println("Cycle 1 centroid: " + centroid1[0] + "," + centroid1[1]);
        for (SleighCycle neighbor : holder.getNeighbors(cycle1, 0, 0)) {
            double[] centroid = holder.getCentroid(neighbor);
            System.out.println("Neighbor centroid: " + centroid[0] + "," + centroid[1]);
        }
    }

}
