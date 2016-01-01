package cost;

import core.Constants;
import core.Location;

public class HaversineDistance {

    public static double compute(Location loc1, Location loc2) {
        
        double phi1 = loc1.getLatitute() * Constants.DEG_TO_RAD;
        double phi2 = loc2.getLatitute() * Constants.DEG_TO_RAD;
        double lamb1 = loc1.getLongitude() * Constants.DEG_TO_RAD;
        double lamb2 = loc2.getLongitude() * Constants.DEG_TO_RAD;
        
        double hav = haversine(phi2 - phi1) + 
                Math.cos(phi1) * Math.cos(phi2) * haversine( lamb2 - lamb1 );
        
        return 2 * Constants.EARTH_RADIUS_KM * Math.asin( Math.sqrt( hav ) );
    }

    public static double haversine(double angle) {
        return 0.5 * ( 1 - Math.cos(angle) );
    }

}
