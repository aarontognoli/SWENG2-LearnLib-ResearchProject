package Domus.Experiments;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.FilteredTestDriver;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.rivestschapire.RivestSchapireDFA;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;

import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.executeExperiment;
import static Domus.Experiments.ExperimentUtils.readJson;

public class FilterWMethodLstar {

    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        int nUsers = 2;
        int nDays = 2;

        // test driver
        DomusTestDriver testDriver = new FilteredTestDriver(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        EquivalenceOracle.DFAEquivalenceOracle<DomusRecord> eqOracle = new DFAWMethodEQOracle<>(mOracle, 6,10);


        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFA<>(DomusTestDriver.SIGMA,mOracle);

        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.FILTER_WMETHOD_LSTAR,true,"");

    }
}
