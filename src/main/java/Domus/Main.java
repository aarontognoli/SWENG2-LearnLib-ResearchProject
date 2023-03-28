package Domus;

import Domus.DatasetUtils.DomusRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // membership oracle
        // in this case test driver is associated only with series 2, user 1, day 1
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle("src/main/resources/Domus Series 2/User 1/Day 1.vna");
        //Equivalence Oracle
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFAWMethodEQOracle<>(mOracle, 4);


        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFABuilder<DomusRecord>()
                .withAlphabet(DomusTestDriver.SIGMA)
                .withOracle(mOracle)
                .create();


        Experiment.DFAExperiment<DomusRecord> experiment = new Experiment.DFAExperiment<>(lStarDFA, eqOracle, DomusTestDriver.SIGMA);

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        DFA<?, DomusRecord> result = experiment.getFinalHypothesis();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //save result to Json, result is a compactDFA
        try (FileWriter writer = new FileWriter("./DomusDFA.json")) {
            gson.toJson(result, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
        System.out.println();
        new ObservationTableASCIIWriter<>().write(lStarDFA.getObservationTable(), System.out);

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + DomusTestDriver.SIGMA.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        GraphDOT.write(result, DomusTestDriver.SIGMA, System.out); // may throw IOException!

        Visualization.visualize(result, DomusTestDriver.SIGMA);

        System.out.println("-------------------------------------------------------");
    }
}
