package cost;

import core.Location;
import core.Path;

public interface PathCostCalculator<P extends Path<? extends Location>> {

    public double computeCost(P path);
    
}
