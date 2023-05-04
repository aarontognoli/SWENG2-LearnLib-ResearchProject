package Domus;

import Domus.DatasetUtils.DomusRecord;
import net.automatalib.automata.fsa.DFA;

import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.*;

public class Main {
    public static void main(String[] args) throws IOException {
        DFA<?, DomusRecord> dfa = DFAfromJSON("./DomusDFA1u-2d.json");

        VisualizeGraph.visualizeJSON("./DomusDFA1u-2d.json", true);

        // TODO change the way to evaluate performance
        // not just starting from nUsers, testing all days of the missing ones
        performanceLog(readJson("./DatasetSeries2.json"), dfa, 1);
    }
}