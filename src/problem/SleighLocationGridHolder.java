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

import com.google.common.collect.Lists;

import cost.HaversineDistance;
import domain.SleighLocation;

public class SleighLocationGridHolder {

    private final Map<GridCoordinate, Collection<SleighLocation>> locationMap;
    private final Set<SleighLocation> locations;
    private final double latGridSize;
    private final double longGridSize;
    
    public SleighLocationGridHolder(double latGridSize, double longGridSize) {
        locationMap = new HashMap<>();
        locations = new HashSet<>();
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
        locations.add(location);
    }
    
    public boolean remove(SleighLocation location) {
        GridCoordinate coordinate = GridCoordinate.getGridCoordinate(location.getLatitute(),
                location.getLongitude(), latGridSize, longGridSize);
        boolean removed = false;
        if (locationMap.containsKey(coordinate)) {
            removed = locationMap.get(coordinate).remove(location);
        }
        locations.remove(location);
        return removed;
    }
    
    public int getNumLocations() {
        return locations.size();
    }
    
    /**
     * Returns the sleigh location that are neighbors to the given location.
     * 
     * @param cycle
     * @param num of neighboring grid points along the latitude lines
     * @param num of neighboring grid points along the longitude lines
     * @return A collection of locations
     */
    public Set<SleighLocation> getNeighbors(SleighLocation location, int numNeighborsLat,
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
        List<SleighLocation> neighbors = getClosestNLocations(location, 1);
        if ( neighbors.isEmpty() ) {
            return Optional.empty();
        } else {
            return Optional.of(neighbors.get(0));
        }
    }
    
    public List<SleighLocation> getClosestNLocations(SleighLocation location, int numNeighbors) {
        Collection<SleighLocation> neighbors = null;
        int numNeighboringGrids = 0;
        double maxNeighboringGrids = Math.ceil( Math.max( 360d / latGridSize, 360d / longGridSize ) );
        int upperBoundNumNeighbors = locations.size() - ( locations.contains(location) ? 1 : 0 );
        numNeighbors = Math.min( numNeighbors, upperBoundNumNeighbors);
        while ( neighbors == null || 
                ( neighbors.size() < numNeighbors && numNeighboringGrids++ <= maxNeighboringGrids ) ) {
            //Widen the search until we find the required number of neighbors
            neighbors = getNeighbors(location, numNeighboringGrids, numNeighboringGrids);
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
            return Lists.newArrayList();
        } else {
            return sortedNeighbors.subList(0, Math.min(numNeighbors, sortedNeighbors.size())); 
        }
    }

}
