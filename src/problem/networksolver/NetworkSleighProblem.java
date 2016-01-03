package problem.networksolver;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import problem.SleighLocationGridHolder;
import xpress.ProblemFactory;

import com.dashoptimization.XPRBexpr;
import com.dashoptimization.XPRBprob;
import com.dashoptimization.XPRBvar;

import core.Constants;
import cost.HaversineDistance;
import data.GiftDao;
import domain.SleighLocation;

public class NetworkSleighProblem {

    private final XPRBprob problem;
    private final Set<Node> nodes;
    private final Set<DirectedEdge> edges;
    private final Map<SleighLocation, Node> innerLocationToNode;
    private final Options options;
    
    public NetworkSleighProblem(Options options, GiftDao dao, int numSleighs) {
        
        this.problem = ProblemFactory.newProblem("network_" + numSleighs + "_sleighs");
        this.options = options;
        
        SleighLocationGridHolder holder = new SleighLocationGridHolder(options.latitudeGridSizeDegrees,
                options.longitudeGridSizeGegrees);
        
        nodes = new LinkedHashSet<>();
        edges = new LinkedHashSet<>();
        innerLocationToNode = new HashMap<>();
        
        //Create a node for each gift location
        int totalDemand = 0;
        for (SleighLocation loc : dao) {
            int demand = (int) Math.round(options.weightRoundingMultiplier * loc.getWeight());
            totalDemand += demand;
            Node node = new Node(loc.getId(), loc, -demand);
            nodes.add(node);
            innerLocationToNode.put( loc, node );
            holder.add(loc);
        }
        
        //Add a node for the North Pole outgoing and incoming
        Node northPoleOutgoing = new Node(0L, Constants.SLEIGH_NORTH_POLE, 
                totalDemand + numSleighs * (int) Constants.SLEIGH_BASE_WEIGHT );
        Node northPoleIncoming = new Node(nodes.size() + 1, Constants.SLEIGH_NORTH_POLE,
                -numSleighs * (int) Constants.SLEIGH_BASE_WEIGHT );

        //Add an edge from the north pole to each possible edge and back
        for ( Node node : nodes ) {
            double outCost = HaversineDistance.compute(northPoleOutgoing.getSleighLocation(),
                    node.getSleighLocation());
            //These edges have an upper bound equal to total demand + one sleigh weight
            DirectedEdge outEdge = new DirectedEdge(northPoleOutgoing, node, outCost,
                    (int) ( Constants.SLEIGH_CARGO_WEIGHT_LIMIT + Constants.SLEIGH_BASE_WEIGHT) );
            edges.add(outEdge);
            double inCost = HaversineDistance.compute(node.getSleighLocation(),
                    northPoleIncoming.getSleighLocation());
            //These edges have an upper bound equal to one sleigh weight
            DirectedEdge inEdge = new DirectedEdge(node, northPoleIncoming, inCost,
                    (int) ( Constants.SLEIGH_BASE_WEIGHT) );
            edges.add(inEdge);
        }

        //Add an edge for each neighbor
        for ( Node node : nodes ) {
            SleighLocation originLoc = node.getSleighLocation();
            for ( SleighLocation neighbor : holder.getClosestNLocations(originLoc, options.numNeighborsPerNode )) {
                if ( !neighbor.isNorthPole() ) {
                    Node neighborNode = innerLocationToNode.get(neighbor);
                    double cost = HaversineDistance.compute(originLoc, neighbor);
                    edges.add( new DirectedEdge(node, neighborNode, cost));
                }
            }
        }
        
        //Add the NP nodes
        nodes.add(northPoleOutgoing);
        nodes.add(northPoleIncoming);
    }
    
    public void init() {
        
        //Add the edge flow variables
        Map<DirectedEdge, XPRBvar> edgeToVar = new HashMap<>();
        for ( DirectedEdge edge : edges ) {
            edgeToVar.put( edge, problem.newVar(edge.toString(), 0.0, edge.getFlowUpperBound()) );
        }
        //Add the constraint expressions
        Map<Node, XPRBexpr> nodeToConstraintExpr = new HashMap<>();
        for ( Node node : nodes ) {
            nodeToConstraintExpr.put( node, new XPRBexpr() );
        }
        for ( DirectedEdge edge : edges ) {
            XPRBvar var = edgeToVar.get(edge);
            nodeToConstraintExpr.get( edge.getFrom() ).addTerm( var, 1.0);
            nodeToConstraintExpr.get( edge.getTo() ).addTerm( var, -1.0);
        }
        //Finalize the constraints
        for ( Node node : nodes ) {
            problem.newCtr(node.toString(), nodeToConstraintExpr.get(node).eql( node.getOutflow() ));
        }

        //TODO Set the objective
        problem.print();
    }
    
    
    public static class Options {
        public double weightRoundingMultiplier = 100;
        public double latitudeGridSizeDegrees = 15;
        public double longitudeGridSizeGegrees = 15;
        public int numNeighborsPerNode = 20;
    }
    
}
