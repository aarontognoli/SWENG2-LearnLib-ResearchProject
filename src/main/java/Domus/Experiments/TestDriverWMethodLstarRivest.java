package Domus.Experiments;

import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.VisualizeGraph;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.algorithms.rivestschapire.RivestSchapireDFA;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;
import de.learnlib.util.Experiment;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;

import java.io.File;
import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.executeExperiment;
import static Domus.Experiments.ExperimentUtils.readJson;

public class TestDriverWMethodLstarRivest {
    //todo try with an even smaller dataset
    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        int nUsers = 1;
        int nDays = 1;

        // test driver
        DomusTestDriver testDriver = new DomusTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFAWMethodEQOracle<>(mOracle, 10);


        // l star algorithm
        RivestSchapireDFA<DomusRecord> lStarDFA = new RivestSchapireDFA<>(DomusTestDriver.SIGMA,mOracle);

        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.TESTDRIVER_WMETHODEQ_RIVEST,true,"");

    }
}
