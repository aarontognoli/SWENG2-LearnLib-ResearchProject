package Domus;

import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.Performance.PerformanceEvaluator;
import com.google.gson.Gson;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;

import java.io.FileWriter;
import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.readJson;

public class Main {
    public static void main(String[] args) throws IOException {

        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        // final int availableProcessors = Runtime.getRuntime().availableProcessors();

        int nUsers = 1;
        int nDays = 1;

        // test driver
        DomusTestDriver testDriver = new DomusTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle<DomusRecord, Boolean> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        // EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFAWMethodEQOracle<>(mOracle, 100);
        SampleSetEQOracle<DomusRecord, Boolean> eqOracle = new SampleSetEQOracle<>(true);
        for (int u = 0; u < nUsers; u++) {
            for (int d = 0; d < nDays; d++) {
                //known output
                eqOracle.add( new DomusWord(datasetSeries2.getUsers().get(u).get(d).getPreTea()),false);
                if(!datasetSeries2.getUsers().get(u).get(d).getDuringTea().isEmpty())
                    eqOracle.add( new DomusWord(datasetSeries2.getUsers().get(u).get(d).getDuringTea()),true);
                eqOracle.add(new DomusWord(datasetSeries2.getUsers().get(u).get(d).getPostTea()),false);
                //unknown output
                eqOracle.addAll(mOracle,new DomusWord(datasetSeries2.getUsers().get(u).get(d).getRecords()));
                eqOracle.addAll(mOracle,new DomusWord(datasetSeries1.getUsers().get(u).get(d).getRecords()));
            }
        }

        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFABuilder<DomusRecord>()
                .withAlphabet(DomusTestDriver.SIGMA)
                .withOracle(mOracle)
                .create();
        // RivestSchapireDFA<DomusRecord> lStarDFA2 = new RivestSchapireDFA<>(DomusTestDriver.SIGMA,mOracle);

        // experiment
        Experiment.DFAExperiment<DomusRecord> experiment = new Experiment.DFAExperiment<>(lStarDFA, eqOracle, DomusTestDriver.SIGMA);

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        DFA<?, DomusRecord> result = experiment.getFinalHypothesis();

        Gson gson = CustomGson.getCustomGson();
        // save result to Json, result is a compactDFA
        try (FileWriter writer = new FileWriter("./DomusDFA"+nUsers+"u-"+nDays+"d.json")) {
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
        // new ObservationTableASCIIWriter<>().write(lStarDFA.getObservationTable(), System.out);

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + DomusTestDriver.SIGMA.size());

        // show model
        System.out.println();
        String fileName = "./TestDot"+nUsers+"u-"+nDays+"d.dot";
        System.out.println("Model: saved in \"" + fileName + "\"");
        try (FileWriter writer = new FileWriter(fileName)) {
            GraphDOT.write(result, DomusTestDriver.SIGMA, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // check accuracy, precision and recall
        // for now it checks all 5 days of the missing users, parameters to tune...
        PerformanceEvaluator performanceEvaluator = new PerformanceEvaluator(result);
        for (int u = nUsers; u < 6; u++) {
            for (int d = 0; d < 5; d++) {
                performanceEvaluator.addToPositive(new DomusWord(datasetSeries2.getUsers().get(u).get(d).getDuringTea()));
                performanceEvaluator.addToNegative(new DomusWord(datasetSeries2.getUsers().get(u).get(d).getPreTea()));
                performanceEvaluator.addToNegative(new DomusWord(datasetSeries2.getUsers().get(u).get(d).getPostTea()));
            }
        }
        performanceEvaluator.run();
        System.out.println("\nPerformance:");
        System.out.format("Accuracy: %f\n", performanceEvaluator.getAccuracy());
        System.out.format("Precision: %f\n", performanceEvaluator.getPrecision());
        System.out.format("Recall: %f\n", performanceEvaluator.getRecall());
        System.out.format("F1score: %f\n", performanceEvaluator.getF1score());

        OTUtils.displayHTMLInBrowser(lStarDFA.getObservationTable()); // may throw IOException!

        // Visualization.visualize(result, DomusTestDriver.SIGMA);
        VisualizeGraph.visualizeGraph((CompactDFA<?>) result);

        System.out.println("-------------------------------------------------------");
    }


}
