package problem.networksolver;

import lombok.Data;

@Data
public class DirectedEdge {

    private final Node from;
    private final Node to;
    private final double cost;
    private int flowUpperBound = Integer.MAX_VALUE;

    public DirectedEdge(Node from, Node to, double cost, int flowUpperBound) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.flowUpperBound = flowUpperBound;
    }
    
    public DirectedEdge(Node from, Node to, double cost) {
        this(from, to, cost, Integer.MAX_VALUE);
    }
    
}
