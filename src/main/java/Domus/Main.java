package Domus;

import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import com.google.gson.Gson;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.algorithms.rivestschapire.RivestSchapireDFA;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.datastructure.observationtable.writer.ObservationTableHTMLWriter;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import Domus.Experiments.ExperimentUtils;

import java.io.*;

import static Domus.Experiments.ExperimentUtils.readJson;

public class Main {
    public static void main(String[] args) throws IOException {

        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
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
        RivestSchapireDFA<DomusRecord> lstar2 = new RivestSchapireDFA<>(DomusTestDriver.SIGMA,mOracle);

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
        //new ObservationTableASCIIWriter<>().write(lStarDFA.getObservationTable(), System.out);

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + DomusTestDriver.SIGMA.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        try (FileWriter writer = new FileWriter("./TestDot"+nUsers+"u-"+nDays+"d.dot")) {
            GraphDOT.write(result, DomusTestDriver.SIGMA, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // may throw IOException!
        OTUtils.displayHTMLInBrowser(lStarDFA.getObservationTable());

        //Visualization.visualize(result, DomusTestDriver.SIGMA);
        VisualizeGraph.visualizeGraph((CompactDFA<?>) result);

        System.out.println("-------------------------------------------------------");
    }


}
