package domain;

import lombok.Getter;
import core.Constants;
import core.InfeasiblePathException;
import core.SimpleCycle;

@Getter
public class SleighCycle extends SimpleCycle<SleighLocation> {

    private static long counter = 0;
    
    private final long id;
    private double takeoffWeight;
    private double cargoWeight;
    
    public SleighCycle() {
        super();
        this.id = counter++;
        takeoffWeight = Constants.SLEIGH_BASE_WEIGHT;
        try {
            add(Constants.SLEIGH_NORTH_POLE);
        } catch ( InfeasiblePathException e ) {
            throw new RuntimeException("Unexpected condition");
        }
    }

    @Override
    public void add(SleighLocation giftLocation) throws InfeasiblePathException {
        super.add(giftLocation);
        takeoffWeight += giftLocation.getWeight();
        cargoWeight += giftLocation.getWeight();
    }
    
    @Override
    public boolean canAdd(SleighLocation giftLocation) {
        return super.canAdd(giftLocation) && canAddWeight(giftLocation.getWeight());
    }
    
    public boolean canAddWeight( double weight ) {
        return takeoffWeight + weight <= Constants.SLEIGH_BASE_WEIGHT + 
                Constants.SLEIGH_CARGO_WEIGHT_LIMIT;
    }
    
}
