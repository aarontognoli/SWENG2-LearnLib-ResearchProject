package AstarBstar;

import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;
import net.automatalib.words.impl.GrowingMapAlphabet;

import java.util.ArrayList;
import java.util.List;

public class TestDriver {

    // input symbols
    private static final Character A = 'a';
    private static final Character B = 'b';
    private boolean firstB = false;
    private boolean aInSecond = false;

    // input alphabet used by learning algorithm
    public static final Alphabet<Character> SIGMA = new GrowingMapAlphabet<>();

    static {
        SIGMA.add(A);
        SIGMA.add(B);
    }

    //oracle behavior
    public boolean executeSymbol(Character s)
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
            if(s.equals(A))
                aInSecond=true;
            return !aInSecond && s.equals(B);
        }
    }

    public void reset()
    {
        firstB=aInSecond=false;

    }
}
