package Domus;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DomusRecord;

public class DomusTestDriverFullList extends DomusTestDriver{

    public DomusTestDriverFullList(int nUsers, int nDays, Dataset trainingSet, Dataset testSet) {
        super(nUsers, nDays, trainingSet, testSet);
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
                else
                {
                    currentSteps.set(indexSequence, 0);
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
