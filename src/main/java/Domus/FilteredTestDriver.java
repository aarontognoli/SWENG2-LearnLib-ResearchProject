package Domus;

import Domus.DatasetUtils.CustomDomusRecord;
import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import Domus.DatasetUtils.SensorState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FilteredTestDriver extends DomusTestDriver{

    public FilteredTestDriver(int nUsers, int nDays, Dataset trainingSet, Dataset testSet) {
        for (int u = 0; u < nUsers; u++) {
            for (int d = 0; d < nDays; d++) {
                // I add all possible records to the Alphabet

                //SIGMA.addAll(trainingSet.getUsers().get(u).get(d).getRecords());//.subList(10,trainingSet.getUsers().get(u).get(d).getRecords().size()));
                List<DomusRecord> preTea = trainingSet.getUsers().get(u).get(d).getPreTea();
                List<DomusRecord> durTea = CustomDomusRecord.toCustomDomusRecord(trainingSet.getUsers().get(u).get(d).getDuringTea());
                List<DomusRecord> pstTea = trainingSet.getUsers().get(u).get(d).getPostTea();

                SIGMA.addAll(preTea.subList(0,Math.min(0, preTea.size())));
                SIGMA.addAll(durTea.subList(0,Math.min(10, durTea.size())).stream().filter((x)->x.state()!= SensorState.Close).toList());
                SIGMA.addAll(pstTea.subList(0,Math.min(0, pstTea.size())));

                // I add only sequences that lead to tea to teaSteps
                List<DomusRecord> teaSequence = trainingSet.getUsers().get(u).get(d).getDuringTea();
                if (!teaSequence.isEmpty()) {
                    allValidTeaSequences.add(CustomDomusRecord.toCustomDomusRecord(teaSequence.subList(0,Math.min(10,teaSequence.size())).stream().filter((x)->x.state()!= SensorState.Close).toList()));
                }
            }
        }

        currentSteps = new ArrayList<>(Collections.nCopies(allValidTeaSequences.size(), 0));
    }
}
