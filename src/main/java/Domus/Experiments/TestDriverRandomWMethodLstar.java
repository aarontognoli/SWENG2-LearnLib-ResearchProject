package Domus.Experiments;

import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.VisualizeGraph;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.oracle.equivalence.DFAIncrementalWMethodEQOracle;
import de.learnlib.oracle.equivalence.DFARandomWMethodEQOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;

import java.io.File;
import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.readJson;

public class TestDriverRandomWMethodLstar {
    //using custom Json
    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./CustomJson.json");
        Dataset datasetSeries2 = readJson("./CustomJson.json");
        int nUsers = 2;
        int nDays = 2;

        // test driver
        DomusTestDriver testDriver = new DomusTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFARandomWMethodEQOracle<>(mOracle, 5,2,10000);


        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFABuilder<DomusRecord>()
                .withAlphabet(DomusTestDriver.SIGMA)
                .withOracle(mOracle)
                .create();

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

        ExperimentUtils.log(experiment,result,DomusTestDriver.SIGMA);
        File image=null;
        try {
            ExperimentUtils.printFiles(result, lStarDFA, nUsers, nDays, ExperimentType.TESTDRIVER_RANDOMWMETHODEQ_LSTAR,"Custom");
            image = ExperimentUtils.printDotSVG(result,nUsers,nDays,ExperimentType.TESTDRIVER_RANDOMWMETHODEQ_LSTAR,"Custom");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // may throw IOException!
        OTUtils.displayHTMLInBrowser(lStarDFA.getObservationTable());

        VisualizeGraph.visualizeFile(image);

    }
}
