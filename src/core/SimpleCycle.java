package core;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

public class SimpleCycle<L extends Location> extends Path<L> {

    @Getter private boolean isClosed = false;
    private boolean isCloseInProgress = false;
    
    public void close() throws InfeasiblePathException {
        if ( isClosed ) {
            throw new InfeasiblePathException("Cannot close a cycle twice!");
        }
        try {
            isCloseInProgress = true;
            add( locations.getFirst() );
            isCloseInProgress = false;
            isClosed = true;
        } catch (InfeasiblePathException e) {
            throw new InfeasiblePathException("Could not close the cycle");
        }
    }
    
    private final Set<L> locationsSet = new HashSet<>();
    
    @Override
    public void add(L location) throws InfeasiblePathException {
        if ( canAdd( location ) ) {
            super.add(location);
            locationsSet.add( location );
        } else {
            throw new InfeasiblePathException("Cannot add " + location + " to cycle");
        }
    }
    
    @Override
    public boolean canAdd(L location) {
        if ( isCloseInProgress ) {
            return locations.getFirst().equals( location );
        }
        if ( !isClosed ) {
            return !locationsSet.contains( location );
        }
        return false;
    }
}
