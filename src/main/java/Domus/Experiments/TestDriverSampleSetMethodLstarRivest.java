package Domus.Experiments;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.VisualizeGraph;
import de.learnlib.algorithms.rivestschapire.RivestSchapireDFA;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;

import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.getSampleSetEqOracle;
import static Domus.Experiments.ExperimentUtils.readJson;

public class TestDriverSampleSetMethodLstarRivest {

    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        int nUsers = 1;
        int nDays = 5;

        // test driver
        DomusTestDriver testDriver = new DomusTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        SampleSetEQOracle<DomusRecord,Boolean> eqOracle = getSampleSetEqOracle(nUsers,nDays,datasetSeries2,datasetSeries1,mOracle);


        // l star algorithm
        RivestSchapireDFA<DomusRecord> lStarDFA = new RivestSchapireDFA<>(DomusTestDriver.SIGMA,mOracle);
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

        try {
            ExperimentUtils.printFiles(result, lStarDFA, nUsers, nDays, ExperimentType.TESTDRIVER_SAMPLESETEQ_RIVEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // may throw IOException!
        OTUtils.displayHTMLInBrowser(lStarDFA.getObservationTable());

        VisualizeGraph.visualizeGraph((CompactDFA<?>) result);

    }
}
