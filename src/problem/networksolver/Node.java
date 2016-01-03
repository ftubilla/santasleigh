package problem.networksolver;

import lombok.Data;
import domain.SleighLocation;

@Data
public class Node {

    private final long id;
    private final SleighLocation sleighLocation;
    private final int outflow;

}
