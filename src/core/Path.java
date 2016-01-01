package core;

import java.util.Iterator;
import java.util.LinkedList;

public class Path<L extends Location> implements Iterable<L> {

    protected LinkedList<L> locations;
    protected LinkedList<Edge> edges;
    
    public Path() {
        locations = new LinkedList<L>();
        edges = new LinkedList<Edge>();
    }
    
    public void add(L location) throws InfeasiblePathException {
        if ( canAdd(location) ) {
                Location lastLocation = null;
            if ( !locations.isEmpty() ) {
                lastLocation = locations.getLast();
            }
            locations.add(location);
            if ( lastLocation != null ) {
                edges.add( new Edge(lastLocation, location ));
            }
        } else {
            throw new InfeasiblePathException("Could not add " + location + " to path");
        }
    }
    
    public boolean canAdd(L location) {
        return true;
    }
    
    @Override
    public Iterator<L> iterator() {
        return locations.iterator();
    }
    
    public Iterable<Edge> getEdges() {
        return edges;
    }
    
    public int getNumLocations() {
        return locations.size();
    }
    
    public int getNumEdges() {
        return edges.size();
    }

}
