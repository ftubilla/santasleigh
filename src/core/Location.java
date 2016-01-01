package core;

import java.io.Serializable;

import lombok.Data;

@Data
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    private final double latitute;
    private final double longitude;

}
