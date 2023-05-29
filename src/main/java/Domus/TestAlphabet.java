package Domus;

import net.automatalib.words.impl.GrowingMapAlphabet;

import java.util.List;

// new class that overrides the getSymbolIndex method so that if in testing it finds a new symbol,
// it simply adds it to the alphabet without throwing exceptions
public class TestAlphabet<I> extends GrowingMapAlphabet<I> {
    public TestAlphabet(List<I> alphabet) {
        super(alphabet);
    }

    public TestAlphabet() {
    }

    @Override
    public int getSymbolIndex(I symbol) {
        Integer result = indexMap.get(symbol);
        if (result == null) {
            super.add(symbol);
        }
        result = indexMap.get(symbol);
        return result;
    }
}
