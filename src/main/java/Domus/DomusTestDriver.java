package Domus;

import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DomusRecord;
import de.learnlib.api.SUL;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.GrowingMapAlphabet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DomusTestDriver implements SUL<DomusRecord, Boolean> {
    public static final Alphabet<DomusRecord> SIGMA = new GrowingMapAlphabet<>();
    protected final List<List<DomusRecord>> allValidTeaSequences = new ArrayList<>();

    List<Integer> currentSteps;

    public DomusTestDriver(int nUsers, int nDays, Dataset trainingSet, Dataset testSet) {
        for (int u = 0; u < nUsers; u++) {
            for (int d = 0; d < nDays; d++) {
                // I add all possible records to the Alphabet
                SIGMA.addAll(trainingSet.getUsers().get(u).get(d).getRecords());
                SIGMA.addAll(testSet.getUsers().get(u).get(d).getRecords());

                // I add only sequences that lead to tea to teaSteps
                List<DomusRecord> teaSequence = trainingSet.getUsers().get(u).get(d).getDuringTea();
                if (!teaSequence.isEmpty()) {
                    allValidTeaSequences.add(teaSequence);
                }
            }
        }

        currentSteps = new ArrayList<>(Collections.nCopies(allValidTeaSequences.size(), 0));
    }

    @Override
    public void pre() {
        Collections.fill(currentSteps, 0);
    }

    @Override
    public void post() {

    }

    @Override
    public Boolean step(DomusRecord in) {
        for (int indexSequence = 0; indexSequence < allValidTeaSequences.size(); indexSequence++) {
            int indexStep = currentSteps.get(indexSequence);
            if (indexStep != allValidTeaSequences.get(indexSequence).size())
            {
                if (in.equals(allValidTeaSequences.get(indexSequence).get(indexStep))) {
                    currentSteps.set(indexSequence, indexStep + 1);
                }
                indexStep = currentSteps.get(indexSequence);
                if(indexStep == allValidTeaSequences.get(indexSequence).size())
                    return true;
            }else
                return true;

        }
        return false;
    }
}
