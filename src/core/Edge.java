package core;

import java.io.Serializable;

import lombok.Data;

@Data
public class Edge implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Location from;
    private final Location to;
    
}
