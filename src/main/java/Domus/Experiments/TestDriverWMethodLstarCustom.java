package Domus.Experiments;

import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.DomusWord;
import Domus.VisualizeGraph;
import com.google.gson.Gson;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.algorithms.rivestschapire.RivestSchapireDFA;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;
import de.learnlib.oracle.equivalence.DFAWpMethodEQOracle;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.executeExperiment;
import static Domus.Experiments.ExperimentUtils.readJson;

public class TestDriverWMethodLstarCustom {
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
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFAWpMethodEQOracle<>(mOracle, 6);


        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFABuilder<DomusRecord>()
                .withAlphabet(DomusTestDriver.SIGMA)
                .withOracle(mOracle)
                .create();

        // experiment
        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.TESTDRIVER_WMETHODEQ_LSTAR,false,"Custom");

    }
}
