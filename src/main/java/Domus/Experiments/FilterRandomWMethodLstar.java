package Domus.Experiments;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.FilteredTestDriver;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.oracle.equivalence.DFARandomWMethodEQOracle;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;

import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.executeExperiment;
import static Domus.Experiments.ExperimentUtils.readJson;

public class FilterRandomWMethodLstar {

    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        int nUsers = 5;
        int nDays = 2;
        int bound = 1000000;

        // test driver
        DomusTestDriver testDriver = new FilteredTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFARandomWMethodEQOracle<>(mOracle,9,2,bound);


        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFA<>(DomusTestDriver.SIGMA,mOracle);

        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.FILTER_RANDOMWMETHOD_LSTAR,true,String.valueOf(bound));

    }
}
