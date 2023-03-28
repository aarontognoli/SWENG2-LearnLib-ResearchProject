package Domus;

import Domus.DatasetUtils.DomusRecord;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;

import java.io.IOException;
import java.util.Collection;

public class DomusOracle implements MembershipOracle.DFAMembershipOracle<DomusRecord> {
    DomusTestDriver testDriver;

    public DomusOracle(String pathname) throws IOException {
        this.testDriver = new DomusTestDriver(pathname);
    }

    @Override
    public void processQueries(Collection<? extends Query<DomusRecord, Boolean>> queries) {
        for(Query<DomusRecord, Boolean> query : queries)
        {
            testDriver.pre();

            // at first the user have not made tea
            boolean out = false;

            for (DomusRecord input : query.getInput()) {
                out = testDriver.step(input);
            }

            testDriver.post();

            query.answer(out);
        }
    }
}
