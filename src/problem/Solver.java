package problem;

import core.InfeasiblePathException;
import data.GiftDao;

public interface Solver {

    public Solution solve(final GiftDao giftDao) throws InfeasiblePathException;
    
    public default Solution solve(final GiftDao giftDao,
            final Solution startingSolution) throws InfeasiblePathException {
        return solve(giftDao);
    }
    
}
