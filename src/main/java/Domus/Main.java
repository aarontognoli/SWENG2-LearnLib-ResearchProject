package Domus;

import Domus.DatasetUtils.DomusRecord;
import net.automatalib.automata.fsa.DFA;

import java.io.IOException;
import java.util.Scanner;

import static Domus.Experiments.ExperimentUtils.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String path ="DomusDFA_TESTDRIVERFULL_WMETHODEQ_LSTAR_2u-2d_Custom.json";
        DFA<?, DomusRecord> dfa = DFAfromJSON(path);

        Scanner scanner = new Scanner(path).useDelimiter("\\D+");
        int u = scanner.nextInt();
        int d = scanner.nextInt();

        performanceLog(readJson("DatasetSeries2.json"), dfa, u, d, path);
    }
}