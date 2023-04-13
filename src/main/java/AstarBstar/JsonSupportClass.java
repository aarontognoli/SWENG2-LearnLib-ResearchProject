package AstarBstar;

import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.words.impl.GrowingMapAlphabet;

import java.util.List;

public class JsonSupportClass<T> {
    List<Integer> acceptance;
    List<Integer> transitions;
    int initial;
    List<T> alphabet;
    float resizeFactor;
    int alphabetSize;
    int stateCapacity;
    int numStates;

    public JsonSupportClass(List<Integer> acceptance, List<Integer> transitions, int initial, List<T> alphabet, float resizeFactor, int alphabetSize, int stateCapacity, int numStates) {
        this.acceptance = acceptance;
        this.transitions = transitions;
        this.initial = initial;
        this.alphabet = alphabet;
        this.resizeFactor = resizeFactor;
        this.alphabetSize = alphabetSize;
        this.stateCapacity = stateCapacity;
        this.numStates = numStates;
    }

    public CompactDFA<T> getDFA()
    {
        CompactDFA<T> ret = new CompactDFA<>(new GrowingMapAlphabet<>(alphabet),stateCapacity,resizeFactor);
        int k;
        for (int i=0;i<acceptance.size();i++)
        {
            ret.addState(acceptance.get(i)==1);
        }
        for(int i=acceptance.size();i<numStates;i++)
        {
            ret.addState(false);
        }
        ret.setInitial(initial,true);
        for(int i=0;i<numStates;i++) {
            k=0;
            for (T symbol : alphabet) {
                if(transitions.get(alphabetSize*i+k)!=-1)
                {
                    ret.addTransition(i,symbol, transitions.get(alphabetSize*i+k));
                }
                k++;
            }
        }

        return ret;
    }
}
