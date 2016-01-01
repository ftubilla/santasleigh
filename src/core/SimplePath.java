package core;

import java.util.LinkedHashSet;
import java.util.Set;

public class SimplePath<L extends Location> extends Path<L> {

    protected final Set<L> locationsSet = new LinkedHashSet<>();
    
    @Override
    public void add(L location) throws InfeasiblePathException {
        super.add( location );
        locationsSet.add( location );
    }
    
    @Override
    public boolean canAdd(L location) {
        return !locationsSet.contains( location );
    }
}
