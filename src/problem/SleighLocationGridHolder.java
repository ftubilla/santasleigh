package problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.Getter;
import cost.HaversineDistance;
import domain.SleighLocation;

public class SleighLocationGridHolder {

    private final Map<GridCoordinate, Collection<SleighLocation>> locationMap;
    private final double latGridSize;
    private final double longGridSize;
    @Getter private int numLocations;
    
    public SleighLocationGridHolder(double latGridSize, double longGridSize) {
        locationMap = new HashMap<>();
        this.latGridSize = latGridSize;
        this.longGridSize = longGridSize;
    }
    
    public void add(SleighLocation location) {
        GridCoordinate coordinate = GridCoordinate.getGridCoordinate(location.getLatitute(),
                location.getLongitude(), latGridSize, longGridSize);
        if (!locationMap.containsKey(coordinate)) {
            locationMap.put(coordinate, new HashSet<>());
        }
        locationMap.get(coordinate).add(location);
        numLocations++;
    }
    
    public boolean remove(SleighLocation location) {
        GridCoordinate coordinate = GridCoordinate.getGridCoordinate(location.getLatitute(),
                location.getLongitude(), latGridSize, longGridSize);
        boolean removed = false;
        if (locationMap.containsKey(coordinate)) {
            removed = locationMap.get(coordinate).remove(location);
        }
        if (removed) {
            numLocations--;
        }
        return removed;
    }
    
    /**
     * Returns the sleigh location that are neighbors to the given location.
     * 
     * @param cycle
     * @param num of neighboring grid points along the latitude lines
     * @param num of neighboring grid points along the longitude lines
     * @return A collection of locations
     */
    public Collection<SleighLocation> getNeighbors(SleighLocation location, int numNeighborsLat,
            int numNeighborsLong) {
        Set<SleighLocation> returnLocations = new LinkedHashSet<>();
        for (int i = -numNeighborsLat; i <= numNeighborsLat; i++) {
            for (int j = -numNeighborsLong; j <= numNeighborsLong; j++) {
                double lat = location.getLatitute() + i * latGridSize;
                double lgt = location.getLongitude() + j * longGridSize;
                GridCoordinate gridCoordinate = GridCoordinate.getGridCoordinate(lat, lgt,
                        latGridSize, longGridSize);
                if (locationMap.get(gridCoordinate) != null) {
                    returnLocations.addAll(locationMap.get(gridCoordinate));
                }
            }
        }
        returnLocations.remove(location);
        return returnLocations;
    }

    public Optional<SleighLocation> getClosestLocation(SleighLocation location) {
        Collection<SleighLocation> neighbors = null;
        int numNeighbors = 0;
        double maxNeighbors = Math.ceil( Math.max( 360d / latGridSize, 360d / longGridSize ) );
        while ( neighbors == null || neighbors.isEmpty() && numNeighbors++ <= maxNeighbors ) {
            //Widen the search until we find at least one neighbor
            neighbors = getNeighbors(location, numNeighbors, numNeighbors);
        }
        //Sort the locations by distance to the given location
        List<SleighLocation> sortedNeighbors = new ArrayList<>();
        sortedNeighbors.addAll(neighbors);
        sortedNeighbors.sort( (loc1, loc2) -> {
            double dist1 = HaversineDistance.compute(location, loc1);
            double dist2 = HaversineDistance.compute(location, loc2);
            return Double.compare(dist1, dist2);
        });
        if ( sortedNeighbors.isEmpty() ) {
            return Optional.empty();
        } else {
            return Optional.of(sortedNeighbors.get(0));
        }
    }

}
