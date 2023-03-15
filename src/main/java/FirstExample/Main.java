package FirstExample;

import de.learnlib.algorithms.lstar.mealy.ClassicLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ClassicLStarMealyBuilder;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.algorithms.rivestschapire.RivestSchapireMealy;
import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.drivers.reflect.MethodInput;
import de.learnlib.oracle.equivalence.MealyWMethodEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.Experiment.MealyExperiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    public static void main(String[] args) throws IOException {
        // create oracle for mutex

        MembershipOracle.MealyMembershipOracle<String, String> testOracle = new PoolOracle();

        MealyWMethodEQOracle<String,String> eqtest = new MealyWMethodEQOracle<>( testOracle,4);


        LearningAlgorithm.MealyLearner<String, String> lstar = new ExtensibleLStarMealyBuilder<String,String>()
                .withAlphabet(PoolTestDriver.SIGMA)
                .withOracle(testOracle)
                .create();
        RivestSchapireMealy<String,String> lstar2 = new RivestSchapireMealy<>(PoolTestDriver.SIGMA,testOracle);

        MealyExperiment<String, String> experiment = new MealyExperiment<>( lstar,eqtest,PoolTestDriver.SIGMA);

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        MealyMachine<?, String, ?, String> result = experiment.getFinalHypothesis();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + PoolTestDriver.SIGMA.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        GraphDOT.write(result, PoolTestDriver.SIGMA, System.out); // may throw IOException!

        Visualization.visualize(result, PoolTestDriver.SIGMA);

        System.out.println("-------------------------------------------------------");



    }

}
