package core;

import domain.SleighLocation;

public class Constants {

    public static final double EARTH_RADIUS_KM = 6371;
    
    public static final double DEG_TO_RAD = Math.PI / 180d;
    
    public static final Location NORTH_POLE = new Location(90, 0);
    
    public static final SleighLocation SLEIGH_NORTH_POLE = new SleighLocation(0L, 0.0, 90.0, 0.0);

    public static final double SLEIGH_BASE_WEIGHT = 10d;
    
    public static final double SLEIGH_CARGO_WEIGHT_LIMIT = 1000;
    
}
