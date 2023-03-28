package Domus;

import Domus.DatasetUtils.DomusParser;
import Domus.DatasetUtils.DomusRecord;
import de.learnlib.api.SUL;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.GrowingMapAlphabet;

import java.io.IOException;

public class DomusTestDriver implements SUL<DomusRecord, Boolean> {
    public static final Alphabet<DomusRecord> SIGMA = new GrowingMapAlphabet<>();

    public DomusTestDriver(String pathname) throws IOException {
            SIGMA.addAll(DomusParser.parse(pathname));
    }

    @Override
    public void pre() {
        // TODO pre
    }

    @Override
    public void post() {
        // TODO post
    }

    @Override
    public Boolean step(DomusRecord in) {
        // TODO step
        return null;
    }
}
