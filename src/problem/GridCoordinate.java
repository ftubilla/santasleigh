package problem;

import lombok.Data;

@Data
public class GridCoordinate {

    private final int latitude;
    private final int longitude;
    
    public static GridCoordinate getGridCoordinate(double latitude, double longitude, double latGridSize,
            double longGridSize) {
        int discLat  = (int) ( Math.floor( latitude % 360 / latGridSize ) * latGridSize );
        int discLong = (int) ( Math.floor( longitude % 360 / longGridSize ) * longGridSize );
        return new GridCoordinate(discLat, discLong);
    }

}
