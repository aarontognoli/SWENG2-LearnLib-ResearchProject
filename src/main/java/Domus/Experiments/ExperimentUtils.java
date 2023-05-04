package Domus.Experiments;

import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusTestDriver;
import Domus.DomusWord;
import Domus.VisualizeGraph;
import com.google.gson.Gson;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.algorithms.lstar.dfa.ExtensibleLStarDFA;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.dot.DOT;
import net.automatalib.words.Alphabet;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;

enum ExperimentType {
    TESTDRIVER_SAMPLESETEQ_LSTAR,
    TESTDRIVER_WMETHODEQ_LSTAR,
    TESTDRIVER_WMETHODEQ_RIVEST,
    TESTDRIVER_INCREMENTALWMETHODEQ_LSTAR,
    TESTDRIVER_RANDOMWMETHODEQ_LSTAR,
    TESTDRIVER_RANDOMWMETHODEQ_RIVEST,
    TESTDRIVER_RANDOMWORDS_LSTAR,
    TESTDRIVERFULL_SAMPLESETEQ_LSTAR,
    TESTDRIVERFULL_WMETHODEQ_LSTAR,
    TESTDRIVER_SAMPLESETEQ_RIVEST
}
public class ExperimentUtils {

    public static Dataset readJson(String path) throws FileNotFoundException {
        Gson g = CustomGson.getCustomGson();
        Reader reader = new FileReader(path);
        Dataset d = g.fromJson(reader,Dataset.class);
        System.out.println("Read " + path);
        return d;
    }
    public static void printFiles(DFA<?, DomusRecord> result, ExtensibleLStarDFA<?> lstar, int nUsers, int nDays, ExperimentType type, String comment) throws IOException {
        String suffix = "_"+type+"_"+nUsers+"u-"+nDays+"d";

        Gson gson = CustomGson.getCustomGson();

        // save result to Json, result is a compactDFA, DotFile and ObservationTable
        try (FileWriter writer  = new FileWriter("./Results/Json/DomusDFA"+suffix+"_"+comment+".json")) {
            gson.toJson(result, writer);
        }
        try (FileWriter writer = new FileWriter("./Results/DotFiles/TestDot"+suffix+"_"+comment+".dot")) {
            GraphDOT.write(result, DomusTestDriver.SIGMA, writer);
        }

        File out = new File("./Results/ObservationTable/OT"+suffix+"_"+comment+".html");
        OTUtils.writeHTMLToFile(lstar.getObservationTable(),out);

    }

    public static File printDotSVG(DFA<?, DomusRecord> result,int nUsers, int nDays, ExperimentType type, String comment, Boolean filter) throws IOException {
        String suffix = "_"+type+"_"+nUsers+"u-"+nDays+"d";
        File out = new File("./Results/XML/"+suffix+"_"+comment+".svg");
        StringBuilder dotString = new StringBuilder();
        if(filter)
            result = VisualizeGraph.filterGraph((CompactDFA<DomusRecord>)result);
        GraphDOT.write((CompactDFA<DomusRecord>)result,dotString);
        DOT.runDOT(new StringReader(dotString.toString()), "svg", out);

        return out;
    }

    public static void printFiles(DFA<?, DomusRecord> result, ExtensibleLStarDFA<?> lstar, int nUsers, int nDays, ExperimentType type) throws IOException {
        printFiles(result,lstar,nUsers,nDays,type,"");
    }

    public static void log( Experiment<?> experiment, DFA<?, DomusRecord> result, Alphabet<?> alphabet)
    {
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
        System.out.println("Sigma: " + alphabet.size());

        // show model
        System.out.println();
        System.out.println("Model: ");

        System.out.println("-------------------------------------------------------");
    }
    public static SampleSetEQOracle<DomusRecord,Boolean> getSampleSetEqOracle(int nUsers, int nDays, Dataset trainingSet, Dataset testSet, MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle )
    {
        SampleSetEQOracle<DomusRecord, Boolean> eqOracle = new SampleSetEQOracle<>(true);
        for (int u = 0; u < nUsers; u++) {
            for (int d = 0; d < nDays; d++) {
                //known output
                eqOracle.add( new DomusWord(trainingSet.getUsers().get(u).get(d).getPreTea()),false);
                if(!trainingSet.getUsers().get(u).get(d).getDuringTea().isEmpty())
                    eqOracle.add( new DomusWord(trainingSet.getUsers().get(u).get(d).getDuringTea()),true);
                eqOracle.add(new DomusWord(trainingSet.getUsers().get(u).get(d).getPostTea()),false);
                //unknown output
                eqOracle.addAll(mOracle,new DomusWord(trainingSet.getUsers().get(u).get(d).getRecords()));
                eqOracle.addAll(mOracle,new DomusWord(testSet.getUsers().get(u).get(d).getRecords()));

            }
        }
        return eqOracle;
    }

    public static DFA<?, DomusRecord> DFAfromJSON(String path) throws IOException {
        Gson gson = CustomGson.getCustomGson();
        JsonSupportClass<DomusRecord> myObj;
        try (Reader reader = new FileReader(path)) {
            Type myType = new TypeToken<JsonSupportClass<DomusRecord>>() {
            }.getType();
            // Convert JSON File to Java Object
            myObj = gson.fromJson(reader, myType);
            return myObj.getDFA();
        }
    }

    public static void performanceLog(Dataset testSet, DFA<?, DomusRecord> dfa, int nUsers) {
        PerformanceEvaluator performanceEvaluator = new PerformanceEvaluator(dfa);
        for (int u = nUsers; u < 6; u++) {
            for (int d = 0; d < 5; d++) {
                performanceEvaluator.addToPositive(new DomusWord(testSet.getUsers().get(u).get(d).getDuringTea()));
                performanceEvaluator.addToNegative(new DomusWord(testSet.getUsers().get(u).get(d).getPreTea()));
                performanceEvaluator.addToNegative(new DomusWord(testSet.getUsers().get(u).get(d).getPostTea()));
            }
        }
        performanceEvaluator.run();
        System.out.println("\nPerformance:");
        System.out.format("Accuracy: %f\n", performanceEvaluator.getAccuracy());
        System.out.format("Precision: %f\n", performanceEvaluator.getPrecision());
        System.out.format("Recall: %f\n", performanceEvaluator.getRecall());
        System.out.format("F1score: %f\n", performanceEvaluator.getF1score());
    }

    public static void executeExperiment(int nUsers,
                                         int nDays,
                                         ExtensibleLStarDFA<DomusRecord> lStar,
                                         EquivalenceOracle<? super DFA<?, DomusRecord>, DomusRecord, Boolean> eqOracle,
                                         ExperimentType type,
                                         Boolean filter,
                                         String comment) throws IOException {


        // experiment
        Experiment.DFAExperiment<DomusRecord> experiment = new Experiment.DFAExperiment<>(lStar, eqOracle, DomusTestDriver.SIGMA);

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        DFA<?, DomusRecord> result = experiment.getFinalHypothesis();

        ExperimentUtils.log(experiment,result,DomusTestDriver.SIGMA);
        File image=null;
        try {
            ExperimentUtils.printFiles(result, lStar, nUsers, nDays, type);
            image = ExperimentUtils.printDotSVG(result,nUsers,nDays,type,comment,filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // may throw IOException!
        OTUtils.displayHTMLInBrowser(lStar.getObservationTable());

        VisualizeGraph.visualizeFile(image);
    }
}
