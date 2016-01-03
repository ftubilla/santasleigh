package xpress;

import com.dashoptimization.XPRB;
import com.dashoptimization.XPRBprob;

public class ProblemFactory {

    private static XPRB xprb = new XPRB();
    
    public static XPRBprob newProblem(String name) {
        XPRBprob prob = xprb.newProb(name);
        return prob;
    }

}
