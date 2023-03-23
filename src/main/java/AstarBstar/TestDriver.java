package AstarBstar;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;
import net.automatalib.words.impl.GrowingMapAlphabet;

import java.util.ArrayList;
import java.util.List;

public class TestDriver {

    // input symbols
    private static final String A = "a";
    private static final String B = "b";
    private boolean firstB = false;

    // input alphabet used by learning algorithm
    public static final Alphabet<String> SIGMA = new GrowingMapAlphabet<>();

    static {
        SIGMA.add(A);
        SIGMA.add(B);
    }

    //oracle behavior
    public boolean executeSymbol(String s)
    {
        if(!firstB)
        {
            if(s.equals(B))
            {
                firstB =true;
            }
            return true;
        }
        else
        {
            return s.equals(B);
        }
    }

    public void reset()
    {
        firstB=false;
    }
}
