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

import static Domus.Experiments.ExperimentUtils.executeExperiment;
import static Domus.Experiments.ExperimentUtils.readJson;

public class TestDriverRandomWMethodLstar {
    //using custom Json
    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        int nUsers = 2;
        int nDays = 2;

        // test driver
        DomusTestDriver testDriver = new DomusTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFARandomWMethodEQOracle<>(mOracle, 60,10,100000000);


        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFABuilder<DomusRecord>()
                .withAlphabet(DomusTestDriver.SIGMA)
                .withOracle(mOracle)
                .create();

        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.TESTDRIVER_RANDOMWMETHODEQ_LSTAR,true,"");

    }
}
