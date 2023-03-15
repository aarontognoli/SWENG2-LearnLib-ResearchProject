package FirstExample;

import de.learnlib.drivers.api.TestDriver;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;


import java.util.ArrayList;
import java.util.List;


public class PoolTestDriver  {
    // input symbols
    private static final String O1_IN = "o1in";
    private static final String O2_IN = "o2in";
    private static final String O1_OUT = "o1out";
    private static final String O2_OUT = "o2out";
    private static final String ISEMPTY = "isempty?";

    // input alphabet used by learning algorithm
    public static final Alphabet<String> SIGMA;

    static {
        List<String> sig = new ArrayList<>();

        sig.add(O1_IN);
        sig.add(O2_IN);
        sig.add(O1_OUT);
        sig.add(O2_OUT);
        sig.add(ISEMPTY);

        SIGMA = Alphabets.fromList(sig);
    }

    // return values
    private static final String TOP = "true";
    private static final String BOT = "false";
    private static final String NOOP = "-";

    // system under test
    public Pool pool;

    // local test variables
    private final String o1 = "o1";
    private final String o2 = "o2";

    public String executeSymbol(String s) {
        if (s.equals(O1_IN)) {
            pool.add(o1);
            return NOOP;
        } else if (s.equals(O2_IN)) {
            pool.add(o2);
            return NOOP;
        } else if (s.equals(O1_OUT)) {
            pool.remove(o1);
            return NOOP;
        } else if (s.equals(O2_OUT)) {
            pool.remove(o2);
            return NOOP;
        } else if (s.equals(ISEMPTY)) {
            return pool.isEmpty() ? TOP : BOT;
        }
        return null;
    }

    public void reset() {
        this.pool = new Pool();
    }
}

