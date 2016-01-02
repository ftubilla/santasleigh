package problem;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.SleighCycle;
import domain.SleighLocation;

public class SleighCycleGridHolder {

    private final Map<GridCoordinate, Collection<SleighCycle>> cycleMap;
    private final double latGridSize;
    private final double longGridSize;
    
    public SleighCycleGridHolder(double latGridSize, double longGridSize) {
        cycleMap = new HashMap<>();
        this.latGridSize = latGridSize;
        this.longGridSize = longGridSize;
    }
    
    public void add(SleighCycle cycle) {
        double[] centroid = getCentroid(cycle);
        GridCoordinate coordinate = GridCoordinate.getGridCoordinate( centroid[0], centroid[1], 
                latGridSize, longGridSize);
        if (!cycleMap.containsKey(coordinate)) {
            cycleMap.put(coordinate, new HashSet<>());
        }
        cycleMap.get(coordinate).add(cycle);
    }
    
    /**
     * Returns the sleigh cycles that are neighbors to the given cycle, based on the mean coordinate
     * of the given cycle.
     * @param cycle
     * @param num of neighboring grid points along the latitude lines
     * @param num of neighboring grid points along the longitude lines
     * @return A collection of cycles
     */
    public Collection<SleighCycle> getNeighbors(SleighCycle cycle, int numNeighborsLat,
            int numNeighborsLong) {
        Set<SleighCycle> returnCycles = new LinkedHashSet<>();
        double[] centroid = getCentroid(cycle);
        for (int i = -numNeighborsLat; i <= numNeighborsLat; i++) {
            for (int j = -numNeighborsLong; j <= numNeighborsLong; j++) {
                double lat = centroid[0] + i * latGridSize;
                double lgt = centroid[1] + j * longGridSize;
                GridCoordinate gridCoordinate = GridCoordinate.getGridCoordinate(lat, lgt,
                        latGridSize, longGridSize);
                if (cycleMap.get(gridCoordinate) != null) {
                    returnCycles.addAll(cycleMap.get(gridCoordinate));
                }
            }
        }
        returnCycles.remove(cycle);
        return returnCycles;
    }
    
    protected double[] getCentroid(SleighCycle cycle) {
        //Compute the centroid
        int numLocs = 0;
        double sumLat = 0d;
        double sumLong = 0d;
        for (SleighLocation loc : cycle) {
            if (loc.isNorthPole()) continue;
            numLocs++;
            sumLat += loc.getLatitute();
            sumLong += loc.getLongitude();
        }
        return new double[] { sumLat / numLocs, sumLong / numLocs };
    }

}
