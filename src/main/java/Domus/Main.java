package Domus;

import Domus.DatasetUtils.DomusRecord;
import net.automatalib.automata.fsa.DFA;

import java.io.IOException;
import java.util.Scanner;

import static Domus.Experiments.ExperimentUtils.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String path ="DomusDFA_FILTER_RANDOMWORDS_LSTAR_5u-2d_.json";
        DFA<?, DomusRecord> dfa = DFAfromJSON(path);

        Scanner scanner = new Scanner(path).useDelimiter("\\D+");
        int u = scanner.nextInt();
        int d = scanner.nextInt();

        performanceLog(readJson("DatasetSeries2.json"), dfa, u, d, path);
    }
}