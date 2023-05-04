package Domus.Experiments;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DomusOracle;
import Domus.DomusTestDriver;
import de.learnlib.algorithms.rivestschapire.RivestSchapireDFA;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.oracle.equivalence.SampleSetEQOracle;

import java.io.IOException;

import static Domus.Experiments.ExperimentUtils.*;

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
        executeExperiment(nUsers,nDays,lStarDFA,eqOracle,ExperimentType.TESTDRIVER_SAMPLESETEQ_RIVEST,true,"");

    }
}
