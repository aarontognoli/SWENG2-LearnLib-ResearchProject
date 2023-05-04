package Domus.Experiments;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import Domus.DomusTestDriverFullList;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;

import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.*;

public class TestDriverFullSampleSetMethodLstar {

    public static void main(String[] args) throws IOException {
        // setting up dataset
        Dataset datasetSeries1 = readJson("./DatasetSeries1.json");
        Dataset datasetSeries2 = readJson("./DatasetSeries2.json");
        int nUsers = 1;
        int nDays = 1;

        // test driver
        DomusTestDriver testDriver = new DomusTestDriverFullList(nUsers, nDays, datasetSeries2, datasetSeries1);

        // membership oracle
        MembershipOracle.DFAMembershipOracle<DomusRecord> mOracle = new DomusOracle(testDriver);

        // equivalence oracle
        SampleSetEQOracle<DomusRecord,Boolean> eqOracle = getSampleSetEqOracle(nUsers,nDays,datasetSeries2,datasetSeries1,mOracle);


        // l star algorithm
        ClassicLStarDFA<DomusRecord> lStarDFA = new ClassicLStarDFABuilder<DomusRecord>()
                .withAlphabet(DomusTestDriver.SIGMA)
                .withOracle(mOracle)
                .create();

        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.TESTDRIVERFULL_SAMPLESETEQ_LSTAR,true,"");

    }
}
