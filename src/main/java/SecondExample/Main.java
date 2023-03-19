package SecondExample;

import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.SUL;
import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.api.statistic.StatisticSUL;
import de.learnlib.drivers.reflect.MethodInput;
import de.learnlib.drivers.reflect.MethodOutput;
import de.learnlib.drivers.reflect.SimplePOJOTestDriver;
import de.learnlib.filter.cache.sul.SULCaches;
import de.learnlib.filter.statistic.sul.ResetCounterSUL;
import de.learnlib.oracle.equivalence.MealyWMethodEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
*
* Example2 (using the SimplePOJOTestDriver)
* Inspired by: https://github.com/LearnLib/learnlib/blob/develop/examples/src/main/java/de/learnlib/examples/Example2.java
*
* */
public class Main {
    public static void main(String[] args) throws NoSuchMethodException, IOException {
        // instantiate test driver
        SimplePOJOTestDriver driver = new SimplePOJOTestDriver(BoundedStringQueue.class.getConstructor());

        // create learning alphabet
        MethodInput offerA = driver.addInput("offer_a", "offer", "a");
        MethodInput offerB = driver.addInput("offer_b", "offer", "b");
        MethodInput poll = driver.addInput("poll", "poll");

        // oracle for counting queries wraps sul
        StatisticSUL<MethodInput, MethodOutput> statisticSul =
                new ResetCounterSUL<>("membership queries", driver);

        SUL<MethodInput, MethodOutput> effectiveSul = statisticSul;
        // use caching in order to avoid duplicate queries
        effectiveSul = SULCaches.createCache(driver.getInputs(), effectiveSul);

        // membership oracle
        SULOracle<MethodInput, MethodOutput> mOracle = new SULOracle<>(effectiveSul);
        // if no counter and no cache: SULOracle<MethodInput, MethodOutput> mOracle = new SULOracle<>(driver);
        // in this case using the cache is a bit slower, but it reduces the number of membership queries

        // create initial set of suffixes
        List<Word<MethodInput>> suffixes = new ArrayList<>();
        suffixes.add(Word.fromSymbols(offerA));
        suffixes.add(Word.fromSymbols(offerB));
        suffixes.add(Word.fromSymbols(poll));

        // learning algorithm (L*)
        LearningAlgorithm.MealyLearner<MethodInput, MethodOutput> lStar = new ExtensibleLStarMealyBuilder<MethodInput, MethodOutput>()
                .withAlphabet(driver.getInputs()) // input alphabet
                .withOracle(mOracle) // membership oracle
                .withInitialSuffixes(suffixes) // initial suffixes
                .create();

        // equivalence oracle
        MealyWMethodEQOracle<MethodInput, MethodOutput> eqOracle = new MealyWMethodEQOracle<>(mOracle, 4);

        // instantiate experiment
        Experiment.MealyExperiment<MethodInput, MethodOutput> experiment = new Experiment.MealyExperiment<>(lStar,
                eqOracle,
                driver.getInputs());

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        MealyMachine<?, MethodInput, ?, MethodOutput> result = experiment.getFinalHypothesis();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
        System.out.println(statisticSul.getStatisticalData().getSummary());

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + driver.getInputs().size());

        // show model
        System.out.println();
        System.out.println("Model: ");

        GraphDOT.write(result, driver.getInputs(), System.out); // may throw IOException!
        Visualization.visualize(result, driver.getInputs());

        System.out.println("-------------------------------------------------------");

    }
}