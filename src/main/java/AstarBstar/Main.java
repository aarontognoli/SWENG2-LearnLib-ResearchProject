package AstarBstar;

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

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        // create oracle
        MembershipOracle.DFAMembershipOracle<String> testOracle = new Oracle();
        //Equivalence Oracle
        EquivalenceOracle.DFAEquivalenceOracle<String> eqtest = new DFAWMethodEQOracle<>( testOracle,4);


        ClassicLStarDFA<String> lstar = new ClassicLStarDFABuilder<String>()
                .withAlphabet(TestDriver.SIGMA)
                .withOracle(testOracle)
                .create();


        Experiment.DFAExperiment<String> experiment = new Experiment.DFAExperiment<>( lstar,eqtest,TestDriver.SIGMA);



        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        DFA<?, String> result = experiment.getFinalHypothesis();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
        System.out.println();
        new ObservationTableASCIIWriter<>().write(lstar.getObservationTable(), System.out);

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + TestDriver.SIGMA.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        GraphDOT.write(result, TestDriver.SIGMA, System.out); // may throw IOException!


        Visualization.visualize(result, TestDriver.SIGMA);

        System.out.println("-------------------------------------------------------");
    }
}
